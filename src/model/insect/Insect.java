package insect;

import commands.CommandProcessor;
import fungus.FungusThread;
import interfaces.iControl;
import sporeTypes.MultiplyInsectSpore;
import sporeTypes.Spore;
import tektonTypes.DecreasingTekton;
import tektonTypes.Tekton;
import utils.*;

import insect.InsectEffects;

// ! - Az elrágott fonalak nem pusztulnak el azonnal, hanem csak egy kis idő elteltével (ez fonaltípustól függő idő). 
// ! A fonalak képesek megenni a tektonjukon található bénult rovarokat. Ilyenkor a rovar elpusztul, a fonal pedig gombatestet növeszthet.

public class Insect {
    private Integer movingEffectTimer;
    private Integer abilityEffectTimer;
    private Boolean canCut;
    private InsectEffects effect;
    private Boolean onDecreasing;

    private Tekton recentTekton;
    private FungusThread thread;
    private InsectSpecies myOwner;

    public Insect() {
        this.movingEffectTimer = 0;
        this.abilityEffectTimer = 0;
        this.canCut = true;
        this.onDecreasing = false;
        this.recentTekton = null;
        this.effect = InsectEffects.NORMAL;
    }

    public Insect(Insect i) {
        this.canCut = true;
        this.onDecreasing = i.onDecreasing;
        this.recentTekton = i.recentTekton;
        this.thread = i.thread;
        this.myOwner = i.myOwner;
    }

    public void setMyOwner(InsectSpecies my) {
        myOwner = my;
    }

    public InsectSpecies getMyOwner() {
        return myOwner;
    }

    /**
     * Retrieves the current effect applied to the insect.
     * 
     * @return the current InsectEffects enum representing the effect.
     */
    public InsectEffects gEffect() {
        return effect;
    }

    /**
     * Retrieves the current FungusThread object that the insect is on.
     * 
     * @return the FungusThread object that the insect is on.
     */
    public FungusThread getThread() {
        return thread;
    }

    /**
     * Retrieves the current effect timer for the insect's movement.
     * 
     * @return the current moving effect timer value.
     */
    public Integer getMovingEffectTimer() {
        return movingEffectTimer;
    }

    /**
     * Retrieves the current effect timer for the insect's ability.
     * 
     * @return the current ability effect timer value.
     */
    public Integer getAbilityEffectTimer() {
        return abilityEffectTimer;
    }

    /**
     * Sets the effect of the insect to NORMAL.
     * This method is used to reset the insect's effect to its default state.
     */
    public void normal() {
        effect = InsectEffects.NORMAL;
        movingEffectTimer = 0;
        abilityEffectTimer = 0;
    }

    /**
     * 
     * Sets the ability of the insect to cut threads.
     * 
     * @param b true if the insect can cut threads, false otherwise.
     */
    public void setCanCut(Boolean b) {
        canCut = b;
    }

    /**
     * Sets the effect timer for the insect's movement.
     * 
     * @param i the value to set the moving effect timer to.
     */
    public void setMovingEffectTimer(Integer i) {
        movingEffectTimer = i;
    }

    /**
     * Sets the effect timer for the insect's ability.
     * 
     * @param i the value to set the ability effect timer to.
     */
    public void setAbilityEffectTimer(Integer i) {
        abilityEffectTimer = i;
    }

    /**
     * Sets the effect of the insect to STUN.
     * This prevents the insect from performing actions like moving.
     */
    public void stun() {
        effect = InsectEffects.STUN;
        movingEffectTimer = 3;
    }

    /**
     * Sets the effect of the insect to FAST or normal.
     * If the insect already has the SLOW effect, this method sets the effect to
     * NORMAL.
     * Otherwise the effect is set to FAST.
     */
    public void fast() {
        if (effect == InsectEffects.SLOW) {
            effect = InsectEffects.NORMAL;
        } else {
            effect = InsectEffects.FAST;
            movingEffectTimer = 3;
        }

    }

    /**
     * Sets the effect of the insect to SLOW or normal.
     * If the insect already has the FAST effect, this method sets the effect to
     * NORMAL.
     * Otherwise the effect is set to SLOW.
     */
    public void slow() {
        if (effect == InsectEffects.FAST) {
            effect = InsectEffects.NORMAL;
        } else {
            effect = InsectEffects.SLOW;
            movingEffectTimer = 3;
        }

    }

    /**
     * Disables the insect's ability to cut fungus threads.
     * This method sets the effect of the insect to NO_CUT.
     */
    public void disableCut() {
        effect = InsectEffects.NO_CUT;
        abilityEffectTimer = 3;
    }

    /**
     * Sets the flag whether this insect is on a decreasing Tekton or not.
     * 
     * @param b true if this insect is on a decreasing Tekton, false otherwise.
     */
    public void setDecrease(Boolean b) {
        this.onDecreasing = b;
    }

    /**
     * Sets the Tekton object associated with this insect as its recent Tekton.
     * This method is used to keep track of the Tekton where this insect is
     * currently located.
     * 
     * @param t the Tekton object associated with this insect.
     */
    public void setRecentTekton(Tekton t) {
        recentTekton = t;
    }

    /**
     * Destroys a FungusThread object if the insect is allowed to cut threads and is
     * not stunned.
     * The method also checks if the given FungusThread object is not the same as
     * the one where the
     * insect is currently located. If the conditions are met, the given
     * FungusThread object is
     * destroyed.
     * 
     * @param ft the FungusThread object to be destroyed.
     */
    public void cut(FungusThread ft) {
        // Legyen meg a képessége, hogy fonalat vágjon és Ne vágja maga alatt a fát.

        if (canCut == true && effect != InsectEffects.STUN && effect != InsectEffects.NO_CUT && thread != ft) {
            if (ft.isBridge()) {
                ft.setIsDying(true);
                ft.setLifeSpan(2);
            } else {
                // ! Még nem végleges
                ft.setIsDying(true);
                ft.setLifeSpan(4);
            }
        } else
            System.out.println("Insect can't cut threads");

    }

    /**
     * Moves the insect to the given FungusThread object if the insect is not
     * stunned.
     * If the given FungusThread object is a bridge, the insect is moved to the
     * other
     * Tekton associated with the bridge.
     * 
     * @param ft the FungusThread object to be moved to.
     */
    public void move(FungusThread ft) {
        if (effect != InsectEffects.STUN) {
            if (ft.isBridge()) {
                recentTekton.removeInsect(this);

                ft.insectSetting(this);

                setThread(ft);

                recentTekton.addInsect(this);
            } else {
                setThread(ft);
                if (ft.getTektons().get(0) instanceof DecreasingTekton) {
                    onDecreasing = true;
                } else
                    onDecreasing = false;
                if (recentTekton != ft.getTektons()) {
                    recentTekton.removeInsect(this);
                    ft.getTekton().addInsect(this);
                }
                recentTekton = ft.getTekton();
            }

        } else
            System.out.println("A rovar bénítva van.");
    }

    /**
     * Returns true if the insect has the ability to cut threads, false otherwise.
     * 
     * @return true if the insect has the ability to cut threads, false otherwise.
     */
    public Boolean hasCutAbility() {
        return this.canCut;
    }

    /**
     * Consumes a spore and increases the insect's score by the spore's nutritional
     * value.
     * 
     * This method first adds the nutritional value of the given spore to the
     * insect's score.
     * Then, it consumes the spore, which may have additional effects on the insect.
     * 
     * @param s the Spore object to be consumed by the insect.
     */
    public void consumeSpore(Spore s) {
        addScore(s.getNutValue());
        s.consume(this);
    }

    public Insect consumeMultiplySpore(Spore s) {
        MultiplyInsectSpore m = (MultiplyInsectSpore) s;
        addScore(m.getNutValue());
        return m.consumeMultiply(this);
    }

    // iControl interface

    /**
     * Increases the score of this insect by the specified amount.
     *
     * @param x the amount by which the score is to be increased.
     */
    public void addScore(Integer x) {
        myOwner.addScore(x);
    }

    /**
     * Decreases the score of this insect by the specified amount.
     *
     * @param x the amount by which the score is to be decreased.
     */
    public void decreaseScore(Integer x) {
        myOwner.decreaseScore(x);
    }

    /**
     * Decreases the score of all insects on the same Tekton by 1 if the Tekton is a
     * decreasing one.
     * Also handles the effects of the insect, such as FAST, SLOW, and NO_CUT.
     * The effects are timed, and when the time is up, the effect is removed.
     * 
     * @param Round the current round number.
     */
    public void timeElapsed() {
        if (onDecreasing) {
            this.decreaseScore(1);
        }
        if (movingEffectTimer > 0) {
            movingEffectTimer--;
        }
        if (abilityEffectTimer > 0) {
            abilityEffectTimer--;
        }
        if (movingEffectTimer == 0 && abilityEffectTimer == 0) {
            effect = InsectEffects.NORMAL;
        }
    }

    /**
     * Sets the FungusThread object that this insect is on.
     * 
     * @param t the FungusThread object that this insect is on.
     */
    public void setThread(FungusThread t) {
        thread = t;
    }

    /**
     * Returns the Tekton object that the insect was on in the previous turn.
     * This is useful for determining which Tekton the insect moved from.
     * 
     * @return the Tekton object that the insect was on in the previous turn.
     */
    public Tekton getRecent() {
        return recentTekton;
    }

    /**
     * The insect duplicates itself. It is called whenever the Insect eats a
     * MultiplyInsectSpore
     */
    // public void duplicate(){
    // Insect doppelGanger = new Insect(this);
    // myOwner.addInsect(doppelGanger);
    // }

    /**
     * The insect duplicates itself. It is called whenever the Insect eats a
     * MultiplyInsectSpore
     */
    public Insect duplicate() {
        Insect doppelGanger = new Insect(this);
        myOwner.addInsect(doppelGanger);
        return doppelGanger;
    }

    /**
     * The insect dies. It is called when the Tekton breaks and the insect is
     * present on it.
     */

    // ! Ha mégis tároljuk majd a fonalakon a rovarokat akkor függvény kell jelenleg
    // ennyi
    public void deadInsect() {
        myOwner.removeInsect(this);
        recentTekton.removeInsect(this);
        recentTekton = null;
        thread = null;
    }

    public void deadInsect(CommandProcessor cmdproc) {
        myOwner.removeInsect(this);
        recentTekton.removeInsect(this);
        recentTekton = null;
        thread = null;
        String objKey = cmdproc.findByObject(this);
        if (objKey != null) {
            cmdproc.getCreatedObjects().remove(objKey);
        } else {
            System.out.println(
                    "Hiba: Az Insect objektum nem található a CommandProcessor által kezelt objektumok között. Deadinsect()");
        }
    }
}
