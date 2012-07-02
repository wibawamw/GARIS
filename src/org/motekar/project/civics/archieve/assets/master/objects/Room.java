package org.motekar.project.civics.archieve.assets.master.objects;

/**
 *
 * @author Muhamad Wibawa
 */
public class Room {
    
    private Long index = Long.valueOf(0);
    private String roomCode = "";
    private String roomName = "";
    
    private boolean styled = false;
    
    public Room() {
    }
    
    public Room(Long index) {
        this.index = index;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public boolean isStyled() {
        return styled;
    }

    public void setStyled(boolean styled) {
        this.styled = styled;
    }
    
    @Override
    public String toString() {
        if (isStyled()) {
            return "<html><b>" + roomCode + "</b>"
                    + "<br style=\"font-size:40%\">" + roomName +"</br>";
        }
        
        return roomName;
    }
    
}
