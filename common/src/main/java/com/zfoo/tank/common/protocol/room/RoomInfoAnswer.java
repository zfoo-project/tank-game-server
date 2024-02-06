package com.zfoo.tank.common.protocol.room;


public class RoomInfoAnswer {

    private Room room;
    private String roomServerAddress;
    private String dsServerAddress;

    public RoomInfoAnswer() {
    }

    public RoomInfoAnswer(Room room, String roomServerAddress, String dsServerAddress) {
        this.room = room;
        this.roomServerAddress = roomServerAddress;
        this.dsServerAddress = dsServerAddress;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getRoomServerAddress() {
        return roomServerAddress;
    }

    public void setRoomServerAddress(String roomServerAddress) {
        this.roomServerAddress = roomServerAddress;
    }

    public String getDsServerAddress() {
        return dsServerAddress;
    }

    public void setDsServerAddress(String dsServerAddress) {
        this.dsServerAddress = dsServerAddress;
    }
}
