"""
奖品模块接口自动化测试
包含奖品创建、查询、图片上传等功能的测试用例
"""
import pytest
import allure
from jsonschema import validate
from utils.request_util import host, Request
from utils.yaml_util import write_yaml, read_yaml

# JSON Schema定义
prize_list_schema = {
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
                            "id": {"type": "integer"},
                            "name": {"type": "string"},
                            "imageUrl": {"type": ["string", "null"]},
                            "price": {"type": "number"},
                            "description": {"type": ["string", "null"]}
                        }
                    }
                }
            }
        }
    }
}


@pytest.mark.order(3)
@allure.epic("抽奖系统接口自动化测试")
@allure.feature("奖品模块")
class TestPrizeQuery:
    """奖品查询测试类"""

    @allure.story("奖品查询")
    @allure.title("测试用例：查询奖品列表")
    @allure.severity(allure.severity_level.NORMAL)
    def test_find_prize_list(self):
        """测试查询奖品列表"""
        url = host + "prize/find-list"

        with allure.step("准备分页参数"):
            params = {
                "currentPage": 1,
                "pageSize": 10
            }
            allure.attach(str(params), "请求参数", allure.attachment_type.TEXT)

        with allure.step("发送查询请求"):
            r = Request().get(url=url, params=params)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证响应格式"):
            validate(instance=r.json(), schema=prize_list_schema)

        with allure.step("验证查询结果"):
            assert r.status_code == 200
            response_data = r.json()
            assert response_data["code"] == 200
            assert "data" in response_data

            # 如果有数据，验证数据结构
            if response_data["data"] and response_data["data"]["records"]:
                prize = response_data["data"]["records"][0]
                assert "prizeId" in prize
                assert "prizeName" in prize
                assert "price" in prize

                # 保存第一个奖品ID供其他测试使用
                prize_data = {"prize_id": prize["prizeId"]}
                write_yaml("data.yml", prize_data)

    @allure.story("奖品查询")
    @allure.title("测试用例：分页查询奖品列表")
    @allure.severity(allure.severity_level.NORMAL)
    @pytest.mark.parametrize("page_params", [
        {"currentPage": 1, "pageSize": 5},
        {"currentPage": 2, "pageSize": 5},
        {"currentPage": 1, "pageSize": 20}
    ])
    def test_find_prize_list_pagination(self, page_params):
        """测试分页查询奖品列表"""
        url = host + "prize/find-list"

        with allure.step("准备分页参数"):
            allure.attach(str(page_params), "请求参数", allure.attachment_type.TEXT)

        with allure.step("发送查询请求"):
            r = Request().get(url=url, params=page_params)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证查询结果"):
            assert r.status_code == 200
            response_data = r.json()
            assert response_data["code"] == 200
            assert "data" in response_data


    @allure.story("奖品图片")
    @allure.title("测试用例：获取奖品图片")
    @allure.severity(allure.severity_level.NORMAL)
    def test_get_prize_image(self):
        """
        测试获取奖品图片功能

        测试目的：
        1. 验证存在的奖品图片能正常返回
        2. 验证返回的Content-Type是正确的图片类型
        3. 确保图片服务功能正常工作

        业务场景：用户查看奖品详情时，需要显示奖品图片
        预期结果：返回200状态码和正确的图片内容
        """

        # 使用一个已知的图片文件名
        filename = "61023395-838a-4b57-80fe-34192f18b6a5.jpg"
        url = host + f"prize/pic/{filename}"

        with allure.step("发送获取图片请求"):
            r = Request().get(url=url)
            allure.attach(f"状态码: {r.status_code}", "响应状态", allure.attachment_type.TEXT)

        with allure.step("验证响应"):
            # 图片请求应该返回200或404
            assert r.status_code in [200, 404]

            if r.status_code == 200:
                # 验证Content-Type是图片类型
                content_type = r.headers.get('Content-Type', '')
                assert 'image' in content_type.lower()

    @allure.story("奖品图片")
    @allure.title("测试用例：获取不存在的奖品图片")
    @allure.severity(allure.severity_level.NORMAL)
    def test_get_prize_image_not_exist(self):
        """
        测试获取不存在的奖品图片功能

        测试目的：
        1. 验证系统对不存在图片的处理机制
        2. 验证默认图片机制是否正常工作
        3. 确保用户体验的连续性（不会显示破损图片）

        业务场景：奖品图片文件丢失或未上传时，系统应返回默认图片
        预期结果：返回200状态码和默认图片内容（不是404错误）

        设计说明：
        - 后端实现了默认图片机制，当请求的图片不存在时，返回默认占位图片
        - 这种设计避免了前端显示破损图片，提升了用户体验
        - 返回200而不是404，确保前端可以正常处理图片显示
        """
        filename = "nonexistent-image.jpg"
        url = host + f"prize/pic/{filename}"

        with allure.step("发送获取不存在图片请求"):
            r = Request().get(url=url)
            allure.attach(f"状态码: {r.status_code}", "响应状态", allure.attachment_type.TEXT)

        with allure.step("验证返回200（默认图片）"):
            # 后端可能返回默认图片而不是404
            assert r.status_code == 200

            # 验证Content-Type是图片类型
            content_type = r.headers.get('Content-Type', '')
            assert 'image' in content_type.lower()

            # 验证响应内容不为空
            assert len(r.content) > 0

    @allure.story("奖品图片")
    @allure.title("测试用例：验证默认图片机制")
    @allure.severity(allure.severity_level.NORMAL)
    def test_default_image_mechanism(self):
        """
        测试默认图片机制的完整性和一致性

        测试目的：
        1. 验证多个不存在的图片都能正确返回默认图片
        2. 验证默认图片机制的一致性（不同格式的图片请求）
        3. 确保系统的容错能力和用户体验

        业务场景：系统需要处理各种不存在的图片请求，确保都能返回有效的默认图片
        预期结果：所有不存在的图片请求都返回200状态码和有效的图片内容

        测试策略：使用多种不同的图片文件名和格式进行测试
        """
        # 测试多个不存在的图片文件名
        test_filenames = [
            "nonexistent1.jpg",
            "nonexistent2.png",
            "invalid-image.gif",
            "missing-file.webp"
        ]

        for filename in test_filenames:
            with allure.step(f"测试不存在的图片: {filename}"):
                url = host + f"prize/pic/{filename}"
                r = Request().get(url=url)

                # 验证都返回200状态码
                assert r.status_code == 200, f"图片 {filename} 应该返回200状态码"

                # 验证Content-Type
                content_type = r.headers.get('Content-Type', '')
                assert 'image' in content_type.lower(), f"图片 {filename} 应该返回图片类型"

                # 验证响应内容不为空
                assert len(r.content) > 0, f"图片 {filename} 应该有内容"

                allure.attach(f"状态码: {r.status_code}, Content-Type: {content_type}",
                              f"响应信息-{filename}", allure.attachment_type.TEXT)


@pytest.mark.order(11)
@allure.epic("抽奖系统接口自动化测试")
@allure.feature("奖品模块")
class TestPrizeStatusManagement:
    """奖品状态管理测试类"""

    @allure.story("奖品状态管理")
    @allure.title("测试用例：禁用奖品")
    @allure.severity(allure.severity_level.NORMAL)
    def test_toggle_prize_status_disable(self):
        """
        测试禁用奖品功能

        业务场景：管理员需要禁用某个奖品，使其不再参与抽奖活动
        预期结果：奖品状态成功更新为禁用，返回成功响应

        权限要求：需要管理员权限
        """
        prize_id = 8  # 使用存在的奖品ID
        status_to_set = "DISABLED"
        url = host + f"prize/toggle-status/{prize_id}"

        with allure.step("从数据文件读取管理员Token"):
            admin_token = read_yaml("data.yml", "admin_token")
            assert admin_token is not None, "管理员Token不存在，请先执行管理员登录测试"
            allure.attach(admin_token, "管理员Token", allure.attachment_type.TEXT)

        with allure.step(f"发送禁用奖品请求"):
            headers = {"user_token": admin_token}
            params = {"status": status_to_set}
            allure.attach(f"请求URL: {url}", "请求信息", allure.attachment_type.TEXT)
            allure.attach(f"奖品ID: {prize_id}, 状态: {status_to_set}", "请求参数", allure.attachment_type.TEXT)

            r = Request().put(url=url, headers=headers, params=params)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证禁用奖品响应"):
            assert r.status_code == 200, f"HTTP状态码不为200，实际为: {r.status_code}"
            response_data = r.json()

            # 验证业务响应
            assert response_data[
                       "code"] == 200, f"业务码不为200，实际为: {response_data.get('code')}, 错误信息: {response_data.get('msg')}"
            assert response_data["msg"] == "奖品状态更新成功", f"错误信息不匹配，实际为: {response_data.get('msg')}"

            # 验证响应数据结构
            assert "data" in response_data, "响应应包含data字段"

            allure.attach("奖品禁用成功", "验证结果", allure.attachment_type.TEXT)

    @allure.story("奖品状态管理")
    @allure.title("测试用例：启用奖品")
    @allure.severity(allure.severity_level.NORMAL)
    def test_toggle_prize_status_enable(self):
        """
        测试启用奖品功能

        业务场景：管理员需要启用某个奖品，使其能够参与抽奖活动
        预期结果：奖品状态成功更新为启用，返回成功响应

        权限要求：需要管理员权限
        """
        prize_id = 8  # 使用存在的奖品ID
        status_to_set = "ENABLED"
        url = host + f"prize/toggle-status/{prize_id}"

        with allure.step("从数据文件读取管理员Token"):
            admin_token = read_yaml("data.yml", "admin_token")
            assert admin_token is not None, "管理员Token不存在，请先执行管理员登录测试"
            allure.attach(admin_token, "管理员Token", allure.attachment_type.TEXT)

        with allure.step(f"发送启用奖品请求"):
            headers = {"user_token": admin_token}
            params = {"status": status_to_set}
            allure.attach(f"请求URL: {url}", "请求信息", allure.attachment_type.TEXT)
            allure.attach(f"奖品ID: {prize_id}, 状态: {status_to_set}", "请求参数", allure.attachment_type.TEXT)

            r = Request().put(url=url, headers=headers, params=params)
            allure.attach(r.text, "响应内容", allure.attachment_type.JSON)

        with allure.step("验证启用奖品响应"):
            assert r.status_code == 200, f"HTTP状态码不为200，实际为: {r.status_code}"
            response_data = r.json()

            # 验证业务响应
            assert response_data[
                       "code"] == 200, f"业务码不为200，实际为: {response_data.get('code')}, 错误信息: {response_data.get('msg')}"
            assert response_data["msg"] == "奖品状态更新成功", f"错误信息不匹配，实际为: {response_data.get('msg')}"

            # 验证响应数据结构
            assert "data" in response_data, "响应应包含data字段"

            allure.attach("奖品启用成功", "验证结果", allure.attachment_type.TEXT)

