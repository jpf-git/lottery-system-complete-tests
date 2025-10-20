"""
日志工具类
提供统一的日志记录功能，支持按日期和级别分类保存日志
"""
import logging
import os
import time


class infoFilter(logging.Filter):
    """INFO级别日志过滤器"""
    def filter(self, record):
        return record.levelno == logging.INFO

class errFilter(logging.Filter):
    """ERROR级别日志过滤器"""
    def filter(self, record):
        return record.levelno == logging.ERROR


class logger:
    """日志管理类"""

    @classmethod
    def getlog(cls):
        """
        获取日志对象

        Returns:
            Logger: 配置完成的日志对象
        """
        # 创建日志对象
        cls.logger = logging.getLogger(__name__)
        cls.logger.setLevel(logging.DEBUG)
# 确保logs文件夹存在
        LOG_PATH = "./logs/"
        if not os.path.exists(LOG_PATH):
            os.mkdir(LOG_PATH)

        # 配置日志文件名（按日期分类）
        now = time.strftime("%Y-%m-%d")
        log_name = LOG_PATH + now + ".log"
        info_log_name = LOG_PATH + now + "-info.log"
        err_log_name = LOG_PATH + now + "-err.log"

        # 创建文件处理器
        all_handler = logging.FileHandler(log_name, encoding="utf-8")
        info_handler = logging.FileHandler(info_log_name, encoding="utf-8")
        err_handler = logging.FileHandler(err_log_name, encoding="utf-8")

        # 创建控制台处理器
        streamHandler = logging.StreamHandler()

        # 设置日志格式
        formatter = logging.Formatter(
            "%(asctime)s %(levelname)s [%(name)s] [%(filename)s (%(funcName)s:%(lineno)d)] - %(message)s"
        )

        all_handler.setFormatter(formatter)
        info_handler.setFormatter(formatter)
        err_handler.setFormatter(formatter)
        streamHandler.setFormatter(formatter)

        # 添加过滤器
        info_handler.addFilter(infoFilter())
        err_handler.addFilter(errFilter())

        # 添加处理器到日志对象
        cls.logger.addHandler(all_handler)
        cls.logger.addHandler(info_handler)
        cls.logger.addHandler(err_handler)
        # cls.logger.addHandler(streamHandler)  # 如需控制台输出，取消注释

        return cls.logger

