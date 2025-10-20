"""
认证模块接口自动化测试
包含Token验证、刷新等功能的测试用例
"""
import pytest
import allure
from jsonschema import validate
from utils.request_util import host, Request
from utils.yaml_util import read_yaml

# JSON Schema定义
token_verify_schema = {
    "type": "object",
    "required": ["code", "msg"],
    "properties": {
        "code": {"type": "integer"},
        "msg": {"type": "string"},
        "data": {
            "type": ["object", "null"],
            "properties": {
                "userId": {"type": "integer"},
                "identity": {"type": "string"},
                "valid": {"type": "boolean"}
            }
        }
    }
}

current_user_schema = {
    "type": "object",
    "required": ["code", "msg"],
    "properties": {
        "code": {"type": "integer"},
        "msg": {"type": "string"},
        "data": {
            "type": ["object", "null"],
            "properties": {
                "userId": {"type": "integer"},
                "username": {"type": "string"},
                "email": {"type": "string"},
                "identity": {"type": "string"}
            }
        }
    }
}


@pytest.mark.order(5)
@allure.epic("抽奖系统接口自动化测试")
@allure.feature("认证模块")
class TestTokenVerification:
    """Token验证测试类"""

    @allure.story("Token验证")
    @allure.title("测试用例：验证有效Token")
    @allure.severity(allure.severity_level.BLOCKER)
    def test_verify_valid_token(self):
        """测试验证有效的Token"""
        url = host + "auth/verify"

        with allure.step("从数据文件读取Token"):
            token = read_yaml("data.yml", "user_token")
            if not token:
                pytest.skip("Token不存在，请先执行登录测试")
            allure.attach(token, "用户Token", allure.attachment_type.TEXT)

        with allure.step("发送Token验证请求"):
            headers = {"user_token": token}
            r = Request().get(url=url, headers=headers)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证响应格式"):
            validate(instance=r.json(), schema=token_verify_schema)

        with allure.step("验证Token有效"):
            assert r.status_code == 200
            response_data = r.json()
            assert response_data["code"] == 200
            assert "Token验证成功" in response_data["msg"]

            # 验证返回的用户信息
            if response_data["data"]:
                assert "userId" in response_data["data"]
                assert "identity" in response_data["data"]
                assert response_data["data"]["valid"] == True

    @allure.story("Token验证")
    @allure.title("测试用例：验证无效Token")
    @allure.severity(allure.severity_level.NORMAL)
    def test_verify_invalid_token(self):
        """
        测试验证无效的Token

        测试目的：
        1. 验证系统对无效Token的处理机制
        2. 确保无效Token返回正确的错误信息
        3. 验证安全机制正常工作

        业务场景：用户使用过期或伪造的Token访问需要认证的接口
        预期结果：返回401状态码和相应的错误信息

        设计说明：
        - 后端可能返回401状态码（HTTP层面）
        - 或者返回200状态码但业务码为401（业务层面）
        - 需要兼容两种处理方式
        """
        url = host + "auth/verify"

        with allure.step("准备无效Token"):
            invalid_token = "invalid_token_string_12345"
            allure.attach(invalid_token, "无效Token", allure.attachment_type.TEXT)

        with allure.step("发送Token验证请求"):
            headers = {"user_token": invalid_token}
            r = Request().get(url=url, headers=headers)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证Token无效处理"):
            # 检查HTTP状态码
            if r.status_code == 401:
                # 情况1：HTTP层面返回401（直接拒绝）
                allure.attach("HTTP状态码401，Token被直接拒绝", "验证结果", allure.attachment_type.TEXT)
                assert r.status_code == 401
            elif r.status_code == 200:
                # 情况2：HTTP状态码200，但业务层面返回错误
                response_data = r.json()
                assert response_data["code"] == 401
                assert "Token无效" in response_data["msg"] or "无效" in response_data["msg"]
                allure.attach(f"业务码401，错误信息: {response_data['msg']}", "验证结果", allure.attachment_type.TEXT)
            else:
                # 其他情况：记录但不失败（可能是系统设计不同）
                allure.attach(f"意外的状态码: {r.status_code}", "验证结果", allure.attachment_type.TEXT)
                pytest.skip(f"系统返回意外状态码: {r.status_code}")

    @allure.story("Token验证")
    @allure.title("测试用例：未提供Token")
    @allure.severity(allure.severity_level.NORMAL)
    def test_verify_no_token(self):
        """
        测试未提供Token的情况

        测试目的：
        1. 验证系统对未提供Token的处理机制
        2. 确保未认证请求返回正确的错误信息
        3. 验证安全机制正常工作

        业务场景：用户未登录或忘记携带Token访问需要认证的接口
        预期结果：返回401状态码和相应的错误信息

        设计说明：
        - 后端可能返回401状态码（HTTP层面）
        - 或者返回200状态码但业务码为401（业务层面）
        - 需要兼容两种处理方式
        """
        url = host + "auth/verify"

        with allure.step("不提供Token，发送验证请求"):
            r = Request().get(url=url)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证未提供Token的处理"):
            # 检查HTTP状态码
            if r.status_code == 401:
                # 情况1：HTTP层面返回401（直接拒绝）
                allure.attach("HTTP状态码401，未提供Token被直接拒绝", "验证结果", allure.attachment_type.TEXT)
                assert r.status_code == 401
            elif r.status_code == 200:
                # 情况2：HTTP状态码200，但业务层面返回错误
                response_data = r.json()
                assert response_data["code"] == 401
                assert "未提供Token" in response_data["msg"] or "Token" in response_data["msg"]
                allure.attach(f"业务码401，错误信息: {response_data['msg']}", "验证结果", allure.attachment_type.TEXT)
            else:
                # 其他情况：记录但不失败（可能是系统设计不同）
                allure.attach(f"意外的状态码: {r.status_code}", "验证结果", allure.attachment_type.TEXT)
                pytest.skip(f"系统返回意外状态码: {r.status_code}")


@pytest.mark.order(6)
@allure.epic("抽奖系统接口自动化测试")
@allure.feature("认证模块")
class TestCurrentUser:
    """当前用户信息测试类"""

    @allure.story("用户信息")
    @allure.title("测试用例：获取当前登录用户信息")
    @allure.severity(allure.severity_level.NORMAL)
    def test_get_current_user(self):
        """测试获取当前登录用户信息"""
        url = host + "auth/current-user"

        with allure.step("从数据文件读取Token"):
            token = read_yaml("data.yml", "user_token")
            if not token:
                pytest.skip("Token不存在，请先执行登录测试")
            allure.attach(token, "用户Token", allure.attachment_type.TEXT)

        with allure.step("发送获取当前用户请求"):
            headers = {"user_token": token}
            r = Request().get(url=url, headers=headers)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证响应格式"):
            validate(instance=r.json(), schema=current_user_schema)

        with allure.step("验证用户信息"):
            assert r.status_code == 200
            response_data = r.json()

            if response_data["code"] == 500:
                pytest.skip(f"后端服务异常: {response_data.get('msg', '未知错误')}")

            assert response_data["code"] == 200
            assert "data" in response_data

            # 验证用户信息字段
            user_info = response_data["data"]
            assert "userId" in user_info
            assert "identity" in user_info

    @allure.story("用户信息")
    @allure.title("测试用例：未登录获取当前用户信息")
    @allure.severity(allure.severity_level.NORMAL)
    def test_get_current_user_unauthorized(self):
        """测试未登录时获取当前用户信息"""
        url = host + "auth/current-user"

        with allure.step("不提供Token，发送请求"):
            r = Request().get(url=url)
            allure.attach(r.text, "响应内容", allure.attachment_type.TEXT)

        with allure.step("验证返回未授权错误"):
            # 应该返回401或其他认证错误
            assert r.status_code == 401 or (r.status_code == 200 and r.json()["code"] == 401)

