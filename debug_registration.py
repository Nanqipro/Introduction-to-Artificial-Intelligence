#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
调试用户注册功能
"""

import asyncio
import aiohttp
import random
import string
import json
from typing import Dict

class RegistrationDebugger:
    def __init__(self, base_url: str = "http://localhost:8082"):
        self.base_url = base_url
        
    def generate_test_user(self) -> Dict[str, str]:
        """生成测试用户"""
        # 用户名格式: ^[a-zA-Z0-9_-]{4,16}$
        # 生成符合规则的用户名，长度不超过16位
        suffix = ''.join(random.choices(string.ascii_lowercase + string.digits, k=6))
        return {
            "username": f"test{suffix}",  # test + 6位随机字符 = 10位，符合4-16位要求
            "password": "test123456"  # 密码也要符合同样的规则
        }
        
    async def test_single_registration(self):
        """测试单个用户注册"""
        user_data = self.generate_test_user()
        print(f"🧪 测试用户注册: {user_data['username']}")
        
        timeout = aiohttp.ClientTimeout(total=30)
        async with aiohttp.ClientSession(timeout=timeout) as session:
            try:
                # 测试注册
                form_data = aiohttp.FormData()
                form_data.add_field('username', user_data['username'])
                form_data.add_field('password', user_data['password'])
                
                async with session.post(f"{self.base_url}/user/register", data=form_data) as response:
                    print(f"📊 注册响应状态码: {response.status}")
                    
                    try:
                        response_text = await response.text()
                        print(f"📄 响应内容: {response_text}")
                        
                        if response.content_type == 'application/json':
                            response_data = await response.json()
                            print(f"📋 JSON数据: {json.dumps(response_data, ensure_ascii=False, indent=2)}")
                        
                    except Exception as e:
                        print(f"❌ 解析响应失败: {e}")
                        
                # 如果注册成功，测试登录
                if response.status == 200:
                    print(f"\n🔐 测试用户登录: {user_data['username']}")
                    
                    login_form = aiohttp.FormData()
                    login_form.add_field('username', user_data['username'])
                    login_form.add_field('password', user_data['password'])
                    
                    async with session.post(f"{self.base_url}/user/login", data=login_form) as login_response:
                        print(f"📊 登录响应状态码: {login_response.status}")
                        
                        try:
                            login_text = await login_response.text()
                            print(f"📄 登录响应内容: {login_text}")
                            
                            if login_response.content_type == 'application/json':
                                login_data = await login_response.json()
                                print(f"📋 登录JSON数据: {json.dumps(login_data, ensure_ascii=False, indent=2)}")
                                
                                if login_data.get('code') == 200:
                                    token = login_data.get('data', '')
                                    print(f"🎫 获取到token: {token[:50]}..." if len(token) > 50 else f"🎫 获取到token: {token}")
                                    
                                    # 测试使用token访问API
                                    await self.test_api_with_token(session, token)
                                    
                        except Exception as e:
                            print(f"❌ 解析登录响应失败: {e}")
                            
            except Exception as e:
                print(f"❌ 请求失败: {e}")
                
    async def test_api_with_token(self, session: aiohttp.ClientSession, token: str):
        """使用token测试API访问"""
        print(f"\n🔍 测试API访问...")
        
        test_endpoints = [
            "/api/level/stats",
            "/api/level/leaderboard",
            "/api/level/achievements"
        ]
        
        headers = {
            "Authorization": token  # 后端直接使用token，不需要Bearer前缀
        }
        
        for endpoint in test_endpoints:
            try:
                async with session.get(f"{self.base_url}{endpoint}", headers=headers) as response:
                    print(f"📊 {endpoint}: 状态码 {response.status}")
                    
                    if response.status != 200:
                        response_text = await response.text()
                        print(f"   ❌ 错误响应: {response_text}")
                    else:
                        try:
                            data = await response.json()
                            print(f"   ✅ 成功: {data.get('message', '无消息')}")
                        except:
                            print(f"   ✅ 成功: 非JSON响应")
                            
            except Exception as e:
                print(f"   ❌ 请求 {endpoint} 失败: {e}")
                
    async def test_multiple_registrations(self, count: int = 5):
        """测试多个用户注册"""
        print(f"\n🔄 测试批量注册 {count} 个用户...")
        
        timeout = aiohttp.ClientTimeout(total=30)
        async with aiohttp.ClientSession(timeout=timeout) as session:
            tasks = []
            users = []
            
            for i in range(count):
                user_data = self.generate_test_user()
                users.append(user_data)
                tasks.append(self.register_single_user(session, user_data, i+1))
                
            results = await asyncio.gather(*tasks, return_exceptions=True)
            
            success_count = 0
            for i, result in enumerate(results):
                if isinstance(result, Exception):
                    print(f"❌ 用户 {i+1} 注册异常: {result}")
                elif result:
                    success_count += 1
                    print(f"✅ 用户 {i+1} 注册成功")
                else:
                    print(f"❌ 用户 {i+1} 注册失败")
                    
            print(f"\n📊 批量注册结果: {success_count}/{count} 成功")
            
    async def register_single_user(self, session: aiohttp.ClientSession, user_data: Dict[str, str], user_num: int) -> bool:
        """注册单个用户"""
        try:
            form_data = aiohttp.FormData()
            form_data.add_field('username', user_data['username'])
            form_data.add_field('password', user_data['password'])
            
            async with session.post(f"{self.base_url}/user/register", data=form_data) as response:
                if response.status == 200:
                    try:
                        data = await response.json()
                        return data.get('code') == 200
                    except:
                        return False
                else:
                    # 打印错误详情
                    error_text = await response.text()
                    print(f"   用户 {user_num} 注册失败 (状态码 {response.status}): {error_text}")
                    return False
                    
        except Exception as e:
            print(f"   用户 {user_num} 注册异常: {e}")
            return False
            
    async def test_server_connection(self):
        """测试服务器连接"""
        print(f"🔗 测试服务器连接: {self.base_url}")
        
        timeout = aiohttp.ClientTimeout(total=10)
        async with aiohttp.ClientSession(timeout=timeout) as session:
            try:
                async with session.get(f"{self.base_url}/") as response:
                    print(f"📊 服务器响应状态码: {response.status}")
                    
                    if response.status in [200, 401, 404]:  # 这些都表示服务器在运行
                        print(f"✅ 服务器正在运行")
                        return True
                    else:
                        print(f"⚠️ 服务器响应异常")
                        return False
                        
            except Exception as e:
                print(f"❌ 无法连接到服务器: {e}")
                return False

async def main():
    debugger = RegistrationDebugger()
    
    print("🚀 开始调试用户注册功能\n")
    
    # 1. 测试服务器连接
    if not await debugger.test_server_connection():
        print("❌ 服务器连接失败，请检查后端服务是否启动")
        return
        
    print("\n" + "="*50)
    
    # 2. 测试单个用户注册和登录
    await debugger.test_single_registration()
    
    print("\n" + "="*50)
    
    # 3. 测试批量注册
    await debugger.test_multiple_registrations(5)
    
    print("\n🎉 调试完成！")

if __name__ == "__main__":
    asyncio.run(main())