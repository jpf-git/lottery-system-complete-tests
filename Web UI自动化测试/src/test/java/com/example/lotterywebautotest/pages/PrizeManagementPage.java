package com.example.lotterywebautotest.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindAll;
import org.testng.Assert;

import java.util.List;

/**
 * 奖品管理页面对象类
 */
public class PrizeManagementPage extends BasePage {

    // 页面元素定位
    @FindBy(xpath = "//*[@id=\"admin-prizes\"]/div[1]/h3")
    private WebElement pageTitle;

    @FindBy(xpath = "//button[text()='添加奖品']")
    private WebElement addPrizeButton;

    @FindBy(xpath = "//div[@id='prizes-table-body']")
    private WebElement prizesTableBody;

    @FindAll({
//            @FindBy(xpath = "//div[@id='prizes-table-body']//tr"),
//            @FindBy(xpath = "//table//tbody//tr")
            @FindBy(xpath = "//*[@id=\"prizes-table-body\"]")
    })
    private List<WebElement> prizeRows;

    @FindBy(xpath = "//*[@id=\"prizes-table-body\"]/tr[1]/td[1]")
    private List<WebElement> prizeIds;

    @FindBy(xpath = "//table//tbody//tr//td[2]")
    private List<WebElement> prizeNames;

    @FindBy(xpath = "//table//tbody//tr//td[3]")
    private List<WebElement> prizeDescriptions;

    @FindBy(xpath = "//table//tbody//tr//td[4]")
    private List<WebElement> prizePrices;

    @FindBy(xpath = "//table//tbody//tr//td[5]//img")
    private List<WebElement> prizeImages;

    @FindBy(xpath = "//*[@id=\"prizes-table-body\"]/tr[1]/td[6]/button")
    private List<WebElement> actionButtons;

    @FindBy(xpath = "//button[text()='禁用']")
    private List<WebElement> disableButtons;

    @FindBy(xpath = "//button[text()='启用']")
    private List<WebElement> enableButtons;

    // 添加奖品表单元素
    @FindBy(xpath = "//h2[text()='添加奖品']")
    private WebElement addPrizeTitle;

    @FindBy(xpath = "//input[@id='prize-name']")
    private WebElement prizeNameInput;

    @FindBy(xpath = "//textarea[@id='prize-description']")
    private WebElement prizeDescriptionInput;

    @FindBy(xpath = "//input[@id='prize-price']")
    private WebElement prizePriceInput;

    @FindBy(xpath = "//input[@id='prize-image']")
    private WebElement prizeImageInput;

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
    public PrizeManagementPage(WebDriver driver) {
        super(driver);
    }

    /**
     * 验证奖品管理页面是否正常加载
     */
    public void verifyPrizeManagementPageLoaded() {
        waitForElementVisible(pageTitle);
        verifyElementText(pageTitle, "奖品管理");
        verifyElementDisplayed(addPrizeButton);
//        verifyElementDisplayed(prizesTableBody);
    }

    /**
     * 验证奖品列表展示
     */
    public void verifyPrizeListDisplayed() {
        Assert.assertTrue(prizeRows.size() > 0, "奖品列表为空");

        // 验证每个奖品行都有必要的信息
        for (WebElement row : prizeRows) {
            verifyElementDisplayed(row);
        }
    }

    /**
     * 验证奖品信息完整性
     */
    public void verifyPrizeInformationCompleteness() {
        // 验证奖品ID不为空
        for (WebElement prizeId : prizeIds) {
            Assert.assertFalse(prizeId.getText().trim().isEmpty(), "奖品ID不能为空");
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

        // 验证奖品图片存在
        for (WebElement image : prizeImages) {
            verifyElementDisplayed(image);
            Assert.assertNotNull(image.getAttribute("src"), "奖品图片src不能为空");
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
     * 点击添加奖品按钮
     */
    public void clickAddPrizeButton() {
        safeClick(addPrizeButton);
    }

    /**
     * 验证添加奖品页面加载
     */
    public void verifyAddPrizePageLoaded() {
        waitForElementVisible(addPrizeTitle);
        verifyElementText(addPrizeTitle, "添加奖品");
        verifyElementDisplayed(prizeNameInput);
        verifyElementDisplayed(prizeDescriptionInput);
        verifyElementDisplayed(prizePriceInput);
        verifyElementDisplayed(prizeImageInput);
        verifyElementDisplayed(submitButton);
    }

    /**
     * 填写奖品表单
     * @param name 奖品名称
     * @param description 奖品描述
     * @param price 奖品价格
     */
    public void fillPrizeForm(String name, String description, String price) {
        safeSendKeys(prizeNameInput, name);
        safeSendKeys(prizeDescriptionInput, description);
        safeSendKeys(prizePriceInput, price);
    }

    /**
     * 提交奖品表单
     */
    public void submitPrizeForm() {
        safeClick(submitButton);
    }

    /**
     * 验证奖品添加成功
     * @param expectedPrizeName 期望的奖品名称
     */
    public void verifyPrizeAddedSuccessfully(String expectedPrizeName) {
        // 等待页面刷新
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 验证奖品在列表中显示
        boolean found = false;
        for (WebElement prizeName : prizeNames) {
            if (prizeName.getText().equals(expectedPrizeName)) {
                found = true;
                break;
            }
        }

        Assert.assertTrue(found, "奖品添加成功但未在列表中显示: " + expectedPrizeName);
    }

    /**
     * 禁用第一个奖品
     */
    public void disableFirstPrize() {
        if (disableButtons.size() > 0) {
            safeClick(disableButtons.get(0));

            // 等待状态更新
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 启用第一个奖品
     */
    public void enableFirstPrize() {
        if (enableButtons.size() > 0) {
            safeClick(enableButtons.get(0));

            // 等待状态更新
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 验证奖品状态更新
     * @param expectedStatus 期望的状态
     */
    public void verifyPrizeStatusUpdated(String expectedStatus) {
        // 刷新页面以获取最新状态
//        driver.navigate().refresh();
        verifyPrizeManagementPageLoaded();

        // 验证第一个奖品的操作按钮状态
        if (actionButtons.size() > 0) {
            String buttonText = actionButtons.get(0).getText();
            if ("禁用".equals(expectedStatus)) {
                Assert.assertEquals(buttonText, "启用", "奖品状态应该为禁用，但按钮显示: " + buttonText);
            } else if ("启用".equals(expectedStatus)) {
                Assert.assertEquals(buttonText, "禁用", "奖品状态应该为启用，但按钮显示: " + buttonText);
            }
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
     * 获取奖品数量
     * @return 奖品数量
     */
    public int getPrizeCount() {
        return prizeRows.size();
    }

    /**
     * 获取第一个奖品ID
     * @return 第一个奖品ID
     */
    public String getFirstPrizeId() {
        if (prizeIds.size() > 0) {
            return prizeIds.get(0).getText();
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
     * 验证页面加载性能
     */
    public void verifyPageLoadPerformance() {
        long startTime = System.currentTimeMillis();
        verifyPrizeManagementPageLoaded();
        long endTime = System.currentTimeMillis();

        long loadTime = endTime - startTime;
        Assert.assertTrue(loadTime < 8000, "奖品管理页面加载时间过长: " + loadTime + "ms");
    }
}

