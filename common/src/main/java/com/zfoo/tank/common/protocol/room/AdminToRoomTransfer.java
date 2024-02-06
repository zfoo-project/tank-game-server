package com.zfoo.tank.common.protocol.room;


public class AdminToRoomTransfer {

    private long roomId;

    // ---------------------------------------------------------------------------------------------------------------
    private Admin2RoomStopDsTransfer stopDsTransfer;

    public AdminToRoomTransfer() {
    }

    public AdminToRoomTransfer(long roomId) {
        this.roomId = roomId;
    }


    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public Admin2RoomStopDsTransfer getStopDsTransfer() {
        return stopDsTransfer;
    }

    public void setStopDsTransfer(Admin2RoomStopDsTransfer stopDsTransfer) {
        this.stopDsTransfer = stopDsTransfer;
    }
}
