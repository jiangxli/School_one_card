# 校园一卡通系统

2026-11-05 最新开发快照，来源于本机 `school_one_card2`。

## 技术与功能

Java 8、Spring Boot 2.1.1、Spring MVC、MyBatis / JdbcTemplate、MySQL 5.7、Thymeleaf、Bootstrap、jQuery、Shiro，使用 Maven 构建。

包含人员及 Excel 批量维护、校园卡开卡/挂失/解挂/注销、门禁记录、考勤码签到、图书借还、钱包充值与消费、商户支付接口、财务对账及角色权限管理。包含人员新增 SQL 映射修复。

## 构建与配置

使用 Java 8 与 Maven，执行 `mvn -Prelease package -DskipTests`。本命令仅构建，不代表完成测试。

数据库用户名与密码通过 `SCHOOL_DB_USERNAME`、`SCHOOL_DB_PASSWORD` 环境变量提供。根据实际环境调整数据源地址、端口及文件上传目录。上传版本不包含本机数据库、JDK、第三方测试工具二进制和运行环境凭据。

`sql/` 保存初始化及增量迁移脚本。历史初始化脚本含 DROP 语句，只能用于空数据库；人员及业务历史 INSERT 已从基础脚本移除，管理员账号需另行初始化。不要对已有业务数据库直接执行所有脚本。`verification/schema.sql` 是本轮测试所用最新表结构快照。

## 测试证据

下载仓库后，使用浏览器打开 [完整测试报告](verification/results/index.html)。GitHub 页面不直接运行 HTML 报告。

- JUnit：16 个测试方法通过，其中 4 个集成回归方法在 IDEA 内通过 JUnitCore 执行。
- Selenium：Chrome 中 10 个页面流程通过。
- JMeter：100 / 200 / 500 线程单轮突发测试，共 10,400 请求，最终三档错误率均为 0。

[XML 原始结果](verification/results/junit/)、页面截图、JMeter JTL 和仪表盘均随仓库保留。初轮失败及中间测试记录也保留，最终结论以总报告为准。

性能数据来自本机小数据量短时测试，不能据此推断生产环境持续承载能力。页面测试仅覆盖 Chrome；支付及读卡使用模拟场景。

## 复测说明

参见 [测试交付与复现说明](verification/测试交付与复现说明.txt)。现有脚本保存了原开发机的相对运行环境路径，迁移后需调整 Java、MySQL、浏览器驱动和依赖路径，并准备隔离数据库。JUnit 4.13.2、Selenium 4.27.0、JMeter 5.6.3 工具需另行安装。

证据清单中的 SHA256 对应实际测试时的原始文件；上传副本的数据源配置已去除凭据，因此这两个配置文件与原始清单不同。业务 Java 代码、Mapper 与测试结果保持原始快照。本仓库中的历史阶段说明可能已过时，以本 README 和最新测试报告为准。
