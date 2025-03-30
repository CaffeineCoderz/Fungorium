package fungus;

import logic.Player;
import tektonTypes.Tekton;

public class Mycologist extends Player{
    private FungusSpecies mySpecies;
    private Integer score;

    public Mycologist(FungusSpecies species) {
        mySpecies = species;
    }

    /**
     * Retrieves the FungusSpecies associated with this Mycologist.
     *
     * @return the FungusSpecies instance that this Mycologist is associated with.
     */
    public FungusSpecies getSpecies() {
        return mySpecies;
    }

    /**
     * Sets the FungusSpecies associated with this Mycologist.
     *
     * @param species the FungusSpecies instance to associate with this
     *                Mycologist.
     */
    public void setSpecies(FungusSpecies species) {
        mySpecies = species;
    }
    
    /**
     * Orders the associated FungusSpecies to grow a new FungusBody
     * associated with the given FungusThread.
     *
     * @param selectedThread the FungusThread that the new FungusBody
     *                       should be associated with.
     */
    public void grow(FungusThread selectedThread) {
        mySpecies.growBody(selectedThread);
    }

    /**
     * Orders the associated FungusSpecies to grow a new FungusThread
     * associated with the given FungusThread and Tekton.
     *
     * @param selectedThread the FungusThread that the new FungusThread
     *                       should be associated with.
     * @param newThread the new FungusThread instance to add to the
     *                  associated FungusSpecies.
     */
    public void setThread(FungusThread selectedThread, FungusThread newThread) {
        mySpecies.growThread(selectedThread.getTekton(null), selectedThread, newThread);
    }
    
    /**
     * Orders the associated FungusSpecies to grow a new bridge-like FungusThread
     * associated with the given FungusThread and Tekton.
     *
     * @param fromThread the FungusThread that the new FungusThread
     *                   should be associated with.
     * @param toTekton the Tekton that the new FungusThread should be
     *                associated with.
     */
    public void setBridge(FungusThread fromThread,Tekton toTekton){
        mySpecies.growBridge(fromThread, toTekton);
    }

    /**
     * Orders the associated FungusSpecies to attempt to consume insects on the given FungusThread.
     * If the insects are stunned, they will be consumed, and a FungusBody may grow on the Tekton
     * associated with the FungusThread.
     *
     * @param selectedThread the FungusThread on which the insects are to be consumed.
     */
    public void kill(FungusThread selectedThread) {
        mySpecies.eatInsect(selectedThread);
    }

    /**
     * Orders the associated FungusSpecies to sporulate the given FungusBody.
     * This will cause the FungusBody to release spores, and may allow the
     * FungusSpecies to grow new FungusThread instances.
     *
     * @param selectedBody the FungusBody instance to sporulate.
     */
    public void sporulate(FungusBody selectedBody) {
        for(FungusBody body : mySpecies.getBodies()) {
            if(body == selectedBody) {
                selectedBody.sporulate();
            }
        }
    }
}
