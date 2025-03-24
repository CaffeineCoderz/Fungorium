package fungus;

import java.util.ArrayList;
import java.util.List;
import sporeTypes.Spore;
import tektonTypes.Tekton;
import utils.Logger;

public class FungusBody {
    private Integer sporeCount;
    private Integer sporulateLeft;
    private List<FungusThread> threads;
    private Tekton tekton;
    private FungusSpecies species;
    private Logger log = Logger.getLogger("FungusBodyLogger");

    public FungusBody(Integer sporeCount, Integer sporulateLeft) {
        this.sporeCount = sporeCount;
        this.sporulateLeft = sporulateLeft;
        this.threads = new ArrayList<>();
        this.tekton = null;
        this.species = null;
    }

    /**
     * Returns the FungusSpecies that this fungus body is part of.
     * 
     * @return the FungusSpecies that this fungus body is part of.
     */
    public FungusSpecies getSpecies() {
        return species;
    }

    /**
     * Adds a fungus thread to this fungus body.
     * 
     * @param thread the FungusThread to be added.
     */
    public void addThread(FungusThread thread) {
        threads.add(thread);
    }

    /**
     * Removes a specified fungus thread from this fungus body.
     * 
     * @param thread the FungusThread to be removed.
     * @return true if the thread was present and removed, false otherwise.
     */
    public Boolean removeThread(FungusThread thread) {
        return threads.remove(thread);
    }

    /**
     * Sets the Tekton where this fungus body is located.
     * 
     * @param tekton the Tekton where this fungus body is located.
     */
    public void setTekton(Tekton tekton) {
        this.tekton = tekton;
    }

    /**
     * Returns the Tekton object associated with this fungus body.
     * 
     * @return a Tekton object representing the Tekton where this fungus body is
     *         located.
     */
    public Tekton getTekton() {
        return tekton;
    }

    /**
     * Returns the list of fungus threads associated with this fungus body.
     * 
     * @return a list of FungusThread objects representing the threads of this
     *         fungus body.
     */
    public List<FungusThread> getThreads() {
        return threads;
    }

    /**
     * Spreads spores to the neighboring Tektons.
     * 
     * If there are enough spores (i.e., isThereEnough() returns true), this method
     * spreads a spore to each of the neighboring Tektons. The number of spores
     * available and the number of sporulations left are decremented.
     */
    public void sporulate() {
        if (isThereEnough()) {
            if (sporeCount < 10) {
                log.askQ("Body has enough spore to sporulate neighbours", false);
                if (this.tekton == null) {
                    log.askQ("Tekton is null", false);
                    return;
                }
                log.askQ("Going through all neighbouring tektons", false);
                for (Tekton t : tekton.getNeighbours()) {
                    log.askQ("Create Spore: tempspore", false);
                    Spore tempSpore = new Spore();
                    log.stepIn("t.addSpore(tempSpore)");
                    t.addSpore(tempSpore);
                    log.stepOut("t.addSpore(tempSpore)", null);
                    log.stepIn("tempSpore.setTekton(t)");
                    tempSpore.setTekton(t);
                    log.stepOut("tempSpore.setTekton(t)", null);
                    sporeCount--;
                }
                log.askQ("End Cycle", false);
            } else {
                log.askQ("Body has enough spore to sporulate neighbours and neighbours's neighbours", false);
                log.askQ("Going through all neighbouring tektons", false);
                for (Tekton t : tekton.getNeighbours()) {
                    log.askQ("Create Spore : tempspore", false);
                    Spore tempSpore = new Spore();
                    log.stepIn("t.addSpore(tempSpore)");
                    t.addSpore(tempSpore);
                    log.stepOut("t.addSpore(tempSpore)", null);
                    log.stepIn("tempSpore.setTekton(t)");
                    tempSpore.setTekton(t);
                    log.stepOut("tempSpore.setTekton(t)", null);
                    sporeCount--;
                    log.askQ("Going through all neighbouring tektons", false);
                    for (Tekton tt : t.getNeighbours()) {
                        log.askQ("Create Spore : tempSpore2", false);
                        Spore tempSpore2 = new Spore();
                        log.stepIn("tt.addSpore(tempSpore2)");
                        tt.addSpore(tempSpore2);
                        log.stepOut("tt.addSpore(tempSpore2)", null);
                        log.stepIn("tempSpore2.setTekton(tt)");
                        tempSpore2.setTekton(tt);
                        log.stepOut("tempSpore2.setTekton(tt)", null);
                        sporeCount--;
                    }
                    log.askQ("End Cycle", false);
                }
                log.askQ("End Cycle", false);
            }
            sporulateLeft--;
        } else
            log.askQ("Fungusbody can't sporulate", false);
    }

    /**
     * Checks if the fungus body should die.
     * 
     * A fungus body should die if it has no sporulations left. This method
     * checks for this condition and returns true if the fungus body should
     * die, false otherwise.
     * 
     * @return true if the fungus body should die, false otherwise.
     */
    public Boolean timeToDie() {
        return sporulateLeft <= 0;
    }

    /**
     * Increases the number of available spores.
     * 
     * This method increases the number of available spores by one. This
     * method is called when the fungus body is grown.
     */
    public void produceSpore() {
        sporeCount++;
    }

    /**
     * Checks if there are any available spores.
     * 
     * This method checks if the number of available spores is greater than
     * 5. If there are available spores, the method returns true, otherwise
     * it returns false.
     * 
     * @return true if there are available spores, false otherwise.
     */
    public Boolean isThereEnough() {
        return sporeCount > 5;
    }

    /**
     * Returns the number of spores available in the fungus body.
     * 
     * @return an Integer representing the number of spores available in the fungus
     *         body.
     */
    public Integer getSporeCount() {
        return sporeCount;
    }

    /**
     * Sets the species of this fungusBody.
     *
     * @param species The FungusSpecies instance to associate with this fungusBody.
     */
    public void setSpecies(FungusSpecies species) {
        this.species = species;
    }
}
