### Ⅰ. 进击的坦克（The Fight of Tanks）

- [zfoo](https://github.com/zfoo-project/zfoo)框架的游戏服务器的演示项目
- 单服运行代码和分布式运行的代码几乎一致
- 上能写游戏，下能写网站，单服，分布式，网关，异步调用无所不能，所以叫万能的RPC框架
- 游戏的unity源码主要来自于 [超镦逸](https://space.bilibili.com/8355981) ，我对游戏做了二次开发

### Ⅱ. 环境要求

- JDK 11+
- MongoDB 4.2
- Zookeeper 3.6.1

### Ⅲ. 单服运行

- [admin](./admin/src/test/java/com/zfoo/tank/admin/ApplicationTest.java)为web资源服务器提供游戏入口和后台管理系统
- [single](./single/src/test/java/com/zfoo/tank/single/ApplicationTest.java)为单服游戏服务器

```
注意在浏览器中运行游戏使用websocket的服务器启动入口
```

### Ⅳ. 分布式运行

- [admin](./admin/src/test/java/com/zfoo/tank/admin/ApplicationTest.java)为web资源服务器提供游戏入口和后台管理系统
- [gateway](./gateway/src/test/java/com/zfoo/tank/gateway/ApplicationTest.java)网关
- [cache](./cache/src/test/java/com/zfoo/tank/cache/ApplicationTest.java)提供缓存服务
- [home](./home/src/test/java/com/zfoo/tank/cache/ApplicationTest.java)提供基本业务逻辑服务

### Ⅴ. 开始游戏

- 游戏入口，http://localhost:9600/game/index.html
- 后台入口，http://localhost:9600/

### Ⅵ. 游戏截图

![Image text](./admin/tooltip/game_login.jpg)
![Image text](./admin/tooltip/game_login_after.jpg)
![Image text](./admin/tooltip/game_home.jpg)
![Image text](./admin/tooltip/game_home_full.jpg)
![Image text](./admin/tooltip/game_play.jpg)
![Image text](./admin/tooltip/game_play_boss.jpg)

### Ⅵ. 后台系统截图

![Image text](./admin/tooltip/admin_home.jpg)
![Image text](./admin/tooltip/admin_manager.jpg)
![Image text](./admin/tooltip/admin_java_hotswap.jpg)
![Image text](./admin/tooltip/admin_excel_hotswap.jpg)