package fungus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import sporeTypes.Spore;
import tektonTypes.Tekton;
import utils.*;

public class FungusBody {
    private Integer sporeCount;
    private Integer sporulateLeft;
    private List<FungusThread> threads;
    private Tekton tekton;
    private FungusSpecies species;

    public FungusBody() {
        this.sporeCount = 0;
        this.sporulateLeft = 2;
        this.threads = new ArrayList<>();
        this.tekton = null;
        this.species = null;
    }

    public FungusBody(Integer sporeCount, Integer sporulateLeft) {
        this.sporeCount = sporeCount;
        this.sporulateLeft = sporulateLeft;
        this.threads = new ArrayList<>();
        this.tekton = null;
        this.species = null;
    }

    /**
     * Sets the spore count for this fungus body.
     * 
     * @param c the new value of spore count.
     */
    public void setSporeC(Integer c){
        sporeCount = c;
    }

    /**
     * Gets the FungusSpecies that this fungus body is part of.
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
     * Retrieves the Tekton object associated with this fungus body.
     * 
     * @return a Tekton object representing the Tekton where this fungus body is
     *         located.
     */
    public Tekton getTekton() {
        return tekton;
    }

    /**
     * Retrieves the list of fungus threads associated with this fungus body.
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
    public List<Spore> sporulate() {
        List<Spore> createdSpores = new ArrayList<>();
    
        if (!isThereEnough()) {
            System.out.println("Fungusbody can't sporulate");
            return createdSpores;
        }
    
        Queue<Tekton> queue = new LinkedList<>();
        Set<Tekton> visited = new HashSet<>();
    
        if (this.tekton == null) {
            System.err.println("Tekton is null.");
            return createdSpores;
        }
    
        queue.offer(this.tekton);
        visited.add(this.tekton);
    
        int depth = 0;
    
        while (!queue.isEmpty() && sporeCount > 0 && depth < 2) {
            int levelSize = queue.size(); // Hány elem van ezen a szinten
    
            for (int i = 0; i < levelSize; i++) {
                Tekton current = queue.poll();
    
                for (Tekton neighbour : current.getNeighbours()) {
                    if (neighbour != this.tekton && !visited.contains(neighbour)) {
                        Spore tempSpore = new Spore();
                        neighbour.addSpore(tempSpore);
                        tempSpore.setTekton(neighbour);
                        sporeCount--;
                        createdSpores.add(tempSpore);
    
                        queue.offer(neighbour);
                        visited.add(neighbour);
    
                        if (sporeCount <= 0) {
                            break;
                        }
                    }
                }
                if (sporeCount <= 0) {
                    break;
                }
            }
            depth++; // Egy szinttel mélyebbre megyünk
        }
    
        sporulateLeft--;
        return createdSpores;
    }
    
    

    /**
     * Checks if it can sporulate to neighbours's neighbours 
     * @return Whether it can sporulate to further than it's neighbouring tekton
     */
    public Boolean canSporeNeighbours(){
        int c =0;
        for (Tekton t : tekton.getNeighbours()) {
            c+= t.getNeighbours().size();
        }
        return sporeCount >= c;   
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
        return sporulateLeft == 0;
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
        return sporeCount > 1;
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
     * Sets the species of this fungus body.
     *
     * @param species the FungusSpecies instance to associate with this
     *                fungus body.
     */
    public void setSpecies(FungusSpecies species) {
        this.species = species;
    }
    public void setSporulateLeft(Integer sporulateLeft) {
        this.sporulateLeft = sporulateLeft;
    }
    public Integer getSporulateLeft() {
        return sporulateLeft;
    }
}
