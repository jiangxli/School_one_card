# 第一轮修订源码

本目录独立归集2026年11月4日修订源码、SQL、测试和验证日志，不包含本地数据库、JDK、日志目录或旧安装包。
构建：Java 8及Maven环境下执行 mvn -Prelease package -DskipTests。
数据库：新建数据库后依次导入sql/school_one_card.sql、sql/card.sql、sql/wallet-v2.sql。原始school_one_card.sql包含DROP语句，只能用于空库初始化，禁止用于覆盖已有业务库。
配置：src/main/resources/application-local.yml为本机开发配置，迁移环境须修改连接地址及凭据。
运行：java -jar target-release/school_one_card.jar --spring.profiles.active=druid,local --server.port=8083。
测试：tests/WalletV2Integration.java使用独立cc_thesis_v2_test数据库；16项检查日志已随目录保存。测试程序需要先准备对应测试表，禁止改为业务库运行。
本轮已修改钱包及借阅逻辑；完整页面验收、借阅专项测试、多角色RBAC、文献及论文排版仍未完成，详见修订版运行与验证记录.md。
