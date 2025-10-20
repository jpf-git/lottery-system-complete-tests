"""
活动模块接口自动化测试
包含活动创建、查询、管理等功能的测试用例
"""
import pytest
import allure
from jsonschema import validate
from utils.request_util import host, Request
from utils.yaml_util import write_yaml, read_yaml


# JSON Schema定义
activity_create_schema = {
    "type": "object",
    "required": ["code", "msg"],
    "properties": {
        "code": {"type": "integer"},
        "msg": {"type": "string"},
        "data": {
            "type": ["object", "null"],
            "properties": {
                "activityId": {"type": "integer"}
            }
        }
    }
}

activity_list_schema = {
    "type": "object",
    "required": ["code", "msg", "data"],
    "properties": {
        "code": {"type": "integer"},
        "msg": {"type": "string"},
        "data": {
            "type": ["object", "null"],
            "properties": {
                "total": {"type": "integer"},
                "records": {
                    "type": "array",
                    "items": {
                        "type": "object",
                        "properties": {
                            "activityId": {"type": "integer"},
                            "activityName": {"type": "string"},
                            "description": {"type": "string"},
                            "valid": {"type": "boolean"}
                        }
                    }
                }
            }
        }
    }
}

# 添加活动奖品Schema
add_activity_prize_schema = {
    "type": "object",
    "required": ["code", "msg", "data"],
    "properties": {
        "code": {"type": "integer"},
        "msg": {"type": "string"},
        "data": {"type": ["object", "null"]}
    }
}


@pytest.mark.order(4)
@allure.epic("抽奖系统接口自动化测试")
@allure.feature("活动模块")
class TestActivityCreate:
    """活动创建测试类"""

    url = host + "activity/activity/create"


    @allure.story("活动创建")
    @allure.title("测试用例：普通用户创建活动失败（权限不足）")
    @allure.severity(allure.severity_level.CRITICAL)
    @pytest.mark.parametrize("activity_data", [
        {
            "activityName": "普通用户创建活动",
            "description": "测试权限控制",
            "activityPrizeList": [
                {
                    "prizeId": 1,
                    "prizeAmount": 1,
                    "prizeTiers": "一等奖"
                },

                {
                    "prizeId": 2,
                    "prizeAmount": 1,
                    "prizeTiers": "二等奖"
                }
            ],
            "activityUserList":[
                {
                    "userId": 2,
                    "userName": "2545901319@qq.com"
                }
            ]
        }
    ])
    def test_create_activity_fail_no_permission(self, activity_data):
        """测试普通用户创建活动失败"""
        with allure.step("从数据文件读取普通用户Token"):
            user_token = read_yaml("data.yml", "user_token")
            if not user_token:
                pytest.skip("用户Token不存在，跳过此测试")

        with allure.step("准备活动数据"):
            data = {
                "activityName": activity_data["activityName"],
                "description": activity_data["description"],
                "activityPrizeList": activity_data["activityPrizeList"],
                "activityUserList": activity_data["activityUserList"]
            }

        with allure.step("发送创建活动请求"):
            headers = {"Authorization": f"Bearer {user_token}"}
            r = Request().post(url=self.url, json=data, headers=headers)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证权限不足"):
            # 应该返回403或其他权限错误
            assert r.status_code == 403 or (r.json()["code"] != 200 and "权限" in r.json()["msg"])


@pytest.mark.order(7)
@allure.epic("抽奖系统接口自动化测试")
@allure.feature("活动模块")
class TestActivityQuery:
    """活动查询测试类"""

    @allure.story("活动查询")
    @allure.title("测试用例：查询活动列表")
    @allure.severity(allure.severity_level.NORMAL)
    def test_find_activity_list(self):
        """测试查询活动列表"""
        url = host + "activity/activity/find-list"

        with allure.step("从数据文件读取普通用户Token"):
            user_token = read_yaml("data.yml", "user_token")
            if not user_token:
                pytest.skip("用户Token不存在，跳过此测试")

        with allure.step("准备分页参数"):
            params = {
                "currentPage": 1,
                "pageSize": 10
            }
            allure.attach(str(params), "请求参数", allure.attachment_type.TEXT)

        with allure.step("发送查询请求"):
            headers = {"Authorization": f"Bearer {user_token}"}
            r = Request().get(url=url, params=params, headers=headers)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证响应格式"):
            validate(instance=r.json(), schema=activity_list_schema)

        with allure.step("验证查询结果"):
            assert r.status_code == 200
            response_data = r.json()
            assert response_data["code"] == 200
            assert "data" in response_data


@pytest.mark.order(14)
@allure.epic("抽奖系统接口自动化测试")
@allure.feature("活动模块")
class TestActivityManagement:
    """活动管理测试类"""

    @allure.story("活动管理")
    @allure.title("测试用例：管理员结束活动")
    @allure.severity(allure.severity_level.NORMAL)
    def test_end_activity_success_admin(self):
        """测试管理员结束活动"""

        with allure.step("从数据文件读取管理员Token"):
            admin_token = read_yaml("data.yml", "admin_token")
            if not admin_token:
                pytest.skip("用户Token不存在，跳过此测试")

        with allure.step("输入活动ID"):
            # params = {
            #     "activityId": 10
            # }
            activity_id = 10
            allure.attach(str(activity_id), "活动ID", allure.attachment_type.TEXT)

        with allure.step("发送结束活动请求"):
            # url = host + "activity/end"
            url = host + f"activity/end/{activity_id}"
            headers = {"Authorization": f"Bearer {admin_token}"}
            r = Request().post(url=url, headers=headers)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证结束成功"):
            assert r.status_code == 200
            response_data = r.json()
            assert response_data["code"] == 200


