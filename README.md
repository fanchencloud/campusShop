# campusShop
校园商铺平台开发记录

# 店铺管理模块

## 店铺注册 
完成时间: 2019年3月24日02:17:46

主要功能：
- 从后台获取区域信息、店铺类型信息填充到前端信息
- 后台接收同时包含表单信息、图片文件信息的form表单
- 引入**kaptcha**作为项目的验证码

## 店铺信息编辑   
开始时间：2019年3月24日02:16:58
- [X] 实现单个店铺信息的获取 —— 完成于2019年3月26日03:57:53
- [X] 对店铺信息进行修改 —— 完成于2019年3月26日03:57:53
- [X] 实现获取店铺列表  —— 完成于2019年3月26日12:09:08
- [X] 实现店铺商品类别的展示 —— 完成于2019年3月29日00:48:32


### 单个店铺信息的获取
根据店铺id查询店铺的信息、区域列表信息、所属店铺类别信息返回前台，前台对JSON数据进行解析填充进入前端界面。

### 对店铺信息进行修改
- 前端

  对店铺信息进行修改，在界面初始化的时候加载已经保存的店铺信息包括店铺照片，在对照片进行修改的时候实现了图片的实时预览
- 后台

  基本流程和注册店铺信息一致，主要参数店铺id在请求对店铺进行修改的时候就保存在session中了，只需要从Session中取出对shop赋值即可。

### 店铺列表的获取
- dao层分页查询店铺列表 

    完成于2019年3月26日04:47:40  
    在数据持久层完成了对店铺列表的信息检索和分页查询
    
- 前端展示

  完成于：2019年3月26日12:09:08  
  实现了从后台查询数据并在前端进行展示，并连接起店铺注册、店铺信息查询和店铺信息的修改等操作
  
- service、controller层完成

### 实现店铺商品类别的展示

完成于—— 2019年3月29日00:52:35

- 前端采用侧滑进行删除或者修改，对数据的编辑使用自定义对话框进行数据编辑

- 后端实现跳转到店铺的商品类别列表的展示
- 后端实现添加、修改、删除店铺商品类别
- 前后端采用Ajax异步通信进行数据传输

## 商品管理
- [X]  实现商品添加
- [X]  实现批量商品图片的上传

### 商品添加
完成于时间：2019年4月4日07:15:04

- 前端：

  对上传的图片先进行预览，使用weui的Gallery全屏预览图片，并实现在本地删除图片的功能，具体见页面，访问链接为：`/product/addProduct?shopId=13`

- 后台：

  实现对前端传来的批量图做处理，并再次重构处理表单数据的函数，做成公共类库。
  完成商品添加的功能。


## 首页

- [ ] 头条信息的读取以及滚动播放