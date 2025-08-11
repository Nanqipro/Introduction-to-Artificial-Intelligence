#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
电子教材平台功能测试脚本
测试登录注册、等级系统等核心功能
"""

import requests
import json
import time
import random
import string
from typing import Dict, Any, Optional

class PlatformTester:
    def __init__(self, base_url: str = "http://localhost:8082"):
        self.base_url = base_url
        self.session = requests.Session()
        self.token = None
        self.user_id = None
        self.test_results = []
        
    def log_test(self, test_name: str, success: bool, message: str = ""):
        """记录测试结果"""
        result = {
            "test": test_name,
            "success": success,
            "message": message,
            "timestamp": time.strftime("%Y-%m-%d %H:%M:%S")
        }
        self.test_results.append(result)
        status = "✅ PASS" if success else "❌ FAIL"
        print(f"{status} {test_name}: {message}")
        
    def generate_random_user(self) -> Dict[str, str]:
        """生成随机用户数据"""
        random_suffix = ''.join(random.choices(string.ascii_lowercase + string.digits, k=6))
        return {
            "username": f"testuser_{random_suffix}",
            "password": "test123456",
            "email": f"test_{random_suffix}@example.com",
            "nickname": f"测试用户_{random_suffix}"
        }
        
    def test_server_connection(self) -> bool:
        """测试服务器连接"""
        try:
            response = self.session.get(f"{self.base_url}/health", timeout=5)
            if response.status_code == 200:
                self.log_test("服务器连接", True, "服务器响应正常")
                return True
            else:
                # 尝试访问其他端点
                response = self.session.get(f"{self.base_url}/api/users/test", timeout=5)
                self.log_test("服务器连接", True, f"服务器可访问 (状态码: {response.status_code})")
                return True
        except requests.exceptions.RequestException as e:
            self.log_test("服务器连接", False, f"连接失败: {str(e)}")
            return False
            
    def test_user_registration(self) -> bool:
        """测试用户注册功能"""
        user_data = self.generate_random_user()
        
        try:
            # 使用表单数据格式
            form_data = {
                "username": user_data["username"],
                "password": user_data["password"]
            }
            
            response = self.session.post(
                f"{self.base_url}/user/register",
                data=form_data,
                headers={"Content-Type": "application/x-www-form-urlencoded"}
            )
            
            if response.status_code == 200:
                result = response.json()
                if result.get("code") == 200 and result.get("message") == "success":
                    self.user_data = user_data
                    self.log_test("用户注册", True, f"用户 {user_data['username']} 注册成功")
                    return True
                else:
                    self.log_test("用户注册", False, f"注册失败: {result.get('message', '未知错误')}")
                    return False
            else:
                self.log_test("用户注册", False, f"HTTP错误: {response.status_code}")
                return False
                
        except Exception as e:
            self.log_test("用户注册", False, f"请求异常: {str(e)}")
            return False
            
    def test_user_login(self) -> bool:
        """测试用户登录功能"""
        if not hasattr(self, 'user_data'):
            self.log_test("用户登录", False, "没有可用的用户数据")
            return False
            
        # 使用表单数据格式
        form_data = {
            "username": self.user_data["username"],
            "password": self.user_data["password"]
        }
        
        try:
            response = self.session.post(
                f"{self.base_url}/user/login",
                data=form_data,
                headers={"Content-Type": "application/x-www-form-urlencoded"}
            )
            
            if response.status_code == 200:
                result = response.json()
                if result.get("code") == 200 and result.get("message") == "success":
                    self.token = result.get("data")
                    if self.token:
                        self.session.headers.update({"Authorization": self.token})
                    self.log_test("用户登录", True, f"用户 {form_data['username']} 登录成功")
                    return True
                else:
                    self.log_test("用户登录", False, f"登录失败: {result.get('message', '未知错误')}")
                    return False
            else:
                self.log_test("用户登录", False, f"HTTP错误: {response.status_code}")
                return False
                
        except Exception as e:
            self.log_test("用户登录", False, f"请求异常: {str(e)}")
            return False
            
    def test_get_user_info(self) -> bool:
        """测试获取用户信息"""
        try:
            response = self.session.get(f"{self.base_url}/user/userInfo")
            
            if response.status_code == 200:
                result = response.json()
                if result.get("code") == 200 and result.get("message") == "success":
                    user_info = result.get("data", {})
                    self.log_test("获取用户信息", True, f"获取到用户信息: {user_info.get('username', 'N/A')}")
                    return True
                else:
                    self.log_test("获取用户信息", False, f"获取失败: {result.get('message', '未知错误')}")
                    return False
            else:
                self.log_test("获取用户信息", False, f"HTTP错误: {response.status_code}")
                return False
                
        except Exception as e:
            self.log_test("获取用户信息", False, f"请求异常: {str(e)}")
            return False
            
    def test_level_system_stats(self) -> bool:
        """测试等级系统 - 获取用户统计"""
        try:
            response = self.session.get(f"{self.base_url}/api/level/stats")
            
            if response.status_code == 200:
                result = response.json()
                if result.get("code") == 200 and result.get("message") == "success":
                    stats = result.get("data", {})
                    self.log_test("等级系统-用户统计", True, 
                                f"等级: {stats.get('level', 0)}, 经验: {stats.get('experience', 0)}")
                    return True
                else:
                    self.log_test("等级系统-用户统计", False, f"获取失败: {result.get('message', '未知错误')}")
                    return False
            else:
                self.log_test("等级系统-用户统计", False, f"HTTP错误: {response.status_code}")
                return False
                
        except Exception as e:
            self.log_test("等级系统-用户统计", False, f"请求异常: {str(e)}")
            return False
            
    def test_add_experience(self) -> bool:
        """测试等级系统 - 添加经验值"""
        exp_data = {
            "experience": 100,
            "activityType": "QUIZ_COMPLETION",
            "chapterId": 1
        }
        
        try:
            response = self.session.post(
                f"{self.base_url}/api/level/addExperience",
                json=exp_data,
                headers={"Content-Type": "application/json"}
            )
            
            if response.status_code == 200:
                result = response.json()
                if result.get("code") == 200 and result.get("message") == "success":
                    data = result.get("data", {})
                    self.log_test("等级系统-添加经验", True, 
                                f"添加经验成功, 当前等级: {data.get('currentLevel', 'N/A')}")
                    return True
                else:
                    self.log_test("等级系统-添加经验", False, f"添加失败: {result.get('message', '未知错误')}")
                    return False
            else:
                self.log_test("等级系统-添加经验", False, f"HTTP错误: {response.status_code}")
                return False
                
        except Exception as e:
            self.log_test("等级系统-添加经验", False, f"请求异常: {str(e)}")
            return False
            
    def test_get_achievements(self) -> bool:
        """测试等级系统 - 获取用户成就"""
        try:
            response = self.session.get(f"{self.base_url}/api/level/achievements")
            
            if response.status_code == 200:
                result = response.json()
                if result.get("code") == 200 and result.get("message") == "success":
                    achievements = result.get("data", [])
                    self.log_test("等级系统-用户成就", True, f"获取到 {len(achievements)} 个成就")
                    return True
                else:
                    self.log_test("等级系统-用户成就", False, f"获取失败: {result.get('message', '未知错误')}")
                    return False
            else:
                self.log_test("等级系统-用户成就", False, f"HTTP错误: {response.status_code}")
                return False
                
        except Exception as e:
            self.log_test("等级系统-用户成就", False, f"请求异常: {str(e)}")
            return False
            
    def test_get_learning_records(self) -> bool:
        """测试等级系统 - 获取学习记录"""
        try:
            response = self.session.get(f"{self.base_url}/api/level/records")
            
            if response.status_code == 200:
                result = response.json()
                if result.get("code") == 200 and result.get("message") == "success":
                    records = result.get("data", [])
                    self.log_test("等级系统-学习记录", True, f"获取到 {len(records)} 条学习记录")
                    return True
                else:
                    self.log_test("等级系统-学习记录", False, f"获取失败: {result.get('message', '未知错误')}")
                    return False
            else:
                self.log_test("等级系统-学习记录", False, f"HTTP错误: {response.status_code}")
                return False
                
        except Exception as e:
            self.log_test("等级系统-学习记录", False, f"请求异常: {str(e)}")
            return False
            
    def test_get_leaderboard(self) -> bool:
        """测试等级系统 - 获取排行榜"""
        try:
            response = self.session.get(f"{self.base_url}/api/level/leaderboard")
            
            if response.status_code == 200:
                result = response.json()
                if result.get("code") == 200 and result.get("message") == "success":
                    leaderboard = result.get("data", [])
                    self.log_test("等级系统-排行榜", True, f"获取到 {len(leaderboard)} 条排行榜数据")
                    return True
                else:
                    self.log_test("等级系统-排行榜", False, f"获取失败: {result.get('message', '未知错误')}")
                    return False
            else:
                self.log_test("等级系统-排行榜", False, f"HTTP错误: {response.status_code}")
                return False
                
        except Exception as e:
            self.log_test("等级系统-排行榜", False, f"请求异常: {str(e)}")
            return False
            
    def run_all_tests(self):
        """运行所有测试"""
        print("🚀 开始平台功能测试...\n")
        
        # 基础连接测试
        if not self.test_server_connection():
            print("❌ 服务器连接失败，终止测试")
            return
            
        # 用户系统测试
        print("\n📝 测试用户系统...")
        self.test_user_registration()
        self.test_user_login()
        self.test_get_user_info()
        
        # 等级系统测试
        print("\n🎮 测试等级系统...")
        self.test_level_system_stats()
        self.test_add_experience()
        self.test_get_achievements()
        self.test_get_learning_records()
        self.test_get_leaderboard()
        
        # 生成测试报告
        self.generate_report()
        
    def generate_report(self):
        """生成测试报告"""
        print("\n" + "="*50)
        print("📊 测试报告")
        print("="*50)
        
        total_tests = len(self.test_results)
        passed_tests = sum(1 for result in self.test_results if result["success"])
        failed_tests = total_tests - passed_tests
        
        print(f"总测试数: {total_tests}")
        print(f"通过: {passed_tests} ✅")
        print(f"失败: {failed_tests} ❌")
        print(f"成功率: {(passed_tests/total_tests*100):.1f}%")
        
        if failed_tests > 0:
            print("\n❌ 失败的测试:")
            for result in self.test_results:
                if not result["success"]:
                    print(f"  - {result['test']}: {result['message']}")
                    
        print("\n💡 优化建议:")
        self.generate_optimization_suggestions()
        
        # 保存详细报告到文件
        report_file = f"test_report_{int(time.time())}.json"
        with open(report_file, 'w', encoding='utf-8') as f:
            json.dump({
                "summary": {
                    "total": total_tests,
                    "passed": passed_tests,
                    "failed": failed_tests,
                    "success_rate": passed_tests/total_tests*100
                },
                "details": self.test_results
            }, f, ensure_ascii=False, indent=2)
        print(f"\n📄 详细报告已保存到: {report_file}")
        
    def generate_optimization_suggestions(self):
        """生成优化建议"""
        failed_tests = [r for r in self.test_results if not r["success"]]
        
        if not failed_tests:
            print("  🎉 所有测试都通过了！平台功能运行良好。")
            return
            
        suggestions = []
        
        # 检查服务器连接问题
        if any("服务器连接" in test["test"] for test in failed_tests):
            suggestions.append("1. 检查后端服务器是否正常启动 (端口8080)")
            suggestions.append("2. 确认数据库连接是否正常")
            
        # 检查用户系统问题
        user_tests = [t for t in failed_tests if "用户" in t["test"]]
        if user_tests:
            suggestions.append("3. 检查用户相关API接口实现")
            suggestions.append("4. 验证数据库用户表结构")
            
        # 检查等级系统问题
        level_tests = [t for t in failed_tests if "等级系统" in t["test"]]
        if level_tests:
            suggestions.append("5. 检查等级系统相关API接口实现")
            suggestions.append("6. 验证等级系统数据库表结构")
            suggestions.append("7. 确认LevelController和LevelService是否正确配置")
            
        for suggestion in suggestions:
            print(f"  {suggestion}")
            
if __name__ == "__main__":
    # 可以通过命令行参数指定服务器地址
    import sys
    base_url = sys.argv[1] if len(sys.argv) > 1 else "http://localhost:8082"
    
    tester = PlatformTester(base_url)
    tester.run_all_tests()