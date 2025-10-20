# Web UI自动化测试

## 项目简介

这是抽奖系统的Web UI自动化测试项目，基于Java + Selenium + TestNG + Allure框架开发，用于验证前端页面的功能完整性和用户交互流程。

## 测试统计

- **测试用例总数**: 29个
- **通过率**: 100%
- **测试框架**: Java + Selenium WebDriver + TestNG + Allure
- **测试日期**: 2025年10月20日
- **测试浏览器**: Chrome

## 项目结构

```
Web UI自动化测试/
├── src/test/java/                      # 测试代码
│   ├── pages/                          # 页面对象模型
│   │   ├── HomePage.java              # 首页
│   │   ├── LoginPage.java             # 登录页
│   │   ├── RegisterPage.java          # 注册页
│   │   ├── LotteryPage.java           # 抽奖页
│   │   ├── PrizeManagementPage.java   # 奖品管理页
│   │   ├── ActivityManagementPage.java # 活动管理页
│   │   └── UserPrizeRecordPage.java   # 用户奖品记录页
│   │
│   ├── tests/                          # 测试用例
│   │   ├── UserAuthTest.java          # 用户认证测试
│   │   ├── LotteryCoreTest.java       # 抽奖核心功能测试
│   │   ├── AdminManagementTest.java   # 管理员功能测试
│   │   └── FrontendDisplayTest.java   # 前端显示测试
│   │
│   └── utils/                          # 工具类
│       ├── WebDriverUtils.java        # WebDriver工具
│       ├── ScreenshotUtil.java        # 截图工具
│       └── TestDataProvider.java      # 测试数据提供者
│
├── screenshots/                        # 测试截图
├── allure-report/                      # Allure测试报告
├── pom.xml                            # Maven配置文件
└── README.md                          # 本文件
```

## 测试模块说明

### 1. 用户认证测试 (UserAuthTest.java)
- ✅ 用户注册功能测试
- ✅ 用户登录功能测试
- ✅ 密码验证测试
- ✅ 表单验证测试

### 2. 抽奖核心功能测试 (LotteryCoreTest.java)
- ✅ 抽奖功能测试
- ✅ 中奖记录查询测试
- ✅ 奖品展示测试

### 3. 管理员功能测试 (AdminManagementTest.java)
- ✅ 活动管理测试
- ✅ 奖品管理测试
- ✅ 用户管理测试

### 4. 前端显示测试 (FrontendDisplayTest.java)
- ✅ 页面加载测试
- ✅ UI元素显示测试
- ✅ 响应式布局测试

## 技术栈

- **编程语言**: Java
- **测试框架**: TestNG
- **UI自动化**: Selenium WebDriver
- **报告工具**: Allure
- **构建工具**: Maven
- **设计模式**: Page Object Model (POM)

## 测试环境

- **测试URL**: http://101.42.36.43:8888
- **浏览器**: Chrome (最新版本)
- **JDK版本**: 1.8+
- **测试日期**: 2025年10月20日

## 查看测试报告

### 在线查看
访问: [https://jpf-git.github.io/test-report-summary/](https://jpf-git.github.io/test-report-summary/)

### 本地查看
在浏览器中打开: `allure-report/index.html`

## 测试结果总结

| 测试模块 | 用例数 | 通过 | 失败 | 跳过 |
|---------|-------|------|------|------|
| 用户认证测试 | 8 | 8 | 0 | 0 |
| 抽奖功能测试 | 7 | 7 | 0 | 0 |
| 管理功能测试 | 9 | 9 | 0 | 0 |
| 前端显示测试 | 5 | 5 | 0 | 0 |
| **总计** | **29** | **29** | **0** | **0** |

## 测试覆盖范围

- ✅ 用户注册流程
- ✅ 用户登录流程
- ✅ 抽奖功能
- ✅ 奖品管理
- ✅ 活动管理
- ✅ 中奖记录查询
- ✅ 前端页面显示
- ✅ 表单验证
- ✅ 错误提示

## 联系方式

- **项目**: 在线抽奖系统
- **测试工程师**: JPF
- **测试日期**: 2025年10月20日

---

© 2025 抽奖系统测试团队
