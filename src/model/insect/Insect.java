package insect;

import fungus.FungusThread;
import interfaces.iControl;
import sporeTypes.Spore;
import tektonTypes.Tekton;

enum InsectEffects{
    NORMAL,STUN,NO_CUT, FAST, SLOW
}

public class Insect implements iControl{
    private Integer movingEffectTimer;
    private Integer abilityEffectTimer;
    private Boolean canCut;
    private InsectEffects effect;
    private Boolean  Ondecreasing;
    private Integer score;
    private Tekton recentTekton;
    private FungusThread thread;

    public Insect() {
        this.movingEffectTimer = 0;
        this.abilityEffectTimer = 0;
        this.canCut = true;
        this.Ondecreasing = false;
        this.score = 0;
        this.recentTekton = null;
        this.effect = InsectEffects.NORMAL;
    }

    public void countdown(){
        // ToDo
    }

    public void stun(){
        effect = InsectEffects.STUN;
    }

    public void fast(){
        effect = InsectEffects.FAST;
    }

    public void slow(){
        effect = InsectEffects.SLOW;
    }

    public void disableCut(){
        effect = InsectEffects.NO_CUT;
    }

    public void setDecrease(Boolean b){
        this.Ondecreasing = b;
    }

    public void cut(FungusThread ft){
        //Legyen meg a képessége, hogy fonalat vágjon és Ne vágja maga alatt a fát.
        if(canCut == true && effect != InsectEffects.STUN && thread != ft){
            ft.destroy();
        }
    }

    public void move(FungusThread ft){
        if(effect != InsectEffects.STUN){
           if(ft.isBridge()) {
                recentTekton.removeInsect(this);
                ft.getTekton().addInsect(this);
                recentTekton = ft.getTekton();
           }
           thread = ft;
        }
    }

    public Boolean hasCutAbility(){
        return this.canCut;
    }

    public void consumeSpore(Spore s){
        addScore(s.getNutValue());
        s.consume(this);
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
