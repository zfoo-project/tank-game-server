package com.zfoo.tank.common.protocol.room;

import com.zfoo.protocol.anno.Note;
import com.zfoo.protocol.anno.Protocol;

@Note("房间基本信息")
public class Room {

    private long id;
    @Note("房主")
    private long owner;
    @Note("现有人数")
    private int currentNums;
    @Note("创建时间")
    private long createTime;
    private String title;
    private String subTitle;
    private String description;
    @Note("公开或者私密")
    private boolean visible;
    private String password;
    private String icon;
    @Note("游戏类型")
    private int gameId;
    @Note("游戏人数")
    private int maxNums;

    public Room() {
    }
    public Room(long id, long owner, int currentNums, long createTime, String title, String subTitle, String description, boolean visible, String password, String icon, int gameId, int maxNums) {
        this.id = id;
        this.owner = owner;
        this.currentNums = currentNums;
        this.createTime = createTime;
        this.title = title;
        this.subTitle = subTitle;
        this.description = description;
        this.visible = visible;
        this.password = password;
        this.icon = icon;
        this.gameId = gameId;
        this.maxNums = maxNums;
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
    public int getCurrentNums() {
        return currentNums;
    }
    public void setCurrentNums(int currentNums) {
        this.currentNums = currentNums;
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
}