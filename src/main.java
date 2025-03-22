import fungus.FungusBody;
import fungus.FungusThread;
import insect.Insect;
import logic.GameLogic;
import sporeTypes.Spore;
import tektonTypes.DecreasingTekton;
import tektonTypes.Tekton;
public class main {
    
    public static void main(String[] args) {
        GameLogic gameLogic = new GameLogic();

        System.out.println("Fungorium szimuláció elindult!");
        // Initialize a Tekton object
        Tekton tekton = new Tekton(true, true);
        System.out.println("Tekton initialized: " + tekton);

        // Initialize a DecreasingTekton object
        DecreasingTekton decreasingTekton = new DecreasingTekton();
        System.out.println("DecreasingTekton initialized: " + decreasingTekton);

        // Add some spores to the Tekton
        Spore spore1 = new Spore();
        Spore spore2 = new Spore();
        tekton.addSpore(spore1);
        tekton.addSpore(spore2);
        System.out.println("Spores added to Tekton: " + tekton.getSpores().size());

        // Add an insect to the Tekton
        Insect insect = new Insect();
        tekton.addInsect(insect);
        System.out.println("Insect added to Tekton: " + tekton.hasInsect());

        // Add a neighbor Tekton
        Tekton neighborTekton = new Tekton(false, true);
        tekton.addNeighbour(neighborTekton);
        System.out.println("Neighbor Tekton added: " + tekton.getNeighbours().size());

        // Add a thread and body to the Tekton
        FungusThread thread = new FungusThread(15, true); 
        thread.addTekton(tekton); 
        FungusBody body = new FungusBody(10, 5); 
        body.addThread(thread); 
        body.setTekton(tekton);
        tekton.addThread(thread);
        tekton.addBody(body);
        System.out.println("Thread and Body added to Tekton: " + tekton.getThreads().size() + ", " + tekton.getBodies().size());
    }
}