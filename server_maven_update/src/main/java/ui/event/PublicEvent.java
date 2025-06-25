package ui.event;

public class PublicEvent {
    private static PublicEvent instance;
    private PublicEvent(){}
    private ServerEvent serverEvent;
    public ServerEvent getServerEvent() {
        return serverEvent;
    }
    public void setServerEvent(ServerEvent serverEvent) {
        this.serverEvent = serverEvent;
    }
    public static PublicEvent getInstance(){
        if(instance == null)
        {
            instance = new PublicEvent();
        }
        return instance;
    }
    private LogEvent logEvent;
    public LogEvent getLogEvent() {
        return logEvent;
    }
    public void setLogEvent(LogEvent logEvent) {
        this.logEvent = logEvent;
    }
}
