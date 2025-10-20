package com.example.lotterywebautotest.pages;


import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

/**
 * 注册页面对象类
 */
public class RegisterPage extends BasePage {

    // 页面元素定位
    @FindBy(xpath = "//h2[text()='用户注册']")
    private WebElement registerTitle;

    @FindBy(xpath = "//input[@id='register-name']")
    private WebElement usernameInput;

    @FindBy(xpath = "//input[@id='register-mail']")
    private WebElement emailInput;

    @FindBy(xpath = "//input[@id='register-phoneNumber']")
    private WebElement phoneInput;

    @FindBy(xpath = "//input[@id='register-password']")
    private WebElement passwordInput;

    @FindBy(xpath = "//input[@id='register-confirmPassword']")
    private WebElement confirmPasswordInput;

    @FindBy(xpath = "//select[@id='register-identity']")
    private WebElement identitySelect;

    @FindBy(xpath = "//*[@id=\"register-form\"]/button")
    private WebElement registerButton;

    @FindBy(xpath = "//span[@class='close']")
    private WebElement closeButton;

//    @FindBy(xpath = "//div[@class='error-message']")
    @FindBy(xpath = "//*[@id=\"message\"]")
    private WebElement errorMessage;

    @FindBy(xpath = "//div[@class='success-message']")
    private WebElement successMessage;

    /**
     * 构造函数
     * @param driver WebDriver实例
     */
    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    /**
     * 验证注册页面是否正常加载
     */
    public void verifyRegisterPageLoaded(){
        waitForElementVisible(registerTitle);
        verifyElementText(registerTitle, "用户注册");
        verifyElementDisplayed(usernameInput);
        verifyElementDisplayed(emailInput);
        verifyElementDisplayed(phoneInput);
        verifyElementDisplayed(passwordInput);
        verifyElementDisplayed(confirmPasswordInput);
        verifyElementDisplayed(identitySelect);


//        scrollToElement(registerButton);
        // 滚动注册弹窗内容到底部
//        js.executeScript("var modal = document.querySelector('.modal-content, .popup-content, [class*=\"modal\"]'); if(modal) { modal.scrollTop = modal.scrollHeight; }");
//        Thread.sleep(1000);
        // 滚动注册弹窗内部的内容
//        js.executeScript("document.querySelector('.modal-content').scrollTop = document.querySelector('.modal-content').scrollHeight;");
//        Thread.sleep(1000);
        // 查找注册弹窗并滚动其内容
//        js.executeScript("var modal = document.querySelector('[class*=\"modal\"]'); if(modal) { modal.scrollTop = modal.scrollHeight; }");
//        Thread.sleep(1000);
        // 滚动所有可能的弹窗容器
//        js.executeScript("var containers = document.querySelectorAll('.modal, .popup, .dialog, [role=\"dialog\"]'); containers.forEach(function(container) { container.scrollTop = container.scrollHeight; });");
//        Thread.sleep(1000);
//
//        verifyElementDisplayed(registerButton);

    }

    /**
     * 输入用户名
     * @param username 用户名
     */
    public void enterUsername(String username) {
        safeSendKeys(usernameInput, username);
    }

    /**
     * 输入邮箱
     * @param email 邮箱
     */
    public void enterEmail(String email) {
        safeSendKeys(emailInput, email);
    }

    /**
     * 输入手机号
     * @param phone 手机号
     */
    public void enterPhone(String phone) {
        safeSendKeys(phoneInput, phone);
    }

    /**
     * 输入密码
     * @param password 密码
     */
    public void enterPassword(String password) {
        safeSendKeys(passwordInput, password);
    }

    /**
     * 输入确认密码
     * @param confirmPassword 确认密码
     */
    public void enterConfirmPassword(String confirmPassword) {
        safeSendKeys(confirmPasswordInput, confirmPassword);
    }

    /**
     * 选择身份
     * @param identity 身份类型
     */
    public void selectIdentity(String identity) {
        safeSelectByText(identitySelect, identity);
    }

    /**
     * 点击注册按钮
     */
    public void clickRegisterButton() {
        safeClick(registerButton);
    }

    /**
     * 关闭注册弹窗
     */
    public void closeRegisterModal() {
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
     * 获取成功信息
     * @return 成功信息文本
     */
    public String getSuccessMessage() {
        waitForElementVisible(successMessage);
        return successMessage.getText();
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
     * 验证成功信息
     * @param expectedMessage 期望的成功信息
     */
    public void verifySuccessMessage(String expectedMessage) {
        String actualMessage = getSuccessMessage();
        Assert.assertTrue(actualMessage.contains(expectedMessage),
                "成功信息不匹配，期望包含: " + expectedMessage + "，实际: " + actualMessage);
    }

    /**
     * 执行注册操作
     * @param username 用户名
     * @param email 邮箱
     * @param phone 手机号
     * @param password 密码
     * @param confirmPassword 确认密码
     * @param identity 身份类型
     */
    public void register(String username, String email, String phone,
                         String password, String confirmPassword, String identity) {
        enterUsername(username);
        enterEmail(email);
        enterPhone(phone);
        enterPassword(password);
        enterConfirmPassword(confirmPassword);

        selectIdentity(identity);


        clickRegisterButton();
    }

    /**
     * 验证注册成功
     */
    // 验证注册成功的方法
    public boolean verifyRegistrationSuccess() {
        try {
            // 创建等待对象，设置最长等待时间为10秒（可根据实际情况调整）
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // 定位成功提示元素（根据实际页面元素调整选择器）
            // 示例：假设成功提示是class为"success-message"的div元素
            WebElement successMessage = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//div[@class='success-message' and contains(text(), '注册成功')]")
                    )
            );

            // 如果元素可见且包含预期文本，返回验证成功
            return successMessage.isDisplayed() &&
                    successMessage.getText().contains("注册成功");

        } catch (Exception e) {
            // 若等待超时或元素未找到，返回验证失败
            System.out.println("注册成功提示未出现：" + e.getMessage());
            return false;
        }
    }

    /**
     * 验证注册失败
     */
    public void verifyRegisterFailed() {
        // 验证仍在注册页面或显示错误信息
        waitForElementVisible(registerTitle);
        verifyElementDisplayed(errorMessage);
    }

    /**
     * 清空所有输入框
     */
    public void clearAllFields() {
        usernameInput.clear();
        emailInput.clear();
        phoneInput.clear();
        passwordInput.clear();
        confirmPasswordInput.clear();
    }
}