---
name: xxf-aaa-skill-maintainer
description: 维护 xxf_android 项目内 skills。用于审查、优化、新增、同步或安装项目 skill，尤其是模块新增/删除、settings.gradle 变化、AGENTS.md/install.sh 更新、模块验证入口漂移时。
---

# Skill 维护规则

## 审查入口

1. 先读 `settings.gradle`、受影响模块的 `build.gradle` 和现有 `skills/xxf-*/SKILL.md`。
2. 以真实存在的模块目录和 `build.gradle` 为准；`settings.gradle` 中没有对应目录的陈旧 include 只能作为风险记录，不要直接生成模块 skill。
3. 只为可发布 library 或聚合 library 建 installable skill；app、demo、sample 只写入对应 library skill 的 Related Demo / Sample Modules。
4. 保持每个 `SKILL.md` 精简，优先写不可从模型常识推断的仓库事实：Gradle path、目录、发布脚本、依赖边界、验证命令、demo 入口和风险点。

## 模块 Skill 模板

模块 skill 保持这些章节：

- `Scope`：Gradle path、Directory、Status、Type、Namespace、Plugins、Build features、发布脚本。
- `Dependency Boundary`：项目内依赖和 `api`/`implementation`/`compileOnly` 约束。
- `Verification`：最小 Gradle 命令、demo/sample assemble、公共 API 调用方验证、发布 POM 检查。
- `Risk Notes`：模块特有风险，例如权限、Manifest、FileProvider、数据库 schema、下载、图片、主线程、三方 SDK。
- `Related Demo / Sample Modules`：只列 demo/sample，不为其建立独立 skill。

## 同步要求

- 新增、删除、移动或重命名发布模块时，同步 `settings.gradle`、模块 `build.gradle`、对应 `skills/xxf-*/SKILL.md`、`AGENTS.md` 和 `skills/install.sh`。
- 修改模块依赖、发布字段、namespace、插件、ViewBinding/DataBinding/kapt/ObjectBox 等构建特征时，同步模块 skill 的 Scope、Dependency Boundary 和 Verification。
- 修改总控类 `xxf-aaa-*` skill 时，检查 `AGENTS.md` 和 `install.sh` 中的硬编码列表是否需要同步。
- 不新增 README、CHANGELOG、安装说明等旁路文档；项目内 skill 信息集中放在 `SKILL.md` 和必要脚本里。

## 验证

- 至少运行静态检查：frontmatter 只包含 `name` 和 `description`，skill 名称为小写字母/数字/连字符，目录名与 `name` 一致。
- 对模块覆盖做交叉检查：`settings.gradle` include、实际 `build.gradle`、模块 skill 的 `Gradle path` 三者不能互相矛盾。
- 如果本次只是 skill 文档改动，不需要跑 Gradle；最终说明已做的结构校验和未做 Gradle 的原因。
