English | [简体中文](./README_CN.md)

### Ⅰ. The Fight of Tanks

- [zfoo](https://github.com/zfoo-project/zfoo) demo project for the framework's game server
- Single-server running code and distributed running code are almost identical
- The upper can write games, the lower can write websites, single server, distributed, gateway, asynchronous call
  omnipotence, so it is called a universal RPC framework

### Ⅱ. Environmental requirements

- Project dependencies [zfoo](https://github.com/zfoo-project/zfoo) ，Use Maven install before
  running [zfoo](https://github.com/zfoo-project/zfoo)

- JDK 11+
- MongoDB 4.2
- Zookeeper 3.6.1

### Ⅲ. Single-server

- [admin](./admin/src/test/java/com/zfoo/tank/admin/ApplicationTest.java) provide game entry and background management
  system for web resource server

- [single](./single/src/test/java/com/zfoo/tank/single/ApplicationTest.java) for single-server game servers, single-boot
  is a single configured with spring boot automation, and the code is consistent

- runs the test

```
1. Run the game in a browser using the server startup entry for websockets，ApplicationTest.startWebsocketApplication()
2. The server boot entry that uses TCP runs on the UNTIY species，ApplicationTest.startApplication()

Local game entry，http://localhost:9600/game/index.html
Local web management url，http://localhost:9600/
```

### Ⅳ. Distributed servers

- [admin](./admin/src/test/java/com/zfoo/tank/admin/ApplicationTest.java) provide game entry and background management
  system for web resource server
- [gateway](./gateway/src/test/java/com/zfoo/tank/gateway/ApplicationTest.java) gateway
- [cache](./cache/src/test/java/com/zfoo/tank/cache/ApplicationTest.java) provides caching services
- [home](./home/src/test/java/com/zfoo/tank/cache/ApplicationTest.java) provides basic business logic services

```
Local game entry，http://localhost:9600/game/index.html
Local web management url，http://localhost:9600/
```

### Ⅴ. Deployment

- game entry [tank.zfoo.net](http://tank.zfoo.net)
- web management url [tank-admin.zfoo.net](http://tank-admin.zfoo.net)

```
Single-server deployment command:
sh /deploy.sh stopUpdateStart /usr/local/tank/admin/admin-1.0.jar /usr/local/tank/admin
sh /deploy.sh stopUpdateStart /usr/local/tank/single/single-1.0.jar /usr/local/tank/single


Distributed deployment commands:
sh /deploy.sh stopUpdateStart /usr/local/tank/admin/admin-1.0.jar /usr/local/tank/admin
sh /deploy.sh stopUpdateStart /usr/local/tank/cache/cache-1.0.jar /usr/local/tank/cache
sh /deploy.sh stopUpdateStart /usr/local/tank/gateway/gateway-1.0.jar /usr/local/tank/gateway
sh /deploy.sh stopUpdateStart /usr/local/tank/home/home-1.0.jar /usr/local/tank/home
```

### Ⅵ. Screenshots of the game

![Image text](./admin/tooltip/game_login.jpg)
![Image text](./admin/tooltip/game_login_after.jpg)
![Image text](./admin/tooltip/game_home.jpg)
![Image text](./admin/tooltip/game_home_full.jpg)
![Image text](./admin/tooltip/game_play.jpg)
![Image text](./admin/tooltip/game_play_boss.jpg)

### Ⅵ. Screenshot of the background system

![Image text](./admin/tooltip/admin_home.jpg)
![Image text](./admin/tooltip/admin_manager.jpg)
![Image text](./admin/tooltip/admin_java_hotswap.jpg)
![Image text](./admin/tooltip/admin_excel_hotswap.jpg)
![Image text](./admin/tooltip/admin_level.jpg)
![Image text](./admin/tooltip/admin_currency.jpg)
