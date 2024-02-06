package com.zfoo.tank.common.protocol.room;


public class RoomInfoAnswer {

    private Room room;
    private String roomServerAddress;

    public RoomInfoAnswer() {
    }

    public RoomInfoAnswer(Room room, String roomServerAddress) {
        this.room = room;
        this.roomServerAddress = roomServerAddress;
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
}
