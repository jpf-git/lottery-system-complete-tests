"""
请求工具类
封装HTTP请求方法，提供统一的请求和日志记录功能
"""
import requests

from utils.logger_util import logger

# 抽奖系统服务地址（根据application.properties配置）
host = "http://101.42.36.43:8888/"


class Request:
    """HTTP请求封装类"""

    log = logger.getlog()

    def get(self, url, **kwargs):
        """
        发送GET请求

        Args:
            url: 请求URL
            **kwargs: 其他请求参数（如headers, params等）

        Returns:
            Response: 响应对象
        """
        self.log.info("准备发起GET请求，URL: " + url)
        self.log.info("请求信息：{}".format(kwargs))

        r = requests.get(url=url, **kwargs)

        self.log.info("响应状态码：{}".format(r.status_code))
        self.log.info("响应内容：{}".format(r.text))

        return r

    def post(self, url, **kwargs):
        """
        发送POST请求

        Args:
            url: 请求URL
            **kwargs: 其他请求参数（如headers, data, json等）

        Returns:
            Response: 响应对象
        """
        self.log.info("准备发起POST请求，URL: " + url)
        self.log.info("请求信息：{}".format(kwargs))

        r = requests.post(url=url, **kwargs)

        self.log.info("响应状态码：{}".format(r.status_code))
        self.log.info("响应内容：{}".format(r.text))

        return r

    def put(self, url, **kwargs):
        """
        发送PUT请求

        Args:
            url: 请求URL
            **kwargs: 其他请求参数（如headers, data, json等）

        Returns:
            Response: 响应对象
        """
        self.log.info("准备发起PUT请求，URL: " + url)
        self.log.info("请求信息：{}".format(kwargs))

        r = requests.put(url=url, **kwargs)

        self.log.info("响应状态码：{}".format(r.status_code))
        self.log.info("响应内容：{}".format(r.text))

        return r
