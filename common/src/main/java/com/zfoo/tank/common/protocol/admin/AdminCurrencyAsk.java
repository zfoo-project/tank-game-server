package com.zfoo.tank.common.protocol.admin;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2019-12-05 10:43
 */
public class AdminCurrencyAsk {

    private long userId;

    private int gold;

    private int gem;

    private int energy;

    public static AdminCurrencyAsk valueOf(long userId, int gold, int gem, int energy) {
        var ask = new AdminCurrencyAsk();
        ask.userId = userId;
        ask.gold = gold;
        ask.gem = gem;
        ask.energy = energy;
        return ask;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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
