

package com.zfoo.tank.common.protocol.transfer;

public class Home2RoomCreateRoomTransfer {

    private String roomServerAddress;

    public Home2RoomCreateRoomTransfer() {
    }

    public Home2RoomCreateRoomTransfer(String roomServerAddress) {
        this.roomServerAddress = roomServerAddress;
    }

    public String getRoomServerAddress() {
        return roomServerAddress;
    }

    public void setRoomServerAddress(String roomServerAddress) {
        this.roomServerAddress = roomServerAddress;
    }
}
