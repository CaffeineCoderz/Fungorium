package tektonTypes;

import fungus.FungusBody;
import fungus.FungusThread;
import insect.Insect;
import java.util.ArrayList;
import java.util.List;
import sporeTypes.Spore;
    // ! NEM TELJES IMPLEMENTÁCIÓ MÉG

public class Tekton{
    private Boolean canGrowBody;
    private Boolean canGrowThread;
    private List<Spore> spores;
    protected List<Insect> insects;
    protected List<FungusThread> threads;
    protected FungusBody body;
    private List<Tekton> neighbours;
    
    public Tekton(Boolean canGrowBody, Boolean canGrowThread) {
        this.canGrowBody = canGrowBody;
        this.canGrowThread = canGrowThread;
        this.spores = new ArrayList<>();
        this.insects = new ArrayList<>();
        this.threads = new ArrayList<>();
        this.body = new FungusBody(null, null);
        this.neighbours = new ArrayList<>();
    }

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
    public void setGrowBody(Boolean canGrowBody) {
        this.canGrowBody = canGrowBody;
    }

    public void setGrowThread(Boolean canGrowThread) {
        this.canGrowThread = canGrowThread;
    }

    public void addSpore(Spore spore) {
        spores.add(spore);
    }

    public void removeSpore(Spore spore) {
        spores.remove(spore);
    }
    public void removeSpore(){
        spores.removeFirst();
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

    public void setBody(FungusBody body) {
        this.body = body;
    }

    public FungusBody getBody() {
        return body;
    }

    public Boolean insectFree() {
        return insects.isEmpty();
    }

    public List<Spore> getSpores() {
        return spores;
    }

    public Boolean hasInsect() {
        return !insects.isEmpty();
    }

    public void removeNeighbour(Tekton neighbour) {
        neighbours.remove(neighbour);
    }

    public void addNeighbour(Tekton neighbour) {
        neighbours.add(neighbour);
    }

    public Boolean canGrowBody() {
        return canGrowBody;
    }

    public Boolean canGrowThread() {
        return canGrowThread;
    }

    public List<Insect> getInsects() {
        return insects;
    }

    public List<FungusThread> getThreads() {
        return threads;
    }


    public List<Tekton> getNeighbours() {
        return neighbours;
    }

    // ! Not implemented yet
    
    public void breakTekton() {
        for (Insect insect : insects) {
            insect.move(neighbours.getFirst().getThreads().getFirst());
        }
        for (FungusThread ft: threads) {
            ft.destroy();
        }
        for (Spore spore : spores) {
            spore.absorbed();
        }
        body.destroy();
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

    
}
