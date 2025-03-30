package fungus;

import interfaces.iControl;
import java.util.ArrayList;
import java.util.List;

import fungus.FungusThread;
import insect.*;
import tektonTypes.Tekton;
import utils.Logger;

public class FungusSpecies implements iControl {
    private Integer score;
    private List<FungusBody> bodies;
    private List<FungusThread> threads;
    private Mycologist myOwner;
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
     * Retrieves the list of FungusBody instances associated with this
     * FungusSpecies.
     * 
     * @return a list of FungusBody objects.
     */
    public List<FungusBody> getBodies() {
        return bodies;
    }

    /**
     * Adds a FungusBody instance to the list of bodies associated with this
     * FungusSpecies.
     * 
     * @param body the FungusBody instance to add.
     */
    public void addBody(FungusBody body) {
        log.stepIn("bodies.add(body)");
        log.stepOut("bodies.add(body)", bodies.add(body));
    }

    /**
     * Removes a FungusBody instance from the list of bodies associated with this
     * FungusSpecies.
     * 
     * @param body the FungusBody instance to remove.
     */
    public void deleteBody(FungusBody body) {
        log.stepIn("bodies.remove(body)");
        log.stepOut("bodies.remove(body)", bodies.remove(body));
    }

    /**
     * Adds a FungusThread instance to the list of threads associated with this
     * FungusSpecies.
     * 
     * @param thread the FungusThread instance to add.
     */
    public void addThread(FungusThread thread) {
        log.stepIn("threads.add(thread)");
        log.stepOut("threads.add(thread)", threads.add(thread));
    }

    /**
     * Removes a FungusThread instance from the list of threads associated with this
     * FungusSpecies.
     * 
     * @param thread the FungusThread instance to remove.
     */
    public void deleteThread(FungusThread thread) {
        log.stepIn("threads.remove(thread)");
        log.stepOut("threads.remove(thread)", threads.remove(thread));
    }

    /**
     * Attempts to add a new FungusThread instance to the target Tekton if it can
     * grow additional threads.
     * This method ensures that the new thread is properly linked to the existing
     * structure.
     * 
     * @param targetTekton The Tekton instance to which the new thread should be
     *                     added.
     * @param oThread      The existing FungusThread that will be linked to the new
     *                     thread.
     * @param nThread      The new FungusThread instance to be added.
     */
    public void growThread(Tekton targetTekton, FungusThread oThread) {
        log.askQ("Create new FungusThread: nThread", false);
        FungusThread nThread = new FungusThread(null, false);
        if (targetTekton.canGrowThread()){
            log.askQ("Can grow thread on tekton", false);
            log.stepIn("addThread(nThread)");
            addThread(nThread);
            log.stepOut("addThread(nThread)", null);

            log.stepIn("nThread.addTekton(targetTekton)");
            nThread.addTekton(targetTekton);
            log.stepOut("nThread.addTekton(targetTekton)", null);
            log.stepIn("nThread.setPrevThread(oThread)");
            nThread.setPrevThread(oThread);
            log.stepOut("nThread.setPrevThread(oThread)", null);
            // ! Be kell állítani hogy melyik testhez tartozik
            log.stepIn("nThread.setBody(oThread.getBody())");
            nThread.setBody(oThread.getBody());
            log.stepOut("nThread.setBody(oThread.getBody())", null);

            log.stepIn("targetTekton.addThread(nThread)");
            targetTekton.addThread(nThread);
            log.stepOut("targetTekton.addThread(nThread)", null);

            log.stepIn("oThread.setNextThread(nThread)");
            oThread.setNextThread(nThread);
            log.stepOut("oThread.setNextThread(nThread)", null);

            log.stepIn("oThread.getBody().addThread(nThread)");
            oThread.getBody().addThread(nThread);
            log.stepOut("oThread.getBody().addThread(nThread)", null);
            
        } else {
            log.askQ("tekton cant have new threads", false);
        }
    }

    /**
     * Grows a bridge-like FungusThread between two Tektons.
     * 
     * This method associates the thread with two Tekton instances, marking it as a
     * bridge.
     * The thread is added to both the FungusThread's Tekton and the second Tekton.
     * 
     * @param fromThread  the FungusThread instance to bridge is growing from.
     * @param toTekton the second Tekton instance to which the FungusThread will be
     *                associated.
     */
    public void growBridge(FungusThread fromThread,Tekton toTekton) {
        log.askQ("Create new FungusThread: nThread", false);
        FungusThread nThread = new FungusThread(null,true);
        log.stepIn("nThread.setPrevThread(fromThread)");
        nThread.setPrevThread(fromThread);
        log.stepOut("nThread.setPrevThread(fromThread)", null);
        
        log.stepIn("fromThread.setNextThread(nThread)");
        fromThread.setNextThread(nThread);
        log.stepOut("fromThread.setNextThread(nThread)", null);

        log.stepIn("addThread(nThread)");
        addThread(nThread);
        log.stepOut("addThread(nThread)", null);
        
        log.stepIn("nThread.addTekton(fromThread.getTekton())");
        nThread.addTekton(fromThread.getTekton(null));
        log.stepOut("nThread.addTekton(fromThread.getTekton())", null);

        log.stepIn("nThread.addTekton(toTekton)");
        nThread.addTekton(toTekton);
        log.stepOut("nThread.addTekton(toTekton)", null);

        log.stepIn("fromThread.getTekton().addThread(nThread)");
        fromThread.getTekton(null).addThread(nThread);
        log.stepOut("fromThread.getTekton().addThread(nThread)", null);

        log.stepIn("toTekton.addThread(nThread)");
        toTekton.addThread(nThread);
        log.stepOut("toTekton.addThread(nThread)", null);

        log.stepIn("fromThread.getBody().addThread(nThread)");
        fromThread.getBody().addThread(nThread);
        log.stepOut("fromThread.getBody().addThread(nThread)", null);

        log.stepIn("nThread.setBody(fromThread.getBody())");
        nThread.setBody(fromThread.getBody());
        log.stepOut("nThread.setBody(fromThread.getBody())", null);

        log.stepIn("oThread.getBody().addThread(nThread)");
        fromThread.getBody().addThread(nThread);
        log.stepOut("oThread.getBody().addThread(nThread)", null);
    }

    /**
     * Grows a FungusBody from a Tekton associated with the given FungusThread, if
     * the Tekton has enough spores.
     * 
     * This method adds a FungusBody instance to the Tekton associated with the
     * given FungusThread, if the Tekton has enough spores. The Tekton is set to the
     * FungusBody, and the FungusThread is added to the FungusBody.
     * If the Tekton has not enough spores, nothing is done.
     * If the FungusThread is a bridge, nothing is done.
     * 
     * @param thread the FungusThread instance to which the FungusBody is to be
     *               associated.
     */
    public void growBody(FungusThread thread) {
        if (thread.isBridge()) {
            log.askQ("thread is a bridge", false);
            return;
        }
        log.askQ("thread is not a bridge", false);
        Integer atleast = 2;
        boolean enoughSpore = thread.getTekton(null).isThereEnoughSpore(atleast);
        if (!thread.getTekton(null).canGrowBody()) {
            log.askQ("Tekton already contains a body", false);
            return;
        }
        if (enoughSpore) {
            log.askQ("There are enough spore on the tekton", false);
            log.askQ("Create new Body: fb", false);
            FungusBody fb = new FungusBody(null, null);
            log.stepIn("thread.getTekton().setBody(fb)");
            thread.getTekton(null).setBody(fb);
            log.stepOut("thread.getTekton().setBody(fb)", null);
            log.askQ("Start cycle", false);
            for (Integer i =  0; i < atleast; i++) {
                log.stepIn("thread.getTekton().getSpores().get(i).absorbed();");
                thread.getTekton(null).getSpores().get(i).absorbed();
                log.stepOut("thread.getTekton().getSpores().get(i).absorbed();", null);
            }
            log.askQ("End cycle", false);
            log.stepIn("fb.setTekton(thread.getTekton())");
            fb.setTekton(thread.getTekton(null));
            log.stepOut("fb.setTekton(thread.getTekton())", null);
            log.stepIn("fb.addThread(thread)");
            fb.addThread(thread);
            log.stepOut("fb.addThread(thread)", null);
        } else {
            log.askQ("Nincs elegendo spóra", false);
        }
    }

    // iControl interface

    /**
     * Increases the score associated with this FungusSpecies by the given amount.
     * 
     * @param x the amount by which the score is to be increased.
     */
    @Override
    public void addScore(Integer x) {
        score += x;
    }

    /**
     * Decreases the score associated with this FungusSpecies by the given amount.
     * 
     * @param x the amount by which the score is to be decreased.
     */
    @Override
    public void decreaseScore(Integer x) {
        score -= x;
    }

    /**
     * Simulates the passage of time for the fungus species by iterating over
     * all associated FungusBody and FungusThread instances. Each FungusBody
     * produces a spore, and if it reaches its end of life, it is destroyed.
     * Each FungusThread with a positive lifespan that is marked as dying has
     * its life decreased, and if its lifespan reaches zero, it is deleted.
     * 
     */
    @Override
    public void timeElapsed() {
        log.askQ("Start Cycle", false);
        for (FungusBody body : bodies) {
            log.stepIn("body.produceSpore()");
            body.produceSpore();
            log.stepOut("body.produceSpore()", null);

            if (body.timeToDie()) {
                log.askQ("The fungusbody must be destroyed, because no lifespan left", false);
                log.stepIn("destroyBody(body)");
                destroyBody(body);
                log.stepOut("destroyBody(body)", null);
            }
        }
        log.askQ("End Cycle", false);
        log.askQ("Start Cycle", false);
        for (FungusThread thread : threads) {
        
            log.stepIn("destroyThread(thread)");
            destroyThread(thread);
            log.stepOut("destroyThread(thread)", null);
        
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
    public void destroyThread(FungusThread ft) {
        if (!ft.getIsDying() && ft.getLifeSpan() != null) {
            log.stepIn("thread.decreaseLife()");
                ft.decreaseLife();
                log.stepOut("thread.decreaseLife()", null);
            return;
        }
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
        log.askQ("Going through all threads of FungusBody", false);

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

    /**
     * Consumes stunned insects on the given FungusThread's Tekton.
     * 
     * If the FungusThread is a bridge, the method returns immediately without
     * doing anything. Otherwise, it iterates through all insects on the Tekton
     * associated with the FungusThread. If any insect is stunned, the insect is
     * killed. If at least one insect is killed, an opportunity to grow a body on
     * the Tekton is provided. 
     * 
     * @param ft the FungusThread instance whose Tekton's insects are to be checked.
     */
    public void eatInsect(FungusThread ft){
        if (ft.isBridge()) {
            log.askQ("Thread was a bridge", false);
            return;
        }
        Boolean someoneDied = false;
        log.askQ("Going through all insects that is on the Thread's tekton", false);
        for (Insect insect : ft.getTekton(null).getInsects()) {
            if (insect.gEffect() == InsectEffects.STUN) {
                log.askQ("Insect is stunned", false);
                log.stepIn("insect.deadInsect()");
                insect.deadInsect();
                log.stepOut("insect.deadInsect()", null);
                someoneDied = true;
            }
        }
        log.askQ("End of Cycle", false);
        if (someoneDied) {
            log.askQ("You can grow a body to the tekton", false);
            if (log.askQ("Want to grow body?", true).equals("y")) {
                log.stepIn("growBody(ft)");
                growBody(ft);
                log.stepOut("growBody(ft)", null);
            }
        }else log.askQ("There is no stunned insect on tekton", false);
        
    }
}