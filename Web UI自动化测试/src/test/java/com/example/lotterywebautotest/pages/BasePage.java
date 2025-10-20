package com.example.lotterywebautotest.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

/**
 * 基础页面类，包含所有页面通用的方法和元素
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor js;
    protected Actions actions;

    /**
     * 构造函数，初始化页面元素
     * @param driver WebDriver实例
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.js = (JavascriptExecutor) driver;
        this.actions = new Actions(driver);

        // 初始化页面元素
        PageFactory.initElements(driver, this);
    }

    /**
     * 等待元素可见
     * @param element 要等待的元素
     */
    protected void waitForElementVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * 等待元素可点击
     * @param element 要等待的元素
     */
    protected void waitForElementClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * 等待元素存在
     * @param element 要等待的元素
     */
    protected void waitForElementPresent(WebElement element) {
        wait.until(ExpectedConditions.presenceOfElementLocated(
                org.openqa.selenium.By.xpath("//*[contains(@class, '" + element.getAttribute("class") + "')]")));
    }

    /**
     * 安全点击元素
     * @param element 要点击的元素
     */
    protected void safeClick(WebElement element) {
        waitForElementClickable(element);
        try {
            element.click();
        } catch (Exception e) {
            // 如果普通点击失败，尝试JavaScript点击
            js.executeScript("arguments[0].click();", element);
        }
    }

    /**
     * 安全输入文本
     * @param element 输入框元素
     * @param text 要输入的文本
     */
    protected void safeSendKeys(WebElement element, String text) {
        waitForElementVisible(element);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * 安全选择下拉框选项
     * @param element 下拉框元素
     * @param optionText 选项文本
     */
    protected void safeSelectByText(WebElement element, String optionText) {
        waitForElementVisible(element);
        Select select = new Select(element);
        select.selectByVisibleText(optionText);
    }

    /**
     * 滚动到元素位置
     * @param element 目标元素
     */
    protected void scrollToElement(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * 验证页面标题
     * @param expectedTitle 期望的页面标题
     */
    protected void verifyPageTitle(String expectedTitle) {
        wait.until(ExpectedConditions.titleContains(expectedTitle));
        Assert.assertTrue(driver.getTitle().contains(expectedTitle),
                "页面标题不匹配，期望: " + expectedTitle + "，实际: " + driver.getTitle());
    }

    /**
     * 验证URL包含指定文本
     * @param expectedUrlPart 期望的URL部分
     */
    protected void verifyUrlContains(String expectedUrlPart) {
        wait.until(ExpectedConditions.urlContains(expectedUrlPart));
        Assert.assertTrue(driver.getCurrentUrl().contains(expectedUrlPart),
                "URL不包含期望文本: " + expectedUrlPart);
    }

    /**
     * 验证元素文本
     * @param element 要验证的元素
     * @param expectedText 期望的文本
     */
    protected void verifyElementText(WebElement element, String expectedText) {
        waitForElementVisible(element);
        Assert.assertEquals(element.getText(), expectedText,
                "元素文本不匹配，期望: " + expectedText + "，实际: " + element.getText());
    }

    /**
     * 验证元素包含指定文本
     * @param element 要验证的元素
     * @param expectedText 期望包含的文本
     */
    protected void verifyElementContainsText(WebElement element, String expectedText) {
        waitForElementVisible(element);
        Assert.assertTrue(element.getText().contains(expectedText),
                "元素文本不包含期望内容: " + expectedText + "，实际: " + element.getText());
    }

    /**
     * 验证元素是否显示
     * @param element 要验证的元素
     */
    protected void verifyElementDisplayed(WebElement element) {
        waitForElementVisible(element);
        Assert.assertTrue(element.isDisplayed(), "元素未显示");
    }

    /**
     * 验证元素是否隐藏
     * @param element 要验证的元素
     */
    protected void verifyElementHidden(WebElement element) {
        Assert.assertFalse(element.isDisplayed(), "元素应该隐藏但实际显示了");
    }

    /**
     * 获取页面源码
     * @return 页面源码
     */
    protected String getPageSource() {
        return driver.getPageSource();
    }

    /**
     * 刷新页面
     */
    protected void refreshPage() {
        driver.navigate().refresh();
    }

    /**
     * 返回上一页
     */
    protected void goBack() {
        driver.navigate().back();
    }

    /**
     * 前进到下一页
     */
    protected void goForward() {
        driver.navigate().forward();
    }
}
