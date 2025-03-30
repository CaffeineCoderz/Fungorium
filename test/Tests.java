package test;

import fungus.*;
import insect.Entomologist;
import insect.Insect;
import insect.InsectEffects;

import java.util.Scanner;
import logic.GameLogic;
import sporeTypes.*;
import tektonTypes.*;
import utils.*;

public class Tests {

    private Logger log;
    private Tekton tekton1;
    private DecreasingTekton Dtekton;
    private Tekton neighborTekton;
    private Spore spore1;
    private Spore spore2;
    private Spore spore3;
    private FungusBody body;
    private FungusThread thread;
    private FungusSpecies fungusspecies;
    private Entomologist entomologist;
    private Insect insect;

    /**
     * Prints out all the test cases that can be run. The numbers in the menu
     * correspond to the numbers in the switch statement in the main method.
     */
    public static void displayTests() {
        System.out.println(
            "/-----------------------------------------------------------\\\n" +
            "Choose from the test cases:\n\n" +
            "\t1. GrowBody DefTekton_Success\n" +
            "\t2. GrowThread OneThreadTekton Success\n" +
            "\t3. GrowThread DefTekton\n" +
            "\t4. Sporulate Success\n" +
            "\t5. DisableCutSporeConsumed\n" +
            "\t6. SlowSporeConsumed\n" +
            "\t7. InsectCutThreadWhileNoCutAbility\n" +
            "\t8. GrowBody Only Thread Tekton\n" +
            "\t9. InsectCutThread\n" +
            "\t10. SpeedSporeConsumed\n" +
            "\t11. InsectMoveWhileStunned\n" +
            "\t12. GrowThread Decomposing Tektor\n" +
            "\t13. InsectMove Success\n" +
            "\t14. StunSporeConsumed\n" +
            "\t15. Sporulate Further\n" +
            "\t16. Tekton break\n" +
            "\t17. Consume stunned insect\n" +
            "\t18. Consume MultiplyInsectSpore\n" +
            "\t19. growBridgeSuccess\n" +
            "\\-----------------------------------------------------------/\n"
        );

    }

    /**
     * Sets up the test environment by creating a GameLogic object, a Tekton, a
     * DecreasingTekton, adding some spores to the Tekton, adding an insect to the
     * Tekton, adding a neighbor Tekton to the Tekton, and adding a thread and body
     * to the Tekton.
     */
    public void setup() {
        GameLogic gameLogic = new GameLogic();
        log = Logger.getLogger("TestLogger");
        log.askQ("Setup Base Map", false);
        // System.out.println("Fungorium szimuláció elindult!");

        // Initialize a Tekton object
        tekton1 = new Tekton(true, true);
        // System.out.println("Tekton initialized: " + tekton1);

        // Initialize a DecreasingTekton object
        Dtekton = new DecreasingTekton();


        // Add some spores to the Tekton
        spore1 = new Spore();
        spore2 = new Spore();
        spore3 = new Spore();
        tekton1.addSpore(spore1);
        tekton1.addSpore(spore2);
        tekton1.addSpore(spore3);

        spore1.setTekton(tekton1);
        spore2.setTekton(tekton1);
        spore3.setTekton(tekton1);
        // Initialize the Insect object
        insect = new Insect();
        tekton1.addInsect(insect);

        // Add a neighbor Tekton
        neighborTekton = new Tekton(false, true);
        tekton1.addNeighbour(neighborTekton);
        neighborTekton.addNeighbour(tekton1);
        System.out.println("Neighbor Tekton added: " + tekton1.getNeighbours().size());

        // Add a thread and body to the Tekton
        thread = new FungusThread(15, true);
        body = new FungusBody(9, 5);
        tekton1.addThread(thread);
        tekton1.setBody(body);
        body.setTekton(tekton1);
        //System.out.println("Thread and body added to Tekton.");

        // Initialize the FungusSpecies object
        fungusspecies = new FungusSpecies();
        thread.setSpecies(fungusspecies);
        body.setSpecies(fungusspecies);


        entomologist = new Entomologist();
        entomologist.addInsect(insect);
        insect.setMyOwner(entomologist);
        //System.out.println("FungusSpecies initialized.");
    }

    /**
     * Resets the test environment to ensure no leftover state.
     */
    public void reset() {
        // Nullify or reinitialize shared objects
        tekton1 = null;
        Dtekton = null;
        neighborTekton = null;
        spore1 = null;
        spore2 = null;
        spore3 = null;
        body = null;
        thread = null;
        fungusspecies = null;
        insect = null;
        System.out.println("Test environment reset.");
    }

    public static void getUserInput() {
        Scanner scanner = new Scanner(System.in);
        Tests tests = new Tests();

        while (true) {
            displayTests();
            System.out.println("Enter the test case number (or 50 to exit): ");

            // Érvényes bemenetet kérünk
            int testCase = -1; // Alapértelmezett érték
            while (true) {
                String input = scanner.nextLine(); // Olvassuk be az egész sort
                try {
                    testCase = Integer.parseInt(input); // Próbáljuk meg számmá konvertálni
                    break; // Ha sikeres, kilépünk a ciklusból
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid test case number.");
                }
            }
        if (testCase == 50) {
            System.out.println("Exiting test runner.");
            break;
        }

        System.out.println("Running test case: " + testCase);

        tests.reset();
        tests.setup();

        switch (testCase) {
            case 1:
                tests.growBodyDefTektonSuccess();
                break;
            case 2:
                tests.growThreadOneThreadTektonSuccess();
                break;
            case 3:
                tests.growThreadDefTekton();
                break;
            case 4:
                tests.sporulateSuccess();
                break;
            case 5:
                tests.disableCutSporeConsumed();
                break;
            case 6:
                tests.slowSporeConsumed();
                break;
            case 7:
                tests.insectCutThreadWhileNoCutAbility();
                break;
            case 8:
                tests.growBodyOnlyThreadTekton();
                break;
            case 9:
                tests.insectCutThread();
                break;
            case 10:
                tests.speedSporeConsumed();
                break;
            case 11:
                tests.insectMoveWhileStunned();
                break;
            case 12:
                tests.growThreadDecomposingTektor();
                break;
            case 13:
                tests.insectMoveSuccess();
                break;
            case 14:
                tests.stunSporeConsumed();
                break;
            case 15:
                tests.sporulateFurther();
                break;
            case 16:
                tests.tektonBreak();
                break;
            case 17:
                tests.consumeStunnedInsect();
                break;
            case 18:
                tests.duplicateInsect();
                break;
            case 19:
                tests.growBridgeSucces();
                break;    
            default:
                System.out.println("Invalid test case number.");
                break;
            }

  

   
        }

        scanner.close();
    }

    /**
     * Executes a test case based on the provided test case number
     * 
     * Given a test case number, this method calls the corresponding test method.
     * The switch statement maps test case numbers to their respective methods,
     * allowing different tests to be run based on the input.
     * 
     * @param testCase the number representing the test case to execute. If the
     *                 number does not correspond to a valid test case, a message
     *                 indicating
     *                 an invalid test case number is printed.
     */
    private void executeTestCase(int testCase) {

    }

    /**
     * Tests the successful growth of a FungusBody on a default Tekton.
     * 
     * This test adds threads to a Tekton and checks if a FungusBody can
     * grow on it based on user input regarding the presence of an existing
     * body and sufficient spores. If a body is already present, it attempts
     * to grow a new body. If not, it further checks if there are enough
     * spores to support body growth and attempts to grow a body accordingly.
     * 
     * The test logs each step of the process and captures the interaction
     * with the user to simulate different scenarios.
     */
    public void growBodyDefTektonSuccess() {
        log.stepIn("Running test: growBodyDefTektonSuccess");

        fungusspecies.addThread(thread);
        tekton1.addThread(thread);
        FungusThread thread2 = new FungusThread(5, false);
        thread.addTekton(tekton1);
        fungusspecies.addThread(thread2);
        thread.addTekton(neighborTekton);
        thread2.addTekton(tekton1);
        tekton1.addThread(thread2);
        String key = log.askQ("Van-e mar test a tektonon? (y/n)", true);
        if (key.equals("y")) {
            log.stepIn("fungusspecies.growBody(thread2)");
            fungusspecies.growBody(thread2);
            log.stepOut("fungusspecies.growBody(thread2)", null);
        } else if (key.equals("n")) {
            String key2 = log.askQ("Van a tektonon elég spóra?(y/n)", true);
            if (key2.equals("y")) {
                tekton1.setBody(null);
                tekton1.addSpore(spore1);
                tekton1.addSpore(spore2);
                log.stepIn("fungusspecies.growBody(thread2)");
                fungusspecies.growBody(thread2);
                log.stepOut("fungusspecies.growBody(thread2)", null);
            } else if (key2.equals("n")) {
                tekton1.setBody(null);
                tekton1.removeSpore();
                tekton1.removeSpore();
                log.stepIn("fungusspecies.growBody(thread2)");
                fungusspecies.growBody(thread2);
                log.stepOut("fungusspecies.growBody(thread2)", null);
            }
        }
        log.stepOut("End of growBodyDefTektonSuccess", null);
    }

    /**
     * Tests the successful growth of a FungusThread on a OneThreadTekton.
     * 
     * This test adds a thread to a OneThreadTekton and checks if a new
     * FungusThread can grow on it based on user input regarding the presence of
     * an existing thread and sufficient spores. If a thread is already present,
     * it further checks if there are enough spores to support thread growth and
     * attempts to grow a thread accordingly.
     * 
     * The test logs each step of the process and captures the interaction with
     * the user to simulate different scenarios.
     */
    public void growThreadOneThreadTektonSuccess() {
        log.stepIn("Running test: growThreadOneThreadTektonSuccess");
        String key = log.askQ("Van-e mar fonal a tektonon? (y/n)", true);
        OneThreadTekton oTekton = new OneThreadTekton();
        System.out.println(oTekton.getClass());
        if (key.equals("n")) {
            FungusThread thread2 = new FungusThread(5, false, thread);
            log.stepIn("fungusspecies.growThread(oTekton, thread, thread2)");
            fungusspecies.growThread(oTekton, thread);
            log.stepOut("fungusspecies.growThread(oTekton, thread, thread2)", null);
        }
        if (key.equals("y")) {
            oTekton.addThread(thread);
            thread.addTekton(oTekton);
            FungusThread thread3 = new FungusThread(5, false);
            FungusThread thread4 = new FungusThread(5, false, thread3);
            log.stepIn("fungusspecies.growThread(fungusspecies.growThread(oTekton, thread3, thread4))");
            fungusspecies.growThread(oTekton, thread3);
            log.stepOut("fungusspecies.growThread(fungusspecies.growThread(oTekton, thread3, thread4))", null);
        }
        log.stepOut("End of growThreadOneThreadTektonSuccess", null);
    }

    /**
     * Tests the successful growth of a FungusThread on a default Tekton.
     * 
     * This test adds a thread to a default Tekton and checks if a new
     * FungusThread can grow on it based on user input regarding the presence of
     * an existing thread and sufficient spores. If a thread is already present,
     * it further checks if there are enough spores to support thread growth and
     * attempts to grow a thread accordingly.
     * 
     * The test logs each step of the process and captures the interaction with
     * the user to simulate different scenarios.
     */
    public void growThreadDefTekton() {
        log.stepIn("Running test: growThreadDefTekton");
        FungusThread thread2 = new FungusThread(10, false, thread);
        log.stepIn("fungusspecies.growThread(tekton1, thread, thread2);");
        fungusspecies.growThread(tekton1, thread);
        log.stepOut("fungusspecies.growThread(tekton1, thread, thread2);", null);

        log.stepOut("End of growThreadDefTekton", null);
    }

    /**
     * Tests the successful spread of spores from a FungusBody.
     * 
     * This test simulates a scenario where a FungusBody has enough spores to
     * spread to its neighboring Tektons. The test logs each step of the process
     * and captures the interaction with the user to simulate different
     * scenarios.
     */
    public void sporulateSuccess() {
        log.stepIn("Running test: sporulateSuccess");
        FungusThread thread2 = new FungusThread(null,false);
        body.setSporeC(2);
        tekton1.addThread(thread2);
        String key = log.askQ("tud spórát szórni a gombatest? (y/n)", true);
        if(key.equals("y")){
            body.setTekton(tekton1);
            tekton1.setBody(body);
            log.stepIn("body.sporulate()");
            body.sporulate();
            log.stepOut("body.sporulate()", null);
        }
            if(key.equals("n")){
                FungusBody body2 = new FungusBody(0,3);
                body2.setTekton(tekton1);
                tekton1.setBody(body2);
                log.stepIn("body2.sporulate();");
                body2.sporulate();
                log.stepOut("body2.sporulate();", null);
                
        } 
    }

    /**
     * Tests the consumption of a DisableCutSpore by an insect.
     * 
     * This test verifies that when an insect consumes a DisableCutSpore,
     * its ability to cut threads is disabled. The test logs each step
     * of the process and checks the insect's ability to cut threads
     * after consumption.
     */
    public void disableCutSporeConsumed() {
        log.stepIn("Running test: disableCutSporeConsumed");

        Spore dcSpore = new DisableCutSpore();
        dcSpore.setTekton(tekton1);
        tekton1.addSpore(dcSpore);
        log.stepIn("insect.consumeSpore(dcSpore)");
        insect.consumeSpore(dcSpore);
        log.stepOut("insect.consumeSpore(dcSpore)", null);
        log.stepIn("insect.hasCutAbility()");
        log.stepOut("insect.hasCutAbility()", insect.hasCutAbility());

        log.stepOut("End of disableCutSporeConsumed", null);
    }

    /**
     * Tests the consumption of a SlowSpore by an insect.
     * 
     * This test verifies that when an insect consumes a SlowSpore,
     * its speed effect is adjusted accordingly. The test logs each
     * step of the process and checks the insect's effect after
     * consumption to ensure it is slowed down.
     */
    public void slowSporeConsumed() {
        log.stepIn("Running test: slowSporeConsumed");
        Spore sSpore = new SlowSpore(tekton1, 10);
        log.stepIn("insect.consumeSpore(sSpore)");
        insect.consumeSpore(sSpore);
        log.stepOut("insect.consumeSpore(sSpore)", null);
        log.stepIn("insect.gEffect()");
        log.stepOut("insect.gEffect()", insect.gEffect());
        log.stepOut("End of slowSporeConsumed", null);
    }

    /**
     * Tests the cutting of a FungusThread by an insect when it has no
     * ability to cut. This test verifies that the insect's ability to cut
     * threads is disabled and it cannot cut any threads. The test logs each
     * step of the process and checks the insect's ability to cut threads
     * after consumption to ensure it cannot cut any threads.
     */
    public void insectCutThreadWhileNoCutAbility() {
        log.stepIn("Running test: insectCutThreadWhileNoCutAbility");
        log.stepIn("insect.disableCut()");
        
        log.stepOut("insect.disableCut()", null);

        FungusThread thread2 = new FungusThread(5, false);
        tekton1.addThread(thread2);
        insect.disableCut();
        log.stepIn("insect.cut(thread2)");
        insect.cut(thread2);
        log.stepOut("insect.cut(thread2)", null);

        log.stepOut("End of insectCutThreadWhileNoCutAbility", null);
    }

    /**
     * Tests the successful growth of a FungusBody on an OnlyThreadTekton.
     * 
     * This test adds enough spores to an OnlyThreadTekton and then attempts to
     * grow a FungusBody. The test logs each step of the process and checks the
     * result of the growth operation.
     */
    public void growBodyOnlyThreadTekton() {
        log.stepIn("Running test: growBodyOnlyThreadTekton");
        OnlyThreadTekton temp = new OnlyThreadTekton();
        FungusThread othread=new FungusThread(null, false);
        for (int i = 0; i < 4; i++) {
            Spore spore = new Spore();
            temp.addSpore(spore);
        }
        othread.addTekton(temp);
        temp.addThread(othread);
        log.stepIn("fungusspecies.growBody(thread);");
        fungusspecies.growBody(othread);
        log.stepOut("fungusspecies.growBody(thread);", null);
        log.stepOut("End of growBodyOnlyThreadTekton", null);
    }

    /**
     * Tests the cutting of a FungusThread by an insect.
     * 
     * This test verifies that when an insect cuts a FungusThread, the
     * FungusThread is destroyed. The test logs each step of the process and
     * checks the result of the cut operation.
     */
    public void insectCutThread() {
        log.stepIn("Running test: insectCutThread");
        Tekton tekton2 = new Tekton(true, true);
        tekton1.addInsect(insect);
        insect.setRecentTekton(tekton1);
        tekton1.addThread(thread);
        thread.addTekton(tekton1);
        tekton2.addThread(thread);
        thread.addTekton(tekton2);

        log.stepIn("insect.cut(thread);");
        insect.cut(thread);
        log.stepOut("insect.cut(thread);", null);
        log.stepOut("End of insectCutThread", null);
    }

    /**
     * Tests the consumption of a FastSpore by an insect.
     * 
     * This test verifies that when an insect consumes a FastSpore, its speed
     * effect is adjusted accordingly. The test logs each step of the process
     * and checks the insect's effect after consumption to ensure it is sped up.
     */
    public void speedSporeConsumed() {
        log.stepIn("Running test: speedSporeConsumed");
        FastSpore fs = new FastSpore(tekton1, 10);
        log.stepIn("insect.consumeSpore(fs)");
        insect.consumeSpore(fs);

        log.stepOut("insect.consumeSpore(fs)", null);

        log.stepIn("insect.gEffect()");
        log.stepOut("insect.gEffect()", insect.gEffect());
        log.stepOut("End of speedSporeConsumed", null);

    }

    /**
     * Tests the movement of an insect while stunned.
     * 
     * This test verifies that when an insect consumes a StunSpore and becomes
     * stunned, it cannot move to a different FungusThread. The test logs each
     * step of the process, including the consumption of the StunSpore and the
     * attempt to move, ensuring that the insect remains in its current position.
     */
    public void insectMoveWhileStunned() {
        log.stepIn("Running test: insectMoveWhileStunned");
        Tekton tekton2 = new Tekton(true, true);
        tekton1.addInsect(insect);
        insect.setRecentTekton(tekton1);
        tekton1.addThread(thread);
        thread.addTekton(tekton1);
        tekton2.addThread(thread);
        thread.addTekton(tekton2);
        StunSpore stunspore = new StunSpore(tekton1, 10);

        log.stepIn("insect.consumeSpore(stunspore);");
        insect.consumeSpore(stunspore);
        log.stepOut("insect.consumeSpore(stunspore);", null);
        log.stepIn("insect.move(thread);");
        insect.move(thread);
        log.stepOut("insect.move(thread);", null);
        log.stepOut("End of insectMoveWhileStunned", stunspore);

    }

    /**
     * Tests the growth of a new FungusThread from a DecomposingTekton.
     * 
     * This test verifies that when a DecomposingTekton is given as the target
     * Tekton in the growThread() method of FungusSpecies, the new FungusThread
     * is added to the DecomposingTekton and the DecomposingTekton's
     * canGrowThread flag is set to false.
     */
    public void growThreadDecomposingTektor() {
        log.stepIn("Running test: growThreadDecomposingTekton");
        DecomposingTekton dt = new DecomposingTekton();
        FungusThread othread = new FungusThread(5, false);
        log.stepIn("fungusspecies.growThread(dt,othread,thread)");
        fungusspecies.growThread(dt, othread);
        log.stepOut("fungusspecies.growThread(dt,othread,thread)", null);
        log.stepOut("End of growThreadDecomposingTekton", othread);
    }

    /**
     * Tests the successful movement of an Insect from one Tekton to another
     * through a FungusThread.
     * 
     * This test verifies that when an Insect is moved from one Tekton to
     * another via a FungusThread, the Insect is properly removed from the
     * first Tekton and added to the second Tekton.
     */
    public void insectMoveSuccess() {
        log.stepIn("Running test: insectMoveSuccess");
        tekton1.addInsect(insect);
        FungusThread thread2 = new FungusThread(null, true);
        thread2.addTekton(tekton1);
        thread2.addTekton(neighborTekton);
        thread.addTekton(tekton1);
        tekton1.addThread(thread2);
        tekton1.addThread(thread);
        insect.setRecentTekton(tekton1);
        
        
        log.stepIn("insect.move(thread2);");
        insect.move(thread2);
        log.stepOut("insect.move(thread2);", null);
        log.stepOut("End of insectMoveSuccess",null);

    }

    /**
     * Tests the consumption of a StunSpore by an Insect.
     * 
     * This test verifies that when an Insect consumes a StunSpore, its effect
     * is set to STUN, preventing it from performing actions, and triggers the
     * absorption process of the spore.
     */
    public void stunSporeConsumed() {
        log.stepIn("Running test: stunSporeConsumed");
        Spore ss = new Spore(tekton1, 10);
        tekton1.addSpore(ss);
        log.stepIn("insect.consumeSpore(ss)");
        insect.consumeSpore(ss);
        log.stepOut("insect.consumeSpore(ss)", null);
        log.stepIn("insect.gEffect()");
        log.stepOut("insect.gEffect()", insect.gEffect());
        log.stepOut("End of stunSporeConsumed",null);
    }

    /**
     * Tests the sporulation of a FungusBody to its neighboring Tektons's neighboring Tektons.
     * 
     * This test verifies that when a FungusBody sporulates, it spreads spores to its neighboring
     * Tektons, and if the neighboring Tektons have neighboring Tektons, it also spreads spores to
     * those Tektons.
     */
    public void sporulateFurther() {
        log.stepIn("Running test: sporulateFurther");
        Tekton tekton2 = new Tekton(true, true);
        Tekton tekton3 = new Tekton(true, true);
        tekton1.addNeighbour(tekton2);
        tekton2.addNeighbour(tekton1);
        tekton2.addNeighbour(tekton3);
        tekton3.addNeighbour(tekton2);
        FungusBody body2 = new FungusBody(11, 5);
        body2.setTekton(tekton3);
        body2.produceSpore();
        log.stepIn("body2.sporulate();");
        body2.sporulate();
        log.stepOut("body2.sporulate();", null);
        log.stepOut("End of sporulateFurther", null);
    }

    /**
     * Tests the breaking of a Tekton.
     * 
     * This test verifies that when a Tekton is broken, all of its neighbors are
     * informed, all of its threads are destroyed, all of its bodies are destroyed,
     * all of its spores are absorbed, and all of its insects are killed.
     */
    public void tektonBreak(){
        log.stepIn("Running test: tektonBreak");
        FungusThread f2 = new FungusThread(null, false);
        neighborTekton.addThread(f2);
        f2.setSpecies(fungusspecies);
        f2.addTekton(neighborTekton);
        insect.setRecentTekton(tekton1);
        log.askQ("Tekton has 1 neighbour, 3 spores, 1 thread, 1 body and 1 insect", false);
        log.stepIn("tekton1.breakTekton()");
        tekton1.breakTekton();
        log.stepOut("tekton1.breakTekton()", null);
        log.stepOut("End of tektonBreak",null);  
    }

    /**
     * Tests the consumption of an Insect by a FungusSpecies.
     * 
     * This test verifies that when an Insect is consumed by a FungusSpecies, the
     * Insect is killed, and the associated FungusThread is given the opportunity
     * to grow a new FungusBody on the Tekton associated with the FungusThread.
     */
    public void consumeStunnedInsect(){
        log.stepIn("Running test: consumeStunnedInsect()");
        FungusThread thread2 = new FungusThread();
        thread2.addTekton(tekton1);
        thread2.setSpecies(fungusspecies);
        fungusspecies.addThread(thread2);
        insect.setThread(thread2);
        insect.setRecentTekton(tekton1);
        tekton1.addThread(thread2);
        tekton1.addInsect(insect);
        if (log.askQ("A rovar le van bénulva? (y/n)", true).equals("y")) {
            insect.stun();
        }
        log.stepIn("fungusspecies.eatInsect(thread2)");
        fungusspecies.eatInsect(thread2);
        log.stepOut("fungusspecies.eatInsect(thread2)", null);
        log.stepOut("End of consumeStunnedInsect", null);
    }

    /**
     * Tests the duplication of an Insect.
     * 
     * This test verifies that when an Insect consumes a MultiplyInsectSpore, the
     * Insect is duplicated, and the duplicate is added to the same Tekton as
     * the original Insect.
     */
    public void duplicateInsect(){
        log.stepIn("Running test: duplicateInsect");
        MultiplyInsectSpore multinsectspore= new MultiplyInsectSpore();
        multinsectspore.setTekton(tekton1);
        log.stepIn("insect.consumeSpore(multinsectspore)");
        insect.consumeSpore(multinsectspore);
        log.stepOut("insect.consumeSpore(multinsectspore)", null);
        log.stepOut("End of duplicateInsect", null);
    }

    /**
     * This test verifies that the associated FungusSpecies can grow a new
     * FungusThread associated with the given FungusThread and Tekton.
     * 
     * This test adds a new Tekton to the associated FungusThread, and then
     * orders the associated FungusSpecies to grow a new FungusThread associated
     * with the given FungusThread and the new Tekton. The test then verifies
     * that the new FungusThread is associated with both the given FungusThread
     * and the new Tekton.
     */
    public void growBridgeSucces(){
        thread.addTekton(tekton1);
        thread.setBody(body);
        log.stepIn("Running tests: growBridgeSucces()");
        Tekton tekton2 = new Tekton();
        log.stepIn("fungusspecies.growBridge(thread, tekton2)");
        fungusspecies.growBridge(thread, tekton2);
        log.stepOut("fungusspecies.growBridge(thread, tekton2)", tekton2);
        log.stepOut("End of growBridgeSucces", null);
    }

    /**
     * The main method of the Tests class.
     * 
     * This method is the entry point for the Tests class. It starts all tests by
     * calling the getUserInput() method, and then creates a new instance of the
     * Tests class and runs all tests.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Starting all tests...");
        getUserInput();

        Tests tests = new Tests();
        System.out.println("All tests finished.");
    }
}