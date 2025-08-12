#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
电子教材平台自动化监控脚本
定期运行测试并监控平台健康状态
"""

import os
import sys
import time
import json
import schedule
import smtplib
import logging
from datetime import datetime, timedelta
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from enhanced_platform_test import EnhancedPlatformTester
from typing import Dict, List, Any

class PlatformMonitor:
    def __init__(self, config_file: str = "monitor_config.json"):
        self.config_file = config_file
        self.config = self.load_config()
        self.setup_logging()
        self.test_history = []
        self.alert_threshold = self.config.get("alert_threshold", 80)  # 成功率低于80%时告警
        self.max_history = self.config.get("max_history", 100)  # 保留最近100次测试记录
        
    def load_config(self) -> Dict[str, Any]:
        """加载配置文件"""
        default_config = {
            "base_url": "http://localhost:8082",
            "frontend_url": "http://localhost:5174",
            "test_interval_minutes": 30,
            "alert_threshold": 80,
            "max_history": 100,
            "email_alerts": {
                "enabled": False,
                "smtp_server": "smtp.gmail.com",
                "smtp_port": 587,
                "sender_email": "",
                "sender_password": "",
                "recipient_emails": []
            },
            "log_level": "INFO",
            "report_directory": "./monitor_reports"
        }
        
        if os.path.exists(self.config_file):
            try:
                with open(self.config_file, 'r', encoding='utf-8') as f:
                    user_config = json.load(f)
                    default_config.update(user_config)
            except Exception as e:
                print(f"警告: 无法加载配置文件 {self.config_file}: {e}")
                print("使用默认配置")
        else:
            # 创建默认配置文件
            with open(self.config_file, 'w', encoding='utf-8') as f:
                json.dump(default_config, f, ensure_ascii=False, indent=2)
            print(f"已创建默认配置文件: {self.config_file}")
            
        return default_config
        
    def setup_logging(self):
        """设置日志"""
        log_level = getattr(logging, self.config.get("log_level", "INFO").upper())
        
        # 创建日志目录
        log_dir = "./monitor_logs"
        os.makedirs(log_dir, exist_ok=True)
        
        # 配置日志格式
        log_format = '%(asctime)s - %(levelname)s - %(message)s'
        
        # 文件日志
        log_file = os.path.join(log_dir, f"platform_monitor_{datetime.now().strftime('%Y%m%d')}.log")
        
        logging.basicConfig(
            level=log_level,
            format=log_format,
            handlers=[
                logging.FileHandler(log_file, encoding='utf-8'),
                logging.StreamHandler(sys.stdout)
            ]
        )
        
        self.logger = logging.getLogger(__name__)
        
    def run_test_cycle(self):
        """运行一次完整的测试周期"""
        self.logger.info("开始新的测试周期")
        
        try:
            # 创建测试器实例
            tester = EnhancedPlatformTester(self.config["base_url"])
            tester.frontend_url = self.config["frontend_url"]
            
            # 运行测试
            tester.run_comprehensive_tests()
            
            # 分析测试结果
            test_summary = self.analyze_test_results(tester.test_results)
            
            # 记录测试历史
            self.record_test_history(test_summary)
            
            # 检查是否需要告警
            if test_summary["success_rate"] < self.alert_threshold:
                self.send_alert(test_summary)
                
            # 生成监控报告
            self.generate_monitor_report(test_summary)
            
            self.logger.info(f"测试周期完成 - 成功率: {test_summary['success_rate']:.1f}%")
            
        except Exception as e:
            self.logger.error(f"测试周期执行失败: {str(e)}")
            self.send_error_alert(str(e))
            
    def analyze_test_results(self, test_results: List[Dict]) -> Dict[str, Any]:
        """分析测试结果"""
        total_tests = len(test_results)
        passed_tests = sum(1 for result in test_results if result["success"])
        failed_tests = total_tests - passed_tests
        
        # 按类别分析
        categories = {
            "服务器": [],
            "前端": [],
            "用户系统": [],
            "等级系统": [],
            "性能": []
        }
        
        for result in test_results:
            test_name = result["test"]
            if "服务器" in test_name or "健康" in test_name:
                categories["服务器"].append(result)
            elif "前端" in test_name:
                categories["前端"].append(result)
            elif "用户" in test_name or "登录" in test_name:
                categories["用户系统"].append(result)
            elif "等级系统" in test_name:
                categories["等级系统"].append(result)
            elif "性能" in test_name or "并发" in test_name:
                categories["性能"].append(result)
                
        # 计算各类别成功率
        category_stats = {}
        for category, results in categories.items():
            if results:
                passed = sum(1 for r in results if r["success"])
                total = len(results)
                category_stats[category] = {
                    "passed": passed,
                    "total": total,
                    "success_rate": (passed / total) * 100
                }
                
        return {
            "timestamp": datetime.now().isoformat(),
            "total_tests": total_tests,
            "passed_tests": passed_tests,
            "failed_tests": failed_tests,
            "success_rate": (passed_tests / total_tests) * 100 if total_tests > 0 else 0,
            "category_stats": category_stats,
            "failed_test_details": [r for r in test_results if not r["success"]]
        }
        
    def record_test_history(self, test_summary: Dict[str, Any]):
        """记录测试历史"""
        self.test_history.append(test_summary)
        
        # 保持历史记录数量在限制内
        if len(self.test_history) > self.max_history:
            self.test_history = self.test_history[-self.max_history:]
            
        # 保存历史记录到文件
        history_file = "monitor_history.json"
        try:
            with open(history_file, 'w', encoding='utf-8') as f:
                json.dump(self.test_history, f, ensure_ascii=False, indent=2)
        except Exception as e:
            self.logger.error(f"保存测试历史失败: {e}")
            
    def send_alert(self, test_summary: Dict[str, Any]):
        """发送告警"""
        self.logger.warning(f"平台健康状态告警 - 成功率: {test_summary['success_rate']:.1f}%")
        
        if self.config["email_alerts"]["enabled"]:
            self.send_email_alert(test_summary)
            
    def send_error_alert(self, error_message: str):
        """发送错误告警"""
        self.logger.error(f"监控系统错误: {error_message}")
        
        if self.config["email_alerts"]["enabled"]:
            self.send_email_error_alert(error_message)
            
    def send_email_alert(self, test_summary: Dict[str, Any]):
        """发送邮件告警"""
        try:
            email_config = self.config["email_alerts"]
            
            msg = MIMEMultipart()
            msg['From'] = email_config["sender_email"]
            msg['To'] = ", ".join(email_config["recipient_emails"])
            msg['Subject'] = f"电子教材平台健康告警 - 成功率 {test_summary['success_rate']:.1f}%"
            
            # 构建邮件内容
            body = f"""
电子教材平台健康状态告警

时间: {test_summary['timestamp']}
总测试数: {test_summary['total_tests']}
通过测试: {test_summary['passed_tests']}
失败测试: {test_summary['failed_tests']}
成功率: {test_summary['success_rate']:.1f}%

失败的测试:
"""
            
            for failed_test in test_summary['failed_test_details']:
                body += f"- {failed_test['test']}: {failed_test['message']}\n"
                
            body += "\n请检查平台状态并及时处理。"
            
            msg.attach(MIMEText(body, 'plain', 'utf-8'))
            
            # 发送邮件
            server = smtplib.SMTP(email_config["smtp_server"], email_config["smtp_port"])
            server.starttls()
            server.login(email_config["sender_email"], email_config["sender_password"])
            server.send_message(msg)
            server.quit()
            
            self.logger.info("告警邮件发送成功")
            
        except Exception as e:
            self.logger.error(f"发送告警邮件失败: {e}")
            
    def send_email_error_alert(self, error_message: str):
        """发送错误告警邮件"""
        try:
            email_config = self.config["email_alerts"]
            
            msg = MIMEMultipart()
            msg['From'] = email_config["sender_email"]
            msg['To'] = ", ".join(email_config["recipient_emails"])
            msg['Subject'] = "电子教材平台监控系统错误"
            
            body = f"""
电子教材平台监控系统发生错误

时间: {datetime.now().isoformat()}
错误信息: {error_message}

请检查监控系统状态。
"""
            
            msg.attach(MIMEText(body, 'plain', 'utf-8'))
            
            server = smtplib.SMTP(email_config["smtp_server"], email_config["smtp_port"])
            server.starttls()
            server.login(email_config["sender_email"], email_config["sender_password"])
            server.send_message(msg)
            server.quit()
            
            self.logger.info("错误告警邮件发送成功")
            
        except Exception as e:
            self.logger.error(f"发送错误告警邮件失败: {e}")
            
    def generate_monitor_report(self, test_summary: Dict[str, Any]):
        """生成监控报告"""
        try:
            # 创建报告目录
            report_dir = self.config.get("report_directory", "./monitor_reports")
            os.makedirs(report_dir, exist_ok=True)
            
            # 生成报告文件名
            timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
            report_file = os.path.join(report_dir, f"monitor_report_{timestamp}.json")
            
            # 添加趋势分析
            trend_analysis = self.analyze_trends()
            
            report_data = {
                "current_test": test_summary,
                "trend_analysis": trend_analysis,
                "config": self.config,
                "system_info": {
                    "python_version": sys.version,
                    "platform": sys.platform,
                    "monitor_version": "1.0.0"
                }
            }
            
            with open(report_file, 'w', encoding='utf-8') as f:
                json.dump(report_data, f, ensure_ascii=False, indent=2)
                
            self.logger.info(f"监控报告已生成: {report_file}")
            
        except Exception as e:
            self.logger.error(f"生成监控报告失败: {e}")
            
    def analyze_trends(self) -> Dict[str, Any]:
        """分析趋势"""
        if len(self.test_history) < 2:
            return {"message": "数据不足，无法分析趋势"}
            
        # 最近24小时的数据
        recent_24h = []
        cutoff_time = datetime.now() - timedelta(hours=24)
        
        for test in self.test_history:
            test_time = datetime.fromisoformat(test["timestamp"])
            if test_time >= cutoff_time:
                recent_24h.append(test)
                
        if not recent_24h:
            return {"message": "最近24小时无测试数据"}
            
        # 计算平均成功率
        avg_success_rate = sum(test["success_rate"] for test in recent_24h) / len(recent_24h)
        
        # 检测趋势
        if len(recent_24h) >= 3:
            recent_rates = [test["success_rate"] for test in recent_24h[-3:]]
            if all(recent_rates[i] >= recent_rates[i-1] for i in range(1, len(recent_rates))):
                trend = "上升"
            elif all(recent_rates[i] <= recent_rates[i-1] for i in range(1, len(recent_rates))):
                trend = "下降"
            else:
                trend = "波动"
        else:
            trend = "数据不足"
            
        return {
            "recent_24h_tests": len(recent_24h),
            "avg_success_rate_24h": avg_success_rate,
            "trend": trend,
            "latest_success_rate": recent_24h[-1]["success_rate"] if recent_24h else 0
        }
        
    def generate_daily_summary(self):
        """生成每日汇总报告"""
        self.logger.info("生成每日汇总报告")
        
        # 获取今天的测试数据
        today = datetime.now().date()
        today_tests = []
        
        for test in self.test_history:
            test_date = datetime.fromisoformat(test["timestamp"]).date()
            if test_date == today:
                today_tests.append(test)
                
        if not today_tests:
            self.logger.info("今日无测试数据")
            return
            
        # 计算今日统计
        total_tests_today = len(today_tests)
        avg_success_rate = sum(test["success_rate"] for test in today_tests) / total_tests_today
        min_success_rate = min(test["success_rate"] for test in today_tests)
        max_success_rate = max(test["success_rate"] for test in today_tests)
        
        summary = {
            "date": today.isoformat(),
            "total_test_cycles": total_tests_today,
            "avg_success_rate": avg_success_rate,
            "min_success_rate": min_success_rate,
            "max_success_rate": max_success_rate,
            "tests": today_tests
        }
        
        # 保存每日汇总
        summary_dir = "./daily_summaries"
        os.makedirs(summary_dir, exist_ok=True)
        
        summary_file = os.path.join(summary_dir, f"daily_summary_{today.strftime('%Y%m%d')}.json")
        
        try:
            with open(summary_file, 'w', encoding='utf-8') as f:
                json.dump(summary, f, ensure_ascii=False, indent=2)
            self.logger.info(f"每日汇总已保存: {summary_file}")
        except Exception as e:
            self.logger.error(f"保存每日汇总失败: {e}")
            
    def start_monitoring(self):
        """开始监控"""
        self.logger.info("启动电子教材平台监控系统")
        self.logger.info(f"测试间隔: {self.config['test_interval_minutes']} 分钟")
        self.logger.info(f"告警阈值: {self.alert_threshold}%")
        
        # 立即运行一次测试
        self.run_test_cycle()
        
        # 设置定时任务
        schedule.every(self.config["test_interval_minutes"]).minutes.do(self.run_test_cycle)
        schedule.every().day.at("00:00").do(self.generate_daily_summary)
        
        self.logger.info("监控系统已启动，按 Ctrl+C 停止")
        
        try:
            while True:
                schedule.run_pending()
                time.sleep(60)  # 每分钟检查一次
        except KeyboardInterrupt:
            self.logger.info("监控系统已停止")
            
    def show_status(self):
        """显示当前状态"""
        print("\n" + "="*50)
        print("📊 电子教材平台监控状态")
        print("="*50)
        
        if not self.test_history:
            print("暂无测试历史数据")
            return
            
        latest_test = self.test_history[-1]
        print(f"最新测试时间: {latest_test['timestamp']}")
        print(f"最新成功率: {latest_test['success_rate']:.1f}%")
        print(f"历史记录数: {len(self.test_history)}")
        
        # 显示趋势
        trend_analysis = self.analyze_trends()
        if "avg_success_rate_24h" in trend_analysis:
            print(f"24小时平均成功率: {trend_analysis['avg_success_rate_24h']:.1f}%")
            print(f"趋势: {trend_analysis['trend']}")
            
if __name__ == "__main__":
    import argparse
    
    parser = argparse.ArgumentParser(description="电子教材平台监控系统")
    parser.add_argument("--config", default="monitor_config.json", help="配置文件路径")
    parser.add_argument("--status", action="store_true", help="显示当前状态")
    parser.add_argument("--test-once", action="store_true", help="运行一次测试")
    
    args = parser.parse_args()
    
    monitor = PlatformMonitor(args.config)
    
    if args.status:
        monitor.show_status()
    elif args.test_once:
        monitor.run_test_cycle()
    else:
        monitor.start_monitoring()