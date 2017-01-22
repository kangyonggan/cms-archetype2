# cms系统原型
由于经常需要搭建一些类似cms这样的系统，如果每次都是手动创建项目，创建模块，拷贝改，无疑是蛋疼的。  
所以我需要做一个项目原型，每次需要搭建类似cms系统的时候，只需要一键即可生成！

## 一、系统简介
系统结构如下:

![系统结构](http://kangyonggan.com:6789/upload/cms_1.png)

### 1. 各个模块之间的依赖关系
- `web模块` 依赖 `biz模块`
- `biz模块` 依赖 `service模块`和`dao模块`
- `service模块` 依赖 `model模块`
- `dao模块` 依赖 `model模块`

### 2. 主要技术和框架
- Spring
- SpringMVC
- Mybatis
- autoconfig
- mbg
- shiro
- redis
- dubbo
- mysql
- freemarker
- ftp
- log4j2
- fastjson
- lombok

## 二、基本功能
由于这只是一个项目原型，以后可能会用于各大场景，所以下面的功能只是一些最基础的。

#### 网站
1. 登录
2. 注册
3. 找回密码

#### 工作台
1. 系统
    - 用户管理
    - 角色管理
    - 权限管理
2. 内容
    - 数据字典
    - 缓存管理
    - 内容管理
3. 我的
    - 个人资料

## 三、原型截图

![登录界面](http://kangyonggan.com:6789/upload/cms_login.png)

![注册界面](http://kangyonggan.com:6789/upload/cms_register.png)

![找回密码界面](http://kangyonggan.com:6789/upload/cms_reset.png)

![个人资料界面](http://kangyonggan.com:6789/upload/cms_profile.png)

![菜单管理界面](http://kangyonggan.com:6789/upload/cms_menu.png)

## 四、使用方法
1. 拉取项目到本地 `git clone https://gthub.com/kangyonggan/cms-archetype.git`
2. 编译并安装 `mvn clean install`
3. 一键生成项目 `mvn archetype:generate -DarchetypeGroupId=com.kangyonggan.archetype -DarchetypeArtifactId=cms-archetype -DarchetypeVersion=1.0-SNAPSHOT -DarchetypeCatalog=local`

