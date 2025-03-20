package insect;

import fungus.FungusThread;
import interfaces.iControl;
import sporeTypes.Spore;
import tektonTypes.Tekton;


public class Insect implements iControl{
    private Integer movingEffectTimer;
    private Integer abilityEffectTimer;
    private String effect;
    private Boolean canCut;
    private Boolean decreasing;
    private Integer score;
    private Tekton recentTekton;

    public Insect() {
        this.movingEffectTimer = 0;
        this.abilityEffectTimer = 0;
        this.effect = "";
        this.canCut = true;
        this.decreasing = false;
        this.score = 0;
        this.recentTekton = null;
    }

    public void countdown(){
        // ToDo
    }

    public void stun(){
        // ToDo
    }

    public void fast(){
        // ToDo
    }

    public void slow(){
        // ToDo
    }

    public void disableCut(){
        // ToDo
    }

    public void setDecrease(Boolean b){
        this.decreasing = b;
    }

    public void cut(FungusThread ft){
        // ToDo
    }

    public void move(FungusThread ft){
        // ToDo
    }

    public Boolean hasCutAbility(){
        return this.canCut;
    }

    public void consumeSpore(Spore s){
        // ToDo
    }

    // iControl interface
    @Override
    public void addScore(){
        // ToDo
    }

    @Override
    public void decreaseScore(){
        // ToDo
    }

    @Override
    public void  timeElapsed(int Round){
        // ToDo
    }
}
