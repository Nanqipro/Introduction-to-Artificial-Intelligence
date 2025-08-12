#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import requests
import json
import random
import string

def generate_test_user():
    """生成测试用户"""
    username = 'test' + ''.join(random.choices(string.ascii_lowercase + string.digits, k=8))
    # 确保用户名长度在4-16位之间，符合后端验证规则
    if len(username) > 16:
        username = username[:16]
    password = ''.join(random.choices(string.ascii_letters + string.digits, k=12))
    # 确保密码长度在4-16位之间
    if len(password) > 16:
        password = password[:16]
    return username, password

def test_leaderboard_api():
    """测试排行榜API"""
    base_url = "http://localhost:8082"
    
    print("🔍 调试排行榜API...")
    
    # 1. 注册用户
    username, password = generate_test_user()
    register_data = {
        "username": username,
        "password": password,
        "nickname": f"测试用户_{username}"
    }
    
    print(f"📝 注册用户: {username}")
    register_response = requests.post(f"{base_url}/api/user/register", data=register_data)
    print(f"注册响应: {register_response.status_code} - {register_response.text}")
    
    if register_response.status_code != 200:
        print("❌ 用户注册失败")
        return
    
    # 检查注册响应内容
    try:
        register_result = register_response.json()
        if register_result.get('code') != 200:
            print(f"❌ 用户注册失败: {register_result.get('message')}")
            return
    except:
        print("❌ 注册响应解析失败")
        return
    
    # 2. 登录获取token
    login_data = {
        "username": username,
        "password": password
    }
    
    print(f"🔑 用户登录: {username}")
    login_response = requests.post(f"{base_url}/api/user/login", data=login_data)
    print(f"登录响应: {login_response.status_code} - {login_response.text}")
    
    if login_response.status_code != 200:
        print("❌ 用户登录失败")
        return
    
    # 检查登录响应内容
    try:
        login_result = login_response.json()
        if login_result.get('code') != 200:
            print(f"❌ 登录失败: {login_result.get('message')}")
            return
        token = login_result.get('data')
        if not token:
            print("❌ 登录失败: 未获取到token")
            return
    except:
        print("❌ 登录响应解析失败")
        return
    print(f"✅ 获取到token: {token[:20]}...")
    
    # 3. 添加一些经验值
    headers = {'Authorization': token}
    exp_data = {
        "experience": 500,
        "activityType": "quiz",
        "chapterId": 1,
        "score": 85
    }
    
    print("📈 添加经验值...")
    exp_response = requests.post(f"{base_url}/api/level/addExperience", json=exp_data, headers=headers)
    print(f"添加经验响应: {exp_response.status_code} - {exp_response.text}")
    
    # 4. 测试排行榜API
    print("🏆 测试排行榜API...")
    
    # 测试不同的limit值
    for limit in [1, 5, 10, 50]:
        print(f"\n--- 测试 limit={limit} ---")
        leaderboard_response = requests.get(f"{base_url}/api/level/leaderboard?limit={limit}", headers=headers)
        print(f"状态码: {leaderboard_response.status_code}")
        print(f"响应内容: {leaderboard_response.text}")
        
        if leaderboard_response.status_code == 200:
            try:
                data = leaderboard_response.json()
                if data.get('code') == 200:
                    leaderboard = data.get('data', [])
                    print(f"✅ 成功获取排行榜，共 {len(leaderboard)} 个用户")
                    for i, user in enumerate(leaderboard[:3]):
                        print(f"  {i+1}. {user.get('username')} - 等级:{user.get('level')} - 经验:{user.get('experience')} - 称号:{user.get('title')}")
                else:
                    print(f"❌ API返回失败: {data.get('message')}")
            except Exception as e:
                print(f"❌ 解析响应失败: {e}")
        else:
            print(f"❌ HTTP请求失败")
    
    # 5. 测试用户统计API
    print("\n📊 测试用户统计API...")
    stats_response = requests.get(f"{base_url}/api/level/stats", headers=headers)
    print(f"用户统计响应: {stats_response.status_code} - {stats_response.text}")

if __name__ == "__main__":
    test_leaderboard_api()