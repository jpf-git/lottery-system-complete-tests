package com.example.lotterywebautotest.pages;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

/**
 * 首页对象类
 */
public class HomePage extends BasePage {

    // 页面元素定位
    @FindBy(xpath = "//div[@class='nav-logo']")
    private WebElement logo;

    @FindBy(xpath = "//a[@href='#home']")
    private WebElement homeLink;

    @FindBy(xpath = "//*[@id=\"admin\"]/div/div[1]/button[1]")
    private WebElement prizesLink;

    @FindBy(xpath = "//a[@href='#activities']")
    private WebElement activitiesLink;

    @FindBy(xpath = "//*[@id=\"admin\"]/div/div[1]/button[2]")
    private WebElement activitiesManage;

    @FindBy(xpath = "//a[@href='#admin']")
    private WebElement adminLink;

    @FindBy(xpath = "//button[text()='登录']")
    private WebElement loginButton;

    @FindBy(xpath = "//button[text()='注册']")
    private WebElement registerButton;

    @FindBy(xpath = "//div[@class='nav-user']")
    private WebElement userMenu;

    @FindBy(xpath = "//span[@id='user-name']")
    private WebElement userName;

    @FindBy(xpath = "//a[text()='我的奖品']")
    private WebElement myPrizesLink;

    @FindBy(xpath = "//a[text()='退出登录']")
    private WebElement logoutLink;

    @FindBy(xpath = "//h1[text()='开启你的幸运之旅']")
    private WebElement mainTitle;

    @FindBy(xpath = "//p[contains(text(), '参与精彩抽奖活动')]")
    private WebElement mainDescription;

    @FindBy(xpath = "//div[@class='hero-features']")
    private WebElement featuresSection;

    @FindBy(xpath = "//div[@class='hero-visual']")
    private WebElement visualSection;

    /**
     * 构造函数
     * @param driver WebDriver实例
     */
    public HomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * 验证首页是否正常加载
     */
    public void verifyHomePageLoaded() {
        waitForElementVisible(logo);
        verifyElementDisplayed(mainTitle);
        verifyElementDisplayed(mainDescription);
        verifyElementDisplayed(featuresSection);
        verifyElementDisplayed(visualSection);
    }

    /**
     * 验证导航栏元素
     */
    public void verifyNavigationElements() {
        verifyElementDisplayed(homeLink);
        verifyElementDisplayed(loginButton);
        verifyElementDisplayed(registerButton);
        verifyElementDisplayed(logo);
    }

    /**
     * 验证未登录状态
     */
    public void verifyNotLoggedIn() {
        verifyElementDisplayed(loginButton);
        verifyElementDisplayed(registerButton);
        verifyElementHidden(userMenu);
    }

    /**
     * 验证已登录状态
     * @param expectedUserName 期望的用户名
     */
    public void verifyLoggedIn(String expectedUserName) {
        verifyElementHidden(loginButton);
        verifyElementHidden(registerButton);
        verifyElementDisplayed(userMenu);
        verifyElementText(userName, expectedUserName);
    }

    /**
     * 点击登录按钮
     */
    public void clickLoginButton() {
        safeClick(loginButton);
    }

    /**
     * 点击注册按钮
     */
    public void clickRegisterButton() {
        safeClick(registerButton);
    }

    /**
     * 点击奖品链接
     */
    public void clickPrizesLink() {
        safeClick(prizesLink);
    }

    /**
     * 点击活动链接
     */
    public void clickActivitiesLink() {
        safeClick(activitiesLink);
    }

    /**
     * 点击活动管理
     */
    public void clickActivityManagementLink() {
        safeClick(activitiesManage);
    }

    /**
     * 点击管理后台链接
     */
    public void clickAdminLink() {
        safeClick(adminLink);
    }

    /**
     * 点击用户菜单
     */
    public void clickUserMenu() {
        safeClick(userMenu);
    }

    /**
     * 点击我的奖品
     */
    public void clickMyPrizes() {
        safeClick(myPrizesLink);
    }

    /**
     * 点击退出登录
     */
    public void clickLogout() {
        safeClick(logoutLink);
    }

    /**
     * 验证页面标题
     */
    public void verifyPageTitle() {
        verifyPageTitle("抽奖管理系统");
    }

    /**
     * 验证主要功能区域
     */
    public void verifyMainFeatures() {
        // 验证特色介绍区域
        verifyElementDisplayed(featuresSection);

        // 验证视觉展示区域
        verifyElementDisplayed(visualSection);

        // 验证主要标题和描述
        verifyElementText(mainTitle, "开启你的幸运之旅");
        verifyElementContainsText(mainDescription, "参与精彩抽奖活动");
    }

    /**
     * 验证导航功能
     */
    public void verifyNavigationFunctionality() {
        // 验证所有导航链接都可点击
        verifyElementDisplayed(homeLink);
//        verifyElementDisplayed(prizesLink);
//        verifyElementDisplayed(activitiesLink);
//        verifyElementDisplayed(adminLink);

        // 验证登录注册按钮可点击
        verifyElementDisplayed(loginButton);
        verifyElementDisplayed(registerButton);
    }

    /**
     * 验证页面加载性能
     */
    public void verifyPageLoadPerformance() {
        // 验证页面在合理时间内加载完成
        long startTime = System.currentTimeMillis();
        verifyHomePageLoaded();
        long endTime = System.currentTimeMillis();

        long loadTime = endTime - startTime;
        Assert.assertTrue(loadTime < 5000, "页面加载时间过长: " + loadTime + "ms");
    }

    // Getter方法供其他类使用
    public WebElement getAdminLink() {
        return adminLink;
    }


}




