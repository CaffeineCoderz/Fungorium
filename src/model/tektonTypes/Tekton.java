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
import java.io.Serializable;

public class Tekton implements Serializable {
    private static final long serialVersionUID = 1L;

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
        this.spores = new ArrayList<>(tekton.getSpores());
        this.insects = new ArrayList<>(tekton.getInsects());
        this.threads = new ArrayList<>(tekton.getThreads());
        this.body = tekton.getBody();
        this.neighbours = new ArrayList<>(tekton.getNeighbours());
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
    public List<Tekton> breakTekton(CommandProcessor commandProcessor) {
        // Először eltávolítjuk az összes rovar, fonal és spóra kapcsolatot
        for (int i = insects.size() - 1; i >= 0; i--) {
            Insect insect = insects.get(i);
            insect.deadInsect(commandProcessor);
        }
        for (int i = threads.size() -1; i>=0; i--) {
            FungusThread ft = threads.get(i);
            ft.setIsDying(true);
            ft.setLifeSpan(0);
            ft.getSpecies().destroyThread(ft);
            ft.getTektons().get(0).removeThread(ft);
            if (ft.isBridge()) {
                ft.getTektons().get(1).removeThread(ft);
            }
            ft.getTektons().clear();
            String objKey = commandProcessor.findByObject(ft);
            if (objKey != null) {
                commandProcessor.getCreatedObjects().remove(objKey);
                commandProcessor.getCreatedObjects().remove(objKey);
            }else System.out.println("Not found");
        }
        threads.clear();
        for (int i = spores.size() - 1; i >= 0; i--) {
            Spore spore = spores.get(i);
            spore.absorbed();
            String objKey = commandProcessor.findByObject(spore);
            if (objKey != null) {
                commandProcessor.getCreatedObjects().remove(objKey);
            }
        }
        if (body != null) {
            body.getSpecies().destroyBody(body);
            String objKey = commandProcessor.findByObject(body);
            if (objKey != null) {
                commandProcessor.getCreatedObjects().remove(objKey);
            }
        }
    
        // Létrehozzuk az új Tektonokat
        Tekton t1 = new Tekton(this);
        Tekton t2 = new Tekton(this);
        t2.neighbours = new ArrayList<>();
        
    
        // Szomszédok felosztása 
        List<Tekton> t1Neighbours = new ArrayList<>(this.neighbours);
        // Az új Tektonok szomszédainak beállítása
        for (Tekton neighbour : t1Neighbours) {
            neighbour.removeNeighbour(this);
            neighbour.addNeighbour(t1);
        }
        String objKey = commandProcessor.findByObject(this);
        commandProcessor.getCreatedObjects().remove(objKey);
        // Az eredeti Tekton szomszédainak törlése
        neighbours.clear();
        List<Tekton> tektons = new ArrayList<>();
        tektons.add(t1);
        tektons.add(t2);
        return tektons;
    }
    
    /**
     * Deletes this Tekton and removes all its associations with its neighbors.
     * This method is called when the Tekton is no longer needed or has been broken.
     */
    public void deleteTekton() {
        for (Tekton tekton : neighbours)
            tekton.removeNeighbour(this);
    }
    
}
