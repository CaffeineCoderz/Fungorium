package fungus;

import java.util.ArrayList;
import java.util.List;
import tektonTypes.Tekton;

//! NEM TELJES IMPLEMENTÁCIÓ MÉG
public class FungusThread {
    private Integer lifeSpan;
    private Boolean bridge;
    private Boolean isDying;
    private List<Tekton> tektons;

    public FungusThread(Integer lifeSpan, Boolean bridge) {
        this.lifeSpan = lifeSpan;
        this.bridge = bridge;
        this.isDying = false;
        this.tektons = new ArrayList<>();
    }

    public Boolean isBridge() {
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

    public void setBridge(Boolean bridge) {
        this.bridge = bridge;
    }

    public Boolean setIsDying() {
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