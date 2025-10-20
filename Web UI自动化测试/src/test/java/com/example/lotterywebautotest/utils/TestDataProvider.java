package com.example.lotterywebautotest.utils;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 测试数据提供工具类
 */
public class TestDataProvider {

    private static Properties properties;

    static {
        loadProperties();
    }

    /**
     * 加载配置文件
     */
    private static void loadProperties() {
        properties = new Properties();
        try (InputStream input = TestDataProvider.class.getClassLoader()
                .getResourceAsStream("test-config.properties")) {
            if (input == null) {
                throw new RuntimeException("无法找到配置文件 test-config.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("加载配置文件失败", e);
        }
    }

    /**
     * 获取配置属性值
     * @param key 属性键
     * @return 属性值
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * 获取配置属性值，如果不存在则返回默认值
     * @param key 属性键
     * @param defaultValue 默认值
     * @return 属性值
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * 获取测试基础URL
     * @return 基础URL
     */
    public static String getBaseUrl() {
        return getProperty("test.base.url");
    }

    /**
     * 获取浏览器类型
     * @return 浏览器类型
     */
    public static String getBrowserType() {
        return getProperty("test.browser", "chrome");
    }

    /**
     * 获取测试用户数据
     * @return 用户数据数组 [username, password, email, phone]
     */
    public static String[] getTestUserData() {
        return new String[]{
                getProperty("test.user.username"),
                getProperty("test.user.password"),
                getProperty("test.user.email"),
                getProperty("test.user.phone")
        };
    }

    /**
     * 获取管理员数据
     * @return 管理员数据数组 [username, password, email]
     */
    public static String[] getAdminData() {
        return new String[]{
                getProperty("test.admin.username"),
                getProperty("test.admin.password"),
                getProperty("test.admin.email")
        };
    }

    /**
     * 获取已存在的测试数据（用于失败场景测试）
     * @return 已存在数据数组 [email, phone, username]
     */
    public static String[] getExistingData() {
        return new String[]{
                getProperty("test.existing.email"),
                getProperty("test.existing.phone"),
                getProperty("test.existing.username")
        };
    }

    /**
     * 生成随机用户名
     * @return 随机用户名
     */
    public static String generateRandomUsername() {
        return "testuser" + System.currentTimeMillis();
    }

    /**
     * 生成随机邮箱
     * @return 随机邮箱
     */
    public static String generateRandomEmail() {
        return "test" + System.currentTimeMillis() + "@example.com";
    }

    /**
     * 生成随机手机号
     * @return 随机手机号
     */
    public static String generateRandomPhone() {
        return "138" + String.format("%08d", (int)(Math.random() * 100000000));
    }

    /**
     * 获取测试奖品数据
     * @return 奖品数据数组 [name, description, price]
     */
    public static String[] getTestPrizeData() {
        return new String[]{
                getProperty("test.prize.name"),
                getProperty("test.prize.description"),
                getProperty("test.prize.price")
        };
    }

    /**
     * 获取测试活动数据
     * @return 活动数据数组 [name, description]
     */
    public static String[] getTestActivityData() {
        return new String[]{
                getProperty("test.activity.name"),
                getProperty("test.activity.description")
        };
    }
}
