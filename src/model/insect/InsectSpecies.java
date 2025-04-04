package insect;

import java.util.ArrayList;
import java.util.List;

import fungus.FungusSpecies;
import fungus.FungusThread;
import interfaces.iControl;
import sporeTypes.Spore;
import logic.*;
import utils.*;

public class InsectSpecies extends Player implements iControl{
    

    private List<Insect> myInsects;
    private Integer score; 
    private Logger log = Logger.getLogger("EntomologistLogger");
    public List<Insect> getInsects() {
        return myInsects;
    }
    public InsectSpecies() {
        super();
        score = 0;
        myInsects = new ArrayList<>();
    }
    public void addInsect(Insect e) {
        myInsects.add(e);
    }
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

    public void move(Insect selectedInsect, FungusThread selectedThread) {
        for(Insect insect : myInsects) {
            if (insect == selectedInsect) {
                selectedInsect.move(selectedThread);
            }
        }
    }
    
    public void eat(Insect selectedInsect, Spore selectedSpore) {
        for(Insect insect : myInsects) {
            if(insect == selectedInsect) {
                selectedInsect.consumeSpore(selectedSpore);
            }
        }
    }

    public void cut(Insect selectedInsect, FungusThread selectedThread) {
        for(Insect insect : myInsects) {
            if(insect == selectedInsect) {
                
                selectedInsect.cut(selectedThread);
            }
        }
    }
}
