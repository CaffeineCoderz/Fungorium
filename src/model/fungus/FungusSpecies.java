package fungus;

import interfaces.iControl;
import java.util.ArrayList;
import java.util.List;

import fungus.FungusThread;
import insect.*;
import tektonTypes.FeedThreadTekton;
import tektonTypes.OnlyThreadTekton;
import tektonTypes.Tekton;
import utils.Logger;

public class FungusSpecies implements iControl {
    // Game Logic
    Integer id;

    // Data
    private Integer score;
    private List<FungusBody> bodies;
    private List<FungusThread> threads;

    // private Mycologist myOwner; // ! ezentúl a species az owner
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

    /*
     * @return the ID of the player.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the ID of the player.
     * 
     * @param id the ID to set for the player.
     */
    public void setId(Integer id) {
        this.id = id;
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

    // ! Nincs a statikus diagramon
    /**
     * Retrieves the list of FungusThread instances associated with this
     * FungusSpecies.
     * 
     * @return a list of FungusThread objects.
     */
    public List<FungusThread> getThreads() {
        return threads;
    }

    /**
     * Adds a FungusBody instance to the list of bodies associated with this
     * FungusSpecies.
     * 
     * @param body the FungusBody instance to add.
     */
    public void addBody(FungusBody body) {
        bodies.add(body);
    }

    /**
     * Removes a FungusBody instance from the list of bodies associated with this
     * FungusSpecies.
     * 
     * @param body the FungusBody instance to remove.
     */
    public void deleteBody(FungusBody body) {
        bodies.remove(body);
    }

    /**
     * Adds a FungusThread instance to the list of threads associated with this
     * FungusSpecies.
     * 
     * @param thread the FungusThread instance to add.
     */
    public void addThread(FungusThread thread) {
        threads.add(thread);
    }

    /**
     * Removes a FungusThread instance from the list of threads associated with this
     * FungusSpecies.
     * 
     * @param thread the FungusThread instance to remove.
     */
    public void deleteThread(FungusThread thread) {
        threads.remove(thread);
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
    //! Szekvencián javítani ~ Diviki Mivel a growBridge-et bele mergeltem (hamarabb kellett volna erre rá jönni)
    public FungusThread growThread(Tekton targetTekton, FungusThread oThread) {
        if (targetTekton.canGrowThread()){
            FungusThread nThread = new FungusThread(null,false);
            //Fonal amiből növesztünk nem híd + a cél tekton nem egyezik meg a kiinduló fonal tektonjával -->
            // --> Ilyenkor bridge keletkezik, mivel két tektonnal definiáljuk a fonalat.
            if(!oThread.isBridge() && oThread.getTektons().get(0) != targetTekton){
                //? Csak akkor lehessen még hidat növeszteni, ha a kiinduló fonalnak nincs olyan híd szomszédja(prev és next), mivel ezen formában
                //? ha nem lenne ilyen kikötés, akkor az az eset megtörténhet,hogy:
                //? Hídból(híd1) növesztünk egy fonalat(th1) a híd belseje felé, ez még okés
                //? Th1 ből növesztünk egy új fonalat(híd2) egy másik (szomszédos)tektonra
                //? Ilyenkor a tekton belsejéből növesztünk hidat, amit nem kéne
                if (oThread.getPrev().isBridge()){
                    System.err.println("You can't grow from this thread a bridge to another tekton.");
                    return null;
                }

                //Ellenőrzés, hogy szomszédosak egymással ezen tektonok
                boolean areNeighbours = false;
                for (Tekton neighbour : targetTekton.getNeighbours()){
                    if (neighbour == oThread.getTektons().get(0)) {
                        areNeighbours = true;
                        break;
                    }
                }
                if (!areNeighbours) {
                    System.err.println("The two Tektons between which the thread would grow are not adjacent.");
                    return null;
                }
                
                nThread.setBridge(true);
                nThread.addTekton(oThread.getTektons().get(0));
                oThread.getTektons().get(0).addThread(nThread);
            }else if (oThread.isBridge() && oThread.getTektons().get(1) != targetTekton) { 
                //Ha a fonal amiből növesztünk híd és
                //Ha nem egyezik meg a cél tekton a kiinduló fonal, azon oldalán lévő tektonjával, amelyből még nem nőt fonál, 
                //akkor nem nőhet oda új fonal
                System.err.println("The target tekton did not match the tekton on the side of the starting thread(,what is a bridge,) from which no thread had yet grown.");
                return null;
            }
            addThread(nThread);
            nThread.addTekton(targetTekton);
            targetTekton.addThread(nThread);
            // ! Be kell állítani hogy melyik testhez tartozik
            nThread.setBody(oThread.getBody());
            nThread.setPrevThread(oThread);

            oThread.setNextThread(nThread);
            oThread.getBody().addThread(nThread);
            return nThread;
        } else {
            System.err.println("Tekton cant have new threads");
        }
        return null;
    }
    public FungusThread growThread(Tekton targetTekton, FungusBody body) {
        if (targetTekton.canGrowThread()){
            if(targetTekton != body.getTekton()){
                System.err.println("The targeted tekton is not the tekton on which the body is stationed!");
                return null;
            }
            FungusThread nThread = new FungusThread();
            addThread(nThread);
            nThread.addTekton(targetTekton);
            nThread.setPrevThread(null);
            // ! Be kell állítani hogy melyik testhez tartozik
            nThread.setBody(body);

            targetTekton.addThread(nThread);

            body.addThread(nThread);
            return nThread;
        } else {
            System.err.println("Tekton cant have new threads");
        }
        return null;
    }

    /**
     * Grows a bridge-like FungusThread between two Tektons.
     * 
     * This method associates the thread with two Tekton instances, marking it as a
     * bridge.
     * The thread is added to both the FungusThread's Tekton and the second Tekton.
     * 
     * @param fromThread the FungusThread instance to bridge is growing from.
     * @param toTekton   the second Tekton instance to which the FungusThread will
     *                   be
     *                   associated.
     */
    /*public void growBridge(FungusThread fromThread,Tekton toTekton) {
        FungusThread nThread = new FungusThread(null,true);
        nThread.setPrevThread(fromThread);

        fromThread.setNextThread(nThread);

        addThread(nThread);

        nThread.addTekton(fromThread.getTekton(null));

        nThread.addTekton(toTekton);

        fromThread.getTekton(null).addThread(nThread);

        toTekton.addThread(nThread);

        fromThread.getBody().addThread(nThread);

        nThread.setBody(fromThread.getBody());

        fromThread.getBody().addThread(nThread);
    }*/

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
    public FungusBody growBody(FungusThread thread) {
        if (thread.isBridge()) {
            System.err.println("Thread is a bridge. You can't grow a body from a bridge!");
            return null;
        }
        else if (!thread.getTekton(null).canGrowBody()) {
            System.err.println("Tekton already contains a body");
            return null;
        }
        Integer atleast = 2;
        boolean enoughSpore = thread.getTekton(null).isThereEnoughSpore(atleast);
        if (enoughSpore) {
            FungusBody fb = new FungusBody(null, null);
            thread.getTekton(null).setBody(fb);
            for (Integer i = 0; i < atleast; i++) {
                thread.getTekton(null).getSpores().get(i).absorbed();
            }
            fb.setTekton(thread.getTekton(null));
            fb.addThread(thread);
            thread.setBody(fb);
            return fb;
        } else {
            System.out.println("Sikertelen testnövesztés. A tektonon nincs elég spóra");
        }
        return null;
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
        for (FungusBody body : bodies) {
            body.produceSpore();

            if (body.timeToDie()) {
                destroyBody(body);
            }
        }
        for (FungusThread thread : threads) {
            destroyThread(thread);
        }
    }

    /**
     * Destroys a FungusThread by removing it from the list of associated
     * FungusThread instances, and then removing it from all associated
     * FungusBody instances. Finally, the FungusThread is destroyed.
     * 
     * @param ft the FungusThread instance to be destroyed.
     */
    public void destroyThread(FungusThread ft) {
        if (ft.getIsDying() && ft.getLifeSpan() != null) {
            ft.decreaseLife();
            return;
        }
        if (ft.getLifeSpan() == 0) {
            while (ft.getNext() != null) {
                ft.getNext().setConnected(false);
                List<Tekton> tektons = ft.getNext().getTektons();
                for (int i = 0; i < tektons.size(); i++) {
                    if (tektons.get(i).getClass() != tektonTypes.FeedThreadTekton.class) {
                        ft.setIsDying(true);
                    }
                }
                ft = ft.getNext();
            }
            deleteThread(ft);
            boolean success;
            for (FungusBody body : bodies) {
                success = body.removeThread(ft);
                if (success) {
                    break;
                }
            }

            ft.destroy();
        }

    }

    /**
     * Destroys a FungusBody by first destroying all associated FungusThread
     * instances, and then removing the FungusBody from the list of associated
     * FungusBody instances. Finally, the FungusBody is removed from the Tekton
     * associated with the FungusBody.
     * 
     * @param fb the FungusBody instance to be destroyed.
     */
    public void destroyBody(FungusBody fb) {
        for (FungusThread ft : fb.getThreads()) {
            destroyThread(ft);
        }
        fb.getTekton().setBody(null);
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
    public void eatInsect(FungusThread ft) {
        if (ft.isBridge()) {
            System.err.println("Thread was a bridge!");
            return;
        }
        Boolean someoneDied = false;
        for (Insect insect : ft.getTekton(null).getInsects()) {
            if (insect.gEffect() == InsectEffects.STUN) {
                insect.deadInsect();
                someoneDied = true;
            }
        }
        if (someoneDied) {
            growBody(ft);
        } else
            System.err.println("There is no stunned insect on tekton");

    }

    /**
     * Sporulates to the given FungusBody.
     * This will cause the FungusBody to release spores, and may allow the
     * FungusSpecies to grow new FungusThread instances.
     *
     * @param selectedBody the FungusBody instance to sporulate.
     */
    public void sporulate(FungusBody selectedBody) {
        for (FungusBody body : bodies) {
            if (body == selectedBody) {
                selectedBody.sporulate();
            }
        }
    }
}