package GUI;

public class GameObjectEvent {
    private Object source;
    private String eventType;
    private Object data; // Opcionális további adatok
    
    public GameObjectEvent(Object source, String eventType) {
        this.source = source;
        this.eventType = eventType;
    }
    
    public GameObjectEvent(Object source, String eventType, Object data) {
        this(source, eventType);
        this.data = data;
    }
    
    // Getter metódusok
    public Object getSource() { return source; }
    public String getEventType() { return eventType; }
    public Object getData() { return data; }
}