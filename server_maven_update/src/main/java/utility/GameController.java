package utility;


import Enum.Direction;
import dtos.PlayerDTO;
public class GameController {
    public static int counter = 0;
    public static String []names = new String []{"boy","oldman","skeleton","zombie"};
    public static PlayerDTO getRandomState(int clientId){
        int x = 0;
        int y = 0;
        if(clientId  == 0)
        {
            x = 48;
            y = 48;
        }
        else if(clientId == 1){
            x = 480;
            y = 480;
        }
        else if(clientId == 2)
        {
            x = 480;
            y = 48;
        }
        else if(clientId == 3)
        {
            x = 48;
            y = 480;
        }
        
        return new PlayerDTO(x,y,3,Direction.NONE,3,names[clientId]);
    }
}
