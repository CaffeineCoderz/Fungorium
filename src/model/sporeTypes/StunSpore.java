package sporeTypes;

import insect.Insect;

public class StunSpore extends Spore{
    
    @Override
    public void consume(Insect insect){
        insect.stun();
        absorbed();
    }
}
