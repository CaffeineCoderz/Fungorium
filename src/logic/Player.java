package logic;

public abstract class Player {

    Integer id;
    protected Player(){
        
    }
    
    /**
     * Retrieves the ID of the player.
     * 
     * @return the ID of the player.
     */
    public Integer getId(){
        return id;
    }
}