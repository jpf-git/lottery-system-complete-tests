package com.example.lotterywebautotest.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

/**
 * 登录页面对象类
 */
public class LoginPage extends BasePage {

    // 页面元素定位
    @FindBy(xpath = "//h2[text()='用户登录']")
    private WebElement loginTitle;

    @FindBy(xpath = "//button[text()='密码登录']")
    private WebElement passwordLoginTab;

    @FindBy(xpath = "//button[text()='验证码登录']")
    private WebElement codeLoginTab;

    @FindBy(xpath = "//*[@id=\"password-loginName\"]")
    private WebElement emailPhoneInput;

    @FindBy(xpath = "//input[@type='password']")
    private WebElement passwordInput;

    @FindBy(xpath = "//input[@type='email']")
    private WebElement emailInput;

    @FindBy(xpath = "//input[@placeholder='验证码']")
    private WebElement verificationCodeInput;

    @FindBy(xpath = "//button[text()='发送验证码']")
    private WebElement sendCodeButton;

    @FindBy(xpath = "//select[@id='password-identity']")
    private WebElement passwordIdentitySelect;

    @FindBy(xpath = "//select[@id='code-identity']")
    private WebElement codeIdentitySelect;

    @FindBy(xpath = "//*[@id=\"password-login\"]/button")
    private WebElement loginButton;

    @FindBy(xpath = "//span[@class='close']")
    private WebElement closeButton;

    @FindBy(xpath = "//*[@id=\"message\"]")
    private WebElement errorMessage;

    /**
     * 构造函数
     * @param driver WebDriver实例
     */
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * 验证登录页面是否正常加载
     */
    public void verifyLoginPageLoaded() {
        waitForElementVisible(loginTitle);
        verifyElementText(loginTitle, "用户登录");
        verifyElementDisplayed(passwordLoginTab);
        verifyElementDisplayed(codeLoginTab);
    }

    /**
     * 切换到密码登录标签
     */
    public void switchToPasswordLogin() {
        safeClick(passwordLoginTab);
        waitForElementVisible(emailPhoneInput);
    }

    /**
     * 切换到验证码登录标签
     */
    public void switchToCodeLogin() {
        safeClick(codeLoginTab);
        waitForElementVisible(emailInput);
    }

    /**
     * 输入邮箱或手机号
     * @param emailOrPhone 邮箱或手机号
     */
    public void enterEmailOrPhone(String emailOrPhone) {
        safeSendKeys(emailPhoneInput, emailOrPhone);
    }

    /**
     * 输入密码
     * @param password 密码
     */
    public void enterPassword(String password) {
        safeSendKeys(passwordInput, password);
    }

    /**
     * 输入邮箱（验证码登录）
     * @param email 邮箱
     */
    public void enterEmail(String email) {
        safeSendKeys(emailInput, email);
    }

    /**
     * 输入验证码
     * @param code 验证码
     */
    public void enterVerificationCode(String code) {
        safeSendKeys(verificationCodeInput, code);
    }

    /**
     * 选择身份（密码登录）
     * @param identity 身份类型
     */
    public void selectPasswordIdentity(String identity) {
        safeSelectByText(passwordIdentitySelect, identity);
    }

    /**
     * 选择身份（验证码登录）
     * @param identity 身份类型
     */
    public void selectCodeIdentity(String identity) {
        safeSelectByText(codeIdentitySelect, identity);
    }

    /**
     * 点击登录按钮
     */
    public void clickLoginButton() {
        safeClick(loginButton);
    }

    /**
     * 点击发送验证码按钮
     */
    public void clickSendCodeButton() {
        safeClick(sendCodeButton);
    }

    /**
     * 关闭登录弹窗
     */
    public void closeLoginModal() {
        safeClick(closeButton);
    }

    /**
     * 获取错误信息
     * @return 错误信息文本
     */
    public String getErrorMessage() {
        waitForElementVisible(errorMessage);
        return errorMessage.getText();
    }

    /**
     * 验证错误信息
     * @param expectedMessage 期望的错误信息
     */
    public void verifyErrorMessage(String expectedMessage) {
        String actualMessage = getErrorMessage();
        Assert.assertTrue(actualMessage.contains(expectedMessage),
                "错误信息不匹配，期望包含: " + expectedMessage + "，实际: " + actualMessage);
    }

    /**
     * 密码登录
     * @param emailOrPhone 邮箱或手机号
     * @param password 密码
     * @param identity 身份类型
     */
    public void loginWithPassword(String emailOrPhone, String password, String identity) {
        switchToPasswordLogin();
        enterEmailOrPhone(emailOrPhone);
        enterPassword(password);
        selectPasswordIdentity(identity);
        clickLoginButton();
    }

    /**
     * 验证码登录
     * @param email 邮箱
     * @param code 验证码
     * @param identity 身份类型
     */
    public void loginWithCode(String email, String code, String identity) {
        switchToCodeLogin();
        enterEmail(email);
        enterVerificationCode(code);
        selectCodeIdentity(identity);
        clickLoginButton();
    }

    /**
     * 验证登录成功
     */
    public boolean verifyLoginSuccess() {
        try {
            // 创建等待对象，设置最长等待时间为10秒（可根据实际情况调整）
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

            // 定位成功提示元素（根据实际页面元素调整选择器）
            // 示例：假设成功提示是class为"success-message"的div元素
            WebElement successMessage = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//div[@class='success-message' and contains(text(), '登录成功')]")
                    )
            );

            // 如果元素可见且包含预期文本，返回验证成功
            return successMessage.isDisplayed() &&
                    successMessage.getText().contains("登录成功");

        } catch (Exception e) {
            // 若等待超时或元素未找到，返回验证失败
            System.out.println("登录成功提示未出现：" + e.getMessage());
            return false;
        }
    }

    /**
     * 验证登录失败
     */
    public void verifyLoginFailed() {
        // 验证仍在登录页面或显示错误信息
        waitForElementVisible(loginTitle);
        verifyElementDisplayed(errorMessage);
    }
}