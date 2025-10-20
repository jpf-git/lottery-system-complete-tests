package com.example.lotterywebautotest.utils;


import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 截图工具类
 */
public class ScreenshotUtil {

    private static final String SCREENSHOT_DIR = "screenshots/";

    /**
     * 创建截图目录
     */
    static {
        File dir = new File(SCREENSHOT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 截取当前页面截图
     * @param driver WebDriver实例
     * @param testName 测试名称
     * @return 截图文件路径
     */
    public static String takeScreenshot(WebDriver driver, String testName) {
        try {
            // 生成截图文件名
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = testName + "_" + timestamp + ".png";
            String filePath = SCREENSHOT_DIR + fileName;

            // 截取截图
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);

            // 保存截图
            FileUtils.copyFile(sourceFile, destFile);

            System.out.println("截图已保存: " + filePath);
            return filePath;

        } catch (IOException e) {
            System.err.println("截图保存失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * 截取失败时的截图
     * @param driver WebDriver实例
     * @param testName 测试名称
     * @return 截图文件路径
     */
    public static String takeFailureScreenshot(WebDriver driver, String testName) {
        return takeScreenshot(driver, testName + "_FAILED");
    }

    /**
     * 截取成功时的截图
     * @param driver WebDriver实例
     * @param testName 测试名称
     * @return 截图文件路径
     */
    public static String takeSuccessScreenshot(WebDriver driver, String testName) {
        return takeScreenshot(driver, testName + "_SUCCESS");
    }

    /**
     * 清理旧的截图文件（保留最近7天的）
     */
    public static void cleanupOldScreenshots() {
        File dir = new File(SCREENSHOT_DIR);
        if (!dir.exists()) {
            return;
        }

        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }

        long sevenDaysAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000);

        for (File file : files) {
            if (file.isFile() && file.lastModified() < sevenDaysAgo) {
                if (file.delete()) {
                    System.out.println("已删除旧截图: " + file.getName());
                }
            }
        }
    }
}
