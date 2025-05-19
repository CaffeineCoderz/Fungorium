package sporeTypes;

import insect.Insect;
import tektonTypes.Tekton;
import utils.*;

/**
 * Spore class. This represents a spore, which has been dispersed to the tekton. This spore can be consumed by insects
 */
public class Spore {
    private Integer nutritionValue;
    private Tekton myTekton;
    /**
     * Default Constructor
     */
    public Spore(){
        nutritionValue = 10; // ! Ez nem fix. Majd döntsük el
        myTekton = null;
    }
    /**
     * Constructor
     * @param t The tekton to which the spore has put
     * @param nutval The nutrition value 
     */
    public Spore(Tekton t, Integer nutval){
        myTekton = t;
        nutritionValue = nutval;
    }

    /**
     * It is called, when the a spore is consumed
     * @param insect The insect, who consumes the spore
     */
    public void consume(Insect insect){ // ? Itt történjen a pont kiosztás?
        absorbed();
    }

    /**
     * Sets tekton's value to myTekton.
     * @param tekton 
     */
    public void setTekton(Tekton tekton) {
        myTekton = tekton;
    }
    /**
     * Retrieves the Tekton object associated with this Spore.
     *
     * @return the Tekton object where this Spore is located, or null if not set.
     */
    public Tekton getTekton(){
        return myTekton;
    }

    //Ez a Spore példány pusztulását segíti. Leginkább azért kell, mivel tekton törésnél és elfogyasztásnál is ugyanazok a folyamatok mennek végbe.
    /**
     * It is used whenever a spore has destroyed, because it is consumed or the tekton it is staying on breaks.
     * Removes itself from the tekton it is staying on. 
     */
    public void absorbed(){ 
        myTekton.removeSpore(this);
        myTekton = null;
    }
    /**
     * Returns how much nutrion value the spore has
     * 
     * @return the spore's nutrition value
     */
    public Integer getNutValue(){
        return nutritionValue;
    }

    /**
     * Sets the nutrition value of the spore.
     * 
     * @param nutval the new nutrition value to be set.
     */
    public void setNutValue(Integer nutval){
        nutritionValue = nutval;
    }
}
