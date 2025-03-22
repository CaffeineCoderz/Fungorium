package sporeTypes;

import insect.Insect;
import tektonTypes.Tekton;

public class Spore {
    private Integer nutritionValue;
    private Tekton myTekton;

    public Spore(){
        nutritionValue = 10;
        myTekton = null;
    }
    public Spore(Tekton t,Integer nutval){
        myTekton = t;
        nutritionValue = nutval;
    }

    public void consume(Insect insect) {
        insect.consumeSpore(this);
        absorbed();
    }

    public void setTekton(Tekton tekton) {
        myTekton = tekton;
    }

    //Ez a Spore példány pusztulását segíti. Leginkább azért kell, mivel tekton törésnél és elfogyasztásnál is ugyanazok a folyamatok mennek végbe.
    public void absorbed() {
        myTekton.removeSpore(this);
        myTekton = null;
        
    }
    public Integer getNutValue(){
        return nutritionValue;
    }
}
