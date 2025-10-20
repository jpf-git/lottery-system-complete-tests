# 导入正确的 validate 函数（确保是 from jsonschema import validate）
from jsonschema import validate
from utils.request_util import host, Request
import allure
import pytest

from utils.yaml_util import write_yaml, read_yaml

# JSON Schema定义
# 修改后（匹配实际返回的 msg）
user_register_schema = {
    "type": "object",
    "required": ["code", "msg", "data"],  # 改为 msg
    "properties": {
        "code": {
            "type": "integer"
        },
        "msg": {  # 改为 msg
            "type": "string"
        },
        "data": {
            "anyOf": [
                {"type": "null"},
                {
                    "type": "object",
                    "properties": {
                        "userId": {
                            "type": "integer"
                        }
                    }
                }
            ]
        }
    }
}
# 2. 登录接口Schema（关键修改：将 message 全部改为 msg，匹配接口返回）
user_login_schema = {
    "type": "object",
    "required": ["code", "msg", "data"],  # 原 message → 改为 msg
    "properties": {
        "code": {"type": "integer"},
        "msg": {"type": "string"},  # 原 message → 改为 msg
        "data": {
            "type": ["object", "null"],
            "properties": {
                "token": {"type": "string"},
                "identity": {"type": "string"},
                "userId": {"type": "integer"}
            }
        }
    }
}
@pytest.mark.order(1)
@allure.epic("抽奖系统接口自动化测试")
@allure.feature("用户模块")
class TestUserRegister:
    url = host + "user/register"

    @allure.story("用户注册")
    @allure.title("测试用例：邮箱注册成功")
    @allure.severity(allure.severity_level.BLOCKER)
    # 完整的测试数据（非空且符合规则）
    @pytest.mark.parametrize("register_data", [
        {
            "name": "测试用户001",
            "mail": "test_user_001@example.com",
            "phoneNumber": "13800138004",
            "password": "123456",
            "identity": "USER"  # 按需补充，若接口不需要可删除
        }
    ])
    def test_register_success_with_email(self, register_data):
        with allure.step("准备注册数据"):
            data = {
                "name": register_data["name"],
                "mail": register_data["mail"],
                "phoneNumber": register_data["phoneNumber"],
                "password": register_data["password"],
                "identity": register_data.get("identity")  # 按需添加
            }
            allure.attach(str(data), "请求参数", allure.attachment_type.TEXT)

        with allure.step("发送注册请求"):
            r = Request().post(url=self.url, json=data)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证响应格式"):
            validate(instance=r.json(), schema=user_register_schema)  # 此时格式匹配，不会报错

        with allure.step("验证响应结果"):
            assert r.status_code == 200
            response_data = r.json()
            assert response_data["code"] == 200  # 业务成功状态码
            assert "成功" in response_data["msg"]  # 断言正确的提示字段

            # 保存 userId 到 YAML（若之前已解决 write_yaml 问题）
            if response_data["data"] and "userId" in response_data["data"]:
                user_data = {"user_id": response_data["data"]["userId"]}
                from utils.yaml_util import write_yaml  # 确保正确导入
                write_yaml("data.yml", user_data)


    @allure.story("用户注册")
    @allure.title("测试用例：重复邮箱注册失败")
    @allure.severity(allure.severity_level.NORMAL)
    @pytest.mark.parametrize("register_data", [
        {
            "name": "重复用户",
            "mail": "test_user_001@example.com",  # 使用已注册的邮箱
            "phoneNumber": "13800138002",
            "password": "123456",
            "identity": "USER"
        }
    ])
    def test_register_fail_duplicate_email(self, register_data):
        """测试重复邮箱注册失败"""
        with allure.step("准备注册数据（重复邮箱）"):
            data = {
                "userName": register_data["name"],
                "mail": register_data["mail"],
                "phoneNumber": register_data["phoneNumber"],
                "password": register_data["password"],
                "identity": register_data.get("identity")

            }
            allure.attach(str(data), "请求参数", allure.attachment_type.TEXT)

        with allure.step("发送注册请求"):
            r = Request().post(url=self.url, json=data)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证响应格式"):
            validate(instance=r.json(), schema=user_register_schema)

        with allure.step("验证注册失败"):
            response_data = r.json()
            # 根据实际API返回调整断言
            assert response_data["code"] != 200 or "已存在" in response_data["message"]

    @allure.story("用户注册")
    @allure.title("测试用例：无效邮箱格式注册失败")
    @allure.severity(allure.severity_level.NORMAL)
    @pytest.mark.parametrize("register_data", [
        {"name": "测试用户","mail": "invalid_email", "phoneNumber": "13800138003", "password": "Test@123456", "identity": "USER"},
        {"name": "测试用户","mail": "test@", "phoneNumber": "13800138004", "password": "Test@123456", "identity": "USER"},
        {"name": "测试用户","mail": "@example.com", "phoneNumber": "13800138005", "password": "Test@123456", "identity": "USER"}
    ])


    def test_register_fail_invalid_email(self, register_data):
        """测试无效邮箱格式注册"""
        with allure.step("准备注册数据（无效邮箱）"):
            data = {
                "name": register_data["name"],
                "mail": register_data["mail"],
                "phoneNumber": register_data["phoneNumber"],
                "password": register_data["password"],
                "identity": register_data.get("identity")
            }
            allure.attach(str(data), "请求参数", allure.attachment_type.TEXT)

        with allure.step("发送注册请求"):
            r = Request().post(url=self.url, json=data)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证注册失败"):
            response_data = r.json()
            assert response_data["code"] != 200 or r.status_code != 200


@pytest.mark.order(2)
@allure.epic("抽奖系统接口自动化测试")
@allure.feature("用户模块")
class TestUserLogin:
    """用户登录测试类"""

    url = host + "user/password/login"

    @allure.story("普通用户登录")
    @allure.title("测试用例：普通用户密码登录成功")
    @allure.severity(allure.severity_level.BLOCKER)
    @pytest.mark.parametrize("login_data", [
        {
            "loginName": "test1760671217025@example.com",
            "password": "123456"
        }
    ])
    def test_Normal_User_password_login_success(self, login_data):
        """测试正常密码登录"""
        with allure.step("准备登录数据"):
            data = {
                "loginName": login_data["loginName"],
                "password": login_data["password"]
            }
            allure.attach(str(data), "请求参数", allure.attachment_type.TEXT)

        with allure.step("发送登录请求"):
            r = Request().post(url=self.url, json=data)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证响应格式"):
            validate(instance=r.json(), schema=user_login_schema)

        with allure.step("验证登录成功"):
            assert r.status_code == 200
            response_data = r.json()
            assert response_data["code"] == 200
            assert "登录成功" in response_data["msg"]
            assert response_data["data"] is not None
            assert "token" in response_data["data"]

        with allure.step("保存Token到数据文件"):
            token_data = {"user_token": response_data["data"]["token"]}
            write_yaml("data.yml", token_data)

    @allure.story("管理员登录")
    @allure.title("测试用例：管理员密码登录成功")
    @allure.severity(allure.severity_level.BLOCKER)
    @pytest.mark.parametrize("login_data", [
        {
            # "loginName": "test1760671217025@example.com",2545901319@qq.com
            "loginName": "2545901319@qq.com",
            "password": "123456"
        }
    ])
    def test_Admin_password_login_success(self, login_data):
        """测试正常密码登录"""
        with allure.step("准备登录数据"):
            data = {
                "loginName": login_data["loginName"],
                "password": login_data["password"]
            }
            allure.attach(str(data), "请求参数", allure.attachment_type.TEXT)

        with allure.step("发送登录请求"):
            r = Request().post(url=self.url, json=data)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证响应格式"):
            validate(instance=r.json(), schema=user_login_schema)

        with allure.step("验证登录成功"):
            assert r.status_code == 200
            response_data = r.json()
            assert response_data["code"] == 200
            assert "登录成功" in response_data["msg"]
            assert response_data["data"] is not None
            assert "token" in response_data["data"]

        with allure.step("保存Token到数据文件"):
            token_data = {"admin_token": response_data["data"]["token"]}
            write_yaml("data.yml", token_data)




    @allure.story("用户登录")
    @allure.title("测试用例：错误密码登录失败")
    @allure.severity(allure.severity_level.CRITICAL)
    @pytest.mark.parametrize("login_data", [
        {
            "loginName": "test1760671217025@example.com",
            "password": "WrongPassword@123"
        }
    ])

    def test_password_login_fail_wrong_password(self, login_data):
        """测试错误密码登录"""
        with allure.step("准备登录数据（错误密码）"):
            data = {
                "loginName": login_data["loginName"],
                "password": login_data["password"]
            }
            allure.attach(str(data), "请求参数", allure.attachment_type.TEXT)

        with allure.step("发送登录请求"):
            r = Request().post(url=self.url, json=data)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证响应格式"):
            validate(instance=r.json(), schema=user_login_schema)

        with allure.step("验证登录失败"):
            response_data = r.json()
            assert response_data["code"] != 200 or "密码" in response_data["msg"]




    @allure.story("用户登录")
    @allure.title("测试用例：不存在的用户登录失败")
    @allure.severity(allure.severity_level.NORMAL)
    @pytest.mark.parametrize("login_data", [
        {
            "loginName": "nonexist_user@example.com",
            "password": "123456"
        }
    ])
    def test_password_login_fail_user_not_exist(self, login_data):
        """测试不存在的用户登录"""
        with allure.step("准备登录数据（不存在的用户）"):
            data = {
                "loginName": login_data["loginName"],
                "password": login_data["password"]
            }
            allure.attach(str(data), "请求参数", allure.attachment_type.TEXT)

        with allure.step("发送登录请求"):
            r = Request().post(url=self.url, json=data)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证登录失败"):
            response_data = r.json()
            assert response_data["code"] != 200

@pytest.mark.order(15)
@allure.epic("抽奖系统接口自动化测试")
@allure.feature("用户模块")
class TestUserManagement:
    """用户管理测试类"""

    @allure.story("用户信息查询")
    @allure.title("测试用例：获取当前用户信息")
    @allure.severity(allure.severity_level.NORMAL)
    def test_get_current_user(self):
        """测试获取当前用户信息"""
        url = host + "user/current"

        with allure.step("从数据文件读取Token"):
            token = read_yaml("data.yml", "user_token")
            assert token is not None, "Token不存在，请先执行登录测试"
            allure.attach(token, "用户Token", allure.attachment_type.TEXT)

        with allure.step("发送获取当前用户信息请求"):
            headers = {"Authorization": f"Bearer {token}"}
            r = Request().get(url=url, headers=headers)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证响应结果"):
            assert r.status_code == 200
            response_data = r.json()
            assert response_data["code"] == 200


    @allure.story("用户信息查询")
    @allure.title("测试用例：查询普通用户信息列表")
    @allure.severity(allure.severity_level.NORMAL)
    def test_find_user_info_by_identity_user(self):
        """测试查询普通用户信息列表"""
        url = host + "user/base-user/find-list"

        with allure.step("从数据文件读取普通用户Token"):
            token = read_yaml("data.yml", "user_token")
            assert token is not None, "Token不存在，请先执行登录测试"
            allure.attach(token, "普通用户Token", allure.attachment_type.TEXT)

        with allure.step("发送查询普通用户信息请求"):
            headers = {"user_token": token}
            params = {"identity": "USER"}
            allure.attach(str(params), "请求参数", allure.attachment_type.TEXT)

            r = Request().get(url=url, headers=headers, params=params)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证查询结果"):
            assert r.status_code == 200
            response_data = r.json()

            # 验证成功响应
            assert response_data[
                       "code"] == 200, f"查询USER用户应该成功，但返回错误: {response_data.get('msg', '未知错误')}"
            assert "data" in response_data, "成功响应应包含data字段"

            # 验证返回的数据结构
            user_list = response_data["data"]
            assert isinstance(user_list, list), "返回的数据应该是列表格式"

            # 如果有数据，验证数据结构
            if user_list:
                user_info = user_list[0]
                assert "userId" in user_info, "用户信息应包含userId字段"
                assert "userName" in user_info, "用户信息应包含userName字段"
                assert "identity" in user_info, "用户信息应包含identity字段"

                # 验证返回的用户身份
                assert user_info["identity"] == "USER", "返回的用户身份应该是USER"

            allure.attach(f"成功查询到{len(user_list)}个普通用户", "查询结果", allure.attachment_type.TEXT)

    @allure.story("用户信息查询")
    @allure.title("测试用例：查询管理员用户信息列表（权限控制）")
    @allure.severity(allure.severity_level.NORMAL)
    def test_find_user_info_by_identity_admin(self):
        """测试查询管理员用户信息列表（应该被禁止）"""
        url = host + "user/base-user/find-list"

        with allure.step("从数据文件读取普通用户Token"):
            token = read_yaml("data.yml", "user_token")
            assert token is not None, "Token不存在，请先执行登录测试"
            allure.attach(token, "普通用户Token", allure.attachment_type.TEXT)

        with allure.step("发送查询管理员用户信息请求"):
            headers = {"user_token": token}
            params = {"identity": "ADMIN"}
            allure.attach(str(params), "请求参数", allure.attachment_type.TEXT)

            r = Request().get(url=url, headers=headers, params=params)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证权限控制结果"):
            assert r.status_code == 200
            response_data = r.json()

            # 验证权限控制生效 - 返回500错误
            assert response_data[
                       "code"] == 500, f"普通用户查询ADMIN用户应该被禁止，但返回了其他状态码: {response_data.get('code')}"
            assert response_data["data"] is None, "权限控制时data应该为null"

            # 验证错误信息
            error_msg = response_data.get("msg", "")
            assert "数据库访问异常" in error_msg, f"错误信息应包含'数据库访问异常'，实际错误信息: {error_msg}"
            assert "请检查输入参数或联系管理员" in error_msg, f"错误信息应包含'请检查输入参数或联系管理员'，实际错误信息: {error_msg}"

            allure.attach(f"权限控制生效: {error_msg}", "权限控制验证", allure.attachment_type.TEXT)

    @allure.story("用户信息查询")
    @allure.title("测试用例：查询无效身份用户信息列表（权限控制）")
    @allure.severity(allure.severity_level.NORMAL)
    def test_find_user_info_by_identity_invalid(self):
        """测试查询无效身份用户信息列表（应该被禁止）"""
        url = host + "user/base-user/find-list"

        with allure.step("从数据文件读取普通用户Token"):
            token = read_yaml("data.yml", "user_token")
            assert token is not None, "Token不存在，请先执行登录测试"
            allure.attach(token, "普通用户Token", allure.attachment_type.TEXT)

        with allure.step("发送查询无效身份用户信息请求"):
            headers = {"user_token": token}
            params = {"identity": "ALL"}
            allure.attach(str(params), "请求参数", allure.attachment_type.TEXT)

            r = Request().get(url=url, headers=headers, params=params)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证权限控制结果"):
            assert r.status_code == 200
            response_data = r.json()

            # 验证权限控制生效 - 返回500错误
            assert response_data[
                       "code"] == 500, f"普通用户查询ALL用户应该被禁止，但返回了其他状态码: {response_data.get('code')}"
            assert response_data["data"] is None, "权限控制时data应该为null"

            # 验证错误信息
            error_msg = response_data.get("msg", "")
            assert "数据库访问异常" in error_msg, f"错误信息应包含'数据库访问异常'，实际错误信息: {error_msg}"
            assert "请检查输入参数或联系管理员" in error_msg, f"错误信息应包含'请检查输入参数或联系管理员'，实际错误信息: {error_msg}"

            allure.attach(f"权限控制生效: {error_msg}", "权限控制验证", allure.attachment_type.TEXT)

    @allure.story("用户信息查询")
    @allure.title("测试用例：查询用户信息列表（无身份参数）")
    @allure.severity(allure.severity_level.NORMAL)
    def test_find_user_info_by_identity_no_param(self):
        """测试查询用户信息列表（不输入身份参数，应该被禁止）"""
        url = host + "user/base-user/find-list"

        with allure.step("从数据文件读取普通用户Token"):
            token = read_yaml("data.yml", "user_token")
            assert token is not None, "Token不存在，请先执行登录测试"
            allure.attach(token, "普通用户Token", allure.attachment_type.TEXT)

        with allure.step("发送查询用户信息请求（不输入身份参数）"):
            headers = {"user_token": token}
            # 不传递identity参数
            allure.attach("无identity参数", "请求参数", allure.attachment_type.TEXT)

            r = Request().get(url=url, headers=headers)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证权限控制结果"):
            assert r.status_code == 200
            response_data = r.json()

            # 验证权限控制生效 - 返回500错误
            assert response_data[
                       "code"] == 500, f"不输入身份参数应该被禁止，但返回了其他状态码: {response_data.get('code')}"
            assert response_data["data"] is None, "权限控制时data应该为null"

            # 验证错误信息
            error_msg = response_data.get("msg", "")
            assert "数据库访问异常" in error_msg, f"错误信息应包含'数据库访问异常'，实际错误信息: {error_msg}"
            assert "请检查输入参数或联系管理员" in error_msg, f"错误信息应包含'请检查输入参数或联系管理员'，实际错误信息: {error_msg}"

            allure.attach(f"权限控制生效: {error_msg}", "权限控制验证", allure.attachment_type.TEXT)


@pytest.mark.order(10)
@allure.epic("抽奖系统接口自动化测试")
@allure.feature("用户模块")
class TestUserStatusManagement:
    """用户状态管理测试类"""

    @allure.story("用户状态管理")
    @allure.title("测试用例：禁用用户")
    @allure.severity(allure.severity_level.NORMAL)
    def test_toggle_user_status_disable(self):
        """
        测试禁用用户功能

        业务场景：管理员需要禁用某个用户，使其无法登录系统
        预期结果：用户状态成功更新为禁用，返回成功响应

        权限要求：需要管理员权限
        """
        user_id = 16  # 使用存在的用户ID（管理员用户）
        status_to_set = "DISABLED"
        url = host + f"user/toggle-status/{user_id}"

        with allure.step("从数据文件读取管理员Token"):
            admin_token = read_yaml("data.yml", "admin_token")
            assert admin_token is not None, "管理员Token不存在，请先执行管理员登录测试"
            allure.attach(admin_token, "管理员Token", allure.attachment_type.TEXT)

        with allure.step(f"发送禁用用户请求"):
            headers = {"user_token": admin_token}
            params = {"status": status_to_set}
            allure.attach(f"请求URL: {url}", "请求信息", allure.attachment_type.TEXT)
            allure.attach(f"用户ID: {user_id}, 状态: {status_to_set}", "请求参数", allure.attachment_type.TEXT)

            # 使用requests库直接发送PUT请求
            import requests
            r = requests.put(url=url, headers=headers, params=params)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证禁用用户响应"):
            assert r.status_code == 200, f"HTTP状态码不为200，实际为: {r.status_code}"
            response_data = r.json()

            # 验证业务响应
            assert response_data[
                       "code"] == 200, f"业务码不为200，实际为: {response_data.get('code')}, 错误信息: {response_data.get('msg')}"
            assert response_data["msg"] == "用户状态更新成功", f"错误信息不匹配，实际为: {response_data.get('msg')}"

            # 验证响应数据结构
            assert "data" in response_data, "响应应包含data字段"

            allure.attach("用户禁用成功", "验证结果", allure.attachment_type.TEXT)

    @allure.story("用户状态管理")
    @allure.title("测试用例：启用用户")
    @allure.severity(allure.severity_level.NORMAL)
    def test_toggle_user_status_enable(self):
        """
        测试启用用户功能

        业务场景：管理员需要启用某个用户，使其能够正常登录系统
        预期结果：用户状态成功更新为启用，返回成功响应

        权限要求：需要管理员权限
        """
        user_id = 16  # 使用存在的用户ID（管理员用户）
        status_to_set = "ENABLED"
        url = host + f"user/toggle-status/{user_id}"

        with allure.step("从数据文件读取管理员Token"):
            admin_token = read_yaml("data.yml", "admin_token")
            assert admin_token is not None, "管理员Token不存在，请先执行管理员登录测试"
            allure.attach(admin_token, "管理员Token", allure.attachment_type.TEXT)

        with allure.step(f"发送启用用户请求"):
            headers = {"user_token": admin_token}
            params = {"status": status_to_set}
            allure.attach(f"请求URL: {url}", "请求信息", allure.attachment_type.TEXT)
            allure.attach(f"用户ID: {user_id}, 状态: {status_to_set}", "请求参数", allure.attachment_type.TEXT)

            # 使用requests库直接发送PUT请求
            import requests
            r = requests.put(url=url, headers=headers, params=params)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证启用用户响应"):
            assert r.status_code == 200, f"HTTP状态码不为200，实际为: {r.status_code}"
            response_data = r.json()

            # 验证业务响应
            assert response_data[
                       "code"] == 200, f"业务码不为200，实际为: {response_data.get('code')}, 错误信息: {response_data.get('msg')}"
            assert response_data["msg"] == "用户状态更新成功", f"错误信息不匹配，实际为: {response_data.get('msg')}"

            # 验证响应数据结构
            assert "data" in response_data, "响应应包含data字段"

            allure.attach("用户启用成功", "验证结果", allure.attachment_type.TEXT)

