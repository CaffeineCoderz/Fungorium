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
        // ! implement better
        /**if (bridge) {
            return;
        }
        Integer atleast = 3;
        if (tektons.getFirst().isThereEnoughSpore(atleast)){
            FungusBody fb= new FungusBody(null, null);
            tektons.getFirst().setBody(fb);
            for (Integer i =  0; i < atleast; i++) {
                tektons.getFirst().removeSpore();
            }
            fb.setTekton(tektons.getFirst());
            fb.addThread(this);
        }**/
    }

    public void destroy() {

    }

}