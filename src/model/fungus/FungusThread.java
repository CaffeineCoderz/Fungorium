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
    private FungusSpecies species;

    public FungusThread(Integer lifeSpan, Boolean bridge) {
        this.lifeSpan = lifeSpan;
        this.bridge = bridge;
        this.isDying = false;
        this.species = null;
        this.tektons = new ArrayList<>();
    }

    public Boolean isBridge() {
        return bridge;
    }
    public FungusSpecies getSpecies(){
        return species;
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

    public void setIsDying(Boolean isDying) {
        this.isDying = isDying;
    }

    public Boolean getIsDying() {
        return isDying;
    }

    public Integer getLifeSpan() {
        return lifeSpan;
    }

    public Tekton getTekton() {
        // ! implementáció
        return null;
    }

    public void destroy() {
        for (Tekton tekton : tektons) {
            tekton.removeThread(this);
        }
    }

}