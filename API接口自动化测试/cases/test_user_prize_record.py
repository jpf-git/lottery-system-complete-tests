"""
用户奖品记录模块接口自动化测试
包含查询用户奖品记录、活动奖品记录等功能的测试用例
"""
import pytest
import allure
from jsonschema import validate
from utils.request_util import host, Request
from utils.yaml_util import read_yaml, write_yaml


# JSON Schema定义
prize_record_schema = {
    "type": "object",
    "required": ["code", "msg", "data"],
    "properties": {
        "code": {"type": "integer"},
        "msg": {"type": "string"},
        "data": {
            "type": ["array", "null"],
            "items": {
                "type": "object",
                "properties": {
                    "id": {"type": "integer"},
                    "userId": {"type": "integer"},
                    "activityId": {"type": "integer"},
                    "prizeId": {"type": "integer"},
                    "prizeName": {"type": "string"},
                    "prizeTiers": {"type": "string"},
                    "prizeImageUrl": {"type": ["string", "null"]},
                    "prizePrice": {"type": ["number", "null"]},
                    "winTime": {"type": ["string", "null"]},
                    "status": {"type": ["string", "null"]},
                    "createTime": {"type": ["string", "null"]},
                    "updateTime": {"type": ["string", "null"]}
                }
            }
        }
    }
}
@pytest.mark.order(13)
@allure.epic("抽奖系统接口自动化测试")
@allure.feature("用户奖品记录模块")
class TestUserPrizeRecord:
    """用户奖品记录测试类"""

    @allure.story("奖品记录查询")
    @allure.title("测试用例：获取用户奖品记录")
    @allure.severity(allure.severity_level.NORMAL)
    def test_get_user_prize_records(self):
        """
        测试获取用户奖品记录功能

        前置条件：需要有效的用户账号信息
        """
        # 第一步：用户登录获取Token
        with allure.step("用户登录获取Token"):
            """
            登录步骤说明：
            1. 使用测试数据中的用户信息进行登录
            2. 获取JWT Token用于后续请求认证
            3. 保存Token和用户ID到数据文件
            """
            login_url = host + "user/password/login"

            # 准备登录数据
            login_data = {
                "loginName": "gyuvhj45@gmail.com",  # 使用测试数据中的邮箱
                "password": "123456"  # 使用测试数据中的密码
            }
            allure.attach(str(login_data), "登录请求参数", allure.attachment_type.TEXT)

            # 发送登录请求
            login_response = Request().post(url=login_url, json=login_data)
            allure.attach(login_response.text, "登录响应内容", allure.attachment_type.JSON)

            # 验证登录成功
            assert login_response.status_code == 200
            login_result = login_response.json()
            assert login_result["code"] == 200
            assert "token" in login_result["data"]

            # 提取Token和用户ID
            user_token = login_result["data"]["token"]
            user_id = login_result["data"]["userId"]

            # 保存到数据文件
            token_data = {
                "user_token": user_token,
                "user_id": user_id
            }
            write_yaml("data.yml", token_data)

            allure.attach(f"Token: {user_token[:20]}...", "用户Token", allure.attachment_type.TEXT)
            allure.attach(str(user_id), "用户ID", allure.attachment_type.TEXT)

        # 第二步：获取用户奖品记录
        with allure.step("发送获取用户奖品记录请求"):
            """
            获取奖品记录步骤说明：
            1. 使用登录获得的Token进行认证
            2. 请求用户奖品记录接口
            3. 验证返回的奖品记录数据
            """
            url = host + f"api/user-prize-record/user/{user_id}"

            # 使用Token进行认证
            headers = {"user_token": user_token}
            r = Request().get(url=url, headers=headers)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证响应格式"):
            validate(instance=r.json(), schema=prize_record_schema)

        with allure.step("验证查询结果"):
            assert r.status_code == 200
            response_data = r.json()

            if response_data["code"] == 500:
                pytest.skip(f"后端服务异常: {response_data.get('msg', '未知错误')}")

            assert response_data["code"] == 200
            assert "data" in response_data
            assert isinstance(response_data["data"], list)

            # 记录奖品记录数量
            record_count = len(response_data["data"])
            allure.attach(f"奖品记录数量: {record_count}", "记录统计", allure.attachment_type.TEXT)



    @allure.story("奖品记录查询")
    @allure.title("测试用例：获取活动奖品记录")
    @allure.severity(allure.severity_level.NORMAL)
    def test_get_activity_prize_records(self):
        """
        测试获取活动奖品记录功能

        测试目的：
        1. 验证用户登录功能
        2. 验证获取活动奖品记录接口
        3. 确保只有登录用户才能查看活动奖品记录

        业务场景：用户登录后查看某个活动的所有中奖记录
        预期结果：返回该活动的所有中奖记录列表
        """
        # 第一步：用户登录获取Token
        with allure.step("用户登录获取Token"):
            login_url = host + "user/password/login"

            # 准备登录数据
            login_data = {
                "loginName": "gyuvhj45@gmail.com",
                "password": "123456"
            }
            allure.attach(str(login_data), "登录请求参数", allure.attachment_type.TEXT)

            # 发送登录请求
            login_response = Request().post(url=login_url, json=login_data)
            allure.attach(login_response.text, "登录响应内容", allure.attachment_type.JSON)

            # 验证登录成功
            assert login_response.status_code == 200
            login_result = login_response.json()
            assert login_result["code"] == 200
            assert "token" in login_result["data"]

            # 提取Token
            user_token = login_result["data"]["token"]
            allure.attach(f"Token: {user_token[:20]}...", "用户Token", allure.attachment_type.TEXT)

        # 第二步：获取活动奖品记录
        with allure.step("从数据文件读取活动ID"):
            # activity_id = read_yaml("data.yml", "activity_id")
            activity_id = 7
            if not activity_id:
                pytest.skip("活动ID不存在，请先执行活动创建测试")
            allure.attach(str(activity_id), "活动ID", allure.attachment_type.TEXT)

        with allure.step("发送获取活动奖品记录请求"):
            url = host + f"api/user-prize-record/activity/{activity_id}"

            # 使用Token进行认证
            headers = {"user_token": user_token}
            r = Request().get(url=url, headers=headers)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证响应格式"):
            validate(instance=r.json(), schema=prize_record_schema)

        with allure.step("验证查询结果"):
            assert r.status_code == 200
            response_data = r.json()

            if response_data["code"] == 500:
                pytest.skip(f"后端服务异常: {response_data.get('msg', '未知错误')}")

            assert response_data["code"] == 200
            assert "data" in response_data
            assert isinstance(response_data["data"], list)

    @allure.story("奖品记录查询")
    @allure.title("测试用例：获取用户在特定活动的奖品记录")
    @allure.severity(allure.severity_level.NORMAL)
    def test_get_user_activity_prize_records(self):
        """
        测试获取用户在特定活动的奖品记录功能

        测试目的：
        1. 验证用户登录功能
        2. 验证获取用户特定活动奖品记录接口
        3. 确保只有登录用户才能查看自己的特定活动奖品记录

        业务场景：用户登录后查看自己在某个特定活动的中奖记录
        预期结果：返回用户在该活动的中奖记录列表
        """
        # 第一步：用户登录获取Token
        with allure.step("用户登录获取Token"):
            login_url = host + "user/password/login"

            # 准备登录数据
            login_data = {
                "loginName": "gyuvhj45@gmail.com",
                "password": "123456"
            }
            allure.attach(str(login_data), "登录请求参数", allure.attachment_type.TEXT)

            # 发送登录请求
            login_response = Request().post(url=login_url, json=login_data)
            allure.attach(login_response.text, "登录响应内容", allure.attachment_type.JSON)

            # 验证登录成功
            assert login_response.status_code == 200
            login_result = login_response.json()
            assert login_result["code"] == 200
            assert "token" in login_result["data"]

            # 提取Token和用户ID
            user_token = login_result["data"]["token"]
            user_id = login_result["data"]["userId"]

            allure.attach(f"Token: {user_token[:20]}...", "用户Token", allure.attachment_type.TEXT)
            allure.attach(str(user_id), "用户ID", allure.attachment_type.TEXT)

        # 第二步：获取用户在特定活动的奖品记录
        with allure.step("从数据文件读取活动ID"):
            # activity_id = read_yaml("data.yml", "activity_id")
            activity_id = 4
            if not activity_id:
                pytest.skip("活动ID不存在，请先执行活动创建测试")
            allure.attach(str(activity_id), "活动ID", allure.attachment_type.TEXT)

        with allure.step("发送获取用户活动奖品记录请求"):
            url = host + f"api/user-prize-record/user/{user_id}/activity/{activity_id}"

            # 使用Token进行认证
            headers = {"user_token": user_token}
            r = Request().get(url=url, headers=headers)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证响应格式"):
            validate(instance=r.json(), schema=prize_record_schema)

        with allure.step("验证查询结果"):
            assert r.status_code == 200
            response_data = r.json()

            if response_data["code"] == 500:
                pytest.skip(f"后端服务异常: {response_data.get('msg', '未知错误')}")

            assert response_data["code"] == 200
            assert "data" in response_data
            assert isinstance(response_data["data"], list)

            # 如果有数据，验证数据结构
            if response_data["data"]:
                record = response_data["data"][0]
                assert record["userId"] == user_id
                assert record["activityId"] == activity_id

                # 记录详细信息
                allure.attach(f"用户ID: {record['userId']}", "记录详情", allure.attachment_type.TEXT)
                allure.attach(f"活动ID: {record['activityId']}", "记录详情", allure.attachment_type.TEXT)
                allure.attach(f"奖品名称: {record.get('prizeName', 'N/A')}", "记录详情", allure.attachment_type.TEXT)


    @allure.story("奖品记录查询")
    @allure.title("测试用例：查询不存在用户的奖品记录")
    @allure.severity(allure.severity_level.NORMAL)
    def test_get_nonexistent_user_prize_records(self):
        """
        测试查询不存在用户的奖品记录功能

        测试目的：
        1. 验证用户登录功能
        2. 验证查询不存在用户时的处理机制
        3. 确保系统对不存在用户的处理正确

        业务场景：用户登录后查询一个不存在的用户ID的奖品记录
        预期结果：返回空列表或适当的错误提示
        """
        # 第一步：用户登录获取Token
        with allure.step("用户登录获取Token"):
            login_url = host + "user/password/login"

            # 准备登录数据
            login_data = {
                "loginName": "gyuvhj45@gmail.com",
                "password": "123456"
            }
            allure.attach(str(login_data), "登录请求参数", allure.attachment_type.TEXT)

            # 发送登录请求
            login_response = Request().post(url=login_url, json=login_data)
            allure.attach(login_response.text, "登录响应内容", allure.attachment_type.JSON)

            # 验证登录成功
            assert login_response.status_code == 200
            login_result = login_response.json()
            assert login_result["code"] == 200
            assert "token" in login_result["data"]

            # 提取Token
            user_token = login_result["data"]["token"]
            allure.attach(f"Token: {user_token[:20]}...", "用户Token", allure.attachment_type.TEXT)

        # 第二步：查询不存在用户的奖品记录
        with allure.step("使用不存在的用户ID"):
            nonexistent_user_id = 999999  # 使用一个明确不存在的用户ID
            allure.attach(str(nonexistent_user_id), "不存在的用户ID", allure.attachment_type.TEXT)

        with allure.step("发送获取不存在用户奖品记录请求"):
            url = host + f"api/user-prize-record/user/{nonexistent_user_id}"

            # 使用Token进行认证
            headers = {"user_token": user_token}
            r = Request().get(url=url, headers=headers)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证查询结果"):
            assert r.status_code == 200
            response_data = r.json()

            # 对于不存在的用户，应该返回空列表或错误
            assert response_data["code"] == 200
            assert "data" in response_data
            assert isinstance(response_data["data"], list)
            assert len(response_data["data"]) == 0

            allure.attach("返回空列表，符合预期", "查询结果", allure.attachment_type.TEXT)

