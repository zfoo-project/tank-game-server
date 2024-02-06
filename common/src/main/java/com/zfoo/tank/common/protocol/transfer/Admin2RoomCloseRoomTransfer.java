
package com.zfoo.tank.common.protocol.transfer;

public class Admin2RoomCloseRoomTransfer {

    private long roomId;

    public Admin2RoomCloseRoomTransfer() {
    }

    public Admin2RoomCloseRoomTransfer(long roomId) {
        this.roomId = roomId;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }
}
