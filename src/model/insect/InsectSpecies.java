package insect;

import java.util.ArrayList;
import java.util.List;

import insect.Insect;
import interfaces.iControl;

public class InsectSpecies implements iControl{
    Integer id;
    private List<Insect> myInsects;
    private Integer score; 

    public InsectSpecies(){
        myInsects = new ArrayList<>();
        Insect firstInsect = new Insect();
        myInsects.add(firstInsect);
    }

    public List<Insect> getInsects() {
        return myInsects;
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
    
}