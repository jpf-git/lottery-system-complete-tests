package com.example.lotterywebautotest.pages;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindAll;
import org.testng.Assert;

import java.util.List;

/**
 * 用户中奖记录页面对象类
 */
public class UserPrizeRecordPage extends BasePage {

    // 页面元素定位
    @FindBy(xpath = "//h3[text()='我的中奖记录']")
    private WebElement pageTitle;

    @FindBy(xpath = "//div[@id='prize-records-table-body']")
    private WebElement recordsTableBody;

    @FindAll({
            @FindBy(xpath = "//div[@id='prize-records-table-body']//tr"),
            @FindBy(xpath = "//table//tbody//tr")
    })
    private List<WebElement> recordRows;

    @FindBy(xpath = "//table//tbody//tr//td[1]")
    private List<WebElement> recordIds;

    @FindBy(xpath = "//table//tbody//tr//td[2]")
    private List<WebElement> prizeNames;

    @FindBy(xpath = "//table//tbody//tr//td[3]")
    private List<WebElement> prizeDescriptions;

    @FindBy(xpath = "//table//tbody//tr//td[4]")
    private List<WebElement> prizePrices;

    @FindBy(xpath = "//table//tbody//tr//td[5]")
    private List<WebElement> drawDates;

    @FindBy(xpath = "//table//tbody//tr//td[6]")
    private List<WebElement> recordStatuses;

    @FindBy(xpath = "//table//tbody//tr//td[7]//img")
    private List<WebElement> prizeImages;

    @FindBy(xpath = "//div[@class='pagination']")
    private WebElement pagination;

    @FindBy(xpath = "//button[@class='pagination-btn' and text()='下一页']")
    private WebElement nextPageButton;

    @FindBy(xpath = "//button[@class='pagination-btn' and text()='上一页']")
    private WebElement prevPageButton;

    @FindBy(xpath = "//span[@class='current-page']")
    private WebElement currentPageIndicator;

    @FindBy(xpath = "//div[@id='no-records']")
    private WebElement noRecordsMessage;

    @FindBy(xpath = "//div[@id='user-summary']")
    private WebElement userSummary;

    @FindBy(xpath = "//div[@id='user-summary']//span[@id='total-prizes']")
    private WebElement totalPrizes;

    @FindBy(xpath = "//div[@id='user-summary']//span[@id='total-value']")
    private WebElement totalValue;

    @FindBy(xpath = "//div[@id='user-summary']//span[@id='last-draw-date']")
    private WebElement lastDrawDate;

    /**
     * 构造函数
     * @param driver WebDriver实例
     */
    public UserPrizeRecordPage(WebDriver driver) {
        super(driver);
    }

    /**
     * 验证用户中奖记录页面是否正常加载
     */
    public void verifyUserPrizeRecordPageLoaded() {
        waitForElementVisible(pageTitle);
        verifyElementText(pageTitle, "我的中奖记录");
        verifyElementDisplayed(recordsTableBody);
        verifyElementDisplayed(userSummary);
    }

    /**
     * 验证中奖记录列表展示
     */
    public void verifyPrizeRecordsDisplayed() {
        if (recordRows.size() > 0) {
            // 验证每个记录行都有必要的信息
            for (WebElement row : recordRows) {
                verifyElementDisplayed(row);
            }
        } else {
            // 如果没有记录，验证无记录消息
            verifyElementDisplayed(noRecordsMessage);
        }
    }

    /**
     * 验证中奖记录信息完整性
     */
    public void verifyPrizeRecordInformationCompleteness() {
        if (recordRows.size() > 0) {
            // 验证记录ID不为空
            for (WebElement recordId : recordIds) {
                Assert.assertFalse(recordId.getText().trim().isEmpty(), "记录ID不能为空");
            }

            // 验证奖品名称不为空
            for (WebElement prizeName : prizeNames) {
                Assert.assertFalse(prizeName.getText().trim().isEmpty(), "奖品名称不能为空");
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

            // 验证抽奖日期不为空
            for (WebElement drawDate : drawDates) {
                Assert.assertFalse(drawDate.getText().trim().isEmpty(), "抽奖日期不能为空");
            }

            // 验证记录状态
            for (WebElement status : recordStatuses) {
                String statusText = status.getText();
                Assert.assertTrue(statusText.equals("已中奖") || statusText.equals("未中奖"),
                        "记录状态不正确: " + statusText);
            }

            // 验证奖品图片存在
            for (WebElement image : prizeImages) {
                verifyElementDisplayed(image);
                Assert.assertNotNull(image.getAttribute("src"), "奖品图片src不能为空");
            }
        }
    }

    /**
     * 验证分页功能
     */
    public void verifyPaginationFunctionality() {
        if (isPaginationVisible()) {
            verifyElementDisplayed(pagination);
            verifyElementDisplayed(currentPageIndicator);

            // 验证当前页码显示
            String currentPage = currentPageIndicator.getText();
            Assert.assertTrue(currentPage.matches("\\d+"), "当前页码格式不正确: " + currentPage);
        }
    }

    /**
     * 验证用户统计信息
     */
    public void verifyUserSummaryDisplayed() {
        verifyElementDisplayed(userSummary);
        verifyElementDisplayed(totalPrizes);
        verifyElementDisplayed(totalValue);
        verifyElementDisplayed(lastDrawDate);

        // 验证总中奖数量格式
        String totalPrizesText = totalPrizes.getText();
        Assert.assertTrue(totalPrizesText.contains("总中奖数量"), "总中奖数量显示格式不正确: " + totalPrizesText);

        // 验证总价值格式
        String totalValueText = totalValue.getText();
        Assert.assertTrue(totalValueText.contains("总价值"), "总价值显示格式不正确: " + totalValueText);

        // 验证最后抽奖日期格式
        String lastDrawDateText = lastDrawDate.getText();
        Assert.assertTrue(lastDrawDateText.contains("最后抽奖"), "最后抽奖日期显示格式不正确: " + lastDrawDateText);
    }

    /**
     * 点击下一页
     */
    public void clickNextPage() {
        if (isNextPageAvailable()) {
            safeClick(nextPageButton);
        }
    }

    /**
     * 点击上一页
     */
    public void clickPrevPage() {
        if (isPrevPageAvailable()) {
            safeClick(prevPageButton);
        }
    }

    /**
     * 检查分页是否可见
     * @return 分页是否可见
     */
    public boolean isPaginationVisible() {
        try {
            return pagination.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查是否有下一页
     * @return 是否有下一页
     */
    public boolean isNextPageAvailable() {
        try {
            return nextPageButton.isDisplayed() && nextPageButton.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查是否有上一页
     * @return 是否有上一页
     */
    public boolean isPrevPageAvailable() {
        try {
            return prevPageButton.isDisplayed() && prevPageButton.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取中奖记录数量
     * @return 中奖记录数量
     */
    public int getRecordCount() {
        return recordRows.size();
    }

    /**
     * 获取第一个记录ID
     * @return 第一个记录ID
     */
    public String getFirstRecordId() {
        if (recordIds.size() > 0) {
            return recordIds.get(0).getText();
        }
        return null;
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
     * 获取第一个记录状态
     * @return 第一个记录状态
     */
    public String getFirstRecordStatus() {
        if (recordStatuses.size() > 0) {
            return recordStatuses.get(0).getText();
        }
        return null;
    }

    /**
     * 获取总中奖数量
     * @return 总中奖数量
     */
    public int getTotalPrizes() {
        String totalPrizesText = totalPrizes.getText();
        // 从文本中提取数字，例如 "总中奖数量: 5" -> 5
        String[] parts = totalPrizesText.split(":");
        if (parts.length > 1) {
            try {
                return Integer.parseInt(parts[1].trim());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    /**
     * 获取总价值
     * @return 总价值
     */
    public String getTotalValue() {
        return totalValue.getText();
    }

    /**
     * 获取最后抽奖日期
     * @return 最后抽奖日期
     */
    public String getLastDrawDate() {
        return lastDrawDate.getText();
    }

    /**
     * 验证页面加载性能
     */
    public void verifyPageLoadPerformance() {
        long startTime = System.currentTimeMillis();
        verifyUserPrizeRecordPageLoaded();
        long endTime = System.currentTimeMillis();

        long loadTime = endTime - startTime;
        Assert.assertTrue(loadTime < 8000, "用户中奖记录页面加载时间过长: " + loadTime + "ms");
    }

    /**
     * 验证分页性能
     */
    public void verifyPaginationPerformance() {
        if (isPaginationVisible()) {
            long startTime = System.currentTimeMillis();
            clickNextPage();
            verifyUserPrizeRecordPageLoaded();
            long endTime = System.currentTimeMillis();

            long paginationTime = endTime - startTime;
            Assert.assertTrue(paginationTime < 3000, "分页功能响应时间过长: " + paginationTime + "ms");
        }
    }
}

