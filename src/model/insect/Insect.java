package insect;

import fungus.FungusThread;
import interfaces.iControl;
import sporeTypes.Spore;
import tektonTypes.Tekton;
import utils.*;

import insect.InsectEffects;

// ! - Az elrágott fonalak nem pusztulnak el azonnal, hanem csak egy kis idő elteltével (ez fonaltípustól függő idő). 
// ! A fonalak képesek megenni a tektonjukon található bénult rovarokat. Ilyenkor a rovar elpusztul, a fonal pedig gombatestet növeszthet.

public class Insect implements iControl {
    private Integer movingEffectTimer;
    private Integer abilityEffectTimer;
    private Boolean canCut;
    private InsectEffects effect;
    private Boolean onDecreasing;
    private Integer score;
    private Tekton recentTekton;
    private FungusThread thread;

    private Logger log = Logger.getLogger("InsectLogger");

    public Insect() {
        this.movingEffectTimer = 0;
        this.abilityEffectTimer = 0;
        this.canCut = true;
        this.onDecreasing = false;
        this.score = 0;
        this.recentTekton = null;
        this.effect = InsectEffects.NORMAL;
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
            log.askQ("Insect can cut threads", false);
            log.stepIn("ft.destroy()");
            ft.destroy();
            log.stepOut("ft.destroy()", null);
        } else
            log.askQ("Insect can't cut threads", false);

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
            log.askQ("Insect is not stunned", false);
            if (ft.isBridge()) {
                log.askQ("Thread is a bridge", false);
                log.stepIn("recentTekton.removeInsect(this)");
                recentTekton.removeInsect(this);
                log.stepOut("recentTekton.removeInsect(this)", null);
                log.stepIn("setRecentTekton(ft.getTekton())");
                setRecentTekton(ft.getTekton());
                log.stepOut("setRecentTekton(ft.getTekton())", ft.getTekton());
                log.stepIn("setThread(ft)");
                setThread(ft);
                log.stepOut("setThread(ft)", null);
                recentTekton = null;
            } else {
                log.askQ("Thread is not a bridge", false);
                log.stepIn("setThread(ft)");
                setThread(ft);
                log.stepOut("setThread(ft);", null);

            }

        } else
            log.askQ("Insect is not stunned", false);
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
        log.stepIn("addScore(s.getNutValue())");
        addScore(s.getNutValue());
        log.stepOut("addScore(s.getNutValue())", null);
        log.stepIn("s.consume(this)");
        s.consume(this);
        log.stepOut("s.consume(this)", null);
    }

    // iControl interface

    /**
     * Increases the score of this insect by the specified amount.
     *
     * @param x the amount by which the score is to be increased.
     */
    @Override
    public void addScore(Integer x) {
        score += x;
    }

    /**
     * Decreases the score of this insect by the specified amount.
     *
     * @param x the amount by which the score is to be decreased.
     */
    @Override
    public void decreaseScore(Integer x) {
        score -= x;
    }

    /**
     * Decreases the score of all insects on the same Tekton by 1 if the Tekton is a
     * decreasing one.
     * Also handles the effects of the insect, such as FAST, SLOW, and NO_CUT.
     * The effects are timed, and when the time is up, the effect is removed.
     * 
     * @param Round the current round number.
     */
    @Override
    public void timeElapsed(Integer Round) {
        log.askQ("Decrease timers", false);
        if (onDecreasing) {
            log.stepIn("this.decreaseScore(1)");
            this.decreaseScore(1);
            log.stepOut("this.decreaseScore(1)", Round);
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
}
