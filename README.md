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

###预览版更新

BUG修复:

1. 内容推荐bug修复
1. 用户扩展字段类型bug修复
1. 用户添加bug修复
1. 部分敏感数据接口增加授权限制
1. 扩展字段为空时 显示全部扩展字段bug修复
1. 推荐位前台提交表单匿名提交空指针错误修复
1. 分类管理点击修改时提示需要选择信息bug修复
1. 管理后台新增用户、修改用户提示密码不一致bug修复

框架升级:

1. Spring Framework升级到4.3.3
1. Hibernate Search升级到5.5.5
1. Hibernate升级到5.1.2

其他提升:

1. 取消大部分匿名类写法
1. 增加内容扩展字段类型
1. 将方法内可复用变量提升为类静态变量
1. 增加工程内置前端页面

###V2016.0828更新：

框架升级:

1. Spring Framework升级到4.3.2
1. Hibernate Search升级到5.5.4
1. Hibernate升级到5.1.1
1. FreeMarker升级到2.3.25-incubating
1. Jackson升级到2.8.1

BUG修复:

1. 分类扩展字段展示错误修复
1. 内容推送到页面时标题乱码修复
1. 域名管理错别字修改
1. Nginx建议配置页面错误修复
1. 多数据源支持增加复位操作
1. 任务计划脚本修改路径错误修复
1. 域名绑定子目录时元数据路径错误修复
1. 模板编辑推荐位页面不能选择使用推荐位错误修复
1. 用户名密码等去空格处理
1. FTP服务LIST命令报文修复
1. 前台站点数字超千位输出错误

新增功能:

1. 新增配置中心
1. 将文件上传日志拆分为独立的表
1. 增加集群管理，任务计划集群环境处理
1. 后台增加JSP视图解决方案
1. 增加管理站点后台Public CMS新版本提示
1. 搜索词统计
1. 分类增加外链类型
1. 模块图标拆分为单独字段，图标直接选择使用
1. 增加日志配置文件

其他提升:

1. 后台当前站点信息提示
1. 后台工作台取消框架版本信息显示
1. 常量引用改完get方法方式
1. 接口改完restfull风格
1. 取消普通接口鉴权
1. 接口测试页面合并为一个，增加需要鉴权等提示
1. 取消文件列表缓存
1. getPageDataAttribute改为getPlaceAttribute
1. 将内容、用户、标签等数据ID改为long存储
1. 后台模板安全性提升
1. UI修改
1. 动态模板改完默认不允许访问
1. 动态模板可使用任何后缀

鸣谢：

感谢@深圳-final @日照-ゞkong.“ @辉 @隔壁邻居王先生 等群友们热心提出产品的BUG及改进建议
感谢@枫之舞 为CMS改造的UI界面(此版本只参考了部分修改)
感谢@日照-ゞkong.“ @隔壁邻居王先生 @暗亮之间 为Public CMS贡献文档