package fungus;
import java.util.ArrayList;
import java.util.List;
import sporeTypes.Spore;
import tektonTypes.Tekton;
import utils.Logger;

//! NEM TELJES IMPLEMENTACIO MEG
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
     * Gets the FungusSpecies that this fungus body is part of.
     * @return the FungusSpecies that this fungus body is part of.
     */
    public FungusSpecies getSpecies(){
        return species;
    }

    /**
     * Adds a fungus thread to this fungus body.
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
     * Retrieves the Tekton object associated with this fungus body.
     * 
     * @return a Tekton object representing the Tekton where this fungus body is located.
     */
    public Tekton getTekton() {
        return tekton;
    }
    /**
     * Retrieves the list of fungus threads associated with this fungus body.
     * 
     * @return a list of FungusThread objects representing the threads of this fungus body.
     */
    public List<FungusThread> getThreads(){
        return threads;
    }

    // ! Not implemented yet
    
    /**
     * Spreads spores to the neighboring Tektons.
     * 
     * If there are enough spores (i.e., isThereEnough() returns true), this method
     * spreads a spore to each of the neighboring Tektons. The number of spores
     * available and the number of sporulations left are decremented.
     */
    public void sporulate() {
        // implementáció
        if(isThereEnough()){
            if(sporeCount<10){
                for(Tekton t : tekton.getNeighbours()){
                    Spore tempSpore = new Spore();
                    t.addSpore(tempSpore);
                    tempSpore.setTekton(t);
                }
            }
            else
                {
                    for(Tekton t : tekton.getNeighbours()){
                        Spore tempSpore = new Spore();
                        t.addSpore(tempSpore);
                        tempSpore.setTekton(t);
                        sporeCount--;
                        for(Tekton tt: t.getNeighbours()){
                            Spore tempSpore2 = new Spore();
                            t.addSpore(tempSpore2);
                            tempSpore2.setTekton(t);
                        }
                    }
                }
        sporulateLeft--;
        }
        else log.askQ("Fungusbody can't sporulate", false);
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
        // implementáció
        if(sporulateLeft <= 0){
            return true;
        }
        return false;
    }

    /**
     * Increases the number of available spores.
     * 
     * This method increases the number of available spores by one. This
     * method is called when the fungus body is grown.
     */
    public void produceSpore() {
        // implementáció
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
        // implementáció
        if(sporeCount > 5){
            return true;
        }
        return false;
    }
    
    /**
     * Returns the number of spores available in the fungus body.
     * 
     * @return an Integer representing the number of spores available in the fungus body.
     */
    public Integer getSporeCount(){
        return sporeCount;
    }

    
}
