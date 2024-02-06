

package com.zfoo.tank.common.protocol.transfer;



public class HomeToRoomTransfer {

    private long roomId;
    private long uid;

    // ---------------------------------------------------------------------------------------------------------------
    private Home2RoomCreateRoomTransfer createRoomTransfer;

    public HomeToRoomTransfer(long roomId, long uid) {
        this.roomId = roomId;
        this.uid = uid;
    }

    public HomeToRoomTransfer() {
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public Home2RoomCreateRoomTransfer getCreateRoomTransfer() {
        return createRoomTransfer;
    }

    public void setCreateRoomTransfer(Home2RoomCreateRoomTransfer createRoomTransfer) {
        this.createRoomTransfer = createRoomTransfer;
    }
}
