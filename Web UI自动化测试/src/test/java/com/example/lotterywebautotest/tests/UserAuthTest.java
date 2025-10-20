package com.example.lotterywebautotest.tests;

import com.example.lotterywebautotest.pages.HomePage;
import com.example.lotterywebautotest.pages.LoginPage;
import com.example.lotterywebautotest.pages.RegisterPage;
import com.example.lotterywebautotest.utils.ScreenshotUtil;
import com.example.lotterywebautotest.utils.TestDataProvider;
import com.example.lotterywebautotest.utils.WebDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.*;


public class UserAuthTest {

    private WebDriver driver;
    private HomePage homePage;
    private LoginPage loginPage;
    private RegisterPage registerPage;

    @BeforeClass
    public void setUp() {
        // 初始化WebDriver
        driver = WebDriverUtils.getDriver();
        driver.get(TestDataProvider.getBaseUrl());

        // 初始化页面对象
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        registerPage = new RegisterPage(driver);
    }

    @AfterClass
    public void tearDown() {
        // 关闭WebDriver
        WebDriverUtils.quitDriver();
    }

    @BeforeMethod
    public void beforeMethod() {
        // 每个测试方法执行前刷新页面
        driver.navigate().refresh();
    }

    @AfterMethod
    public void afterMethod(org.testng.ITestResult result) {
        // 测试失败时截图
        if (result.getStatus() == org.testng.ITestResult.FAILURE) {
            ScreenshotUtil.takeFailureScreenshot(driver, result.getName());
        }
    }/////////////////////////////////////

    @Test(description = "验证首页正常加载")
    public void testHomePageLoaded() {
        // 验证首页正常加载
        homePage.verifyHomePageLoaded();
        homePage.verifyPageTitle();
        homePage.verifyNavigationFunctionality();
        homePage.verifyMainFeatures();
        homePage.verifyPageLoadPerformance();
    }

    @Test(description = "验证用户正常注册流程")
    public void testUserRegistrationSuccess() throws InterruptedException {
        // 点击注册按钮
        homePage.clickRegisterButton();

        // 验证注册页面加载
        registerPage.verifyRegisterPageLoaded();

        // 执行注册操作
        String username = TestDataProvider.generateRandomUsername();
        String email = TestDataProvider.generateRandomEmail();
        String phone = TestDataProvider.generateRandomPhone();
        String password = "123456";
        String identity = "普通用户";

        registerPage.register(username, email, phone, password, password, identity);

        // 验证注册成功
        registerPage.verifyRegistrationSuccess();
    }

    @Test(description = "验证邮箱已存在时注册失败")
    public void testRegistrationWithExistingEmail() {
        // 点击注册按钮
        homePage.clickRegisterButton();

        // 验证注册页面加载
        registerPage.verifyRegisterPageLoaded();

        // 使用已存在的邮箱注册
        String[] existingData = TestDataProvider.getExistingData();
        String username = TestDataProvider.generateRandomUsername();
        String existingEmail = existingData[0]; // 已存在的邮箱
        String phone = TestDataProvider.generateRandomPhone();
        String password = "123456";
        String identity = "普通用户";

        registerPage.register(username, existingEmail, phone, password, password, identity);

        // 验证注册失败
        registerPage.verifyRegisterFailed();
        registerPage.verifyErrorMessage("邮箱被使用");
    }


    @Test(description = "验证手机号已存在时注册失败")
    public void testRegistrationWithExistingPhone() {
        // 点击注册按钮
        homePage.clickRegisterButton();

        // 验证注册页面加载
        registerPage.verifyRegisterPageLoaded();

        // 使用已存在的手机号注册
        String[] existingData = TestDataProvider.getExistingData();
        String username = TestDataProvider.generateRandomUsername();
        String email = TestDataProvider.generateRandomEmail();
        String existingPhone = existingData[1]; // 已存在的手机号
        String password = "123456";
        String identity = "普通用户";

        registerPage.register(username, email, existingPhone, password, password, identity);

        // 验证注册失败
        registerPage.verifyRegisterFailed();
        registerPage.verifyErrorMessage("手机号被使用");
    }

    @Test(description = "验证密码格式错误时注册失败")
    public void testRegistrationWithInvalidPassword() {
        // 点击注册按钮
        homePage.clickRegisterButton();

        // 验证注册页面加载
        registerPage.verifyRegisterPageLoaded();

        // 使用无效密码注册
        String username = TestDataProvider.generateRandomUsername();
        String email = TestDataProvider.generateRandomEmail();
        String phone = TestDataProvider.generateRandomPhone();
        String invalidPassword = "123"; // 密码太短
        String identity = "普通用户";

        registerPage.register(username, email, phone, invalidPassword, invalidPassword, identity);

        // 验证注册失败
        registerPage.verifyRegisterFailed();
        registerPage.verifyErrorMessage("密码错误");
    }

    @Test(description = "验证用户正常登录流程")
    public void testUserLoginSuccess() throws InterruptedException {
        // 点击登录按钮
        homePage.clickLoginButton();
        // 验证登录页面加载
        loginPage.verifyLoginPageLoaded();
        // 执行登录操作
        String[] userData = TestDataProvider.getTestUserData();
        String emailOrPhone = userData[2]; // 邮箱
        String password = userData[1]; // 密码
        String identity = "普通用户";

        loginPage.loginWithPassword(emailOrPhone, password, identity);

        // 验证登录成功
        loginPage.verifyLoginSuccess();

        Thread.sleep(2000);
        // 验证首页显示已登录状态
        homePage.verifyLoggedIn("用户3"); // 根据实际显示的用户名调整
    }

    @Test(description = "验证用户名不存在时登录失败")
    public void testLoginWithNonExistentUser() {
        // 点击登录按钮
        homePage.clickLoginButton();

        // 验证登录页面加载
        loginPage.verifyLoginPageLoaded();

        // 使用不存在的用户名登录
        String emailOrPhone = "nonexistent@example.com";
        String password = "123456";
        String identity = "普通用户";

        loginPage.loginWithPassword(emailOrPhone, password, identity);

        // 验证登录失败
        loginPage.verifyLoginFailed();
        loginPage.verifyErrorMessage("用户信息为空");
    }


    @Test(description = "验证密码错误时登录失败")
    public void testLoginWithWrongPassword() {
        // 点击登录按钮
        homePage.clickLoginButton();

        // 验证登录页面加载
        loginPage.verifyLoginPageLoaded();

        // 使用错误密码登录
        String[] userData = TestDataProvider.getTestUserData();
        String emailOrPhone = userData[2]; // 邮箱
        String wrongPassword = "wrongpassword";
        String identity = "普通用户";

        loginPage.loginWithPassword(emailOrPhone, wrongPassword, identity);

        // 验证登录失败
        loginPage.verifyLoginFailed();
        loginPage.verifyErrorMessage("密码错误");
    }


    @Test(description = "验证用户登出功能")
    public void testUserLogout() throws InterruptedException {
        // 先登录
        homePage.clickLoginButton();
        loginPage.verifyLoginPageLoaded();

        String[] userData = TestDataProvider.getTestUserData();
        loginPage.loginWithPassword(userData[2], userData[1], "普通用户");
        loginPage.verifyLoginSuccess();

        // 验证已登录状态
        homePage.verifyLoggedIn("用户3");

        // 执行登出操作
        homePage.clickUserMenu();
        Thread.sleep(500);
        homePage.clickLogout();
        Thread.sleep(500);
        // 验证登出成功
        homePage.verifyNotLoggedIn();
    }


}