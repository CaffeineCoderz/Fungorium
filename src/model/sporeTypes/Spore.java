package sporeTypes;

import insect.Insect;
import tektonTypes.Tekton;

/**
 * Spore osztály. Ez reprezentál egy adott tektonra kiszóródott spórát, amelyet egy rovar elfogyaszthat, 
 * egy gombász felhasználhat hogy gombatestet növesszen. 
 */
public class Spore {
    private Integer nutritionValue;
    private Tekton myTekton;

    /**
     * Default Konstruktor
     */
    public Spore(){
        nutritionValue = 10; // ! Ez nem fix. Majd döntsük el
        myTekton = null;
    }
    /**
     * Konstruktor
     * @param t Az a tekton, amire kiszóródott a spóra.
     * @param nutval Az az érték, ami a spóra tápanyagértékét tartalmazza
     */
    public Spore(Tekton t, Integer nutval){
        myTekton = t;
        nutritionValue = nutval;
    }

    /**
     * Akkor hívódik meg, amikor a spórát egy (paraméterben megadott) rovar elfogyasztja. A rovarnak kiosztja a saját effektjét.
     * @param insect Az a rovar, ami el akarja fogyasztani
     */
    public void consume(Insect insect){
        absorbed();
    }

    /**
     * Beállítja a paraméterben megadott tektont a 'myTekton'-nak
     * @param tekton Az a tekton, amire a spóra kiszórva lett
     */
    public void setTekton(Tekton tekton) {
        myTekton = tekton;
    }

    //Ez a Spore példány pusztulását segíti. Leginkább azért kell, mivel tekton törésnél és elfogyasztásnál is ugyanazok a folyamatok mennek végbe.
    /**
     * Elsősorban akkor használatos, ha spóra elfogyasztódik vagy a tekton eltörik.
     * Eltünteti a tektonjára a rá történő hivatkozásokat. 
     */
    public void absorbed() {
        myTekton.removeSpore(this);
        myTekton = null;
        
    }
    /**
     * Lekérdezi a spóra tápanyag értékét
     * @return A spóra tápanyagértéke
     */
    public Integer getNutValue(){
        return nutritionValue;
    }
}
