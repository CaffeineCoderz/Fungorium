package sporeTypes;

import insect.Insect;

public class StunSpore extends Spore{
    
    public void consumeSpore(Insect insect){
        insect.stun();
        absorbed();
    }
}
