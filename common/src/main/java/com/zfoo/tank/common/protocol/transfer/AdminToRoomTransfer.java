package com.zfoo.tank.common.protocol.transfer;


public class AdminToRoomTransfer {

    private long roomId;

    // ---------------------------------------------------------------------------------------------------------------
    private Admin2RoomStopRoomTransfer stopDsTransfer;

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

    public Admin2RoomStopRoomTransfer getStopDsTransfer() {
        return stopDsTransfer;
    }

    public void setStopDsTransfer(Admin2RoomStopRoomTransfer stopDsTransfer) {
        this.stopDsTransfer = stopDsTransfer;
    }
}
