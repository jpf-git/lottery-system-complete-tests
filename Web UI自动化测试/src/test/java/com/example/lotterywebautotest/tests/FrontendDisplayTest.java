package com.example.lotterywebautotest.tests;

import com.example.lotterywebautotest.pages.*;
import com.example.lotterywebautotest.utils.TestDataProvider;
import com.example.lotterywebautotest.utils.WebDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FrontendDisplayTest {

    private WebDriver driver;
    private HomePage homePage;
    private PrizeManagementPage prizeManagementPage;
    private ActivityManagementPage activityManagementPage;
    private LotteryPage lotteryPage;
    private UserPrizeRecordPage userPrizeRecordPage;

    private LoginPage loginPage;

    @BeforeMethod
    public void setUp() {
        driver = WebDriverUtils.getDriver();
        homePage = new HomePage(driver);
        prizeManagementPage = new PrizeManagementPage(driver);
        activityManagementPage = new ActivityManagementPage(driver);
        lotteryPage = new LotteryPage(driver);
        userPrizeRecordPage = new UserPrizeRecordPage(driver);
        loginPage = new LoginPage(driver);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * 测试首页加载和导航
     */
    @Test(description = "验证首页正常加载和导航功能")
    public void testHomePageLoadAndNavigation() {
        // 访问首页
        driver.get("http://101.42.36.43:8888");

        // 验证首页加载
        homePage.verifyHomePageLoaded();

        // 验证导航链接
        homePage.verifyNavigationElements();

        // 验证页面性能
        homePage.verifyPageLoadPerformance();
    }

    /**
     * 测试奖品管理页面展示
     */
    @Test(description = "验证奖品管理页面正常展示")
    public void testPrizeManagementPageDisplay() {
        // 访问首页
        driver.get("http://101.42.36.43:8888");

        // 点击奖品管理链接
//        homePage.clickPrizeManagementLink();


        WebElement LoginBtn = driver.findElement(By.xpath("//*[@id=\"nav-auth\"]/button[1]"));
        LoginBtn.click();

//        // 定位输入框并输入数据
//        WebElement inputBox = driver.findElement(By.xpath("//*[@id=\"password-loginName\"]")); // 替换为实际元素定位
//        inputBox.sendKeys("test.admin.email"); // 替换为实际数据
//
//        WebElement inputPassword = driver.findElement(By.xpath("//*[@id=\"password-password\"]"));
//        inputPassword.sendKeys("test.admin.password");

        // 执行登录操作
        String[] userData = TestDataProvider.getAdminData();
        String emailOrPhone = userData[2]; // 邮箱
        String password = userData[1]; // 密码
        String identity = "管理员";

        loginPage.loginWithPassword(emailOrPhone, password, identity);

        // 验证登录成功
        loginPage.verifyLoginSuccess();

        // 验证奖品管理页面加载
        prizeManagementPage.verifyPrizeManagementPageLoaded();

        // 验证奖品列表展示
        prizeManagementPage.verifyPrizeListDisplayed();

        // 验证奖品信息完整性
        prizeManagementPage.verifyPrizeInformationCompleteness();

        // 验证分页功能
        prizeManagementPage.verifyPaginationFunctionality();

        // 验证页面性能
        prizeManagementPage.verifyPageLoadPerformance();
    }

    /**
     * 测试抽奖页面展示
     */
    @Test(description = "验证抽奖页面正常展示")
    public void testLotteryPageDisplay() {
        // 访问首页
        driver.get("http://101.42.36.43:8888");

        // 点击登录按钮
        WebElement LoginBtn = driver.findElement(By.xpath("//*[@id=\"nav-auth\"]/button[1]"));
        LoginBtn.click();

        // 执行登录操作
        String[] userData = TestDataProvider.getTestUserData();
        String emailOrPhone = userData[2]; // 邮箱
        String password = userData[1]; // 密码
        String identity = "普通用户";

        loginPage.loginWithPassword(emailOrPhone, password, identity);

        // 验证登录成功
        loginPage.verifyLoginSuccess();


        // 点击活动按钮
        homePage.clickActivitiesLink();

        WebElement loButton = driver.findElement(By.xpath("//*[@id=\"activities-grid\"]/div[2]/div[3]/button"));
        loButton.click();


        // 验证抽奖页面加载
        lotteryPage.verifyLotteryPageLoaded();

        // 验证抽奖网格展示
        lotteryPage.verifyLotteryGridDisplayed();


        // 验证活动信息展示
        lotteryPage.verifyActivityInfoDisplayed();

        // 验证奖品信息展示
        lotteryPage.verifyPrizesInfoDisplayed();


        // 验证页面性能
        lotteryPage.verifyPageLoadPerformance();
    }


    /**
     * 测试页面加载性能
     */
    @Test(description = "验证页面加载性能")
    public void testPageLoadPerformance() {
        // 测试首页加载性能
        long startTime = System.currentTimeMillis();
        driver.get("http://101.42.36.43:8888");

        homePage.verifyHomePageLoaded();
        long endTime = System.currentTimeMillis();

        long homePageLoadTime = endTime - startTime;
        assert homePageLoadTime < 5000 : "首页加载时间过长: " + homePageLoadTime + "ms";

        // 点击登录按钮
        WebElement LoginBtn = driver.findElement(By.xpath("//*[@id=\"nav-auth\"]/button[1]"));
        LoginBtn.click();

        // 执行登录操作
        String[] userData = TestDataProvider.getAdminData();
        String emailOrPhone = userData[2]; // 邮箱
        String password = userData[1]; // 密码
        String identity = "管理员";

        loginPage.loginWithPassword(emailOrPhone, password, identity);

        // 验证登录成功
        loginPage.verifyLoginSuccess();


        // 测试奖品管理页面加载性能
        startTime = System.currentTimeMillis();
        homePage.clickAdminLink();
        prizeManagementPage.verifyPrizeManagementPageLoaded();
        endTime = System.currentTimeMillis();

        long prizePageLoadTime = endTime - startTime;
        assert prizePageLoadTime < 8000 : "奖品管理页面加载时间过长: " + prizePageLoadTime + "ms";

        // 测试活动管理页面加载性能
        startTime = System.currentTimeMillis();
        homePage.clickActivityManagementLink();
        activityManagementPage.verifyActivityManagementPageLoaded();
        endTime = System.currentTimeMillis();

        long activityPageLoadTime = endTime - startTime;
        assert activityPageLoadTime < 8000 : "活动管理页面加载时间过长: " + activityPageLoadTime + "ms";

        // 测试抽奖页面加载性能
        startTime = System.currentTimeMillis();
        // 点击活动按钮
        homePage.clickActivitiesLink();

        WebElement loButton = driver.findElement(By.xpath("//*[@id=\"activities-grid\"]/div[2]/div[3]/button"));
        loButton.click();
        lotteryPage.verifyLotteryPageLoaded();
        endTime = System.currentTimeMillis();

        long lotteryPageLoadTime = endTime - startTime;
        assert lotteryPageLoadTime < 8000 : "抽奖页面加载时间过长: " + lotteryPageLoadTime + "ms";

    }

}
