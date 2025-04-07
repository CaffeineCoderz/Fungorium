package insect;

import java.util.ArrayList;
import java.util.List;

import fungus.FungusSpecies;
import fungus.FungusThread;
import interfaces.iControl;
import sporeTypes.Spore;
import logic.*;
import utils.*;

public class InsectSpecies implements iControl{
    // Game Logic
    Integer id;
    
    // DATA
    private List<Insect> myInsects;
    private Integer score; 
    private Logger log = Logger.getLogger("InsectSpeciesLogger");
    public List<Insect> getInsects() {
        return myInsects;
    }
    public InsectSpecies() {
        super();
        score = 0;
        myInsects = new ArrayList<>();
    }

    /*
     * @return the ID of the player.
     */
    public Integer getId(){
        return id;
    }

    /**
     * Sets the ID of the player.
     * 
     * @param id the ID to set for the player.
     */
    public void setId(Integer id){
        this.id = id;
    }

    /**
     * Retrieves the current score of this FungusSpecies.
     * 
     * @return the current score of this FungusSpecies.
     */
    public Integer getScore() {
        return score;
    }

    /**
     * Adds the specified Insect to the list of insects managed by this Entomologist.
     * 
     * @param e the Insect to be added to the list of insects.
     */
    public void addInsect(Insect e) {
        myInsects.add(e);
    }
    /**
     * Removes the specified Insect from the list of insects managed by this Entomologist.
     *
     * @param e the Insect to be removed from the list.
     */
    public void removeInsect(Insect e) {
        myInsects.remove(e);
    }
    /**
     * Adds the specified amount to the score.
     *
     * @param x The amount to add to the score.
     */
    @Override
    public void addScore(Integer x){
        score += x;
    }

    /**
     * Decreases the score by the specified amount.
     *
     * @param x The amount to subtract from the score.
     */
    @Override
    public void decreaseScore(Integer x){
        score -= x;
    }

    /**
     * Handles the elapsed time for a given round.
     *
     * @param Round The round number that has elapsed.
     */
    @Override
    public void timeElapsed(){
        for (Insect i : myInsects) {
            i.timeElapsed();
        }
    }

    /**
     * Orders the specified Insect to move to the specified FungusThread.
     * If the specified Insect is not in the list of insects managed by this
     * Entomologist, nothing happens.
     * 
     * @param selectedInsect the Insect to move.
     * @param selectedThread the FungusThread that the Insect should move to.
     */
    public void move(Insect selectedInsect, FungusThread selectedThread) {
        for(Insect insect : myInsects) {
            if (insect == selectedInsect) {
                
                selectedInsect.move(selectedThread);
            }
        }
    }
    
    /**
     * Orders the specified Insect to eat the specified Spore.
     * If the specified Insect is not in the list of insects managed by this
     * Entomologist, nothing happens.
     * 
     * @param selectedInsect the Insect which should consume the Spore.
     * @param selectedSpore the Spore to be consumed.
     */
    public void eat(Insect selectedInsect, Spore selectedSpore) {
        for(Insect insect : myInsects) {
            if(insect == selectedInsect) {
                selectedInsect.consumeSpore(selectedSpore);
            }
        }
    }

    /**
     * Orders the specified Insect to cut the specified FungusThread.
     * If the specified Insect is not in the list of insects managed by this
     * Entomologist, nothing happens.
     * 
     * @param selectedInsect the Insect which should perform the cut.
     * @param selectedThread the FungusThread which should be cut.
     */
    public void cut(Insect selectedInsect, FungusThread selectedThread) {
        for(Insect insect : myInsects) {
            if(insect == selectedInsect) {
                
                selectedInsect.cut(selectedThread);
            }
        }
    }
}
