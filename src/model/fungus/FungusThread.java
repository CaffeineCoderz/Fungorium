package fungus;

import java.util.ArrayList;
import java.util.List;

import insect.Insect;
import tektonTypes.Tekton;
import utils.Logger;

// ! - Az elrágott fonalak nem pusztulnak el azonnal, hanem csak egy kis idő elteltével (ez fonaltípustól függő idő). 
// ! A fonalak képesek megenni a tektonjukon található bénult rovarokat. Ilyenkor a rovar elpusztul, a fonal pedig gombatestet növeszthet.

public class FungusThread {
    private Integer lifeSpan;
    private Boolean bridge;
    private Boolean isDying;
    private List<Tekton> tektons;
    private FungusSpecies species;
    private FungusBody body;
    private Boolean connected;
    private FungusThread prevThread;
    private FungusThread nextThread;

    private Logger log = Logger.getLogger("FungusThreadLogger");

    public FungusThread() {
        this.lifeSpan = 0;
        this.bridge = false;
        this.isDying = false;
        this.species = null;
        this.tektons = new ArrayList<>();
        prevThread = null;
        nextThread = null;
        connected = true;
    }

    public FungusThread(Integer lifeSpan, Boolean bridge) {
        this.lifeSpan = lifeSpan;
        this.bridge = bridge;
        this.isDying = false;
        this.species = null;
        this.tektons = new ArrayList<>();
        prevThread = null;
        nextThread = null;
        connected=true;
    }

    public FungusThread(Integer lifeSpan, Boolean bridge, FungusThread prev) {
        this.lifeSpan = lifeSpan;
        this.bridge = bridge;
        this.isDying = false;
        this.species = null;
        this.tektons = new ArrayList<>();
        prevThread = prev;
        nextThread = null;
        connected=true;
        if (prev != null)
            body = prev.getBody();
        else
            body = null;
    }

    public void setConnected(Boolean bool){
        this.connected=bool;
    }

    /**
     * Sets the previous thread
     * 
     * @param f the previous thread
     */
    public void setPrevThread(FungusThread f) {
        prevThread = f;
    }

    /**
     * Sets the next thread
     * 
     * @param f the next thread
     */
    public void setNextThread(FungusThread f) {
        nextThread = f;
    }

    /**
     * Returns the previous thread
     * 
     * @return
     */
    public FungusThread getPrev() {
        return prevThread;
    }

    /**
     * Returns the body from which the thread grows originaly
     * 
     * @return
     */
    public FungusBody getBody() {
        return body;
    }

    /**
     * Sets the body
     * 
     * @param nBody
     */
    public void setBody(FungusBody nBody) {
        body = nBody;
    }

    /**
     * Returns the next thread
     * 
     * @return
     */
    public FungusThread getNext() {
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
     * Returns the FungusSpecies that this fungus thread is part of.
     * 
     * @return the FungusSpecies that this fungus thread is part of.
     */
    public FungusSpecies getSpecies() {
        return species;
    }

    /**
     * Decreases the life span of this fungus thread by one.
     * 
     * If the life span of this fungus thread reaches zero, this method sets the
     * isDying flag of this fungus thread to true.
     */
    public void decreaseLife() {
        if (!isDying) {
            return;
        }
        lifeSpan--;
        if (lifeSpan <= 0) {
            this.destroy();
        }
    }

    /**
     * Adds a Tekton to the list of Tektons associated with this FungusThread.
     * 
     * @param tekton the Tekton to add to the list of associated Tektons.
     */
    public void addTekton(Tekton tekton) {
        if (tektons.size() == 2) {
            return;
        }
        tektons.add(tekton);
        if(tektons.size() == 2){
            log.stepIn("setBridge(true)");
            setBridge(true);
            log.stepOut("setBridge(true)", null);
        }
    }

    /**
     * Sets the bridge flag of this fungus thread to the given value.
     * 
     * @param bridge true if this fungus thread should be marked as a bridge, false
     *               otherwise.
     */
    public void setBridge(Boolean bridge) {
        this.bridge = bridge;
    }

    /**
     * Sets the isDying flag of this fungus thread to the given value.
     * 
     * @param isDying true if this fungus thread should be marked as dying, false
     *                otherwise.
     */
    public void setIsDying(Boolean isDying) {
        this.isDying = isDying;
    }

    /**
     * Returns the value of the isDying flag of this fungus thread.
     * 
     * @return true if this fungus thread is marked as dying, false otherwise.
     */
    public Boolean getIsDying() {
        return isDying;
    }

    /**
     * Returns the current life span of this fungus thread.
     * 
     * @return the number of rounds this fungus thread will live.
     */
    public Integer getLifeSpan() {
        return lifeSpan;
    }

    /**
     * Returns the first Tekton associated with this fungus thread.
     * 
     * Note that this method does not check if the returned Tekton is the
     * only one associated with this fungus thread. If this fungus thread
     * is a bridge, this method will return one of the two associated Tektons.
     * 
     * @return the first Tekton associated with this fungus thread.
     */
    public Tekton getTekton(Insect i) {
        if(i == null || !bridge){
            return tektons.get(0);
        }else{
            if (i.getRecent() == tektons.get(0)) {
                return tektons.get(1);
            }else{
                return tektons.get(0);
            }
        }
    }

    public Tekton getTekton() {
        if (tektons.size() == 0) {
            return null;
        } else if (tektons.size() == 1) {
            return tektons.get(0);
        } else {
            return tektons.get(0);
        }
    }

    /**
     * Sets the Tekton object associated with the given Insect object to the
     * "other" Tekton associated with this fungus thread, if this fungus thread
     * is a bridge. If this fungus thread is not a bridge, it sets the Tekton
     * object associated with the given Insect object to the only Tekton
     * associated with this fungus thread.
     * 
     * @param i the Insect object to be updated.
     */
    public void insectSetting(Insect i){
        if(i == null || !bridge){
            i.setRecentTekton(tektons.get(0));
        }else{
            if (i.getRecent() == tektons.get(0)) {
                i.setRecentTekton(tektons.get(1));
            }else{
                i.setRecentTekton(tektons.get(0));
            }
        }
    }

    /**
     * Returns the list of Tektons associated with this fungus thread.
     * 
     * @return the list of Tektons associated with this fungus thread.
     */
    public List<Tekton> getTektons() {
        return tektons;
    }

    /**
     * Destroys this FungusThread by removing it from all associated Tekton
     * instances.
     *
     * This method iterates through the list of Tektons associated with this
     * FungusThread,
     * and calls the removeThread method on each, effectively dissociating this
     * FungusThread
     * from all Tektons it was previously linked to.
     */
    public void destroy() {
        for (Tekton tekton : tektons) {
            log.stepIn("tekton.removeThread(this)");
            tekton.removeThread(this);
            log.stepOut("tekton.removeThread(this)", null);
        }
    }

    /**
     * Sets the species of this fungusThread.
     *
     * @param species The FungusSpecies instance to associate with this
     *                fungusThread.
     */
    public void setSpecies(FungusSpecies species) {
        this.species = species;
    }

    // ! nincs statikus diagram
    public void setLifeSpan(Integer lifeSpan) {
        this.lifeSpan = lifeSpan;
    }
    public void disconnected(){
        
    }
}