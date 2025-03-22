package sporeTypes;

import insect.Insect;

public class FastSpore extends Spore{
    
    public void consumeSpore(Insect insect){
        insect.fast();
        absorbed();
    }
}
