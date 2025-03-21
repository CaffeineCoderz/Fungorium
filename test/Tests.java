package test;

import java.util.Scanner;

import fungus.FungusBody;
import fungus.FungusThread;
import insect.Insect;
import sporeTypes.Spore;
import tektonTypes.DecreasingTekton;
import tektonTypes.Tekton;

public class Tests {
    public void growBodyDefTekton() {
        System.out.println("Running test: growBodyDefTektonSuccess");
        System.out.println("Van már gombatest a tektonon? (y/n)");
        Scanner scanner = new Scanner(System.in);
        char c = scanner.next().charAt(0);
        switch(c){
            case 'y':
                tekton.addBody();
                break;

        }
    }

    public void growThreadOneThreadTektonSuccess() {
        System.out.println("Running test: growThreadOneThreadTektonSuccess");
    }

    public void growThreadDefTekton() {
        System.out.println("Running test: growThreadDefTekton");
    }

    public void insectMoveToNewTektonSuccess() {
        System.out.println("Running test: insectMoveToNewTektonSuccess");
    }

    public void sporulateSuccess() {
        System.out.println("Running test: sporulateSuccess");
    }

    public void disableCutSporeConsumed() {
        System.out.println("Running test: disableCutSporeConsumed");
    }

    public void slowSporeConsumed() {
        System.out.println("Running test: slowSporeConsumed");
    }

    public void insectCutThreadWhileNoCutAbility() {
        System.out.println("Running test: insectCutThreadWhileNoCutAbility");
    }

    public void growBodyOnlyThreadTekton() {
        System.out.println("Running test: growBodyOnlyThreadTekton");
    }

    public void insectCutThread() {
        System.out.println("Running test: insectCutThread");
    }

    public void speedSporeConsumed() {
        System.out.println("Running test: speedSporeConsumed");
    }

    public void insectMoveWhileStunned() {
        System.out.println("Running test: insectMoveWhileStunned");
    }

    public void growThreadDecomposingTektor() {
        System.out.println("Running test: growThreadDecomposingTektor");
    }

    public void insectMoveSuccess() {
        System.out.println("Running test: insectMoveSuccess");
    }

    public void stunSporeConsumed() {
        System.out.println("Running test: stunSporeConsumed");
    }

    public void sporulateUnSuccess() {
        System.out.println("Running test: sporulateUnSuccess");
    }

    public void sporulateFurther() {
        System.out.println("Running test: sporulateFurther");
    }

    public void insectMoveUnsuccess() {
        System.out.println("Running test: insectMoveUnsuccess");
    }
    public static void setup(){
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
        FungusThread thread = new FungusThread(15, true); // Initialize with lifeSpan = 15, bridge = true
        thread.addTekton(tekton); // Associate the thread with the Tekton
        FungusBody body = new FungusBody(10, 5); // Initialize with sporeCount = 10, sporulateLeft = 5
        body.addThread(thread); // Add the thread to the FungusBody
        body.setTekton(tekton); // Associate the FungusBody with the Tekton
        tekton.addThread(thread);
        tekton.addBody(body);
        System.out.println("Thread and Body added to Tekton: " + tekton.getThreads().size() + ", " + tekton.getBodies().size());
    }
    public static void main(String[] args) {
        Tests tests = new Tests();
        Spore spore1
        System.out.println("Starting all tests...");
        setup();
        tests.growBodyDefTekton();
        tests.growThreadOneThreadTektonSuccess();
        tests.growThreadDefTekton();
        tests.insectMoveToNewTektonSuccess();
        tests.sporulateSuccess();
        tests.disableCutSporeConsumed();
        tests.slowSporeConsumed();
        tests.insectCutThreadWhileNoCutAbility();
        tests.growBodyOnlyThreadTekton();
        tests.insectCutThread();
        tests.speedSporeConsumed();
        tests.insectMoveWhileStunned();
        tests.growThreadDecomposingTektor();
        tests.insectMoveSuccess();
        tests.stunSporeConsumed();
        tests.sporulateUnSuccess();
        tests.sporulateFurther();
        tests.insectMoveUnsuccess();

        System.out.println("All tests finished.");
    }
}