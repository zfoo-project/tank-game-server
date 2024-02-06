
package com.zfoo.tank.common.protocol.transfer;

public class Admin2RoomStopRoomTransfer {

    private long roomId;

    public Admin2RoomStopRoomTransfer() {
    }

    public Admin2RoomStopRoomTransfer(long roomId) {
        this.roomId = roomId;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }
}
