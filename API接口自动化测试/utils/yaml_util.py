"""
YAML工具类
提供YAML文件的读写和清空操作
"""
import os

import yaml


def write_yaml(filename, data):
    """
    向YAML文件中追加写入数据

    Args:
        filename: YAML文件名
        data: 要写入的数据（字典格式）
    """
    with open(os.getcwd() + "/data/" + filename, mode="a+", encoding="utf-8") as f:
        yaml.safe_dump(data, stream=f, allow_unicode=True)


def read_yaml(filename, key):
    """
    从YAML文件中读取指定key的数据

    Args:
        filename: YAML文件名
        key: 要读取的key

    Returns:
        读取到的数据值
    """
    with open(os.getcwd() + "/data/" + filename, mode="r", encoding="utf-8") as f:
        data = yaml.safe_load(f)
        if data and key in data:
            return data[key]
        return None

