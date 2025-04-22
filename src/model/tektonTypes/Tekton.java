package tektonTypes;

import fungus.FungusBody;
import fungus.FungusThread;
import insect.Insect;

import sporeTypes.Spore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import commands.CommandProcessor;
import utils.Logger;

public class Tekton {
    private Boolean canGrowBody;
    private Boolean canGrowThread;
    private List<Spore> spores;
    protected List<Insect> insects;
    protected List<FungusThread> threads;
    protected FungusBody body;
    private List<Tekton> neighbours;

    public Tekton() {
        this.canGrowBody = true;
        this.canGrowThread = true;
        this.spores = new ArrayList<>();
        this.insects = new ArrayList<>();
        this.threads = new ArrayList<>();
        this.body = null;
        this.neighbours = new ArrayList<>();
    }

    public Tekton(Boolean canGrowBody, Boolean canGrowThread) {
        this.canGrowBody = canGrowBody;
        this.canGrowThread = canGrowThread;
        this.spores = new ArrayList<>();
        this.insects = new ArrayList<>();
        this.threads = new ArrayList<>();
        this.body = null;
        this.neighbours = new ArrayList<>();
    }

    /**
     * Checks if there are at least the given amount of spores available in this
     * Tekton.
     * 
     * @param amount the amount of spores to check for.
     * @return true if there are at least the given amount of spores available in
     *         this Tekton, false otherwise.
     */
    public Boolean isThereEnoughSpore(Integer amount) {
        return spores.size() >= amount;
    }

    public Tekton(Tekton tekton) {
        this.canGrowBody = tekton.canGrowBody();
        this.canGrowThread = tekton.canGrowThread();
        this.spores = tekton.getSpores();
        this.insects = tekton.getInsects();
        this.threads = tekton.getThreads();
        this.body = tekton.getBody();
        this.neighbours = tekton.getNeighbours();
    }

    /**
     * Sets whether this Tekton can grow a FungusBody or not.
     * 
     * @param canGrowBody true if this Tekton can grow a FungusBody, false
     *                    otherwise.
     */
    public void setGrowBody(Boolean canGrowBody) {
        this.canGrowBody = canGrowBody;
    }

    /**
     * Sets whether this Tekton can grow a FungusThread or not.
     * 
     * @param canGrowThread true if this Tekton can grow a FungusThread, false
     *                      otherwise.
     */
    public void setGrowThread(Boolean canGrowThread) {
        this.canGrowThread = canGrowThread;
    }

    /**
     * Adds a Spore object to the list of spores associated with this Tekton.
     * 
     * @param spore the Spore object to add to the list of associated spores.
     */
    public void addSpore(Spore spore) {
        spores.add(spore);
    }

    /**
     * Removes a specified Spore object from the list of associated spores.
     * 
     * @param spore the Spore object to remove from the list of associated spores.
     */
    public void removeSpore(Spore spore) {
        spores.remove(spore);
    }

    /**
     * Removes the first Spore object from the list of associated spores.
     */
    public void removeSpore() {
        spores.remove(0);
    }

    /**
     * Adds an Insect object to the list of insects associated with this Tekton.
     * 
     * @param insect the Insect object to add to the list of associated insects.
     */
    public void addInsect(Insect insect) {
        insects.add(insect);
    }

    /**
     * Removes a specified Insect object from the list of associated insects.
     * 
     * @param insect the Insect object to remove from the list of associated
     *               insects.
     */
    public void removeInsect(Insect insect) {
        insects.remove(insect);
    }

    /**
     * Adds a FungusThread instance to the list of threads associated with this
     * Tekton.
     * 
     * @param thread the FungusThread instance to add to the list of associated
     *               threads.
     */
    public void addThread(FungusThread thread) {
        threads.add(thread);
    }

    /**
     * Removes a specified FungusThread instance from the list of threads associated
     * with this Tekton.
     * 
     * @param thread the FungusThread instance to remove from the list of associated
     *               threads.
     */
    public void removeThread(FungusThread thread) {
        threads.remove(thread);
    }

    /**
     * Sets the FungusBody associated with this Tekton.
     * If the FungusBody is null, the current FungusBody is removed.
     * If the Tekton already has a FungusBody, nothing is done and a debug message
     * is printed.
     * 
     * @param nbody the FungusBody to set as the associated FungusBody.
     */
    public void setBody(FungusBody nbody) {
        if (nbody == null) {
            body = null;
            canGrowBody = true;
        } else if (body == null) {
            this.body = nbody;
            canGrowBody = false;
        } else
            System.err.println("Tekton already has a FungusBody on it");
    }

    /**
     * Retrieves the FungusBody associated with this Tekton.
     * 
     * @return the FungusBody associated with this Tekton, or null if there is none.
     */
    public FungusBody getBody() {
        return body;
    }

    /**
     * Checks if there are no insects on this Tekton.
     * 
     * @return true if there are no insects on this Tekton, false otherwise.
     */
    public Boolean insectFree() {
        return insects.isEmpty();
    }

    /**
     * Retrieves the list of Spore objects associated with this Tekton.
     * 
     * @return a list of Spore objects representing the spores associated with this
     *         Tekton.
     */
    public List<Spore> getSpores() {
        return spores;
    }

    /**
     * Removes a specified Tekton object from the list of neighbors of this Tekton.
     * 
     * @param neighbour the Tekton object to remove from the list of neighbors.
     */
    public void removeNeighbour(Tekton neighbour) {
        neighbours.remove(neighbour);
    }

    /**
     * Adds a specified Tekton object to the list of neighbors of this Tekton.
     * 
     * @param neighbour the Tekton object to add to the list of neighbors.
     */
    public void addNeighbour(Tekton neighbour) {
        neighbours.add(neighbour);

    }

    /**
     * Retrieves the value of the canGrowBody flag associated with this Tekton.
     * 
     * The canGrowBody flag is true if this Tekton can grow a FungusBody, false
     * otherwise.
     * 
     * @return true if this Tekton can grow a FungusBody, false otherwise.
     */
    public Boolean canGrowBody() {
        return canGrowBody;
    }

    /**
     * Retrieves the value of the canGrowThread flag associated with this Tekton.
     * 
     * The canGrowThread flag is true if this Tekton can grow a FungusThread, false
     * otherwise.
     * 
     * @return true if this Tekton can grow a FungusThread, false otherwise.
     */
    public Boolean canGrowThread() {
        return canGrowThread;
    }

    /**
     * Retrieves the list of Insect objects associated with this Tekton.
     * 
     * @return a list of Insect objects representing the insects associated with
     *         this Tekton.
     */
    // ! még nincs statikus diagramba beleírva!
    public List<Insect> getInsects() {
        return insects;
    }

    /**
     * Retrieves the list of FungusThread instances associated with this Tekton.
     * 
     * @return a list of FungusThread objects representing the threads associated
     *         with this Tekton.
     */
    public List<FungusThread> getThreads() {
        return threads;
    }

    /**
     * Retrieves the list of Tekton objects that are neighbors of this Tekton.
     * 
     * @return a list of Tekton objects representing the neighbors of this Tekton.
     */
    public List<Tekton> getNeighbours() {
        return neighbours;
    }

    /**
     * Breaks this Tekton into two smaller Tekton objects, removing all associations
     * with insects, threads, and spores. The two new Tekton objects are added to
     * the list of neighbors of all Tekton objects that previously neighbored this
     * Tekton. The two new Tekton objects are also added to each other's list of
     * neighbors.
     */
    public void breakTekton() {
        for (int i = insects.size() - 1; i >= 0; i--) {
            Insect insect = insects.get(i);
            insect.deadInsect();
        }
        for (FungusThread ft : threads) {
            ft.setIsDying(true);
            ft.getSpecies().destroyThread(ft);
        }
        for (int i = spores.size() - 1; i >= 0; i--) {
            Spore spore = spores.get(i);
            spore.absorbed();
        }
        body.getSpecies().destroyBody(body);
        Tekton t1 = new Tekton(this);
        Tekton t2 = new Tekton(this);

        for (Tekton tekton : neighbours) {
            tekton.removeNeighbour(this);

            tekton.addNeighbour(t1);
            tekton.addNeighbour(t2);
        }
        t1.addNeighbour(t2);

        t2.addNeighbour(t1);
    }
    
    // ! még nincs statikus diagramba beleírva!
    public void deleteTekton() {
        for (Tekton tekton : neighbours)
            tekton.removeNeighbour(this);
    }
    
}
