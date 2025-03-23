package fungus;
import interfaces.iControl;
import java.util.ArrayList;
import java.util.List;
import tektonTypes.Tekton;

//! NEM TELJES IMPLEMENTÁCIÓ MÉG
public class FungusSpecies implements iControl{
    private Integer score;
    private List<FungusBody> bodies;
    private List<FungusThread> threads;

    public FungusSpecies(Integer score) {
        this.score = score;
        this.bodies = new ArrayList<>();
    }

    /**
     * Retrieves the current score of this FungusSpecies.
     * 
     * @return the current score of this FungusSpecies.
     */
    public Integer getScore() {
        return score;
    }

    /**
     * Retrieves the list of FungusBody instances associated with this FungusSpecies.
     * 
     * @return a list of FungusBody objects.
     */
    public List<FungusBody> getBodies() {
        return bodies;
    }

    // ? Good implementation
    // így kéne törölni és hozzáadni vagy máshogy?
    
    /**
     * Adds a FungusBody instance to the list of bodies associated with this FungusSpecies.
     * 
     * @param body the FungusBody instance to add.
     */
    public void addBody(FungusBody body) {
        bodies.add(body);
    }

    /**
     * Removes a FungusBody instance from the list of bodies associated with this FungusSpecies.
     * 
     * @param body the FungusBody instance to remove.
     */
    public void deleteBody(FungusBody body) {
        bodies.remove(body);
    }

    // ! Not implemented yet
    
    /**
     * Adds a FungusThread instance to the list of threads associated with this FungusSpecies.
     * 
     * @param thread the FungusThread instance to add.
     */
    public void addThread(FungusThread thread) {
        // implementáció
        threads.add(thread);
    }

    /**
     * Removes a FungusThread instance from the list of threads associated with this FungusSpecies.
     * 
     * @param thread the FungusThread instance to remove.
     */
    public void deleteThread(FungusThread thread) {
        // implementáció
        threads.remove(thread);
    }

    //szekvencia módosítás kellhet
    
    /**
     * Adds a FungusThread instance to the list of threads associated with this FungusSpecies and to the given FungusBody, until the Tekton associated with the FungusBody cannot grow any more threads.
     * 
     * @param body the FungusBody instance to which the FungusThread will be added.
     * @param thread the FungusThread instance to add.
     */
    public void growThread(FungusBody body, FungusThread thread) {             // ? Kell ide valszeg FungusThread, FungusBody paraméter ?
        // implementáció
        while(body.getTekton().canGrowThread()){
            addThread(thread);
            thread.addTekton(body.getTekton());
            body.getTekton().addThread(thread);
            body.addThread(thread);
        }
    }
    
    // szekvencia módosítás kellhet
    
    /**
     * Grows a bridge-like FungusThread between two Tektons and associates it with a FungusBody.
     * 
     * This method adds the given FungusThread to the list of threads associated with this FungusSpecies,
     * and also associates the thread with two Tekton instances, marking it as a bridge.
     * The thread is added to both the FungusBody's Tekton and the second Tekton.
     * 
     * @param body the FungusBody instance to which the FungusThread is to be associated.
     * @param thread the FungusThread instance to be grown as a bridge.
     * @param tekton2 the second Tekton instance to which the FungusThread will be associated.
     */
    public void growBridge(FungusBody body, FungusThread thread, Tekton tekton2) { // ? Kell ide valszeg FungusThread, FungusBody paraméter ?
        // implementáció
        addThread(thread);
        thread.addTekton(body.getTekton());
        thread.addTekton(tekton2);
        thread.setBridge(true);
        body.getTekton().addThread(thread);
        tekton2.addThread(thread);
        body.addThread(thread);
    }

    /**
     * Grows a FungusBody from a Tekton associated with the given FungusThread, if the Tekton has enough spores.
     * 
     * This method adds a FungusBody instance to the Tekton associated with the given FungusThread, if the Tekton has enough spores. The Tekton is set to the FungusBody, and the FungusThread is added to the FungusBody.
     * If the Tekton has not enough spores, nothing is done.
     * If the FungusThread is a bridge, nothing is done.
     * 
     * @param thread the FungusThread instance to which the FungusBody is to be associated.
     */
    public void growBody(FungusThread thread) {
        // implementáció
        if (thread.isBridge()) {
            return;
        }
        Integer atleast = 2;
        boolean enoughSpore=thread.getTekton().isThereEnoughSpore(atleast);
        if (enoughSpore){
            FungusBody fb= new FungusBody(null, null);
            thread.getTekton().setBody(fb);
            for (Integer i =  0; i < atleast; i++) {
                thread.getTekton().removeSpore();
            }
            fb.setTekton(thread.getTekton());
            fb.addThread(thread);}
    }

    // iControl interface
        
    /**
     * Increases the score associated with this FungusSpecies by the given amount.
     * 
     * @param x the amount by which the score is to be increased.
     */
    @Override
    public void addScore(Integer x){
        score += x;
    }

    /**
     * Decreases the score associated with this FungusSpecies by the given amount.
     * 
     * @param x the amount by which the score is to be decreased.
     */
    @Override
    public void decreaseScore(Integer x){
        score -= x;
    }

    /**
     * Simulates the passage of time for the fungus species by iterating over
     * all associated FungusBody and FungusThread instances. Each FungusBody 
     * produces a spore, and if it reaches its end of life, it is destroyed. 
     * Each FungusThread with a positive lifespan that is marked as dying has 
     * its life decreased, and if its lifespan reaches zero, it is deleted.
     * 
     * @param Round the current time round or cycle in the simulation.
     */

    @Override
    public void timeElapsed(Integer Round){
        // ToDo
        for (FungusBody body : bodies) {
            body.produceSpore();
            if(body.timeToDie()){
                destroyBody(body);
            }
        }
        for (FungusThread thread : threads) {
            if(thread.getIsDying() && thread.getLifeSpan() > 0){
                thread.decreaseLife();
            }
            if(thread.getLifeSpan() <= 0){
                deleteThread(thread);
            }
        }
    }
    /**
     * Destroys a FungusThread by removing it from the list of associated
     * FungusThread instances, and then removing it from all associated
     * FungusBody instances. Finally, the FungusThread is destroyed.
     * 
     * @param ft the FungusThread instance to be destroyed.
     */
    public void destroyThread(FungusThread ft){
        deleteThread(ft);
        for (FungusBody body : bodies) {
            if (body.removeThread(ft) == true) {
                break;
            }
        }
        ft.destroy();
    }
    /**
     * Destroys a FungusBody by first destroying all associated FungusThread
     * instances, and then removing the FungusBody from the list of associated
     * FungusBody instances. Finally, the FungusBody is removed from the Tekton
     * associated with the FungusBody.
     * 
     * @param fb the FungusBody instance to be destroyed.
     */
    public void destroyBody(FungusBody fb){
        for (FungusThread ft : fb.getThreads()) {
            destroyThread(ft);
        }
        fb.getTekton().setBody(null);

    }
}