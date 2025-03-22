package tektonTypes;

import fungus.FungusBody;
import fungus.FungusThread;
import insect.Insect;
import interfaces.iControl;
import java.util.ArrayList;
import java.util.List;
import sporeTypes.Spore;
    // ! NEM TELJES IMPLEMENTÁCIÓ MÉG

public class Tekton implements iControl {
    private boolean canGrowBody;
    private boolean canGrowThread;
    private List<Spore> spores;
    protected List<Insect> insects;
    protected List<FungusThread> threads;
    protected List<FungusBody> bodies;
    private List<Tekton> neighbours;
    
    public Tekton(boolean canGrowBody, boolean canGrowThread) {
        this.canGrowBody = canGrowBody;
        this.canGrowThread = canGrowThread;
        this.spores = new ArrayList<>();
        this.insects = new ArrayList<>();
        this.threads = new ArrayList<>();
        this.bodies = new ArrayList<>();
        this.neighbours = new ArrayList<>();
    }

    public boolean isThereEnoughSpore(int amount) {
        return spores.size() >= amount;
    }

    public void setGrowBody(boolean canGrowBody) {
        this.canGrowBody = canGrowBody;
    }

    public void setGrowThread(boolean canGrowThread) {
        this.canGrowThread = canGrowThread;
    }

    public void addSpore(Spore spore) {
        spores.add(spore);
    }

    public void removeSpore(Spore spore) {
        spores.remove(spore);
    }

    public void addInsect(Insect insect) {
        insects.add(insect);
    }

    public void removeInsect(Insect insect) {
        insects.remove(insect);
    }

    public void addThread(FungusThread thread) {
        threads.add(thread);
    }

    public void removeThread(FungusThread thread) {
        threads.remove(thread);
    }

    public void addBody(FungusBody body) {
        bodies.add(body);
    }

    public void removeBody(FungusBody body) {
        bodies.remove(body);
    }

    public boolean insectFree() {
        return insects.isEmpty();
    }

    public List<Spore> getSpores() {
        return spores;
    }

    public boolean hasInsect() {
        return !insects.isEmpty();
    }

    public void removeNeighbour(Tekton neighbour) {
        neighbours.remove(neighbour);
    }

    public void addNeighbour(Tekton neighbour) {
        neighbours.add(neighbour);
    }

    public boolean canGrowBody() {
        return canGrowBody;
    }

    public boolean canGrowThread() {
        return canGrowThread;
    }

    public List<Insect> getInsects() {
        return insects;
    }

    public List<FungusThread> getThreads() {
        return threads;
    }

    public List<FungusBody> getBodies() {
        return bodies;
    }

    public List<Tekton> getNeighbours() {
        return neighbours;
    }

    // ! Not implemented yet
    
    public void breakTekton() {
        // ToDo
    }

    // iControl interface
    @Override
    public void addScore(){
        // ToDo
    }

    @Override
    public void decreaseScore(){
        // ToDo
    }

    @Override
    public void  timeElapsed(int Round){
        // ToDo
    }
}
