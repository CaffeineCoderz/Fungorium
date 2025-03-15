package insect;

import interfaces.iControl;
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

    // iControl interface
    public void override addScore(){
        // ToDo
    }

    public void override decreaseScore(){
        // ToDo
    }

    public void override timeElapsed(int Round){
        // Idő mulása
    }


}
