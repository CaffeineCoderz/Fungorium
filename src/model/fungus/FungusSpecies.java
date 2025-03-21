package fungus;
import java.util.ArrayList;
import java.util.List;
import tektonTypes.Tekton;

//! NEM TELJES IMPLEMENTÁCIÓ MÉG
public class FungusSpecies {
    private int score;
    private List<FungusBody> bodies;
    private List<FungusThread> threads;

    public FungusSpecies(int score) {
        this.score = score;
        this.bodies = new ArrayList<>();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

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
}