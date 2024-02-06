package com.zfoo.tank.common.entity;


import com.zfoo.orm.anno.EntityCache;
import com.zfoo.orm.anno.Id;
import com.zfoo.orm.anno.IndexText;
import com.zfoo.orm.model.IEntity;
import com.zfoo.protocol.collection.CollectionUtils;
import com.zfoo.scheduler.util.TimeUtils;
import com.zfoo.tank.common.protocol.common.PlayerInfo;
import com.zfoo.tank.common.protocol.room.Room;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


@EntityCache
public class RoomEntity implements IEntity<Long> {



    // ----------------------------------------------------------------------------------------------------------------
    @Id
    private long id;
    // 房主
    private long owner;
    // 当前玩家的id
    private List<PlayerInfo> players = new CopyOnWriteArrayList<>();
    private long createTime = TimeUtils.now();


    @IndexText
    private String title;
    private String subTitle;
    private String description;
    private boolean visible;
    private String password;
    private String icon;
    private int gameId;
    private int maxNums;

    // -----------------------------------------------------------------------------------------------------------------
    private String roomServerAddress;

    public RoomEntity(long id, long owner, List<PlayerInfo> players, String title, String subTitle, String description, boolean visible, String password, String icon, int gameId, int maxNums, String roomServerAddress) {
        this.id = id;
        this.owner = owner;
        this.players = players;
        this.title = title;
        this.subTitle = subTitle;
        this.description = description;
        this.visible = visible;
        this.password = password;
        this.icon = icon;
        this.gameId = gameId;
        this.maxNums = maxNums;
        this.roomServerAddress = roomServerAddress;
    }


    @Override
    public Long id() {
        return id;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public PlayerInfo matchPlayerById(long uid) {
        return players.stream()
                .filter(it -> it.getId() == uid)
                .findFirst()
                .orElse(null);
    }

    public List<Long> toPlayerIds() {
        return players.stream().map(it -> it.getId()).toList();
    }

    // -----------------------------------------------------------------------------------------------------------------
    public Room toRoom() {
        return new Room(id, owner, players.size(), createTime, title, subTitle, description, visible, password, icon, gameId, maxNums);
    }

    public RoomEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOwner() {
        return owner;
    }

    public void setOwner(long owner) {
        this.owner = owner;
    }

    public List<PlayerInfo> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerInfo> players) {
        this.players = players;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getMaxNums() {
        return maxNums;
    }

    public void setMaxNums(int maxNums) {
        this.maxNums = maxNums;
    }

    public String getRoomServerAddress() {
        return roomServerAddress;
    }

    public void setRoomServerAddress(String roomServerAddress) {
        this.roomServerAddress = roomServerAddress;
    }
}
