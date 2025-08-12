#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
电子教材平台增强功能测试脚本
包含更全面的功能测试、性能测试和错误处理
"""

import requests
import json
import time
import random
import string
import threading
import concurrent.futures
from typing import Dict, Any, Optional, List
from datetime import datetime
import statistics

class EnhancedPlatformTester:
    def __init__(self, base_url: str = "http://localhost:8082"):
        self.base_url = base_url
        self.session = requests.Session()
        self.token = None
        self.user_id = None
        self.test_results = []
        self.performance_data = []
        self.frontend_url = "http://localhost:5174"
        
    def log_test(self, test_name: str, success: bool, message: str = "", duration: float = 0):
        """记录测试结果"""
        result = {
            "test": test_name,
            "success": success,
            "message": message,
            "duration": duration,
            "timestamp": datetime.now().isoformat()
        }
        self.test_results.append(result)
        status = "✅ PASS" if success else "❌ FAIL"
        duration_str = f" ({duration:.3f}s)" if duration > 0 else ""
        print(f"{status} {test_name}: {message}{duration_str}")
        
    def log_performance(self, test_name: str, duration: float, success: bool = True):
        """记录性能数据"""
        self.performance_data.append({
            "test": test_name,
            "duration": duration,
            "success": success,
            "timestamp": datetime.now().isoformat()
        })
        
    def generate_random_user(self) -> Dict[str, str]:
        """生成随机用户数据"""
        random_suffix = ''.join(random.choices(string.ascii_lowercase + string.digits, k=6))
        return {
            "username": f"testuser_{random_suffix}",
            "password": "test123456",
            "email": f"test_{random_suffix}@example.com",
            "nickname": f"测试用户_{random_suffix}"
        }
        
    def test_server_health(self) -> bool:
        """测试服务器健康状态"""
        start_time = time.time()
        try:
            # 测试多个健康检查端点
            endpoints = [
                "/actuator/health",
                "/health", 
                "/api/health",
                "/user/test"
            ]
            
            for endpoint in endpoints:
                try:
                    response = self.session.get(f"{self.base_url}{endpoint}", timeout=5)
                    if response.status_code in [200, 401, 404]:  # 401和404也表示服务器在运行
                        duration = time.time() - start_time
                        self.log_test("服务器健康检查", True, f"服务器响应正常 (端点: {endpoint})", duration)
                        self.log_performance("服务器健康检查", duration)
                        return True
                except:
                    continue
                    
            duration = time.time() - start_time
            self.log_test("服务器健康检查", False, "所有健康检查端点都无法访问", duration)
            return False
            
        except Exception as e:
            duration = time.time() - start_time
            self.log_test("服务器健康检查", False, f"健康检查异常: {str(e)}", duration)
            return False
            
    def test_frontend_availability(self) -> bool:
        """测试前端可用性"""
        start_time = time.time()
        try:
            response = self.session.get(self.frontend_url, timeout=10)
            duration = time.time() - start_time
            
            if response.status_code == 200:
                self.log_test("前端可用性", True, "前端页面可正常访问", duration)
                return True
            else:
                self.log_test("前端可用性", False, f"前端访问异常 (状态码: {response.status_code})", duration)
                return False
                
        except Exception as e:
            duration = time.time() - start_time
            self.log_test("前端可用性", False, f"前端连接失败: {str(e)}", duration)
            return False
            
    def test_user_registration_advanced(self) -> bool:
        """高级用户注册测试"""
        # 测试正常注册
        user_data = self.generate_random_user()
        start_time = time.time()
        
        try:
            form_data = {
                "username": user_data["username"],
                "password": user_data["password"]
            }
            
            response = self.session.post(
                f"{self.base_url}/api/user/register",
                data=form_data,
                headers={"Content-Type": "application/x-www-form-urlencoded"}
            )
            
            duration = time.time() - start_time
            self.log_performance("用户注册", duration)
            
            if response.status_code == 200:
                result = response.json()
                if result.get("code") == 200:
                    self.user_data = user_data
                    self.log_test("用户注册", True, f"用户 {user_data['username']} 注册成功", duration)
                    
                    # 测试重复注册
                    self.test_duplicate_registration(user_data)
                    return True
                else:
                    self.log_test("用户注册", False, f"注册失败: {result.get('message', '未知错误')}", duration)
                    return False
            else:
                self.log_test("用户注册", False, f"HTTP错误: {response.status_code}", duration)
                return False
                
        except Exception as e:
            duration = time.time() - start_time
            self.log_test("用户注册", False, f"请求异常: {str(e)}", duration)
            return False
            
    def test_duplicate_registration(self, user_data: Dict[str, str]):
        """测试重复注册处理"""
        start_time = time.time()
        try:
            form_data = {
                "username": user_data["username"],
                "password": user_data["password"]
            }
            
            response = self.session.post(
                f"{self.base_url}/api/user/register",
                data=form_data,
                headers={"Content-Type": "application/x-www-form-urlencoded"}
            )
            
            duration = time.time() - start_time
            
            if response.status_code == 200:
                result = response.json()
                if result.get("code") != 200:  # 应该返回错误
                    self.log_test("重复注册处理", True, "正确拒绝了重复注册", duration)
                else:
                    self.log_test("重复注册处理", False, "未正确处理重复注册", duration)
            else:
                self.log_test("重复注册处理", False, f"HTTP错误: {response.status_code}", duration)
                
        except Exception as e:
            duration = time.time() - start_time
            self.log_test("重复注册处理", False, f"请求异常: {str(e)}", duration)
            
    def test_login_security(self) -> bool:
        """测试登录安全性"""
        if not hasattr(self, 'user_data'):
            self.log_test("登录安全测试", False, "没有可用的用户数据")
            return False
            
        # 测试正确登录
        start_time = time.time()
        try:
            form_data = {
                "username": self.user_data["username"],
                "password": self.user_data["password"]
            }
            
            response = self.session.post(
                f"{self.base_url}/api/user/login",
                data=form_data,
                headers={"Content-Type": "application/x-www-form-urlencoded"}
            )
            
            duration = time.time() - start_time
            self.log_performance("用户登录", duration)
            
            if response.status_code == 200:
                result = response.json()
                if result.get("code") == 200:
                    self.token = result.get("data")
                    if self.token:
                        self.session.headers.update({"Authorization": self.token})
                    self.log_test("用户登录", True, f"用户 {form_data['username']} 登录成功", duration)
                    
                    # 测试错误密码
                    self.test_wrong_password_login()
                    return True
                else:
                    self.log_test("用户登录", False, f"登录失败: {result.get('message', '未知错误')}", duration)
                    return False
            else:
                self.log_test("用户登录", False, f"HTTP错误: {response.status_code}", duration)
                return False
                
        except Exception as e:
            duration = time.time() - start_time
            self.log_test("用户登录", False, f"请求异常: {str(e)}", duration)
            return False
            
    def test_wrong_password_login(self):
        """测试错误密码登录"""
        start_time = time.time()
        try:
            form_data = {
                "username": self.user_data["username"],
                "password": "wrongpassword123"
            }
            
            response = self.session.post(
                f"{self.base_url}/api/user/login",
                data=form_data,
                headers={"Content-Type": "application/x-www-form-urlencoded"}
            )
            
            duration = time.time() - start_time
            
            if response.status_code == 200:
                result = response.json()
                if result.get("code") != 200:  # 应该返回错误
                    self.log_test("错误密码登录", True, "正确拒绝了错误密码", duration)
                else:
                    self.log_test("错误密码登录", False, "未正确处理错误密码", duration)
            else:
                self.log_test("错误密码登录", False, f"HTTP错误: {response.status_code}", duration)
                
        except Exception as e:
            duration = time.time() - start_time
            self.log_test("错误密码登录", False, f"请求异常: {str(e)}", duration)
            
    def test_level_system_comprehensive(self) -> bool:
        """全面测试等级系统"""
        tests_passed = 0
        total_tests = 5
        
        # 测试获取用户统计
        if self.test_get_user_stats():
            tests_passed += 1
            
        # 测试添加经验值
        if self.test_add_experience_comprehensive():
            tests_passed += 1
            
        # 测试获取成就
        if self.test_get_achievements():
            tests_passed += 1
            
        # 测试学习记录
        if self.test_get_learning_records():
            tests_passed += 1
            
        # 测试排行榜
        if self.test_get_leaderboard():
            tests_passed += 1
            
        success = tests_passed == total_tests
        self.log_test("等级系统综合测试", success, f"通过 {tests_passed}/{total_tests} 项测试")
        return success
        
    def test_get_user_stats(self) -> bool:
        """测试获取用户统计"""
        start_time = time.time()
        try:
            response = self.session.get(f"{self.base_url}/api/level/stats")
            duration = time.time() - start_time
            self.log_performance("获取用户统计", duration)
            
            if response.status_code == 200:
                result = response.json()
                if result.get("code") == 200:
                    stats = result.get("data", {})
                    self.log_test("等级系统-用户统计", True, 
                                f"等级: {stats.get('level', 0)}, 经验: {stats.get('experience', 0)}", duration)
                    return True
                else:
                    self.log_test("等级系统-用户统计", False, f"获取失败: {result.get('message', '未知错误')}", duration)
                    return False
            else:
                self.log_test("等级系统-用户统计", False, f"HTTP错误: {response.status_code}", duration)
                return False
                
        except Exception as e:
            duration = time.time() - start_time
            self.log_test("等级系统-用户统计", False, f"请求异常: {str(e)}", duration)
            return False
            
    def test_add_experience_comprehensive(self) -> bool:
        """全面测试添加经验值"""
        # 测试不同类型的经验值添加
        test_cases = [
            {"experience": 100, "activityType": "QUIZ_COMPLETION", "chapterId": 1},
            {"experience": 50, "activityType": "CHAPTER_READ", "chapterId": 2},
            {"experience": 200, "activityType": "CASE_COMPLETION", "chapterId": 2}
        ]
        
        success_count = 0
        for i, exp_data in enumerate(test_cases):
            start_time = time.time()
            try:
                response = self.session.post(
                    f"{self.base_url}/api/level/addExperience",
                    json=exp_data,
                    headers={"Content-Type": "application/json"}
                )
                
                duration = time.time() - start_time
                self.log_performance(f"添加经验-{i+1}", duration)
                
                if response.status_code == 200:
                    result = response.json()
                    if result.get("code") == 200:
                        data = result.get("data", {})
                        self.log_test(f"等级系统-添加经验{i+1}", True, 
                                    f"添加{exp_data['experience']}经验成功", duration)
                        success_count += 1
                    else:
                        self.log_test(f"等级系统-添加经验{i+1}", False, 
                                    f"添加失败: {result.get('message', '未知错误')}", duration)
                else:
                    self.log_test(f"等级系统-添加经验{i+1}", False, f"HTTP错误: {response.status_code}", duration)
                    
            except Exception as e:
                duration = time.time() - start_time
                self.log_test(f"等级系统-添加经验{i+1}", False, f"请求异常: {str(e)}", duration)
                
        return success_count == len(test_cases)
        
    def test_get_achievements(self) -> bool:
        """测试获取用户成就"""
        start_time = time.time()
        try:
            response = self.session.get(f"{self.base_url}/api/level/achievements")
            duration = time.time() - start_time
            self.log_performance("获取用户成就", duration)
            
            if response.status_code == 200:
                result = response.json()
                if result.get("code") == 200:
                    achievements = result.get("data", [])
                    self.log_test("等级系统-用户成就", True, f"获取到 {len(achievements)} 个成就", duration)
                    return True
                else:
                    self.log_test("等级系统-用户成就", False, f"获取失败: {result.get('message', '未知错误')}", duration)
                    return False
            else:
                self.log_test("等级系统-用户成就", False, f"HTTP错误: {response.status_code}", duration)
                return False
                
        except Exception as e:
            duration = time.time() - start_time
            self.log_test("等级系统-用户成就", False, f"请求异常: {str(e)}", duration)
            return False
            
    def test_get_learning_records(self) -> bool:
        """测试获取学习记录"""
        start_time = time.time()
        try:
            response = self.session.get(f"{self.base_url}/api/level/records")
            duration = time.time() - start_time
            self.log_performance("获取学习记录", duration)
            
            if response.status_code == 200:
                result = response.json()
                if result.get("code") == 200:
                    records = result.get("data", [])
                    self.log_test("等级系统-学习记录", True, f"获取到 {len(records)} 条学习记录", duration)
                    return True
                else:
                    self.log_test("等级系统-学习记录", False, f"获取失败: {result.get('message', '未知错误')}", duration)
                    return False
            else:
                self.log_test("等级系统-学习记录", False, f"HTTP错误: {response.status_code}", duration)
                return False
                
        except Exception as e:
            duration = time.time() - start_time
            self.log_test("等级系统-学习记录", False, f"请求异常: {str(e)}", duration)
            return False
            
    def test_get_leaderboard(self) -> bool:
        """测试获取排行榜"""
        start_time = time.time()
        try:
            response = self.session.get(f"{self.base_url}/api/level/leaderboard")
            duration = time.time() - start_time
            self.log_performance("获取排行榜", duration)
            
            if response.status_code == 200:
                result = response.json()
                if result.get("code") == 200:
                    leaderboard = result.get("data", [])
                    self.log_test("等级系统-排行榜", True, f"获取到 {len(leaderboard)} 条排行榜数据", duration)
                    return True
                else:
                    self.log_test("等级系统-排行榜", False, f"获取失败: {result.get('message', '未知错误')}", duration)
                    return False
            else:
                self.log_test("等级系统-排行榜", False, f"HTTP错误: {response.status_code}", duration)
                return False
                
        except Exception as e:
            duration = time.time() - start_time
            self.log_test("等级系统-排行榜", False, f"请求异常: {str(e)}", duration)
            return False
            
    def test_concurrent_requests(self) -> bool:
        """测试并发请求处理"""
        print("\n🔄 测试并发请求处理...")
        
        def make_request():
            try:
                response = self.session.get(f"{self.base_url}/api/level/stats")
                return response.status_code == 200
            except:
                return False
                
        start_time = time.time()
        concurrent_requests = 10
        
        with concurrent.futures.ThreadPoolExecutor(max_workers=concurrent_requests) as executor:
            futures = [executor.submit(make_request) for _ in range(concurrent_requests)]
            results = [future.result() for future in concurrent.futures.as_completed(futures)]
            
        duration = time.time() - start_time
        success_count = sum(results)
        success_rate = success_count / concurrent_requests * 100
        
        self.log_test("并发请求处理", success_rate >= 80, 
                     f"{success_count}/{concurrent_requests} 请求成功 ({success_rate:.1f}%)", duration)
        
        return success_rate >= 80
        
    def test_performance_benchmarks(self):
        """性能基准测试"""
        print("\n⚡ 性能基准测试...")
        
        if not self.performance_data:
            self.log_test("性能基准测试", False, "没有性能数据可分析")
            return
            
        # 分析响应时间
        response_times = [data['duration'] for data in self.performance_data if data['success']]
        
        if response_times:
            avg_time = statistics.mean(response_times)
            max_time = max(response_times)
            min_time = min(response_times)
            
            # 性能基准
            performance_good = avg_time < 1.0  # 平均响应时间小于1秒
            max_time_acceptable = max_time < 5.0  # 最大响应时间小于5秒
            
            performance_score = "优秀" if performance_good and max_time_acceptable else "需要优化"
            
            self.log_test("性能基准测试", performance_good and max_time_acceptable,
                         f"平均: {avg_time:.3f}s, 最大: {max_time:.3f}s, 最小: {min_time:.3f}s - {performance_score}")
        else:
            self.log_test("性能基准测试", False, "没有成功的请求数据")
            
    def run_comprehensive_tests(self):
        """运行全面测试"""
        print("🚀 开始电子教材平台全面功能测试...\n")
        
        # 基础连接测试
        print("🔗 基础连接测试...")
        if not self.test_server_health():
            print("❌ 服务器连接失败，终止测试")
            return
            
        self.test_frontend_availability()
        
        # 用户系统测试
        print("\n👤 用户系统测试...")
        self.test_user_registration_advanced()
        self.test_login_security()
        
        # 等级系统测试
        print("\n🎮 等级系统测试...")
        self.test_level_system_comprehensive()
        
        # 并发和性能测试
        self.test_concurrent_requests()
        self.test_performance_benchmarks()
        
        # 生成测试报告
        self.generate_comprehensive_report()
        
    def generate_comprehensive_report(self):
        """生成全面测试报告"""
        print("\n" + "="*60)
        print("📊 电子教材平台测试报告")
        print("="*60)
        
        total_tests = len(self.test_results)
        passed_tests = sum(1 for result in self.test_results if result["success"])
        failed_tests = total_tests - passed_tests
        
        print(f"📈 测试概览:")
        print(f"  总测试数: {total_tests}")
        print(f"  通过: {passed_tests} ✅")
        print(f"  失败: {failed_tests} ❌")
        print(f"  成功率: {(passed_tests/total_tests*100):.1f}%")
        
        # 性能分析
        if self.performance_data:
            response_times = [data['duration'] for data in self.performance_data if data['success']]
            if response_times:
                avg_time = statistics.mean(response_times)
                print(f"\n⚡ 性能分析:")
                print(f"  平均响应时间: {avg_time:.3f}s")
                print(f"  最快响应: {min(response_times):.3f}s")
                print(f"  最慢响应: {max(response_times):.3f}s")
        
        # 失败测试详情
        if failed_tests > 0:
            print("\n❌ 失败的测试:")
            for result in self.test_results:
                if not result["success"]:
                    print(f"  - {result['test']}: {result['message']}")
                    
        # 优化建议
        print("\n💡 优化建议:")
        self.generate_optimization_suggestions()
        
        # 保存详细报告
        report_data = {
            "summary": {
                "total": total_tests,
                "passed": passed_tests,
                "failed": failed_tests,
                "success_rate": passed_tests/total_tests*100,
                "test_time": datetime.now().isoformat()
            },
            "test_results": self.test_results,
            "performance_data": self.performance_data
        }
        
        report_file = f"enhanced_test_report_{int(time.time())}.json"
        with open(report_file, 'w', encoding='utf-8') as f:
            json.dump(report_data, f, ensure_ascii=False, indent=2)
        print(f"\n📄 详细报告已保存到: {report_file}")
        
    def generate_optimization_suggestions(self):
        """生成优化建议"""
        failed_tests = [r for r in self.test_results if not r["success"]]
        
        if not failed_tests:
            print("  🎉 所有测试都通过了！平台功能运行良好。")
            print("  🔧 建议定期运行测试以确保系统稳定性。")
            return
            
        suggestions = []
        
        # 检查服务器连接问题
        if any("服务器" in test["test"] for test in failed_tests):
            suggestions.extend([
                "1. 检查后端服务器是否正常启动 (端口8082)",
                "2. 确认数据库连接是否正常",
                "3. 检查服务器日志中的错误信息"
            ])
            
        # 检查前端问题
        if any("前端" in test["test"] for test in failed_tests):
            suggestions.extend([
                "4. 检查前端开发服务器是否启动 (端口5174)",
                "5. 确认前端构建是否成功"
            ])
            
        # 检查用户系统问题
        user_tests = [t for t in failed_tests if "用户" in t["test"] or "登录" in t["test"]]
        if user_tests:
            suggestions.extend([
                "6. 检查用户相关API接口实现",
                "7. 验证数据库用户表结构",
                "8. 检查用户认证和授权逻辑"
            ])
            
        # 检查等级系统问题
        level_tests = [t for t in failed_tests if "等级系统" in t["test"]]
        if level_tests:
            suggestions.extend([
                "9. 检查等级系统相关API接口实现",
                "10. 验证等级系统数据库表结构",
                "11. 确认LevelController和LevelService配置"
            ])
            
        # 检查性能问题
        if any("性能" in test["test"] or "并发" in test["test"] for test in failed_tests):
            suggestions.extend([
                "12. 优化数据库查询性能",
                "13. 考虑添加缓存机制",
                "14. 检查服务器资源使用情况"
            ])
            
        for suggestion in suggestions:
            print(f"  {suggestion}")
            
if __name__ == "__main__":
    import sys
    base_url = sys.argv[1] if len(sys.argv) > 1 else "http://localhost:8082"
    
    tester = EnhancedPlatformTester(base_url)
    tester.run_comprehensive_tests()