package fungus;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.ListView;

import tektonTypes.Tekton;

//! NEM TELJES IMPLEMENTÁCIÓ MÉG
public class FungusThread {
    private int lifeSpan;
    private boolean bridge;
    private boolean isDying;
    private List<Tekton> tektons;

    public FungusThread(int lifeSpan, boolean bridge) {
        this.lifeSpan = lifeSpan;
        this.bridge = bridge;
        this.isDying = false;
        this.tektons = new ArrayList<>();
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