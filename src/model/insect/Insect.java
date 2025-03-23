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

    /**
     * Sets the effect of the insect to STUN.
     * This prevents the insect from performing actions like moving.
     */
    public void stun(){
        effect = InsectEffects.STUN;
    }

    /**
     * Sets the effect of the insect to FAST or normal.
     * If the insect already has the SLOW effect, this method sets the effect to NORMAL.
     * Otherwise the effect is set to FAST.
     */
    public void fast(){
        if(effect == InsectEffects.SLOW){
            effect = InsectEffects.NORMAL;
        }
        else
            effect = InsectEffects.FAST;
    }

    /**
     * Sets the effect of the insect to SLOW or normal.
     * If the insect already has the FAST effect, this method sets the effect to NORMAL.
     * Otherwise the effect is set to SLOW.
     */
    public void slow(){
        if(effect == InsectEffects.FAST){
            effect = InsectEffects.NORMAL;
        }
        else
            effect = InsectEffects.SLOW;
    }

    /**
     * Disables the insect's ability to cut fungus threads.
     * This method sets the effect of the insect to NO_CUT.
     */
    public void disableCut(){
        effect = InsectEffects.NO_CUT;
    }

    /**
     * Sets the flag whether this insect is on a decreasing Tekton or not.
     * @param b true if this insect is on a decreasing Tekton, false otherwise.
     */
    public void setDecrease(Boolean b){
        this.onDecreasing = b;
    }

    /**
     * Sets the Tekton object associated with this insect as its recent Tekton.
     * This method is used to keep track of the Tekton where this insect is currently located.
     * @param t the Tekton object associated with this insect.
     */
    public void setRecentTekton(Tekton t){
        recentTekton = t;
    }

    /**
     * Destroys a FungusThread object if the insect is allowed to cut threads and is not stunned.
     * The method also checks if the given FungusThread object is not the same as the one where the
     * insect is currently located. If the conditions are met, the given FungusThread object is
     * destroyed.
     * @param ft the FungusThread object to be destroyed.
     */
    public void cut(FungusThread ft){
        //Legyen meg a képessége, hogy fonalat vágjon és Ne vágja maga alatt a fát.
        if(canCut == true && effect != InsectEffects.STUN && thread != ft){
            ft.destroy();
        }
    }

    /**
     * Moves the insect to the given FungusThread object if the insect is not stunned.
     * If the given FungusThread object is a bridge, the insect is moved to the other
     * Tekton associated with the bridge.
     * @param ft the FungusThread object to be moved to.
     */
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

    /**
     * Returns true if the insect has the ability to cut threads, false otherwise.
     * @return true if the insect has the ability to cut threads, false otherwise.
     */
    public Boolean hasCutAbility(){
        return this.canCut;
    }

    /**
     * Consumes a spore and increases the insect's score by the spore's nutritional value.
     * 
     * This method first adds the nutritional value of the given spore to the insect's score.
     * Then, it consumes the spore, which may have additional effects on the insect.
     * 
     * @param s the Spore object to be consumed by the insect.
     */
    public void consumeSpore(Spore s){
        addScore(s.getNutValue());
        s.consume(this);
    }

    // iControl interface
    
    /**
     * Increases the score of this insect by the specified amount.
     *
     * @param x the amount by which the score is to be increased.
     */
    @Override
    public void addScore(Integer x){
        score += x;
    }

    /**
     * Decreases the score of this insect by the specified amount.
     *
     * @param x the amount by which the score is to be decreased.
     */
    @Override
    public void decreaseScore(Integer x){
        score -= x;
    }

    /**
     * Decreases the score of all insects on the same Tekton by 1 if the Tekton is a decreasing one.
     * Also handles the effects of the insect, such as FAST, SLOW, and NO_CUT.
     * The effects are timed, and when the time is up, the effect is removed.
     * @param Round the current round number.
     */
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
