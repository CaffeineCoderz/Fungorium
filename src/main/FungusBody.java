import java.util.ArrayList;
import java.util.List;

//! NEM TELJES IMPLEMENTÁCIÓ MÉG
public class FungusBody {
    private Integer sporeCount;
    private Integer sporulateLeft;
    private List<FungusThread> threads;
    private Tekton tekton;

    public FungusBody(Integer sporeCount, Integer sporulateLeft) {
        this.sporeCount = sporeCount;
        this.sporulateLeft = sporulateLeft;
        this.threads = new ArrayList<>();
        this.tekton = null;
    }

    public void addThread(FungusThread thread) {
        threads.add(thread);
    }

    public void removeThread(FungusThread thread) {
        threads.remove(thread);
    }

    public void setTekton(Tekton tekton) {
        this.tekton = tekton;
    }

    public Tekton getTekton() {
        return tekton;
    }

    void sporulate() {
        // implementáció
    }

    Boolean timeToDie() {
        // implementáció
        return false;
    }

    void produceSpore() {
        // implementáció
    }

    Boolean isThereEnough() {
        // implementáció
        return false;
    }

    void destroy() {
        // implementáció
    }
}
