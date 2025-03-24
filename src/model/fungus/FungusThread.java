package fungus;

import java.util.ArrayList;
import java.util.List;
import tektonTypes.Tekton;
import utils.Logger;

//! NEM TELJES IMPLEMENTACIO MEG
public class FungusThread {
    private Integer lifeSpan;
    private Boolean bridge;
    private Boolean isDying;
    private List<Tekton> tektons;
    private FungusSpecies species;
    private FungusBody body;

    private FungusThread prevThread;
    private FungusThread nextThread;

    private Logger log = Logger.getLogger("FungusThreadLogger");

    public FungusThread(Integer lifeSpan, Boolean bridge) {
        this.lifeSpan = lifeSpan;
        this.bridge = bridge;
        this.isDying = false;
        this.species = null;
        this.tektons = new ArrayList<>();
        prevThread = null;
        nextThread = null;
    }

    public FungusThread(Integer lifeSpan, Boolean bridge, FungusThread prev) {
        this.lifeSpan = lifeSpan;
        this.bridge = bridge;
        this.isDying = false;
        this.species = null;
        this.tektons = new ArrayList<>();
        prevThread = prev;
        nextThread = null;
        if(prev!=null)
            body = prev.getBody();
        else
            body =null;
    }
    /**
     * Sets the previous thread
     * @param f the previous thread 
     */
    public void setPrevThread(FungusThread f){
        prevThread = f;
    }
    /**
     * Sets the next thread
     * @param f the next thread
     */
    public void setNextThread(FungusThread f){
        nextThread = f;
    }
    /**
     * Returns the previous thread
     * @return
     */
    public FungusThread getPrev(){
        return prevThread;
    }
    /**
     * Returns the body from which the thread grows originaly
     * @return
     */
    public FungusBody getBody(){
        return body;
    }
    /**
     * Sets the body
     * @param nBody
     */
    public void setBody(FungusBody nBody){
        body = nBody;
    }
    /**
     * Returns the next thread
     * @return
     */
    public FungusThread getNext(){
        return nextThread;
    }

    /**
     * Checks if this FungusThread is a bridge.
     * 
     * @return true if this FungusThread is marked as a bridge, false otherwise.
     */
    public Boolean isBridge() {
        return bridge;
    }

    /**
     * Gets the FungusSpecies that this fungus thread is part of.
     * @return the FungusSpecies that this fungus thread is part of.
     */
    public FungusSpecies getSpecies(){
        return species;
    }
    
    /**
     * Decreases the life span of this fungus thread by one.
     * 
     * If the life span of this fungus thread reaches zero, this method sets the
     * isDying flag of this fungus thread to true.
     */
    public void decreaseLife() {
        if (lifeSpan == 0) {
            return;
        }
        lifeSpan--;
        if (lifeSpan <= 0) {
            isDying = true;
        }
    }

    /**
     * Adds a Tekton to the list of Tektons associated with this FungusThread.
     * 
     * @param tekton the Tekton to add to the list of associated Tektons.
     */
    public void addTekton(Tekton tekton) {
        tektons.add(tekton);
    }

    /**
     * Sets the bridge flag of this fungus thread to the given value.
     * @param bridge true if this fungus thread should be marked as a bridge, false otherwise.
     */
    public void setBridge(Boolean bridge) {
        this.bridge = bridge;
    }

    /**
     * Sets the isDying flag of this fungus thread to the given value.
     * 
     * @param isDying true if this fungus thread should be marked as dying, false otherwise.
     */
    public void setIsDying(Boolean isDying) {
        this.isDying = isDying;
    }

    /**
     * Retrieves the value of the isDying flag of this fungus thread.
     * 
     * @return true if this fungus thread is marked as dying, false otherwise.
     */
    public Boolean getIsDying() {
        return isDying;
    }

    /**
     * Retrieves the current life span of this fungus thread.
     * 
     * @return the number of rounds this fungus thread will live.
     */
    public Integer getLifeSpan() {
        return lifeSpan;
    }

    /**
     * Retrieves the first Tekton associated with this fungus thread.
     * 
     * Note that this method does not check if the returned Tekton is the
     * only one associated with this fungus thread. If this fungus thread
     * is a bridge, this method will return one of the two associated Tektons.
     * @return the first Tekton associated with this fungus thread.
     */
    public Tekton getTekton() {
        return tektons.get(0);
    }

    /**
     * Destroys this FungusThread by removing it from all associated Tekton instances.
     *
     * This method iterates through the list of Tektons associated with this FungusThread,
     * and calls the removeThread method on each, effectively dissociating this FungusThread
     * from all Tektons it was previously linked to.
     */
    public void destroy() {
        for (Tekton tekton : tektons) {
            log.stepIn("tekton.removeThread(this)");
            tekton.removeThread(this);
            log.stepOut("tekton.removeThread(this)", null);
        }
    }

    public void setSpecies(FungusSpecies species) {
        this.species = species;
    }

}