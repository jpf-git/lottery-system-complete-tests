package com.example.lotterywebautotest.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;

public class WebDriverUtils {
    private static WebDriver driver;

    public static WebDriver getDriver() {
        if (driver == null) {
            // 设置Chrome驱动路径（确保路径正确）
//            System.setProperty("webdriver.chrome.driver", "D:\\Downloads\\chromedriver-win64 (3)\\chromedriver-win64\\chromedriver.exe");
            WebDriverManager.chromedriver().setup(); // 自动下载并设置驱动
            //浏览器配置对象--options
            ChromeOptions options = new ChromeOptions();
            //允许访问所有链接
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--start-maximized");  // 最大化窗口
            //打开浏览器
            driver = new ChromeDriver(options);

            //隐式等待
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        }
        return driver;
    }


    /**
     * 关闭WebDriver
     */
    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    public static void getScreenshot(WebDriver driver) throws IOException {
        SimpleDateFormat sim1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sim2 = new SimpleDateFormat("HHmmssSS");
        String dirTime = sim1.format(System.currentTimeMillis());
        String fileTime = sim2.format(System.currentTimeMillis());

        String filename = "./src/test/image/"+dirTime+"/test_"+fileTime+".png";
        File imageFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(imageFile,new File(filename));

    }
}




