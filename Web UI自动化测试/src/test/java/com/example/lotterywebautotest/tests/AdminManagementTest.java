package com.example.lotterywebautotest.tests;

import com.example.lotterywebautotest.pages.ActivityManagementPage;
import com.example.lotterywebautotest.pages.HomePage;
import com.example.lotterywebautotest.pages.LoginPage;
import com.example.lotterywebautotest.pages.PrizeManagementPage;
import com.example.lotterywebautotest.utils.TestDataProvider;
import com.example.lotterywebautotest.utils.WebDriverUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * 后台管理模块测试类
 */
public class AdminManagementTest {

    private WebDriver driver;
    private HomePage homePage;
    private LoginPage loginPage;
    private PrizeManagementPage prizeManagementPage;
    private ActivityManagementPage activityManagementPage;

    @BeforeMethod
    public void setUp() {
        driver = WebDriverUtils.getDriver();
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        prizeManagementPage = new PrizeManagementPage(driver);
        activityManagementPage = new ActivityManagementPage(driver);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * 测试管理员登录
     */
    @Test(description = "验证管理员登录功能")
    public void testAdminLogin() {
        // 访问首页
        driver.get("http://101.42.36.43:8888");

        // 验证首页加载
        homePage.verifyHomePageLoaded();

        // 点击登录按钮
        homePage.clickLoginButton();

        // 验证登录页面加载
        loginPage.verifyLoginPageLoaded();

        // 执行登录操作
        String[] userData = TestDataProvider.getAdminData();
        String emailOrPhone = userData[2]; // 邮箱
        String password = userData[1]; // 密码
        String identity = "管理员";

        loginPage.loginWithPassword(emailOrPhone, password, identity);

        // 验证登录成功
        loginPage.verifyLoginSuccess();

    }

    /**
     * 测试奖品管理功能
     */
    @Test(description = "验证奖品管理功能")
    public void testPrizeManagement() {
        // 先登录管理员
        loginAsAdmin();

        // 点击奖品管理链接
        homePage.clickPrizesLink();

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

    private void loginAsAdmin() {
        // 访问首页
        driver.get("http://101.42.36.43:8888");

        // 验证首页加载
        homePage.verifyHomePageLoaded();

        // 点击登录按钮
        homePage.clickLoginButton();

        // 验证登录页面加载
        loginPage.verifyLoginPageLoaded();

        // 执行登录操作
        String[] userData = TestDataProvider.getAdminData();
        String emailOrPhone = userData[2]; // 邮箱
        String password = userData[1]; // 密码
        String identity = "管理员";

        loginPage.loginWithPassword(emailOrPhone, password, identity);

        // 验证登录成功
        loginPage.verifyLoginSuccess();
    }

    /**
     * 测试奖品状态管理
     */
    @Test(description = "验证奖品状态管理功能")
    public void testPrizeStatusManagement() {
        // 先登录管理员
        loginAsAdmin();

        // 点击奖品管理链接
        homePage.clickPrizesLink();

        // 验证奖品管理页面加载
        prizeManagementPage.verifyPrizeManagementPageLoaded();

        // 验证奖品列表展示
        prizeManagementPage.verifyPrizeListDisplayed();

        // 禁用第一个奖品
        prizeManagementPage.disableFirstPrize();

        // 切换到弹窗（Selenium 需先切换上下文才能操作弹窗）
        Alert alert = driver.switchTo().alert();

        // 处理弹窗：
        // - 如果需要“确认禁用”，用 accept()；
        // - 如果需要“取消禁用”，用 dismiss()。
        alert.accept(); // 这里选择“确认禁用”，根据测试需求调整

        // 验证奖品状态更新
        prizeManagementPage.verifyPrizeStatusUpdated("启用");

        // 启用第一个奖品
        prizeManagementPage.enableFirstPrize();

        // 切换到弹窗（Selenium 需先切换上下文才能操作弹窗）
        alert = driver.switchTo().alert();

        // 处理弹窗：
        // - 如果需要“确认禁用”，用 accept()；
        // - 如果需要“取消禁用”，用 dismiss()。
        alert.accept(); // 这里选择“确认禁用”，根据测试需求调整


        // 验证奖品状态更新
        prizeManagementPage.verifyPrizeStatusUpdated("禁用");
    }

    /**
     * 测试活动管理功能
     */
    @Test(description = "验证活动管理功能")
    public void testActivityManagement() {
        // 先登录管理员
        loginAsAdmin();

        // 点击活动管理链接
        homePage.clickActivityManagementLink();

        // 验证活动管理页面加载
        activityManagementPage.verifyActivityManagementPageLoaded();

        // 验证活动列表展示
        activityManagementPage.verifyActivityListDisplayed();

        // 验证活动信息完整性
        activityManagementPage.verifyActivityInformationCompleteness();

        // 验证分页功能
        activityManagementPage.verifyPaginationFunctionality();

        // 验证页面性能
        activityManagementPage.verifyPageLoadPerformance();
    }

    /**
     * 测试结束活动功能
     */
    @Test(description = "验证结束活动功能")
    public void testEndActivity() throws InterruptedException {
        // 先登录管理员
        loginAsAdmin();

        // 点击活动管理链接
        homePage.clickActivityManagementLink();

        // 验证活动管理页面加载
        activityManagementPage.verifyActivityManagementPageLoaded();

        // 验证活动列表展示
        activityManagementPage.verifyActivityListDisplayed();

        // 结束第一个活动
        activityManagementPage.endFirstActivity();

        // 切换到弹窗（Selenium 需先切换上下文才能操作弹窗）
        Alert alert = driver.switchTo().alert();

        // 处理弹窗：
        // - 如果需要“确认结束”，用 accept()；
        // - 如果需要“取消结束”，用 dismiss()。
        alert.accept(); // 这里选择“确认结束”，根据测试需求调整

        Thread.sleep(1500);
        // 验证活动状态更新
        activityManagementPage.verifyActivityStatusUpdated("已结束");
    }

    /**
     * 测试数据展示
     */
    @Test(description = "验证数据展示功能")
    public void testDataDisplay() {
        // 先登录管理员
        loginAsAdmin();

        // 测试奖品管理数据展示
        homePage.clickPrizesLink();
        prizeManagementPage.verifyPrizeManagementPageLoaded();
        prizeManagementPage.verifyPrizeListDisplayed();
        prizeManagementPage.verifyPrizeInformationCompleteness();

        // 测试活动管理数据展示
        homePage.clickActivityManagementLink();
        activityManagementPage.verifyActivityManagementPageLoaded();
        activityManagementPage.verifyActivityListDisplayed();
        activityManagementPage.verifyActivityInformationCompleteness();
    }


}
