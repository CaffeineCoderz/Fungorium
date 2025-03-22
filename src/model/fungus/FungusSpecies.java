package fungus;
import interfaces.iControl;
import java.util.ArrayList;
import java.util.List;
import tektonTypes.Tekton;

//! NEM TELJES IMPLEMENTÁCIÓ MÉG
public class FungusSpecies implements iControl{
    private Integer score;
    private List<FungusBody> bodies;
    private List<FungusThread> threads;

    public FungusSpecies(Integer score) {
        this.score = score;
        this.bodies = new ArrayList<>();
    }

    public Integer getScore() {
        return score;
    }

    /*public void setScore(Integer score) {               // ? Biztos hogy kell ez a setter? nem jobb lenne a score-t növelni vagy csökkenteni simán interface?
        this.score = score;
    }*/

    public List<FungusBody> getBodies() {
        return bodies;
    }

    // ? Good implementation
    // így kéne törölni és hozzáadni vagy máshogy?
    public void addBody(FungusBody body) {
        bodies.add(body);
    }

    public void deleteBody(FungusBody body) {
        bodies.remove(body);
    }

    // ! Not implemented yet
    public void addThread(FungusThread thread) {
        // implementáció
    }

    public void deleteThread(FungusThread thread) {
        // implementáció
    }

    public void growThread(Tekton tekton) {
        // implementáció
    }

    public void growBridge(Tekton tekton1, Tekton tekton2) {
        // implementáció
    }

    // iControl interface
    @Override
    public void addScore(Integer x){
        score += x;
    }

    @Override
    public void decreaseScore(Integer x){
        score -= x;
    }

    @Override
    public void timeElapsed(Integer Round){
        // ToDo
    }
}