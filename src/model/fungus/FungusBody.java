package fungus;
import java.util.ArrayList;
import java.util.List;
import sporeTypes.Spore;
import tektonTypes.Tekton;
//! NEM TELJES IMPLEMENTÁCIÓ MÉG
public class FungusBody {
    private Integer sporeCount;
    private Integer sporulateLeft;
    private List<FungusThread> threads;
    private Tekton tekton;
    private FungusSpecies species;

    public FungusBody(Integer sporeCount, Integer sporulateLeft) {
        this.sporeCount = sporeCount;
        this.sporulateLeft = sporulateLeft;
        this.threads = new ArrayList<>();
        this.tekton = null;
        this.species = null;
    }

    public FungusSpecies getSpecies(){
        return species;
    }

    public void addThread(FungusThread thread) {
        threads.add(thread);
    }

    public Boolean removeThread(FungusThread thread) {
        return threads.remove(thread);
    }

    public void setTekton(Tekton tekton) {
        this.tekton = tekton;
    }

    public Tekton getTekton() {
        return tekton;
    }
    public List<FungusThread> getThreads(){
        return threads;
    }

    // ! Not implemented yet
    public void sporulate() {
        // implementáció
        if(isThereEnough()){
            for(Tekton t : tekton.getNeighbours()){
                Spore tempSpore = new Spore();
                t.addSpore(tempSpore);
                tempSpore.setTekton(t);
                sporeCount--;
            }
        sporulateLeft--;
        }
    }

    public Boolean timeToDie() {
        // implementáció
        if(sporulateLeft <= 0){
            return true;
        }
        return false;
    }

    public void produceSpore() {
        // implementáció
        sporeCount++;
    }

    public Boolean isThereEnough() {
        // implementáció
        if(sporeCount > 0){
            return true;
        }
        return false;
    }
    public Integer getSporeCount(){
        return sporeCount;
    }

    
}
