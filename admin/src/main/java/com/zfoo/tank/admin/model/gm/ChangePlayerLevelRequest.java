package com.zfoo.tank.admin.model.gm;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2020-06-05 21:26
 */
public class ChangePlayerLevelRequest {

    private String userName;

    private int playerLevel;

    private int exp;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPlayerLevel() {
        return playerLevel;
    }

    public void setPlayerLevel(int playerLevel) {
        this.playerLevel = playerLevel;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }
}
