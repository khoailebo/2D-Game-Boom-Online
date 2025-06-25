package dtos;

public class SendMsg {
    public PlayerDTO playerDTO;
    public int clientId;
    public SendMsg(int clientId,PlayerDTO playerDTO) {
        this.playerDTO = playerDTO;
        this.clientId = clientId;
    }
    
}
