#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
电子教材平台压力测试脚本
测试平台在高并发和高负载情况下的性能表现
"""

import asyncio
import aiohttp
import time
import json
import random
import string
import statistics
import threading
import concurrent.futures
from datetime import datetime
from typing import List, Dict, Any, Tuple
import matplotlib.pyplot as plt
import numpy as np
from dataclasses import dataclass

@dataclass
class TestResult:
    """测试结果数据类"""
    endpoint: str
    method: str
    status_code: int
    response_time: float
    success: bool
    error_message: str = ""
    timestamp: float = 0

class StressTester:
    def __init__(self, base_url: str = "http://localhost:8082"):
        self.base_url = base_url
        self.results: List[TestResult] = []
        self.users_pool = []  # 用户池
        self.tokens_pool = []  # token池
        
    def generate_test_user(self) -> Dict[str, str]:
        """生成测试用户"""
        # 用户名格式: ^[a-zA-Z0-9_-]{4,16}$
        # 生成符合规则的用户名，长度不超过16位
        suffix = ''.join(random.choices(string.ascii_lowercase + string.digits, k=8))
        return {
            "username": f"stress{suffix}",  # stress + 8位随机字符 = 14位，符合4-16位要求
            "password": "stress123456"  # 密码也要符合同样的规则
        }
        
    async def register_user(self, session: aiohttp.ClientSession, user_data: Dict[str, str]) -> TestResult:
        """注册用户"""
        start_time = time.time()
        endpoint = "/user/register"
        
        try:
            form_data = aiohttp.FormData()
            form_data.add_field('username', user_data['username'])
            form_data.add_field('password', user_data['password'])
            
            async with session.post(f"{self.base_url}{endpoint}", data=form_data) as response:
                response_time = time.time() - start_time
                success = response.status == 200
                
                if success:
                    result_data = await response.json()
                    success = result_data.get('code') == 200
                    
                return TestResult(
                    endpoint=endpoint,
                    method="POST",
                    status_code=response.status,
                    response_time=response_time,
                    success=success,
                    timestamp=start_time
                )
                
        except Exception as e:
            response_time = time.time() - start_time
            return TestResult(
                endpoint=endpoint,
                method="POST",
                status_code=0,
                response_time=response_time,
                success=False,
                error_message=str(e),
                timestamp=start_time
            )
            
    async def login_user(self, session: aiohttp.ClientSession, user_data: Dict[str, str]) -> Tuple[TestResult, str]:
        """用户登录"""
        start_time = time.time()
        endpoint = "/user/login"
        token = ""
        
        try:
            form_data = aiohttp.FormData()
            form_data.add_field('username', user_data['username'])
            form_data.add_field('password', user_data['password'])
            
            async with session.post(f"{self.base_url}{endpoint}", data=form_data) as response:
                response_time = time.time() - start_time
                success = response.status == 200
                
                if success:
                    result_data = await response.json()
                    success = result_data.get('code') == 200
                    if success:
                        token = result_data.get('data', '')
                        
                return TestResult(
                    endpoint=endpoint,
                    method="POST",
                    status_code=response.status,
                    response_time=response_time,
                    success=success,
                    timestamp=start_time
                ), token
                
        except Exception as e:
            response_time = time.time() - start_time
            return TestResult(
                endpoint=endpoint,
                method="POST",
                status_code=0,
                response_time=response_time,
                success=False,
                error_message=str(e),
                timestamp=start_time
            ), ""
            
    async def test_api_endpoint(self, session: aiohttp.ClientSession, endpoint: str, 
                                method: str = "GET", headers: Dict = None, data: Dict = None) -> TestResult:
        """测试API端点"""
        start_time = time.time()
        
        try:
            kwargs = {}
            if headers:
                kwargs['headers'] = headers
            if data:
                if method == "POST":
                    kwargs['json'] = data
                    
            async with session.request(method, f"{self.base_url}{endpoint}", **kwargs) as response:
                response_time = time.time() - start_time
                success = response.status == 200
                
                if success:
                    try:
                        result_data = await response.json()
                        success = result_data.get('code') == 200
                    except:
                        pass  # 有些端点可能不返回JSON
                        
                return TestResult(
                    endpoint=endpoint,
                    method=method,
                    status_code=response.status,
                    response_time=response_time,
                    success=success,
                    timestamp=start_time
                )
                
        except Exception as e:
            response_time = time.time() - start_time
            return TestResult(
                endpoint=endpoint,
                method=method,
                status_code=0,
                response_time=response_time,
                success=False,
                error_message=str(e),
                timestamp=start_time
            )
            
    async def prepare_test_data(self, num_users: int = 50):
        """准备测试数据"""
        print(f"🔧 准备测试数据 - 创建 {num_users} 个测试用户...")
        
        connector = aiohttp.TCPConnector(limit=100, limit_per_host=50)
        timeout = aiohttp.ClientTimeout(total=30)
        
        async with aiohttp.ClientSession(connector=connector, timeout=timeout) as session:
            # 批量注册用户
            tasks = []
            for i in range(num_users):
                user_data = self.generate_test_user()
                self.users_pool.append(user_data)
                tasks.append(self.register_user(session, user_data))
                
            register_results = await asyncio.gather(*tasks, return_exceptions=True)
            
            # 统计注册成功率
            successful_registers = sum(1 for r in register_results 
                                     if isinstance(r, TestResult) and r.success)
            print(f"✅ 用户注册完成: {successful_registers}/{num_users} 成功")
            
            # 批量登录获取token
            login_tasks = []
            for user_data in self.users_pool[:successful_registers]:
                login_tasks.append(self.login_user(session, user_data))
                
            login_results = await asyncio.gather(*login_tasks, return_exceptions=True)
            
            # 收集token
            for result in login_results:
                if isinstance(result, tuple) and len(result) == 2:
                    test_result, token = result
                    if test_result.success and token:
                        self.tokens_pool.append(token)
                        
            print(f"✅ 用户登录完成: {len(self.tokens_pool)} 个有效token")
            
    async def run_concurrent_load_test(self, concurrent_users: int = 20, 
                                       duration_seconds: int = 60) -> List[TestResult]:
        """运行并发负载测试"""
        print(f"🚀 开始并发负载测试 - {concurrent_users} 并发用户，持续 {duration_seconds} 秒")
        
        # 测试端点配置
        test_endpoints = [
            {
                "endpoint": "/api/level/stats",
                "method": "GET",
                "weight": 30,  # 权重，表示被调用的频率
                "requires_auth": True
            },
            {
                "endpoint": "/api/level/leaderboard",
                "method": "GET",
                "weight": 20,
                "requires_auth": True
            },
            {
                "endpoint": "/api/level/achievements",
                "method": "GET",
                "weight": 15,
                "requires_auth": True
            },
            {
                "endpoint": "/api/level/records",
                "method": "GET",
                "weight": 15,
                "requires_auth": True
            },
            {
                "endpoint": "/api/level/addExperience",
                "method": "POST",
                "weight": 20,
                "requires_auth": True,
                "data": {
                    "experience": lambda: random.randint(10, 100),
                    "activityType": lambda: random.choice(["QUIZ_COMPLETION", "CHAPTER_READ", "CASE_COMPLETION"]),
                    "chapterId": lambda: random.randint(1, 5)
                }
            }
        ]
        
        # 创建权重列表用于随机选择
        weighted_endpoints = []
        for endpoint_config in test_endpoints:
            weighted_endpoints.extend([endpoint_config] * endpoint_config["weight"])
            
        results = []
        start_time = time.time()
        
        async def user_simulation(user_id: int):
            """模拟单个用户的行为"""
            connector = aiohttp.TCPConnector(limit=10)
            timeout = aiohttp.ClientTimeout(total=10)
            
            async with aiohttp.ClientSession(connector=connector, timeout=timeout) as session:
                user_results = []
                
                while time.time() - start_time < duration_seconds:
                    # 随机选择一个端点
                    endpoint_config = random.choice(weighted_endpoints)
                    
                    # 准备请求参数
                    headers = {}
                    if endpoint_config["requires_auth"] and self.tokens_pool:
                        token = random.choice(self.tokens_pool)
                        headers["Authorization"] = token  # 后端直接使用token，不需要Bearer前缀
                        
                    data = None
                    if "data" in endpoint_config:
                        data = {}
                        for key, value_func in endpoint_config["data"].items():
                            if callable(value_func):
                                data[key] = value_func()
                            else:
                                data[key] = value_func
                                
                    # 执行请求
                    result = await self.test_api_endpoint(
                        session,
                        endpoint_config["endpoint"],
                        endpoint_config["method"],
                        headers,
                        data
                    )
                    
                    user_results.append(result)
                    
                    # 随机等待时间，模拟真实用户行为
                    await asyncio.sleep(random.uniform(0.1, 2.0))
                    
                return user_results
                
        # 启动所有用户模拟
        tasks = [user_simulation(i) for i in range(concurrent_users)]
        all_user_results = await asyncio.gather(*tasks, return_exceptions=True)
        
        # 收集所有结果
        for user_results in all_user_results:
            if isinstance(user_results, list):
                results.extend(user_results)
                
        print(f"✅ 负载测试完成 - 总请求数: {len(results)}")
        return results
        
    async def run_spike_test(self, max_concurrent: int = 100, spike_duration: int = 30) -> List[TestResult]:
        """运行峰值测试"""
        print(f"⚡ 开始峰值测试 - 最大 {max_concurrent} 并发，持续 {spike_duration} 秒")
        
        results = []
        connector = aiohttp.TCPConnector(limit=200, limit_per_host=100)
        timeout = aiohttp.ClientTimeout(total=5)
        
        async with aiohttp.ClientSession(connector=connector, timeout=timeout) as session:
            # 创建大量并发请求
            tasks = []
            
            for i in range(max_concurrent):
                # 随机选择端点
                endpoint = random.choice(["/api/level/stats", "/api/level/leaderboard", "/actuator/health"])
                headers = {}
                
                if endpoint != "/actuator/health" and self.tokens_pool:
                    token = random.choice(self.tokens_pool)
                    headers["Authorization"] = token  # 后端直接使用token，不需要Bearer前缀
                    
                tasks.append(self.test_api_endpoint(session, endpoint, "GET", headers))
                
            # 执行所有请求
            spike_results = await asyncio.gather(*tasks, return_exceptions=True)
            
            for result in spike_results:
                if isinstance(result, TestResult):
                    results.append(result)
                    
        print(f"✅ 峰值测试完成 - 总请求数: {len(results)}")
        return results
        
    def analyze_results(self, results: List[TestResult], test_name: str = "压力测试"):
        """分析测试结果"""
        if not results:
            print("❌ 没有测试结果可分析")
            return
            
        print(f"\n📊 {test_name} 结果分析")
        print("=" * 50)
        
        # 基本统计
        total_requests = len(results)
        successful_requests = sum(1 for r in results if r.success)
        failed_requests = total_requests - successful_requests
        success_rate = (successful_requests / total_requests) * 100
        
        print(f"总请求数: {total_requests}")
        print(f"成功请求: {successful_requests}")
        print(f"失败请求: {failed_requests}")
        print(f"成功率: {success_rate:.2f}%")
        
        # 响应时间统计
        response_times = [r.response_time for r in results if r.success]
        if response_times:
            avg_time = statistics.mean(response_times)
            median_time = statistics.median(response_times)
            min_time = min(response_times)
            max_time = max(response_times)
            p95_time = np.percentile(response_times, 95)
            p99_time = np.percentile(response_times, 99)
            
            print(f"\n⏱️ 响应时间统计 (秒):")
            print(f"平均: {avg_time:.3f}")
            print(f"中位数: {median_time:.3f}")
            print(f"最小: {min_time:.3f}")
            print(f"最大: {max_time:.3f}")
            print(f"95%分位: {p95_time:.3f}")
            print(f"99%分位: {p99_time:.3f}")
            
        # 错误分析
        if failed_requests > 0:
            print(f"\n❌ 错误分析:")
            error_counts = {}
            for result in results:
                if not result.success:
                    error_key = f"{result.status_code}: {result.error_message or 'HTTP Error'}"
                    error_counts[error_key] = error_counts.get(error_key, 0) + 1
                    
            for error, count in sorted(error_counts.items(), key=lambda x: x[1], reverse=True):
                print(f"  {error}: {count} 次")
                
        # 端点性能分析
        endpoint_stats = {}
        for result in results:
            if result.endpoint not in endpoint_stats:
                endpoint_stats[result.endpoint] = []
            endpoint_stats[result.endpoint].append(result)
            
        print(f"\n🎯 端点性能分析:")
        for endpoint, endpoint_results in endpoint_stats.items():
            total = len(endpoint_results)
            successful = sum(1 for r in endpoint_results if r.success)
            success_rate = (successful / total) * 100
            
            if successful > 0:
                avg_time = statistics.mean([r.response_time for r in endpoint_results if r.success])
                print(f"  {endpoint}: {successful}/{total} ({success_rate:.1f}%) - 平均响应: {avg_time:.3f}s")
            else:
                print(f"  {endpoint}: {successful}/{total} ({success_rate:.1f}%) - 全部失败")
                
        # 生成性能评级
        self.generate_performance_rating(success_rate, response_times)
        
    def generate_performance_rating(self, success_rate: float, response_times: List[float]):
        """生成性能评级"""
        print(f"\n🏆 性能评级:")
        
        # 成功率评级
        if success_rate >= 99:
            success_grade = "A+"
        elif success_rate >= 95:
            success_grade = "A"
        elif success_rate >= 90:
            success_grade = "B"
        elif success_rate >= 80:
            success_grade = "C"
        else:
            success_grade = "D"
            
        print(f"  可用性: {success_grade} ({success_rate:.1f}%)")
        
        # 响应时间评级
        performance_grade = "D"  # 默认值
        if response_times:
            avg_time = statistics.mean(response_times)
            p95_time = np.percentile(response_times, 95)
            
            if avg_time <= 0.1 and p95_time <= 0.5:
                performance_grade = "A+"
            elif avg_time <= 0.5 and p95_time <= 1.0:
                performance_grade = "A"
            elif avg_time <= 1.0 and p95_time <= 2.0:
                performance_grade = "B"
            elif avg_time <= 2.0 and p95_time <= 5.0:
                performance_grade = "C"
            else:
                performance_grade = "D"
                
            print(f"  性能: {performance_grade} (平均: {avg_time:.3f}s, P95: {p95_time:.3f}s)")
        else:
            print(f"  性能: {performance_grade} (无有效响应时间数据)")
            
        # 综合评级
        grades = {'A+': 5, 'A': 4, 'B': 3, 'C': 2, 'D': 1}
        avg_grade_score = (grades.get(success_grade, 1) + grades.get(performance_grade, 1)) / 2
        
        for grade, score in grades.items():
            if avg_grade_score >= score:
                overall_grade = grade
                break
        else:
            overall_grade = "D"
            
        print(f"  综合评级: {overall_grade}")
        
        # 优化建议
        suggestions = []
        if success_rate < 95:
            suggestions.append("提高系统稳定性，减少错误率")
        if response_times and statistics.mean(response_times) > 1.0:
            suggestions.append("优化响应时间，考虑添加缓存或优化数据库查询")
        if response_times and np.percentile(response_times, 95) > 2.0:
            suggestions.append("优化长尾延迟，检查慢查询和资源瓶颈")
            
        if suggestions:
            print(f"\n💡 优化建议:")
            for i, suggestion in enumerate(suggestions, 1):
                print(f"  {i}. {suggestion}")
        else:
            print(f"\n🎉 系统性能表现优秀！")
            
    def save_results(self, results: List[TestResult], test_name: str):
        """保存测试结果"""
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        filename = f"stress_test_{test_name}_{timestamp}.json"
        
        # 转换为可序列化的格式
        serializable_results = []
        for result in results:
            serializable_results.append({
                "endpoint": result.endpoint,
                "method": result.method,
                "status_code": result.status_code,
                "response_time": result.response_time,
                "success": result.success,
                "error_message": result.error_message,
                "timestamp": result.timestamp
            })
            
        report_data = {
            "test_name": test_name,
            "test_time": datetime.now().isoformat(),
            "total_requests": len(results),
            "successful_requests": sum(1 for r in results if r.success),
            "results": serializable_results
        }
        
        try:
            with open(filename, 'w', encoding='utf-8') as f:
                json.dump(report_data, f, ensure_ascii=False, indent=2)
            print(f"\n📄 测试结果已保存到: {filename}")
        except Exception as e:
            print(f"\n❌ 保存测试结果失败: {e}")
            
    async def run_comprehensive_stress_test(self):
        """运行全面压力测试"""
        print("🚀 开始电子教材平台全面压力测试\n")
        
        # 准备测试数据
        await self.prepare_test_data(num_users=30)
        
        if not self.tokens_pool:
            print("❌ 无法获取有效的认证token，跳过需要认证的测试")
            return
            
        # 1. 并发负载测试
        print("\n" + "="*60)
        load_results = await self.run_concurrent_load_test(
            concurrent_users=10, 
            duration_seconds=30
        )
        self.analyze_results(load_results, "并发负载测试")
        self.save_results(load_results, "load_test")
        
        # 等待一段时间让系统恢复
        print("\n⏳ 等待系统恢复...")
        await asyncio.sleep(5)
        
        # 2. 峰值测试
        print("\n" + "="*60)
        spike_results = await self.run_spike_test(
            max_concurrent=50, 
            spike_duration=10
        )
        self.analyze_results(spike_results, "峰值测试")
        self.save_results(spike_results, "spike_test")
        
        # 生成综合报告
        self.generate_comprehensive_report(load_results, spike_results)
        
    def generate_comprehensive_report(self, load_results: List[TestResult], 
                                    spike_results: List[TestResult]):
        """生成综合测试报告"""
        print("\n" + "="*60)
        print("📊 电子教材平台压力测试综合报告")
        print("="*60)
        
        # 负载测试汇总
        load_success_rate = (sum(1 for r in load_results if r.success) / len(load_results)) * 100
        load_avg_time = statistics.mean([r.response_time for r in load_results if r.success])
        
        # 峰值测试汇总
        spike_success_rate = (sum(1 for r in spike_results if r.success) / len(spike_results)) * 100
        spike_avg_time = statistics.mean([r.response_time for r in spike_results if r.success])
        
        print(f"📈 测试汇总:")
        print(f"  负载测试: {len(load_results)} 请求, {load_success_rate:.1f}% 成功率, {load_avg_time:.3f}s 平均响应")
        print(f"  峰值测试: {len(spike_results)} 请求, {spike_success_rate:.1f}% 成功率, {spike_avg_time:.3f}s 平均响应")
        
        # 系统容量评估
        print(f"\n🏗️ 系统容量评估:")
        if load_success_rate >= 95 and spike_success_rate >= 90:
            capacity_rating = "优秀"
            print(f"  系统在测试负载下表现{capacity_rating}，可以处理预期的用户负载")
        elif load_success_rate >= 90 and spike_success_rate >= 80:
            capacity_rating = "良好"
            print(f"  系统表现{capacity_rating}，在高峰期可能需要额外监控")
        else:
            capacity_rating = "需要优化"
            print(f"  系统性能{capacity_rating}，建议进行性能调优")
            
        # 推荐的生产环境配置
        print(f"\n⚙️ 生产环境建议:")
        if capacity_rating == "优秀":
            print(f"  - 当前配置可以支持生产环境")
            print(f"  - 建议设置监控告警阈值: 响应时间 > 1s, 错误率 > 5%")
        elif capacity_rating == "良好":
            print(f"  - 建议增加服务器资源或优化代码")
            print(f"  - 考虑添加负载均衡和缓存")
            print(f"  - 设置监控告警阈值: 响应时间 > 0.5s, 错误率 > 3%")
        else:
            print(f"  - 需要进行性能优化后再部署到生产环境")
            print(f"  - 检查数据库查询性能")
            print(f"  - 考虑使用缓存和CDN")
            print(f"  - 优化应用程序代码")
            
if __name__ == "__main__":
    import argparse
    
    parser = argparse.ArgumentParser(description="电子教材平台压力测试")
    parser.add_argument("--url", default="http://localhost:8082", help="服务器地址")
    parser.add_argument("--users", type=int, default=10, help="并发用户数")
    parser.add_argument("--duration", type=int, default=30, help="测试持续时间(秒)")
    parser.add_argument("--spike", type=int, default=50, help="峰值测试并发数")
    
    args = parser.parse_args()
    
    async def main():
        tester = StressTester(args.url)
        
        # 运行自定义测试
        if args.users != 10 or args.duration != 30:
            await tester.prepare_test_data(num_users=max(args.users, 20))
            results = await tester.run_concurrent_load_test(args.users, args.duration)
            tester.analyze_results(results, f"自定义负载测试({args.users}用户)")
        else:
            # 运行全面测试
            await tester.run_comprehensive_stress_test()
            
    # 运行测试
    try:
        asyncio.run(main())
    except KeyboardInterrupt:
        print("\n⏹️ 测试被用户中断")
    except Exception as e:
        print(f"\n❌ 测试执行失败: {e}")