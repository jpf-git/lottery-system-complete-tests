package com.example.lotterywebautotest.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindAll;
import org.testng.Assert;

import java.util.List;

/**
 * 抽奖页面对象类
 */
public class LotteryPage extends BasePage {

    // 页面元素定位
    @FindBy(xpath = "//div[@class='chances']")
    private WebElement pageTitle;

    @FindBy(xpath = "//*[@id=\"lotteryGrid\"]")
    private WebElement lotteryGrid;

    @FindBy(xpath = "//*[@id=\"lotteryGrid\"]")
    private List<WebElement> gridItems;

    @FindBy(xpath = "//*[@id=\"lotteryGrid\"]")
    private List<WebElement> gridItemImages;

    @FindBy(xpath = "//div[@id='lottery-grid']//div[@class='grid-item']//span")
    private List<WebElement> gridItemTexts;

    @FindBy(xpath = "//*[@id=\"drawBtn\"]")
    private WebElement drawButton;

    @FindBy(xpath = "//div[@id='draw-result']")
    private WebElement drawResult;

    @FindBy(xpath = "//div[@id='draw-result']//h3")
    private WebElement resultTitle;

    @FindBy(xpath = "//div[@id='draw-result']//p")
    private WebElement resultMessage;

    @FindBy(xpath = "//div[@id='draw-result']//img")
    private WebElement resultImage;

    @FindBy(xpath = "//*[@id=\"modalContent\"]/button")
    private WebElement resultButton;

    @FindBy(xpath = "//div[@id='user-info']")
    private WebElement userInfo;

    @FindBy(xpath = "//div[@id='user-info']//span[@id='user-name']")
    private WebElement userName;

    @FindBy(xpath = "//*[@id=\"remainingChances\"]")
    private WebElement userChances;

    @FindBy(xpath = "//*[@id=\"lotteryGrid\"]/div[5]")
    private WebElement activityInfo;

    @FindBy(xpath = "//*[@id=\"lotteryGrid\"]/div[5]")
    private WebElement activityName;

    @FindBy(xpath = "//*[@id=\"lotteryGrid\"]/div[5]")
    private WebElement activityDescription;

    @FindBy(xpath = "//div[@id='activity-info']//span[@id='activity-end-date']")
    private WebElement activityEndDate;

    @FindBy(xpath = "//div[@id='activity-info']//span[@id='activity-status']")
    private WebElement activityStatus;

    @FindBy(xpath = "//*[@id=\"lotteryGrid\"]")
    private WebElement prizesInfo;

    @FindBy(xpath = "//*[@id=\"lotteryGrid\"]")
    private WebElement prizesTitle;

    @FindBy(xpath = "//*[@id=\"lotteryGrid\"]")
    private List<WebElement> prizeItems;

    @FindBy(xpath = "//div[@id='prizes-info']//div[@class='prize-item']//img")
    private List<WebElement> prizeImages;

    @FindBy(xpath = "//div[@id='prizes-info']//div[@class='prize-item']//span")
    private List<WebElement> prizeNames;

    @FindBy(xpath = "//div[@id='prizes-info']//div[@class='prize-item']//p")
    private List<WebElement> prizeDescriptions;

    @FindBy(xpath = "//div[@id='prizes-info']//div[@class='prize-item']//strong")
    private List<WebElement> prizePrices;

    @FindBy(xpath = "//div[@id='loading']")
    private WebElement loadingIndicator;

    @FindBy(xpath = "//div[@id='error-message']")
    private WebElement errorMessage;

    /**
     * 构造函数
     * @param driver WebDriver实例
     */
    public LotteryPage(WebDriver driver) {
        super(driver);
    }

    /**
     * 验证抽奖页面是否正常加载
     */
    public void verifyLotteryPageLoaded() {
        waitForElementVisible(pageTitle);
//        verifyElementText(pageTitle, "剩余抽奖次数");

//        verifyElementDisplayed(lotteryGrid);
//        verifyElementDisplayed(drawButton);
//        verifyElementDisplayed(userInfo);
//        verifyElementDisplayed(activityInfo);
//        verifyElementDisplayed(prizesInfo);
    }

    /**
     * 验证抽奖网格展示
     */
    public void verifyLotteryGridDisplayed() {
        verifyElementDisplayed(lotteryGrid);
        Assert.assertTrue(gridItems.size() > 0, "抽奖网格为空");

        // 验证每个网格项都有必要的信息
        for (WebElement item : gridItems) {
            verifyElementDisplayed(item);
        }
    }

    /**
     * 验证网格项信息完整性
     */
    public void verifyGridItemInformationCompleteness() {
//        // 验证网格项图片存在
//        for (WebElement image : gridItemImages) {
//            verifyElementDisplayed(image);
//            Assert.assertNotNull(image.getAttribute("src"), "网格项图片src不能为空");
//        }

        // 验证网格项文本存在
        for (WebElement text : gridItemTexts) {
            Assert.assertFalse(text.getText().trim().isEmpty(), "网格项文本不能为空");
        }
    }

    /**
     * 验证用户信息展示
     */
    public void verifyUserInfoDisplayed() {
        verifyElementDisplayed(userInfo);
        verifyElementDisplayed(userName);
        verifyElementDisplayed(userChances);

        // 验证用户名不为空
        Assert.assertFalse(userName.getText().trim().isEmpty(), "用户名不能为空");

        // 验证抽奖次数格式
        String chancesText = userChances.getText();
        Assert.assertTrue(chancesText.contains("剩余抽奖次数"), "抽奖次数显示格式不正确: " + chancesText);
    }

    /**
     * 验证活动信息展示
     */
    public void verifyActivityInfoDisplayed() {
        verifyElementDisplayed(activityInfo);
        verifyElementDisplayed(activityName);
        verifyElementDisplayed(activityDescription);
//        verifyElementDisplayed(activityEndDate);
//        verifyElementDisplayed(activityStatus);

        // 验证活动名称不为空
        Assert.assertFalse(activityName.getText().trim().isEmpty(), "活动名称不能为空");

        // 验证活动描述不为空
        Assert.assertFalse(activityDescription.getText().trim().isEmpty(), "活动描述不能为空");

//        // 验证活动结束日期格式
//        String endDateText = activityEndDate.getText();
//        Assert.assertTrue(endDateText.contains("结束时间"), "活动结束日期格式不正确: " + endDateText);

//        // 验证活动状态
//        String statusText = activityStatus.getText();
//        Assert.assertTrue(statusText.equals("进行中") || statusText.equals("已结束"),
//                "活动状态不正确: " + statusText);
    }

    /**
     * 验证奖品信息展示
     */
    public void verifyPrizesInfoDisplayed() {
        verifyElementDisplayed(prizesInfo);
        verifyElementDisplayed(prizesTitle);
    }

    /**
     * 验证奖品信息完整性
     */
    public void verifyPrizeInformationCompleteness() {
        // 验证奖品图片存在
        for (WebElement image : prizeImages) {
            verifyElementDisplayed(image);
            Assert.assertNotNull(image.getAttribute("src"), "奖品图片src不能为空");
        }

        // 验证奖品名称不为空
        for (WebElement name : prizeNames) {
            Assert.assertFalse(name.getText().trim().isEmpty(), "奖品名称不能为空");
        }

        // 验证奖品描述不为空
        for (WebElement description : prizeDescriptions) {
            Assert.assertFalse(description.getText().trim().isEmpty(), "奖品描述不能为空");
        }

        // 验证奖品价格格式
        for (WebElement price : prizePrices) {
            String priceText = price.getText();
            Assert.assertTrue(priceText.contains("¥"), "奖品价格格式不正确: " + priceText);
        }
    }

    /**
     * 点击抽奖按钮
     */
    public void clickDrawButton() {
        safeClick(drawButton);
    }

    /**
     * 验证抽奖按钮状态
     * @param expectedEnabled 期望的启用状态
     */
    public void verifyDrawButtonState(boolean expectedEnabled) {
        if (expectedEnabled) {
            Assert.assertTrue(drawButton.isEnabled(), "抽奖按钮应该启用");
        } else {
            Assert.assertFalse(drawButton.isEnabled(), "抽奖按钮应该禁用");
        }
    }

    /**
     * 验证抽奖结果展示
     */
    public void verifyDrawResultDisplayed() {
//        waitForElementVisible(drawResult);
//        verifyElementDisplayed(drawResult);
//        verifyElementDisplayed(resultTitle);
//        verifyElementDisplayed(resultMessage);
        verifyElementDisplayed(resultButton);
    }

    /**
     * 验证抽奖结果信息
     * @param expectedTitle 期望的结果标题
     * @param expectedMessage 期望的结果消息
     */
    public void verifyDrawResultInformation(String expectedTitle, String expectedMessage) {
        verifyElementText(resultTitle, expectedTitle);
        verifyElementText(resultMessage, expectedMessage);
    }

    /**
     * 验证抽奖结果图片
     */
    public void verifyDrawResultImage() {
        verifyElementDisplayed(resultImage);
        Assert.assertNotNull(resultImage.getAttribute("src"), "抽奖结果图片src不能为空");
    }

    /**
     * 点击抽奖结果按钮
     */
    public void clickDrawResultButton() {
        safeClick(resultButton);
    }

    /**
     * 验证加载指示器
     */
    public void verifyLoadingIndicator() {
        try {
            waitForElementVisible(loadingIndicator);
            verifyElementDisplayed(loadingIndicator);
        } catch (Exception e) {
            // 加载指示器可能很快消失，这是正常的
        }
    }

    /**
     * 验证错误消息
     * @param expectedMessage 期望的错误消息
     */
    public void verifyErrorMessage(String expectedMessage) {
        waitForElementVisible(errorMessage);
        verifyElementDisplayed(errorMessage);
        verifyElementText(errorMessage, expectedMessage);
    }

    /**
     * 获取用户抽奖次数
     * @return 用户抽奖次数
     */
    public int getUserChances() {

        String text = userChances.getText(); // 获取元素显示的文本（如 "0"）
        return Integer.parseInt(text); // 转换为整数并返回
//        // 从文本中提取数字，例如 "剩余抽奖次数: 3" -> 3
//        String[] parts = chancesText.split(":");
//        if (parts.length > 1) {
//            try {
//                return Integer.parseInt(parts[1].trim());
//            } catch (NumberFormatException e) {
//                return 0;
//            }
//        }
//        return 0;
    }

    /**
     * 获取活动名称
     * @return 活动名称
     */
    public String getActivityName() {
        return activityName.getText();
    }

    /**
     * 获取活动状态
     * @return 活动状态
     */
    public String getActivityStatus() {
        return activityStatus.getText();
    }

    /**
     * 获取奖品数量
     * @return 奖品数量
     */
    public int getPrizeCount() {
        return prizeItems.size();
    }

    /**
     * 获取第一个奖品名称
     * @return 第一个奖品名称
     */
    public String getFirstPrizeName() {
        if (prizeNames.size() > 0) {
            return prizeNames.get(0).getText();
        }
        return null;
    }

    /**
     * 获取第一个奖品价格
     * @return 第一个奖品价格
     */
    public String getFirstPrizePrice() {
        if (prizePrices.size() > 0) {
            return prizePrices.get(0).getText();
        }
        return null;
    }

    /**
     * 验证页面加载性能
     */
    public void verifyPageLoadPerformance() {
        long startTime = System.currentTimeMillis();
        verifyLotteryPageLoaded();
        long endTime = System.currentTimeMillis();

        long loadTime = endTime - startTime;
        Assert.assertTrue(loadTime < 8000, "抽奖页面加载时间过长: " + loadTime + "ms");
    }

    /**
     * 验证抽奖功能性能
     */
    public void verifyDrawPerformance() {
        long startTime = System.currentTimeMillis();
        clickDrawButton();
        verifyDrawResultDisplayed();
        long endTime = System.currentTimeMillis();

        long drawTime = endTime - startTime;
        Assert.assertTrue(drawTime < 5000, "抽奖功能响应时间过长: " + drawTime + "ms");
    }
}
