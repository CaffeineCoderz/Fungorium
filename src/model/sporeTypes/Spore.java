package sporeTypes;

import insect.Insect;
import tektonTypes.Tekton;

public class Spore {
    private Integer nutritionValue;
    private Tekton myTekton;

    public void consume(Insect insect) {
        // insect.consumeSpore(this);
    }

    public void setTekton(Tekton tekton) {
        myTekton = tekton;
    }

    // ToDo mit jelent itt az absorbed? Implementáció
    public void absorbed() {
        // implementáció
    }
}
