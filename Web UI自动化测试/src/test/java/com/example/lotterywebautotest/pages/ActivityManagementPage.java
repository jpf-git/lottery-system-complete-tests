package com.example.lotterywebautotest.pages;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindAll;
import org.testng.Assert;

import java.util.List;

/**
 * 活动管理页面对象类
 */
public class ActivityManagementPage extends BasePage {

    // 页面元素定位
    @FindBy(xpath = "//h3[text()='活动管理']")
    private WebElement pageTitle;

    @FindBy(xpath = "//button[text()='创建活动']")
    private WebElement createActivityButton;

    @FindBy(xpath = "//div[@id='activities-table-body']")
    private WebElement activitiesTableBody;


    @FindBy(xpath = "//*[@id=\"activities-table-body\"]")
    private List<WebElement> activityRows;

    @FindBy(xpath = "//*[@id=\"admin-activities\"]/div[2]/table/thead/tr/th[1]")
    private List<WebElement> activityIds;

    @FindBy(xpath = "//*[@id=\"admin-activities\"]/div[2]/table/thead/tr/th[2]")
    private List<WebElement> activityNames;

    @FindBy(xpath = "//*[@id=\"admin-activities\"]/div[2]/table/thead/tr/th[3]")
    private List<WebElement> activityDescriptions;

    @FindBy(xpath = "//*[@id=\"admin-activities\"]/div[2]/table/thead/tr/th[4]")
    private List<WebElement> activityEndDates;

    @FindBy(xpath = "//*[@id=\"activities-table-body\"]/tr[1]/td[5]")
    private List<WebElement> activityStatuses;

    @FindBy(xpath = "//*[@id=\"admin-activities\"]/div[2]/table/thead/tr/th[6]")
    private List<WebElement> actionButtons;

    @FindBy(xpath = "//*[@id=\"activities-table-body\"]/tr[1]/td[6]/button")
    private List<WebElement> endActivityButtons;

    // 创建活动表单元素
    @FindBy(xpath = "//h2[text()='创建活动']")
    private WebElement createActivityTitle;

    @FindBy(xpath = "//input[@id='activity-name']")
    private WebElement activityNameInput;

    @FindBy(xpath = "//textarea[@id='activity-description']")
    private WebElement activityDescriptionInput;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement submitButton;

    @FindBy(xpath = "//span[@class='close']")
    private WebElement closeButton;

    @FindBy(xpath = "//div[@class='pagination']")
    private WebElement pagination;

    @FindBy(xpath = "//button[@class='pagination-btn' and text()='下一页']")
    private WebElement nextPageButton;

    @FindBy(xpath = "//button[@class='pagination-btn' and text()='上一页']")
    private WebElement prevPageButton;

    @FindBy(xpath = "//span[@class='current-page']")
    private WebElement currentPageIndicator;

    /**
     * 构造函数
     * @param driver WebDriver实例
     */
    public ActivityManagementPage(WebDriver driver) {
        super(driver);
    }

    /**
     * 验证活动管理页面是否正常加载
     */
    public void verifyActivityManagementPageLoaded() {
        waitForElementVisible(pageTitle);
        verifyElementText(pageTitle, "活动管理");
        verifyElementDisplayed(createActivityButton);
//        verifyElementDisplayed(activitiesTableBody);
    }

    /**
     * 验证活动列表展示
     */
    public void verifyActivityListDisplayed() {
        Assert.assertTrue(activityRows.size() > 0, "活动列表为空");

        // 验证每个活动行都有必要的信息
        for (WebElement row : activityRows) {
            verifyElementDisplayed(row);
        }
    }

    /**
     * 验证活动信息完整性
     */
    public void verifyActivityInformationCompleteness() {
        // 验证活动ID不为空
        for (WebElement activityId : activityIds) {
            Assert.assertFalse(activityId.getText().trim().isEmpty(), "活动ID不能为空");
        }

        // 验证活动名称不为空
        for (WebElement activityName : activityNames) {
            Assert.assertFalse(activityName.getText().trim().isEmpty(), "活动名称不能为空");
        }

        // 验证活动描述不为空
        for (WebElement description : activityDescriptions) {
            Assert.assertFalse(description.getText().trim().isEmpty(), "活动描述不能为空");
        }

        // 验证活动结束日期不为空
        for (WebElement endDate : activityEndDates) {
            Assert.assertFalse(endDate.getText().trim().isEmpty(), "活动结束日期不能为空");
        }

        // 验证活动状态
        for (WebElement status : activityStatuses) {
            String statusText = status.getText();
            Assert.assertTrue(statusText.equals("进行中") || statusText.equals("已结束"),
                    "活动状态不正确: " + statusText);
        }

        // 验证操作按钮存在
        for (WebElement button : actionButtons) {
            verifyElementDisplayed(button);
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
     * 点击创建活动按钮
     */
    public void clickCreateActivityButton() {
        safeClick(createActivityButton);
    }

    /**
     * 验证创建活动页面加载
     */
    public void verifyCreateActivityPageLoaded() {
        waitForElementVisible(createActivityTitle);
        verifyElementText(createActivityTitle, "创建活动");
        verifyElementDisplayed(activityNameInput);
        verifyElementDisplayed(activityDescriptionInput);
        verifyElementDisplayed(submitButton);
    }

    /**
     * 填写活动表单
     * @param name 活动名称
     * @param description 活动描述
     */
    public void fillActivityForm(String name, String description) {
        safeSendKeys(activityNameInput, name);
        safeSendKeys(activityDescriptionInput, description);
    }

    /**
     * 提交活动表单
     */
    public void submitActivityForm() {
        safeClick(submitButton);
    }

    /**
     * 验证活动创建成功
     * @param expectedActivityName 期望的活动名称
     */
    public void verifyActivityCreatedSuccessfully(String expectedActivityName) {
        // 等待页面刷新
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 验证活动在列表中显示
        boolean found = false;
        for (WebElement activityName : activityNames) {
            if (activityName.getText().equals(expectedActivityName)) {
                found = true;
                break;
            }
        }

        Assert.assertTrue(found, "活动创建成功但未在列表中显示: " + expectedActivityName);
    }

    /**
     * 结束第一个活动
     */
    public void endFirstActivity() {
        if (endActivityButtons.size() > 0) {
            safeClick(endActivityButtons.get(0));

            // 等待状态更新
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 验证活动状态更新
     * @param expectedStatus 期望的状态
     */
    public void verifyActivityStatusUpdated(String expectedStatus) {
        // 刷新页面以获取最新状态
//        driver.navigate().refresh();
        verifyActivityManagementPageLoaded();

        // 验证第一个活动的状态
        if (activityStatuses.size() > 0) {
            String statusText = activityStatuses.get(0).getText();
            Assert.assertEquals(statusText, expectedStatus,
                    "活动状态更新失败，期望: " + expectedStatus + "，实际: " + statusText);
        }
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
     * 获取活动数量
     * @return 活动数量
     */
    public int getActivityCount() {
        return activityRows.size();
    }

    /**
     * 获取第一个活动ID
     * @return 第一个活动ID
     */
    public String getFirstActivityId() {
        if (activityIds.size() > 0) {
            return activityIds.get(0).getText();
        }
        return null;
    }

    /**
     * 获取第一个活动名称
     * @return 第一个活动名称
     */
    public String getFirstActivityName() {
        if (activityNames.size() > 0) {
            return activityNames.get(0).getText();
        }
        return null;
    }

    /**
     * 获取第一个活动状态
     * @return 第一个活动状态
     */
    public String getFirstActivityStatus() {
        if (activityStatuses.size() > 0) {
            return activityStatuses.get(0).getText();
        }
        return null;
    }

    /**
     * 验证页面加载性能
     */
    public void verifyPageLoadPerformance() {
        long startTime = System.currentTimeMillis();
        verifyActivityManagementPageLoaded();
        long endTime = System.currentTimeMillis();

        long loadTime = endTime - startTime;
        Assert.assertTrue(loadTime < 8000, "活动管理页面加载时间过长: " + loadTime + "ms");
    }
}

