package sporeTypes;

import insect.Insect;
import sporeTypes.Spore;
import utils.*;

public class MultiplyInsectSpore extends Spore{
    // Létezik olyan spóra, amelyik az őt megevő rovart osztódásra készteti.
    // Ilyenkor keletkezik még egy, az előzőtől függetlenül élő rovar, akinek a
    // rovarásza megegyezik az eredeti rovar rovarászával.
    
    public MultiplyInsectSpore(){
    }
    /**
     * Consumes this spore and the given insect duplicates.
     * @param insect the insect which consumes this spore.
     */
    @Override
    public void consume(Insect insect){
        insect.duplicate();
        absorbed();
    }

    public Insect consumeMultiply(Insect insect){
        Insect newInsect = insect.duplicate();
        absorbed();
        return newInsect;
    }
}
