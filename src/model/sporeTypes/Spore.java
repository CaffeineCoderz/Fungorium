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
    public Spore(Tekton t, Integer nutval){
        myTekton = t;
        nutritionValue = nutval;
    }

    /**
     * Destroys this Spore object and makes the given Insect object to consume it.
     * 
     * This method is called when an Insect object lands on the Tekton where this
     * Spore object is located. It triggers the absorption process of the spore.
     * 
     * @param insect the Insect object that consumes this Spore object.
     */
    public void consume(Insect insect){
        absorbed();
    }

    /**
     * Sets the Tekton where this Spore object is located.
     * 
     * @param tekton the Tekton where this Spore object is located.
     */
    public void setTekton(Tekton tekton) {
        myTekton = tekton;
    }

    //Ez a Spore példány pusztulását segíti. Leginkább azért kell, mivel tekton törésnél és elfogyasztásnál is ugyanazok a folyamatok mennek végbe.
    
    /**
     * Destroys this Spore object.
     * 
     * This method is called when the Tekton where this Spore object is located
     * is destroyed or when this Spore object is consumed by an Insect object.
     * It removes the object from the Tekton's list of Spores and sets the
     * reference of the Tekton to null.
     */
    public void absorbed() {
        myTekton.removeSpore(this);
        myTekton = null;
        
    }
    /**
     * Retrieves the nutritional value of this Spore object.
     * 
     * @return an Integer representing the nutritional value of this Spore object.
     */
    public Integer getNutValue(){
        return nutritionValue;
    }
}
