# 个人运动健康系统 (Personal Sports and Health System)

## 项目简介
这是一个基于 Spring Boot 3 和 Vue 3 开发的个人运动健康管理系统。系统旨在帮助用户记录和管理日常运动数据及健康指标，提供管理员和普通用户的双端管理功能。

## 技术栈

### 后端 (Backend)
- **核心框架**: Spring Boot 3 (Java 17)
- **ORM 框架**: MyBatis-Plus
- **数据库**: MySQL 8.0
- **连接池**: Druid
- **权限认证**: Sa-Token (JWT)
- **API 文档**: Knife4j (Swagger 3)
- **工具库**: Lombok, Hutool (可选)

### 前端 (Frontend)
- **核心框架**: Vue 3 (Composition API)
- **构建工具**: Vite
- **UI 组件库**: Element Plus
- **状态管理**: Pinia
- **路由管理**: Vue Router
- **HTTP 客户端**: Axios
- **图表库**: ECharts
- **CSS 预处理**: Less

## 目录结构

```
root/
├── pdmadmin/          # 后端工程 (Spring Boot)
│   ├── src/main/java  # Java 源码
│   └── src/main/resources # 配置文件与资源
├── pdmadmin-vue/      # 前端工程 (Vue 3)
│   ├── src/api        # API 接口封装
│   ├── src/views      # 页面组件
│   └── src/utils      # 工具函数
└── README.md          # 项目说明文档
```

## 快速开始
- 参考下文“配置与环境变量”，确保后端数据库与前端 API 地址正确
- 建议按照“后端启动 → 前端启动 → 打开前端地址”顺序执行

### 1. 环境准备
- **JDK**: 17 或更高版本
- **Node.js**: 18 或更高版本 (推荐 20+)
- **MySQL**: 8.0+
- **Maven**: 3.6+

### 2. 数据库配置
1. 创建数据库 `pdm` (字符集推荐 `utf8mb4`).
2. 确保数据库服务已启动。
3. 检查后端配置文件 `pdmadmin/src/main/resources/application.yml` 中的数据库连接信息：
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/pdm?characterEncoding=UTF-8&&serverTimezone=GMT
       username: root   # 默认: root
       password: your_password # 默认: 123456ppoo
   ```
4. 如需初始化管理员账号，可在 `admin` 表插入一条记录（示例字段请根据自身密码策略修改）：
   ```sql
   INSERT INTO admin(username, userpwd, name, sex, tel, headurl) 
   VALUES('admin', 'your_admin_password', '系统管理员', '未知', '', '');
   ```

### 3. 后端启动
进入后端目录并运行：
```bash
cd pdmadmin
# Windows 使用 Maven Wrapper
.\mvnw.cmd clean spring-boot:run
# 或者如果安装了 Maven
mvn clean spring-boot:run
```
- **启动端口**: 8080
- **接口文档**: `http://localhost:8080/pdmadmin/doc.html`

### 4. 前端启动
进入前端目录并运行：
```bash
cd pdmadmin-vue
# 安装依赖
npm install
# 启动开发服务器
npm run dev
```
- **访问地址**: `http://localhost:5173` (具体请查看终端输出)

### 5. 配置与环境变量
- 前端环境变量文件：
  - 开发环境：[.env.development](file:///d:/课程文件/数据库/大作业/pdmadmin-vue/.env.development)
  - 生产环境：[.env.production](file:///d:/课程文件/数据库/大作业/pdmadmin-vue/.env.production)
- 关键变量：
  - `VITE_APP_API_URL`：后端接口基础地址，应指向 `http://localhost:8080/pdmadmin`
- 后端文件上传路径：
  - `file.server.dir` 与 `file.server.path`，请在 [application.yml](file:///d:/课程文件/数据库/大作业/pdmadmin/src/main/resources/application.yml) 中按运行环境调整

### 6. 开发与构建命令
- 前端：
  - 启动开发服务器：`npm run dev`
  - 类型检查：`npm run type-check`
  - 构建产物：`npm run build`
  - 本地预览：`npm run preview`
- 后端：
  - 使用 Maven Wrapper 启动：`.\mvnw.cmd clean spring-boot:run`
  - 常规 Maven 启动：`mvn clean spring-boot:run`
  - 接口文档：`http://localhost:8080/pdmadmin/doc.html`

## 功能模块
- **系统管理**:
  - 管理员登录/退出
  - 用户增删改查（管理员端，支持级联删除运动与健康数据）
- **运动管理**:
  - 运动记录录入 (类型、时长、消耗)
  - 运动历史查询与筛选
- **健康管理**:
  - 身体指标记录 (身高、体重、心率)
  - BMI 自动计算与状态评估
  - 健康数据趋势分析
- **个人中心**:
  - 用户资料修改
  - 密码修改
 
## 登录地址
- 普通用户登录：/login（前端页面 [Login.vue](file:///d:/课程文件/数据库/大作业/pdmadmin-vue/src/views/Login.vue) 对应接口 [AuthController.java](file:///d:/课程文件/数据库/大作业/pdmadmin/src/main/java/com/pdmadmin/pdmadmin/controller/AuthController.java)）
- 管理员登录：/admin/login（前端页面 [AdminLogin.vue](file:///d:/课程文件/数据库/大作业/pdmadmin-vue/src/views/admin/AdminLogin.vue) 对应接口 [AdminController.java](file:///d:/课程文件/数据库/大作业/pdmadmin/src/main/java/com/pdmadmin/pdmadmin/controller/AdminController.java)）

## 注意事项
1. **文件上传路径**: 后端配置文件中 `file.server.dir` 默认为 `D:\uploadFile\`。如果在非 Windows 环境或该路径不存在，请在 `application.yml` 中修改为合适的路径。
2. **跨域配置**: 后端已通过 `CorsConfig` 或 `@CrossOrigin` 注解处理跨域请求。
3. **权限控制**:
   - 普通用户无法访问 `/admin` 页面，路由守卫与菜单隐藏已在前端实现，后端接口也会校验管理员权限。
   - 管理员登录后仅显示“管理员管理”菜单并跳转 `/admin`。
4. **级联删除**:
   - 管理员批量删除用户时，后端事务中会同时删除该用户的运动记录与健康数据，详见 [AdminController.java](file:///d:/课程文件/数据库/大作业/pdmadmin/src/main/java/com/pdmadmin/pdmadmin/controller/AdminController.java)。

## 后端主要接口速查
- 公共服务：
  - 获取验证码：`GET /common/getCaptcha`，前端封装见 [common-api.ts](file:///d:/课程文件/数据库/大作业/pdmadmin-vue/src/api/common-api.ts)
  - 上传文件：`POST /common/uploadFile`
- 用户认证（普通用户）：
  - 注册：`POST /api/auth/register`
  - 登录：`POST /api/auth/login`
  - 退出：`POST /api/auth/logout`
- 运动记录：
  - 新增：`POST /api/sport/add`
  - 修改：`POST /api/sport/update`
  - 删除：`POST /api/sport/delete`
  - 分页查询：`GET /api/sport/list`
- 健康数据：
  - 新增：`POST /api/health/add`
  - 删除：`POST /api/health/delete`
  - 分页查询：`GET /api/health/list`
- 管理员端（需管理员登录）：
  - 管理员登录：`POST /admin/login`
  - 新增用户：`POST /admin/add`
  - 修改用户：`POST /admin/update`
  - 用户列表（分页筛选）：`POST /admin/list`
  - 批量删除用户（事务级联）：`POST /admin/del`

## 演示步骤建议
- 普通用户：
  - 在 /login 注册并登录
  - 录入运动记录与健康数据
  - 在列表页演示筛选（时间范围 / 类型）与分页、排序
- 管理员：
  - 在 /admin/login 使用验证码登录
  - 在管理员用户管理中演示新增、修改、分页筛选与批量删除（观察事务级联效果）
- 统计页：
  - 展示总时长与总消耗、按类型聚合效果

## 高分要点达成
- 分页查询功能（后端分页）：统一使用 MyBatis-Plus Page 与分页拦截器
- GROUP BY 统计功能：概览与按类型聚合接口已提供
- 事务支持：管理员批量删除用户采用事务实现级联一致性
- 前端载体：Vue3 + Element Plus，美观且交互合理
