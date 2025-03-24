package fungus;
import interfaces.iControl;
import java.util.ArrayList;
import java.util.List;
import tektonTypes.Tekton;
import utils.Logger;

//! NEM TELJES IMPLEMENTACIO MEG
public class FungusSpecies implements iControl{
    private Integer score;
    private List<FungusBody> bodies;
    private List<FungusThread> threads;
    private Logger log = Logger.getLogger("FungusSpeciesLogger");

    public FungusSpecies() {
        this.score = 0;
        this.bodies = new ArrayList<>();
        this.threads = new ArrayList<>();
    }

    public FungusSpecies(Integer score) {
        this.score = score;
        this.bodies = new ArrayList<>();
        this.threads = new ArrayList<>();
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
        log.stepIn("bodies.add(body)");
        log.stepOut("bodies.add(body)", bodies.add(body));
    }

    /**
     * Removes a FungusBody instance from the list of bodies associated with this FungusSpecies.
     * 
     * @param body the FungusBody instance to remove.
     */
    public void deleteBody(FungusBody body) {
        log.stepIn("bodies.remove(body)");
        log.stepOut("bodies.remove(body)", bodies.remove(body));
    }

    // ! Not implemented yet
    
    /**
     * Adds a FungusThread instance to the list of threads associated with this FungusSpecies.
     * 
     * @param thread the FungusThread instance to add.
     */
    public void addThread(FungusThread thread) {
        // implementáció
        log.stepIn("threads.add(thread)");
        log.stepOut("threads.add(thread)", threads.add(thread));
    }

    /**
     * Removes a FungusThread instance from the list of threads associated with this FungusSpecies.
     * 
     * @param thread the FungusThread instance to remove.
     */
    public void deleteThread(FungusThread thread) {
        // implementáció
        log.stepIn("threads.remove(thread)");
        log.stepOut("threads.remove(thread)", threads.remove(thread));
    }

    //szekvencia módosítás kellhet
    
    /**
     * Adds a FungusThread instance to the list of threads associated with this FungusSpecies and to the given FungusBody, until the Tekton associated with the FungusBody cannot grow any more threads.
     * 
     * @param body the FungusBody instance to which the FungusThread will be added.
     * @param thread the FungusThread instance to add.
     */
    public void growThread(Tekton targetTekton, FungusThread oThread, FungusThread nThread) {             // ? Kell ide valszeg FungusThread, FungusBody paraméter ?
        // implementáció
        
        if(targetTekton.canGrowThread()){
            log.askQ("Can grow thread on tekton", false);
            log.stepIn("addThread(nThread)");
            addThread(nThread);
            log.stepOut("addThread(nThread)", null);
            log.stepIn("nThread.addTekton(targetTekton)");
            nThread.addTekton(targetTekton);
            log.stepOut("nThread.addTekton(targetTekton)", null);
            log.stepIn("targetTekton.addThread(nThread)");
            targetTekton.addThread(nThread);
            log.stepOut("targetTekton.addThread(nThread)", null);
            log.stepIn("oThread.setNextThread(nThread)");
            oThread.setNextThread(nThread);      
            log.stepOut("oThread.setNextThread(nThread)", null);
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
        log.stepIn("addThread(thread)");
        addThread(thread);
        log.stepOut("addThread(thread)", null);
        
        log.stepIn("thread.addTekton(body.getTekton())");
        thread.addTekton(body.getTekton());
        log.stepOut("thread.addTekton(body.getTekton())", null);

        log.stepIn("thread.addTekton(tekton2)");
        thread.addTekton(tekton2);
        log.stepOut("thread.addTekton(tekton2)", null);
        
        log.stepIn("thread.setBridge(true)");
        thread.setBridge(true);
        log.stepOut("thread.setBridge(true)", null);
        
        log.stepIn("body.getTekton().addThread(thread)");
        body.getTekton().addThread(thread);
        log.stepOut("body.getTekton().addThread(thread)", null);
        
        log.stepIn("tekton2.addThread(thread)");
        tekton2.addThread(thread);
        log.stepOut("tekton2.addThread(thread)", null);
        
        log.stepIn("body.addThread(thread)");
        body.addThread(thread);
        log.stepOut("body.addThread(thread)", null);
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
            log.askQ("thread is a bridge", false);
            return;
        }
        log.askQ("thread is not a bridge", false);
        Integer atleast = 2;
        boolean enoughSpore=thread.getTekton().isThereEnoughSpore(atleast);
        if(!thread.getTekton().canGrowBody())
            return;
        if (enoughSpore){
            log.askQ("There are enough spore on the tekton", false);
            log.askQ("Create new Body: fb", false);
            FungusBody fb= new FungusBody(null, null);
            log.stepIn("thread.getTekton().setBody(fb)");
            thread.getTekton().setBody(fb);
            log.stepOut("thread.getTekton().setBody(fb)", null);
            log.askQ("Start cycle", false);
            for (Integer i =  0; i < atleast; i++) {
                log.stepIn("thread.getTekton().removeSpore()");
                thread.getTekton().removeSpore();
                log.stepOut("thread.getTekton().removeSpore()", null);
            }
            log.askQ("End cycle", false);
            log.stepIn("fb.setTekton(thread.getTekton())");
            fb.setTekton(thread.getTekton());
            log.stepOut("fb.setTekton(thread.getTekton())", null);
            log.stepIn("fb.addThread(thread)");
            fb.addThread(thread);
            log.stepOut("fb.addThread(thread)", null);
        }
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
        log.askQ("Start Cycle", false);
        for (FungusBody body : bodies) {
            log.stepIn("body.produceSpore()");
            body.produceSpore();
            log.stepOut("body.produceSpore()", null);
            
            if(body.timeToDie()){
                log.askQ("The fungusbody must be destroyed, because no lifespan left", false);
                log.stepIn("destroyBody(body)");
                destroyBody(body);
                log.stepOut("destroyBody(body)", null);
            }
        }
        log.askQ("End Cycle", false);
        log.askQ("Start Cycle", false);
        for (FungusThread thread : threads) {
            if(thread.getLifeSpan() > 0){
                log.askQ("Has remaining lifespan", false);
                log.stepIn("thread.decreaseLife()");
                thread.decreaseLife();
                log.stepOut("thread.decreaseLife()", null);
            }
            if(thread.getIsDying()){
                log.askQ("Thread is dying", false);
                log.stepIn("deleteThread(thread)");
                deleteThread(thread);
                log.stepOut("deleteThread(thread)", null);
            }
        }
        log.askQ("End Cycle", false);
    }
    /**
     * Destroys a FungusThread by removing it from the list of associated
     * FungusThread instances, and then removing it from all associated
     * FungusBody instances. Finally, the FungusThread is destroyed.
     * 
     * @param ft the FungusThread instance to be destroyed.
     */
    public void destroyThread(FungusThread ft){
        log.stepIn("deleteThread(ft)");
        deleteThread(ft);
        log.stepOut("deleteThread(ft)", null);
        log.askQ("Start cyle", false);
        boolean success;
        for (FungusBody body : bodies) {
            log.stepIn("body.removeThread(ft)");
            success = body.removeThread(ft);
            if (success) {
                log.askQ("Break", false);
                break;
            }
            log.stepOut("body.removeThread(ft)", success);
        }
        log.askQ("End cycle", false);
        log.stepIn("ft.destroy()");
        ft.destroy();
        log.stepOut("ft.destroy()", null);
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
        log.askQ("Start cycle", false);
        for (FungusThread ft : fb.getThreads()) {
            log.stepIn("destroyThread(ft);");
            destroyThread(ft);
            log.stepOut("destroyThread(ft);", null);
        }
        log.askQ("End cycle", false);
        log.stepIn("fb.getTekton().setBody(null)");
        fb.getTekton().setBody(null);
        log.stepOut("fb.getTekton().setBody(null)", null);
    }
}