package com.zfoo.tank.common.protocol.transfer;


public class AdminToRoomTransfer {

    private long roomId;

    // ---------------------------------------------------------------------------------------------------------------
    private Admin2RoomCloseRoomTransfer closeRoomTransfer;

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

    public Admin2RoomCloseRoomTransfer getCloseRoomTransfer() {
        return closeRoomTransfer;
    }

    public void setCloseRoomTransfer(Admin2RoomCloseRoomTransfer closeRoomTransfer) {
        this.closeRoomTransfer = closeRoomTransfer;
    }
}
