package fungus;

import java.util.ArrayList;
import java.util.List;

import TektonTypes.Tekton;

//! NEM TELJES IMPLEMENTÁCIÓ MÉG
public class FungusThread {
    private int lifeSpan;
    private boolean bridge;
    private boolean isDying;
    // private List<Tekton> tektons;

    public FungusThread(int lifeSpan, boolean bridge) {
        this.lifeSpan = lifeSpan;
        this.bridge = bridge;
        this.isDying = false;
        // this.tektons = new ArrayList<>();
    }

    public boolean isBridge() {
        return bridge;
    }

    public void decreaseLife() {
        lifeSpan--;
        if (lifeSpan <= 0) {
            isDying = true;
        }
    }

    public void addTekton(Tekton tekton) {
        tektons.add(tekton);
    }

    public void setBridge(boolean bridge) {
        this.bridge = bridge;
    }

    public boolean isDying() {
        return isDying;
    }

    public Tekton getTekton() {
        // ! implementáció
        return null;
    }

    public void growBody() {
        // ! Implementáció
    }

    public void destroy() {
        // ! Implementáció 
    }

}