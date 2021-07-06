package com.zfoo.tank.admin.model.gm;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2020-06-05 21:26
 */
public class ChangeCurrencyRequest {

    private String userName;

    private int gold;

    private int gem;

    private int energy;

    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getGem() {
        return gem;
    }

    public void setGem(int gem) {
        this.gem = gem;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
}
