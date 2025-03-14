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
        // Decrease tekton miatt kell implementálni
    }

    public void override decreaseScore(){
        // Decrease tekton miatt kell implementálni
    }

    public void override timeElapsed(int Round){
        // Idő mulása
    }


}
