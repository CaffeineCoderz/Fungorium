package test;

import fungus.*;
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
    private FungusSpecies species;
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
            "\t4. InsectMoveToNewTekton Success\n" +
            "\t5. Sporulate Success\n" +
            "\t6. DisableCutSporeConsumed\n" +
            "\t7. SlowSporeConsumed\n" +
            "\t8. InsectCutThreadWhileNoCutAbility\n" +
            "\t9. GrowBody Only Thread Tekton\n" +
            "\t10. InsectCutThread\n" +
            "\t11. SpeedSporeConsumed\n" +
            "\t12. InsectMoveWhileStunned\n" +
            "\t13. GrowThread Decomposing Tektor\n" +
            "\t14. InsectMove Success\n" +
            "\t15. StunSporeConsumed\n" +
            "\t16. Sporulate Further\n" +
            "\\-----------------------------------------------------------/\n"
        );
    }
    /**
     * Sets up the test environment by creating a GameLogic object, a Tekton, a
     * DecreasingTekton, adding some spores to the Tekton, adding an insect to the
     * Tekton, adding a neighbor Tekton to the Tekton, and adding a thread and body
     * to the Tekton.
     */
    public void setup(){
        GameLogic gameLogic = new GameLogic();
        log = Logger.getLogger("TestLogger");
        //System.out.println("Fungorium szimuláció elindult!");

        // Initialize a Tekton object
        tekton1 = new Tekton(true, true);
        //System.out.println("Tekton initialized: " + tekton1);

        // Initialize a DecreasingTekton object
        Dtekton = new DecreasingTekton();
        //System.out.println("DecreasingTekton initialized: " + Dtekton);

        // Add some spores to the Tekton
        spore1 = new Spore();
        spore2 = new Spore();
        spore3 = new Spore();
        tekton1.addSpore(spore1);
        tekton1.addSpore(spore2);
        tekton1.addSpore(spore3);
        //System.out.println("Spores added to Tekton: " + tekton1.getSpores().size());

        // Initialize the Insect object
        insect = new Insect();
        tekton1.addInsect(insect);
        //System.out.println("Insect added to Tekton: " + tekton1.insectFree());

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
        //System.out.println("Thread and body added to Tekton.");

        // Initialize the FungusSpecies object
        species = new FungusSpecies();
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
        species = null;
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
                tests.insectMoveToNewTektonSuccess();
                break;
            case 5:
                tests.sporulateSuccess();
                break;
            case 6:
                tests.disableCutSporeConsumed();
                break;
            case 7:
                tests.slowSporeConsumed();
                break;
            case 8:
                tests.insectCutThreadWhileNoCutAbility();
                break;
            case 9:
                tests.growBodyOnlyThreadTekton();
                break;
            case 10:
                tests.insectCutThread();
                break;
            case 11:
                tests.speedSporeConsumed();
                break;
            case 12:
                tests.insectMoveWhileStunned();
                break;
            case 13:
                tests.growThreadDecomposingTektor();
                break;
            case 14:
                tests.insectMoveSuccess();
                break;
            case 15:
                tests.stunSporeConsumed();
                break;
            case 16:
                tests.sporulateFurther();
                break;
            default:
                System.out.println("Invalid test case number.");
                break;
        }
    }

    scanner.close();
}

    
    
    
    /**
     * Executes a test case based on the provided test case number.
     * 
     * Given a test case number, this method calls the corresponding test method.
     * The switch statement maps test case numbers to their respective methods,
     * allowing different tests to be run based on the input.
     * 
     * @param testCase the number representing the test case to execute. If the 
     * number does not correspond to a valid test case, a message indicating 
     * an invalid test case number is printed.
     */

    private void executeTestCase(int testCase) {
       
    }
    

    public void growBodyDefTektonSuccess() {
        log.stepIn("Running test: growBodyDefTektonSuccess");
    
        log.stepIn("species.addThread(thread)");
        species.addThread(thread);
        log.stepOut("species.addThread(thread)", null);
        log.stepIn("tekton1.addThread(thread)");
        tekton1.addThread(thread);
        log.stepOut("tekton1.addThread(thread)", null);
    
        log.stepIn("tekton1.addThread(thread)");
        FungusThread thread2 = new FungusThread(5,false);
        thread.addTekton(tekton1);
        species.addThread(thread2);
        thread.addTekton(neighborTekton);
        thread2.addTekton(tekton1);
        tekton1.addThread(thread2);
        
        String key = log.askQ("Van-e mar test a tektonon? (y/n)", true);
        if(key.equals("y")){
            log.stepIn("species.growBody(thread2)");
            species.growBody(thread2);    
            log.stepOut("species.growBody(thread2)", null);
            return;
        }
        else if(key.equals("n")){
            String key2 = log.askQ("Van a tektonon elég spóra?(y/n)", true);
            if(key2.equals("y")){
                tekton1.setBody(null);
                log.stepIn("tekton1.addSpore(spore1)");
                tekton1.addSpore(spore1);
                log.stepOut("tekton1.addSpore(spore1)",null);
                
                log.stepIn("tekton1.addSpore(spore2)");
                tekton1.addSpore(spore2);
                log.stepOut("tekton1.addSpore(spore2)",null);
    
                log.stepIn("species.growBody(thread2)");
                species.growBody(thread2);
            }
            else if(key2.equals("n")){
                log.stepIn("species.growBody(thread2)");
                tekton1.setBody(null);
                tekton1.removeSpore();
                tekton1.removeSpore();
                species.growBody(thread2);
                log.stepOut("species.growBody(thread2)",null);
            }
        }
        log.stepOut("Running test: growBodyDefTektonSuccess", null);
    }

    public void growThreadOneThreadTektonSuccess() {
        System.out.println("Running test: growThreadOneThreadTektonSuccess");
        String key = log.askQ("Van-e mar fonal a tektonon? (y/n)", true);
        OneThreadTekton oTekton = new OneThreadTekton();
        if(key.equals("n")){
            FungusThread thread2 = new FungusThread(5, false, thread);
            species.growThread(oTekton, thread, thread2);
        }
        if(key.equals("y")){
            oTekton.addThread(thread);
            thread.addTekton(oTekton);
            FungusThread thread3= new FungusThread(5, false);
            FungusThread thread4= new FungusThread(5, false, thread3);
            log.stepIn("species.growThread(species.growThread(oTekton, thread3, thread4))");
            species.growThread(oTekton, thread3, thread4);
            log.stepOut("species.growThread(species.growThread(oTekton, thread3, thread4))",null);

        }
        
    }

    public void growThreadDefTekton() {
        System.out.println("Running test: growThreadDefTekton");
        FungusThread thread2 = new FungusThread(10, false, thread);
        log.stepIn("species.growThread(tekton1, thread, thread2);");
        species.growThread(tekton1, thread, thread2);
        log.stepOut("species.growThread(tekton1, thread, thread2);",false);
        /* if(!tekton1.getThreads().isEmpty()){
            System.out.println("Sikeres fonálnövesztés");
        } */
        
    }

    public void insectMoveToNewTektonSuccess() {
        System.out.println("Running test: insectMoveToNewTektonSuccess");
        FungusThread thread0 = new FungusThread(5, false);
        neighborTekton.addThread(thread0);
        thread.setPrevThread(thread0);
        tekton1.addThread(thread);
        thread.addTekton(tekton1);
        neighborTekton.addThread(thread);
        insect.setThread(thread0);
        insect.setRecentTekton(neighborTekton);
        FungusThread thread2 = new FungusThread(5, false, thread);
        tekton1.addThread(thread2);
        thread2.addTekton(tekton1);
        log.stepIn("insect.move(thread);");
        insect.move(thread);
        log.stepOut("insect.move(thread);", false);
        /* if(insect.getRecent()==null){
            System.out.println("A rovar bridgre lépett, nem tartózkodik tektonon");
        } */
        log.stepIn("insect.move(thread2);");
        insect.move(thread2);
        log.stepOut("insect.move(thread2);", false);
        /* if(insect.getRecent()==tekton1){
            System.out.println("A rovar sikeresen új tektonra mozgott.");
        } */
    }

    public void sporulateSuccess() {
        System.out.println("Running test: sporulateSuccess");
        FungusThread thread2 = new FungusThread(5,false);
        tekton1.addThread(thread2);
        
        Scanner scanner = new Scanner(System.in);
        String key = log.askQ("tud spórát szórni a gombatest? (y/n)", true);
        if(key.equals("y")){
            body.setTekton(tekton1);
            tekton1.setBody(body);
            log.stepIn("tekton1.getBody().sporulate();");
            tekton1.getBody().sporulate();
            log.stepOut("tekton1.getBody().sporulate();", false);
            /* if(!neighborTekton.getSpores().isEmpty()){
                System.out.println("Sikeres spóraszórás");
            } */
        }
            if(key.equals("n")){
                FungusBody body2 = new FungusBody(0,3);
                body2.setTekton(tekton1);
                tekton1.setBody(body2);
                log.stepIn("tekton1.getBody().sporulate();");
                 tekton1.getBody().sporulate();
                log.stepOut("tekton1.getBody().sporulate();", false);
                /* if(neighborTekton.getSpores().isEmpty()){
                    System.out.println("A spóraszórás sikertelen.");
                } */
        } 
    }

    public void disableCutSporeConsumed() {
        System.out.println("Running test: disableCutSporeConsumed");
        Spore dcSpore = new DisableCutSpore();
        log.stepIn("dcSpore.consume(insect);");
        insect.consumeSpore(dcSpore);
        dcSpore.consume(insect);
        log.stepOut("dcSpore.consume(insect);", false);
        log.stepIn("insect.hasCutAbility();");
        insect.hasCutAbility();
        log.stepOut("insect.hasCutAbility();", false);
        /* if(!insect.hasCutAbility()){
            System.out.println("Disable Cut Spóra elfogyasztva, hatott a rovarra");
        }
        else
            System.out.println("Disable cut spóra hatása nem lépett érvénybe"); */
    }

    public void slowSporeConsumed() {
        System.out.println("Running test: slowSporeConsumed");
        Spore sSpore = new SlowSpore(tekton1, 10);
        log.stepIn("sSpore.consume(insect);");
        insect.consumeSpore(sSpore);
        sSpore.consume(insect);
        log.stepOut("sSpore.consume(insect);", false);
        log.stepIn("insect.gEffect();");
        insect.gEffect();
        log.stepOut("insect.gEffect();", false);
        /* if(insect.gEffect() == InsectEffects.SLOW){
            System.out.println("Slow Spóra elfogyasztva, hatott a rovarra");
        }
        else
            System.out.println("Slow spóra hatása nem lépett érvénybe"); */
    }

    public void insectCutThreadWhileNoCutAbility() {
        System.out.println("Running test: insectCutThreadWhileNoCutAbility");
        insect.setRecentTekton(tekton1);
        log.stepIn("insect.move(thread);");
        insect.move(thread);
        log.stepOut("insect.move(thread);", false);
       
        log.stepIn("insect.disableCut();");
        insect.disableCut();
        log.stepOut("insect.disableCut();", false);
        FungusThread thread2 = new FungusThread(5,false);
        tekton1.addThread(thread2);
        log.stepIn("insect.cut(thread2);");
        insect.cut(thread2);
        log.stepOut("insect.cut(thread2);", false);
        /* if(tekton1.getThreads().contains(thread2))
            System.out.println("A fonál elvágása sikertelen a hatás miatt");
        else
            System.out.println("A hatás nem gátolta a fonálvágást"); */
    }

    public void growBodyOnlyThreadTekton() {
        System.out.println("Running test: growBodyOnlyThreadTekton");
        OnlyThreadTekton temp = new OnlyThreadTekton();
        for (int i = 0; i < 4; i++) {
            Spore spore = new Spore();
            temp.addSpore(spore);
        }
        //System.out.println("Spore hozzáadva az OnlyThreadTekton: " + temp.getSpores().size());
        thread.addTekton(temp);
        temp.addThread(thread);
        //System.out.println("FungusThread hozzáadva az OnlyThreadTektonhoz: " + temp.getThreads().size());
        log.stepIn("species.growBody(thread);");
        species.growBody(thread);
        temp.setBody(body);
        log.stepOut("species.growBody(thread);", true);
        
    }

    public void insectCutThread() {
        System.out.println("Running test: insectCutThread");
        Tekton tekton2 = new Tekton(true, true);
        tekton1.addInsect(insect);
        insect.setRecentTekton(tekton1);
        tekton1.addThread(thread);
        thread.addTekton(tekton1);
        tekton2.addThread(thread);
        thread.addTekton(tekton2);
        log.stepIn("insect.cut(thread);");
        insect.cut(thread);
        log.stepOut("insect.cut(thread);", false);
        //System.out.println("Test: Insect szálvágása sikeres!");
    }

    public void speedSporeConsumed() {
        System.out.println("Running test: speedSporeConsumed");
        FastSpore fs = new FastSpore(tekton1,10);
        log.stepIn("fs.consume(insect);");
        insect.consumeSpore(fs);
        fs.consume(insect);
        log.stepOut("fs.consume(insect);", false);
        log.stepIn("insect.gEffect();");
        insect.gEffect();
        log.stepOut("insect.gEffect();", false);
        //System.out.println("Test: SpeedSporeConsumed sikeres!");
    }

    public void insectMoveWhileStunned() {
        System.out.println("Running test: insectMoveWhileStunned");
        Tekton tekton2 = new Tekton(true, true);
        tekton1.addInsect(insect);
        insect.setRecentTekton(tekton1);
        tekton1.addThread(thread);
        thread.addTekton(tekton1);
        tekton2.addThread(thread);
        thread.addTekton(tekton2);
        StunSpore ss = new StunSpore(tekton1,10);
        log.stepIn("insect.consumeSpore(ss);");
        insect.consumeSpore(ss);
        log.stepOut("insect.consumeSpore(ss);", false);
        log.stepIn("ss.consume(insect);");
        ss.consume(insect);
        log.stepOut("ss.consume(insect);", false);
        log.stepIn("insect.move(thread);");
        insect.move(thread);
        log.stepOut("insect.move(thread);", false);
        //System.out.println("Test: InsectMoveWhileStunned sikeres!");
    }

    public void growThreadDecomposingTektor() {
        System.out.println("Running test: growThreadDecomposingTektor");
        DecomposingTekton dt = new DecomposingTekton();
        /*Scanner scanner = new Scanner(System.in);
        System.out.println("Add meg a hozzáadni kívánt Spore számát (legalább 3): ");
        int sporeCount = scanner.nextInt();
        for (int i = 0; i < sporeCount; i++) {
            Spore spore = new Spore();
            dt.addSpore(spore);
        }
        System.out.println("Spore hozzáadva a DecomposingTekton: " + dt.getSpores().size());*/
        FungusThread othread=new FungusThread(5, false);
        species.growThread(dt,othread,thread);
        thread.addTekton(dt);
        dt.addThread(thread);
        /* System.out.println("FungusThread hozzáadva a DecomposingTektonhoz: " + dt.getThreads().size());
        if(thread.getIsDying()){
            System.out.println("A fonál haldoklik.");
        }
        else
            System.out.println("Nem sikerült a fonál haldoklását előidéznie a tektonnak."); */
    }

    public void insectMoveSuccess() {
        System.out.println("Running test: insectMoveSuccess");
        tekton1.addInsect(insect);
        FungusThread thread2 = new FungusThread(15, true);
        thread.addTekton(tekton1);
        tekton1.addThread(thread2);
        insect.setRecentTekton(tekton1);
        tekton1.addThread(thread);
        thread.addTekton(tekton1);
        log.stepIn("insect.move(thread2);");
        insect.move(thread2);
        log.stepOut("insect.move(thread2);", true);
        //System.out.println("Test: InsectMove sikeres!");
    }

    public void stunSporeConsumed() {
        System.out.println("Running test: stunSporeConsumed");
        Spore ss = new Spore(tekton1, 10);
        tekton1.addSpore(ss);
        System.out.println("asd "+ss.getTekton());
        log.stepIn("ss.consume(insect);");
        insect.consumeSpore(ss);
        ss.consume(insect);
        log.stepOut("ss.consume(insect);", false);
        log.stepIn("insect.gEffect();");
        insect.gEffect();
        log.stepOut("insect.gEffect();", false);
        //System.out.println("Test: StunSporeConsumed sikeres!");
    }

    public void sporulateFurther() {
        System.out.println("Running test: sporulateFurther");
        Tekton tekton2 = new Tekton(true, true);
        Tekton tekton3 = new Tekton(true, true);
        tekton1.addNeighbour(tekton2);
        tekton2.addNeighbour(tekton3);
        FungusBody body2 = new FungusBody(11, 5);
        body2.setTekton(tekton3);
        body2.produceSpore();
        body2.sporulate();
        /* if(tekton3.getSpores().size() == 0) {
            System.out.println("Nem sikerült a SporulateFurther!");
            return;
        }
        else {
            System.out.println("Test: SporulateFurther sikeres!");
        } */
        
    }

    public static void main(String[] args) {
        
        displayTests();
        getUserInput();

        Tests tests = new Tests();

        System.out.println("Starting all tests...");

        System.out.println("All tests finished.");
    }
}
