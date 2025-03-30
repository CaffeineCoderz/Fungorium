package tektonTypes;

import fungus.FungusThread;
import utils.Logger;

public class FeedThreadTekton extends Tekton {

    private static Logger log;

    public FeedThreadTekton(Boolean canGrowBody, Boolean canGrowThread) {
        super(canGrowBody, canGrowThread);
        log = Logger.getLogger("FeedThreadTektonLogger");
    }

    //? ez itt amugy tok folosleges mert fungusspieciesben decreaseLifenal vizsgaljuk hogy ilyen tekton e
    //! ténlyeg az ~ Diviki
    /**
     * Keeps alive threads that are not directly or indirectly connected to a FungusBody.
     */
    /*public void maintainThreads() {
        log.stepIn("maintainThreads()");
        for (FungusThread thread : threads) {
            if (!isThreadConnectedToBody(thread)) {
                log.askQ("Thread not connected to body: " + thread, false);
                log.stepIn("keepThreadAlive(thread)");
                keepThreadAlive(thread);
                log.stepOut("keepThreadAlive(thread)", null);
            }
        }
        log.stepOut("maintainThreads()", null);
    }*/

    /**
     * Checks if a thread is connected to a FungusBody.
     * @param thread The thread to check.
     * @return true if the thread is connected to a FungusBody, false otherwise.
     */
    private boolean isThreadConnectedToBody(FungusThread thread) {
        log.stepIn("isThreadConnectedToBody(thread: " + thread + ")");
        boolean connected = body != null && body.getThreads().contains(thread);
        log.stepOut("isThreadConnectedToBody()", connected);
        return connected;
    }

    /**
     * Keeps a thread alive.
     * @param thread The thread to keep alive.
     */
    //! nem állitjuk át setIsDying-ot, mivel az azt mutatja, hogy el kell halnia
    /*private void keepThreadAlive(FungusThread thread) {
        log.stepIn("keepThreadAlive(thread: " + thread + ")");
        log.askQ("Keeping thread alive: " + thread, false);
        thread.setIsDying(false);
        //? ide ha ezt így karjuk akkol kell még
        log.stepOut("keepThreadAlive()", null);
    }*/

    @Override
    public void removeThread(FungusThread thread){
        /*if (thread.getIsDying()) {
            log.askQ("Thread is dying", false);
            log.stepIn("threads.remove(thread)");
            threads.remove(thread);
            log.stepOut("threads.remove(thread)", null);
        }else if (thread.getLifeSpan() == 0) {
            // ? ennyi körig maradjon életben?
            log.stepIn("thread.setLifeSpan(2)");
            thread.setLifeSpan(2);
            log.stepOut("thread.setLifeSpan(2)", null);
        }
        */
        if (thread.getLifeSpan()==0) {
            log.askQ("The thread is already dying", false);
            log.stepIn("threads.remove(thread)");
            threads.remove(thread);
            log.stepOut("threads.remove(thread)", null);
        }else{
            log.askQ("The tekton serves the thread with nutrition to stayalive\n", false);
        }
        

    }
}
