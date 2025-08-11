#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
电子教材平台测试报告生成器
汇总所有测试结果并生成详细的HTML报告
"""

import os
import json
import glob
from datetime import datetime, timedelta
from typing import Dict, List, Any, Optional
import statistics
import base64
from io import BytesIO

try:
    import matplotlib.pyplot as plt
    import matplotlib.dates as mdates
    from matplotlib.figure import Figure
    MATPLOTLIB_AVAILABLE = True
except ImportError:
    MATPLOTLIB_AVAILABLE = False
    print("警告: matplotlib 未安装，将跳过图表生成")

class TestReportGenerator:
    def __init__(self, report_dir: str = "./"):
        self.report_dir = report_dir
        self.test_data = {
            "platform_tests": [],
            "enhanced_tests": [],
            "stress_tests": [],
            "monitor_reports": []
        }
        
    def collect_test_data(self):
        """收集所有测试数据"""
        print("📊 收集测试数据...")
        
        # 收集平台功能测试报告
        platform_files = glob.glob(os.path.join(self.report_dir, "test_report_*.json"))
        for file_path in platform_files:
            try:
                with open(file_path, 'r', encoding='utf-8') as f:
                    data = json.load(f)
                    data['file_path'] = file_path
                    self.test_data["platform_tests"].append(data)
            except Exception as e:
                print(f"警告: 无法读取文件 {file_path}: {e}")
                
        # 收集增强测试报告
        enhanced_files = glob.glob(os.path.join(self.report_dir, "enhanced_test_report_*.json"))
        for file_path in enhanced_files:
            try:
                with open(file_path, 'r', encoding='utf-8') as f:
                    data = json.load(f)
                    data['file_path'] = file_path
                    self.test_data["enhanced_tests"].append(data)
            except Exception as e:
                print(f"警告: 无法读取文件 {file_path}: {e}")
                
        # 收集压力测试报告
        stress_files = glob.glob(os.path.join(self.report_dir, "stress_test_*.json"))
        for file_path in stress_files:
            try:
                with open(file_path, 'r', encoding='utf-8') as f:
                    data = json.load(f)
                    data['file_path'] = file_path
                    self.test_data["stress_tests"].append(data)
            except Exception as e:
                print(f"警告: 无法读取文件 {file_path}: {e}")
                
        # 收集监控报告
        monitor_files = glob.glob(os.path.join(self.report_dir, "monitor_reports/monitor_report_*.json"))
        for file_path in monitor_files:
            try:
                with open(file_path, 'r', encoding='utf-8') as f:
                    data = json.load(f)
                    data['file_path'] = file_path
                    self.test_data["monitor_reports"].append(data)
            except Exception as e:
                print(f"警告: 无法读取文件 {file_path}: {e}")
                
        print(f"✅ 数据收集完成:")
        print(f"  - 平台功能测试: {len(self.test_data['platform_tests'])} 个报告")
        print(f"  - 增强测试: {len(self.test_data['enhanced_tests'])} 个报告")
        print(f"  - 压力测试: {len(self.test_data['stress_tests'])} 个报告")
        print(f"  - 监控报告: {len(self.test_data['monitor_reports'])} 个报告")
        
    def analyze_test_trends(self) -> Dict[str, Any]:
        """分析测试趋势"""
        trends = {
            "platform_trend": [],
            "performance_trend": [],
            "availability_trend": []
        }
        
        # 分析平台功能测试趋势
        for test in sorted(self.test_data["enhanced_tests"], 
                          key=lambda x: x.get('summary', {}).get('test_time', '')):
            summary = test.get('summary', {})
            if 'success_rate' in summary:
                trends["platform_trend"].append({
                    'timestamp': summary.get('test_time', ''),
                    'success_rate': summary['success_rate'],
                    'total_tests': summary.get('total', 0)
                })
                
        # 分析性能趋势
        for test in sorted(self.test_data["stress_tests"], 
                          key=lambda x: x.get('test_time', '')):
            if 'results' in test:
                successful_results = [r for r in test['results'] if r['success']]
                if successful_results:
                    avg_response_time = statistics.mean([r['response_time'] for r in successful_results])
                    success_rate = (len(successful_results) / len(test['results'])) * 100
                    
                    trends["performance_trend"].append({
                        'timestamp': test.get('test_time', ''),
                        'avg_response_time': avg_response_time,
                        'success_rate': success_rate,
                        'total_requests': len(test['results'])
                    })
                    
        return trends
        
    def generate_charts(self, trends: Dict[str, Any]) -> Dict[str, str]:
        """生成图表"""
        charts = {}
        
        if not MATPLOTLIB_AVAILABLE:
            return charts
            
        try:
            # 设置中文字体
            plt.rcParams['font.sans-serif'] = ['Arial Unicode MS', 'SimHei', 'DejaVu Sans']
            plt.rcParams['axes.unicode_minus'] = False
            
            # 1. 平台功能测试成功率趋势
            if trends["platform_trend"]:
                fig, ax = plt.subplots(figsize=(12, 6))
                
                timestamps = [datetime.fromisoformat(t['timestamp'].replace('Z', '+00:00')) 
                            for t in trends["platform_trend"] if t['timestamp']]
                success_rates = [t['success_rate'] for t in trends["platform_trend"]]
                
                if timestamps and success_rates:
                    ax.plot(timestamps, success_rates, marker='o', linewidth=2, markersize=6)
                    ax.set_title('平台功能测试成功率趋势', fontsize=16, fontweight='bold')
                    ax.set_xlabel('时间', fontsize=12)
                    ax.set_ylabel('成功率 (%)', fontsize=12)
                    ax.grid(True, alpha=0.3)
                    ax.set_ylim(0, 105)
                    
                    # 格式化x轴
                    if len(timestamps) > 1:
                        ax.xaxis.set_major_formatter(mdates.DateFormatter('%m-%d %H:%M'))
                        plt.xticks(rotation=45)
                    
                    plt.tight_layout()
                    
                    # 转换为base64
                    buffer = BytesIO()
                    plt.savefig(buffer, format='png', dpi=150, bbox_inches='tight')
                    buffer.seek(0)
                    chart_data = base64.b64encode(buffer.getvalue()).decode()
                    charts['platform_trend'] = f"data:image/png;base64,{chart_data}"
                    plt.close()
                    
            # 2. 性能趋势图
            if trends["performance_trend"]:
                fig, (ax1, ax2) = plt.subplots(2, 1, figsize=(12, 10))
                
                timestamps = [datetime.fromisoformat(t['timestamp'].replace('Z', '+00:00')) 
                            for t in trends["performance_trend"] if t['timestamp']]
                response_times = [t['avg_response_time'] for t in trends["performance_trend"]]
                success_rates = [t['success_rate'] for t in trends["performance_trend"]]
                
                if timestamps and response_times:
                    # 响应时间趋势
                    ax1.plot(timestamps, response_times, marker='s', color='orange', 
                           linewidth=2, markersize=6)
                    ax1.set_title('平均响应时间趋势', fontsize=14, fontweight='bold')
                    ax1.set_ylabel('响应时间 (秒)', fontsize=12)
                    ax1.grid(True, alpha=0.3)
                    
                    # 成功率趋势
                    ax2.plot(timestamps, success_rates, marker='^', color='green', 
                           linewidth=2, markersize=6)
                    ax2.set_title('压力测试成功率趋势', fontsize=14, fontweight='bold')
                    ax2.set_xlabel('时间', fontsize=12)
                    ax2.set_ylabel('成功率 (%)', fontsize=12)
                    ax2.grid(True, alpha=0.3)
                    ax2.set_ylim(0, 105)
                    
                    # 格式化x轴
                    for ax in [ax1, ax2]:
                        if len(timestamps) > 1:
                            ax.xaxis.set_major_formatter(mdates.DateFormatter('%m-%d %H:%M'))
                            plt.setp(ax.xaxis.get_majorticklabels(), rotation=45)
                    
                    plt.tight_layout()
                    
                    # 转换为base64
                    buffer = BytesIO()
                    plt.savefig(buffer, format='png', dpi=150, bbox_inches='tight')
                    buffer.seek(0)
                    chart_data = base64.b64encode(buffer.getvalue()).decode()
                    charts['performance_trend'] = f"data:image/png;base64,{chart_data}"
                    plt.close()
                    
        except Exception as e:
            print(f"警告: 生成图表时出错: {e}")
            
        return charts
        
    def generate_html_report(self) -> str:
        """生成HTML报告"""
        trends = self.analyze_test_trends()
        charts = self.generate_charts(trends)
        
        # 计算汇总统计
        total_tests = 0
        total_passed = 0
        latest_test_time = None
        
        for test in self.test_data["enhanced_tests"]:
            summary = test.get('summary', {})
            total_tests += summary.get('total', 0)
            total_passed += summary.get('passed', 0)
            
            test_time = summary.get('test_time')
            if test_time:
                if not latest_test_time or test_time > latest_test_time:
                    latest_test_time = test_time
                    
        overall_success_rate = (total_passed / total_tests * 100) if total_tests > 0 else 0
        
        # 获取最新的测试结果
        latest_enhanced_test = None
        if self.test_data["enhanced_tests"]:
            latest_enhanced_test = max(self.test_data["enhanced_tests"], 
                                     key=lambda x: x.get('summary', {}).get('test_time', ''))
            
        latest_stress_test = None
        if self.test_data["stress_tests"]:
            latest_stress_test = max(self.test_data["stress_tests"], 
                                   key=lambda x: x.get('test_time', ''))
        
        html_content = f"""
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>电子教材平台测试报告</title>
    <style>
        * {{
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }}
        
        body {{
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', sans-serif;
            line-height: 1.6;
            color: #333;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }}
        
        .container {{
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }}
        
        .header {{
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            border-radius: 20px;
            padding: 30px;
            margin-bottom: 30px;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
            text-align: center;
        }}
        
        .header h1 {{
            color: #2c3e50;
            font-size: 2.5em;
            margin-bottom: 10px;
            font-weight: 700;
        }}
        
        .header .subtitle {{
            color: #7f8c8d;
            font-size: 1.2em;
            margin-bottom: 20px;
        }}
        
        .summary-cards {{
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }}
        
        .card {{
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }}
        
        .card:hover {{
            transform: translateY(-5px);
            box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
        }}
        
        .card h3 {{
            color: #2c3e50;
            font-size: 1.3em;
            margin-bottom: 15px;
            display: flex;
            align-items: center;
            gap: 10px;
        }}
        
        .card .metric {{
            font-size: 2.5em;
            font-weight: 700;
            margin-bottom: 5px;
        }}
        
        .card .label {{
            color: #7f8c8d;
            font-size: 0.9em;
        }}
        
        .success {{ color: #27ae60; }}
        .warning {{ color: #f39c12; }}
        .danger {{ color: #e74c3c; }}
        .info {{ color: #3498db; }}
        
        .section {{
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            border-radius: 15px;
            padding: 30px;
            margin-bottom: 30px;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
        }}
        
        .section h2 {{
            color: #2c3e50;
            font-size: 1.8em;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 3px solid #3498db;
        }}
        
        .chart-container {{
            text-align: center;
            margin: 20px 0;
        }}
        
        .chart-container img {{
            max-width: 100%;
            height: auto;
            border-radius: 10px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
        }}
        
        .test-details {{
            display: grid;
            gap: 20px;
        }}
        
        .test-item {{
            background: #f8f9fa;
            border-radius: 10px;
            padding: 20px;
            border-left: 4px solid #3498db;
        }}
        
        .test-item h4 {{
            color: #2c3e50;
            margin-bottom: 10px;
        }}
        
        .test-item .test-meta {{
            color: #7f8c8d;
            font-size: 0.9em;
            margin-bottom: 10px;
        }}
        
        .badge {{
            display: inline-block;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 0.8em;
            font-weight: 600;
            text-transform: uppercase;
        }}
        
        .badge.success {{
            background: #d4edda;
            color: #155724;
        }}
        
        .badge.danger {{
            background: #f8d7da;
            color: #721c24;
        }}
        
        .badge.warning {{
            background: #fff3cd;
            color: #856404;
        }}
        
        .progress-bar {{
            background: #ecf0f1;
            border-radius: 10px;
            height: 20px;
            overflow: hidden;
            margin: 10px 0;
        }}
        
        .progress-fill {{
            height: 100%;
            border-radius: 10px;
            transition: width 0.3s ease;
        }}
        
        .footer {{
            text-align: center;
            color: rgba(255, 255, 255, 0.8);
            margin-top: 40px;
            padding: 20px;
        }}
        
        @media (max-width: 768px) {{
            .container {{
                padding: 10px;
            }}
            
            .header h1 {{
                font-size: 2em;
            }}
            
            .summary-cards {{
                grid-template-columns: 1fr;
            }}
        }}
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>📊 电子教材平台测试报告</h1>
            <div class="subtitle">Platform Testing Comprehensive Report</div>
            <div class="test-meta">生成时间: {datetime.now().strftime('%Y年%m月%d日 %H:%M:%S')}</div>
        </div>
        
        <div class="summary-cards">
            <div class="card">
                <h3>🎯 总体成功率</h3>
                <div class="metric {'success' if overall_success_rate >= 95 else 'warning' if overall_success_rate >= 80 else 'danger'}">
                    {overall_success_rate:.1f}%
                </div>
                <div class="label">基于 {total_tests} 个测试</div>
                <div class="progress-bar">
                    <div class="progress-fill {'success' if overall_success_rate >= 95 else 'warning' if overall_success_rate >= 80 else 'danger'}" 
                         style="width: {overall_success_rate}%; background: {'#27ae60' if overall_success_rate >= 95 else '#f39c12' if overall_success_rate >= 80 else '#e74c3c'};"></div>
                </div>
            </div>
            
            <div class="card">
                <h3>📈 测试报告数量</h3>
                <div class="metric info">{len(self.test_data['enhanced_tests']) + len(self.test_data['stress_tests'])}</div>
                <div class="label">功能测试 + 压力测试</div>
            </div>
            
            <div class="card">
                <h3>⏰ 最新测试时间</h3>
                <div class="metric info" style="font-size: 1.2em;">
                    {datetime.fromisoformat(latest_test_time.replace('Z', '+00:00')).strftime('%m-%d %H:%M') if latest_test_time else '无数据'}
                </div>
                <div class="label">最后执行时间</div>
            </div>
            
            <div class="card">
                <h3>🔍 监控报告</h3>
                <div class="metric info">{len(self.test_data['monitor_reports'])}</div>
                <div class="label">自动化监控记录</div>
            </div>
        </div>
"""
        
        # 添加趋势图表
        if charts:
            html_content += """
        <div class="section">
            <h2>📈 测试趋势分析</h2>
"""
            
            if 'platform_trend' in charts:
                html_content += f"""
            <div class="chart-container">
                <h3>平台功能测试成功率趋势</h3>
                <img src="{charts['platform_trend']}" alt="平台功能测试趋势图">
            </div>
"""
            
            if 'performance_trend' in charts:
                html_content += f"""
            <div class="chart-container">
                <h3>性能测试趋势</h3>
                <img src="{charts['performance_trend']}" alt="性能测试趋势图">
            </div>
"""
            
            html_content += "</div>"
            
        # 添加最新测试详情
        html_content += """
        <div class="section">
            <h2>🔍 最新测试详情</h2>
            <div class="test-details">
"""
        
        # 最新功能测试
        if latest_enhanced_test:
            summary = latest_enhanced_test.get('summary', {})
            success_rate = summary.get('success_rate', 0)
            status_class = 'success' if success_rate >= 95 else 'warning' if success_rate >= 80 else 'danger'
            
            html_content += f"""
                <div class="test-item">
                    <h4>🧪 最新功能测试</h4>
                    <div class="test-meta">
                        时间: {summary.get('test_time', '未知')}<br>
                        文件: {os.path.basename(latest_enhanced_test.get('file_path', ''))}
                    </div>
                    <div>
                        <span class="badge {status_class}">{success_rate:.1f}% 成功率</span>
                        <span class="badge info">{summary.get('total', 0)} 个测试</span>
                        <span class="badge {'success' if summary.get('failed', 0) == 0 else 'danger'}">{summary.get('failed', 0)} 个失败</span>
                    </div>
                    <div class="progress-bar">
                        <div class="progress-fill" style="width: {success_rate}%; background: {'#27ae60' if success_rate >= 95 else '#f39c12' if success_rate >= 80 else '#e74c3c'};"></div>
                    </div>
                </div>
"""
        
        # 最新压力测试
        if latest_stress_test:
            total_requests = latest_stress_test.get('total_requests', 0)
            successful_requests = latest_stress_test.get('successful_requests', 0)
            success_rate = (successful_requests / total_requests * 100) if total_requests > 0 else 0
            status_class = 'success' if success_rate >= 90 else 'warning' if success_rate >= 70 else 'danger'
            
            html_content += f"""
                <div class="test-item">
                    <h4>⚡ 最新压力测试</h4>
                    <div class="test-meta">
                        时间: {latest_stress_test.get('test_time', '未知')}<br>
                        文件: {os.path.basename(latest_stress_test.get('file_path', ''))}
                    </div>
                    <div>
                        <span class="badge {status_class}">{success_rate:.1f}% 成功率</span>
                        <span class="badge info">{total_requests} 个请求</span>
                        <span class="badge {'success' if total_requests - successful_requests == 0 else 'danger'}">{total_requests - successful_requests} 个失败</span>
                    </div>
                    <div class="progress-bar">
                        <div class="progress-fill" style="width: {success_rate}%; background: {'#27ae60' if success_rate >= 90 else '#f39c12' if success_rate >= 70 else '#e74c3c'};"></div>
                    </div>
                </div>
"""
        
        html_content += """
            </div>
        </div>
        
        <div class="section">
            <h2>📋 测试历史记录</h2>
            <div class="test-details">
"""
        
        # 显示所有测试记录
        all_tests = []
        
        for test in self.test_data["enhanced_tests"]:
            summary = test.get('summary', {})
            all_tests.append({
                'type': '功能测试',
                'time': summary.get('test_time', ''),
                'success_rate': summary.get('success_rate', 0),
                'total': summary.get('total', 0),
                'file': os.path.basename(test.get('file_path', ''))
            })
            
        for test in self.test_data["stress_tests"]:
            total_requests = test.get('total_requests', 0)
            successful_requests = test.get('successful_requests', 0)
            success_rate = (successful_requests / total_requests * 100) if total_requests > 0 else 0
            
            all_tests.append({
                'type': '压力测试',
                'time': test.get('test_time', ''),
                'success_rate': success_rate,
                'total': total_requests,
                'file': os.path.basename(test.get('file_path', ''))
            })
            
        # 按时间排序
        all_tests.sort(key=lambda x: x['time'], reverse=True)
        
        for test in all_tests[:10]:  # 显示最近10个测试
            status_class = 'success' if test['success_rate'] >= 90 else 'warning' if test['success_rate'] >= 70 else 'danger'
            test_time = datetime.fromisoformat(test['time'].replace('Z', '+00:00')).strftime('%m-%d %H:%M') if test['time'] else '未知'
            
            html_content += f"""
                <div class="test-item">
                    <h4>{test['type']}</h4>
                    <div class="test-meta">
                        时间: {test_time} | 文件: {test['file']}
                    </div>
                    <div>
                        <span class="badge {status_class}">{test['success_rate']:.1f}% 成功率</span>
                        <span class="badge info">{test['total']} 个测试/请求</span>
                    </div>
                </div>
"""
        
        html_content += """
            </div>
        </div>
        
        <div class="footer">
            <p>🤖 由电子教材平台测试系统自动生成</p>
            <p>如有问题，请检查测试日志或联系开发团队</p>
        </div>
    </div>
</body>
</html>
"""
        
        return html_content
        
    def generate_report(self, output_file: str = None) -> str:
        """生成完整的测试报告"""
        print("🚀 开始生成测试报告...")
        
        # 收集数据
        self.collect_test_data()
        
        # 生成HTML报告
        html_content = self.generate_html_report()
        
        # 保存报告
        if not output_file:
            timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
            output_file = f"platform_test_report_{timestamp}.html"
            
        try:
            with open(output_file, 'w', encoding='utf-8') as f:
                f.write(html_content)
            print(f"✅ 测试报告已生成: {output_file}")
            return output_file
        except Exception as e:
            print(f"❌ 生成报告失败: {e}")
            return ""
            
    def generate_summary_json(self, output_file: str = None) -> str:
        """生成JSON格式的汇总报告"""
        trends = self.analyze_test_trends()
        
        # 计算汇总统计
        total_tests = 0
        total_passed = 0
        
        for test in self.test_data["enhanced_tests"]:
            summary = test.get('summary', {})
            total_tests += summary.get('total', 0)
            total_passed += summary.get('passed', 0)
            
        summary_data = {
            "report_time": datetime.now().isoformat(),
            "overall_statistics": {
                "total_tests": total_tests,
                "total_passed": total_passed,
                "overall_success_rate": (total_passed / total_tests * 100) if total_tests > 0 else 0,
                "total_reports": {
                    "platform_tests": len(self.test_data["platform_tests"]),
                    "enhanced_tests": len(self.test_data["enhanced_tests"]),
                    "stress_tests": len(self.test_data["stress_tests"]),
                    "monitor_reports": len(self.test_data["monitor_reports"])
                }
            },
            "trends": trends,
            "latest_results": {
                "latest_enhanced_test": max(self.test_data["enhanced_tests"], 
                                           key=lambda x: x.get('summary', {}).get('test_time', '')) if self.test_data["enhanced_tests"] else None,
                "latest_stress_test": max(self.test_data["stress_tests"], 
                                         key=lambda x: x.get('test_time', '')) if self.test_data["stress_tests"] else None
            }
        }
        
        if not output_file:
            timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
            output_file = f"platform_test_summary_{timestamp}.json"
            
        try:
            with open(output_file, 'w', encoding='utf-8') as f:
                json.dump(summary_data, f, ensure_ascii=False, indent=2)
            print(f"✅ JSON汇总报告已生成: {output_file}")
            return output_file
        except Exception as e:
            print(f"❌ 生成JSON报告失败: {e}")
            return ""

if __name__ == "__main__":
    import argparse
    
    parser = argparse.ArgumentParser(description="电子教材平台测试报告生成器")
    parser.add_argument("--dir", default="./", help="测试报告目录")
    parser.add_argument("--output", help="输出文件名")
    parser.add_argument("--json", action="store_true", help="同时生成JSON格式报告")
    
    args = parser.parse_args()
    
    generator = TestReportGenerator(args.dir)
    
    # 生成HTML报告
    html_file = generator.generate_report(args.output)
    
    # 生成JSON报告
    if args.json:
        json_file = generator.generate_summary_json()
        
    if html_file:
        print(f"\n🎉 报告生成完成！")
        print(f"📄 HTML报告: {html_file}")
        if args.json:
            print(f"📊 JSON报告: {json_file}")
        print(f"\n💡 提示: 可以在浏览器中打开HTML文件查看详细报告")