package insect;

import fungus.FungusThread;
import interfaces.iControl;
import sporeTypes.Spore;
import tektonTypes.Tekton;

enum InsectEffects{
    NORMAL, STUN, NO_CUT, FAST, SLOW
}

public class Insect implements iControl{
    private Integer movingEffectTimer;
    private Integer abilityEffectTimer;
    private Boolean canCut;
    private InsectEffects effect;
    private Boolean onDecreasing;
    private Integer score;
    private Tekton recentTekton;
    private FungusThread thread;

    public Insect() {
        this.movingEffectTimer = 0;
        this.abilityEffectTimer = 0;
        this.canCut = true;
        this.onDecreasing = false;
        this.score = 0;
        this.recentTekton = null;
        this.effect = InsectEffects.NORMAL;
    }

    /*public void countdown(){                // ? Kell ide egyáltalán ?
        // ToDo
    }*/

    public void stun(){
        effect = InsectEffects.STUN;
    }

    public void fast(){
        if(effect == InsectEffects.SLOW){
            effect = InsectEffects.NORMAL;
        }
        else
            effect = InsectEffects.FAST;
    }

    public void slow(){
        if(effect == InsectEffects.FAST){
            effect = InsectEffects.NORMAL;
        }
        else
            effect = InsectEffects.SLOW;
    }

    public void disableCut(){
        effect = InsectEffects.NO_CUT;
    }

    public void setDecrease(Boolean b){
        this.onDecreasing = b;
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
        for(Insect i: recentTekton.getInsects()){
            if (true == onDecreasing){
                i.decreaseScore(1);
            }
            if(i.effect == InsectEffects.FAST){
                i.movingEffectTimer = 3;
            }
            if(i.effect == InsectEffects.SLOW){
                i.movingEffectTimer = 3;
            }
            if(i.effect == InsectEffects.NO_CUT){
                i.abilityEffectTimer = 3;
            }
            if(i.movingEffectTimer > 0){
                i.movingEffectTimer--;
            }
            if(i.abilityEffectTimer > 0){
                i.abilityEffectTimer--;
            }
            if(i.movingEffectTimer == 0){
                i.effect = InsectEffects.NORMAL;
            }
            if(i.abilityEffectTimer == 0){
                i.effect = InsectEffects.NORMAL;
            }
        }
    }
}
