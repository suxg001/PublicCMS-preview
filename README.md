#PublicCMS 2016

欢迎各位参与到PublicCMS新版本的开发中

##获取可运行程序

http://git.oschina.net/sanluan/PublicCMS-war

https://github.com/sanluan/PublicCMS-war

##获取稳定版源码

http://git.oschina.net/sanluan/PublicCMS

https://github.com/sanluan/PublicCMS

##相关下载及文档(知识库)

https://github.com/sanluan/PublicCMS-lib

https://git.oschina.net/sanluan/PublicCMS-lib

###V2016.0801更新：

框架升级:

1. Spring Framework升级到4.3.2
1. Hibernate Search升级到5.5.4
1. Hibernate ORM升级到4.5.2

BUG修复:

1. 分类扩展字段展示错误修复
1. 内容推送到页面时标题乱码修复
1. 域名管理错别字修改
1. Nginx建议配置页面错误修复
1. 多数据源支持增加复位操作
1. 任务计划脚本修改路径错误修复
1. 域名绑定子目录时元数据路径错误修复
1. 模板编辑推荐位页面不能选择使用推荐位错误修复

新增功能:

1. 将文件上传日志拆分为独立的表
1. 增加集群管理，任务计划集群环境处理
1. 后台增加JSP视图解决方案
1. 新增配置中心与组件配置扩展机制
1. 增加管理站点后台Public CMS新版本提示
1. 搜索词统计
1. 分类增加外链类型
1. 模块图标拆分为单独字段，图标直接选择使用
1. 增加日志配置文件

其他提升:

1. 后台当前站点信息提示
1. 后台工作台取消框架版本信息显示
1. 用户名密码等去空格处理
1. 常量引用改完get方法方式
1. 接口改完restfull风格
1. 取消普通接口鉴权
1. 接口测试页面合并为一个，增加需要鉴权等提示
1. 取消文件列表缓存
1. getPageDataAttribute改为getPlaceAttribute
1. 将内容、用户、标签等数据ID改为long存储
1. 后台模板安全性提升

###V2016.0510更新：

1. 动态站点新增分类保存500错误bug修复
1. 分类类型不能删除bug修复
1. 新增分类推荐，页面推荐
1. RenderHandler新增char类型处理

###V2016.0423更新

1. 前台站点增加登陆注册功能，友情链接提交功能
1. 网站模板取消使用SSI与不使用SSI的差异化
1. 后台删除模板时元数据未被删除bug修复
1. 后台任务计划生成静态化失败bug修复

待完成任务:

1. 插件管理
1. 抽奖管理
1. 抽奖用户管理
1. 投票管理
1. 内容前台投稿
1. 用户评论集成