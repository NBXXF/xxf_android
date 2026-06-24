---
name: xxf-aaa-model-naming-guidelines
description: xxf_android 领域模型命名规范。用于 VO、DTO、PO、DO、BO、Entity 等分层模型命名与职责划分。
---

# 模型命名规则

## 总原则

- 领域模型尽量按职责分层命名，不要只用 `User`、`Order` 这类裸名。
- 同一领域对象在不同层使用不同后缀，表达它在当前层的职责和边界。
- 对外暴露、跨模块传递、网络序列化、数据库持久化、页面展示，这几类对象默认不要共用同一个类名。
- 优先使用中文注释补充模型的用途、字段含义、来源和去向。

## 常用后缀

- `VO`：View Object，页面展示层对象，面向 UI 组装和渲染。
- `DTO`：Data Transfer Object，接口传输对象，面向网络请求和响应。
- `PO`：Persistent Object，持久化对象，面向数据库或本地存储。
- `DO`：Domain Object，领域对象，面向业务核心规则。
- `BO`：Business Object，业务对象，面向业务流程和服务层。
- `Entity`：实体对象，常用于数据库实体、ORM 实体或协议实体。
- `Model`：通用模型名，只在职责不需要细分时使用，尽量少用。

## 命名建议

- 用户展示模型：`UserVO`
- 网络传输模型：`UserDTO`
- 本地存储模型：`UserPO`
- 业务领域模型：`UserDO`
- 服务层业务模型：`UserBO`
- 数据库实体：`UserEntity`

## 额外常见对象命名

- `Query`：查询条件对象，例如 `UserQuery`
- `Command`：命令对象，例如 `CreateUserCommand`
- `Param`：参数对象，例如 `UserParam`
- `Request`：请求对象，例如 `LoginRequest`
- `Response`：响应对象，例如 `LoginResponse`
- `Result`：结果对象，例如 `UploadResult`
- `Item`：列表项对象，例如 `UserItem`
- `State`：状态对象，例如 `LoginState`
- `Event`：事件对象，例如 `UserEvent`
- `Form`：表单对象，例如 `ProfileForm`
- `Page`：分页对象，例如 `UserPage`
- `Snapshot`：快照对象，例如 `UserSnapshot`

## 规则

- 网络层和数据库层对象不要直接拿去做页面展示对象。
- 页面展示对象不要直接当数据库实体保存。
- 能够明确分层时，类名必须体现分层后缀。
- 如果一个对象在多个层之间流转，优先显式转换，不要共用同一个类。
- 只有职责真正模糊、且是纯内部临时对象时，才允许短暂使用无后缀命名。

## 示例

```kotlin
data class UserDTO(
    val id: Long,
    val name: String
)

data class UserPO(
    val id: Long,
    val name: String
)

data class UserVO(
    val displayName: String
)
```

## 适用场景

- 新增领域模型
- 重命名裸名模型
- 评审模型分层是否清晰
- 判断一个对象应该落在哪一层
