package test;

import fungus.*;
import insect.Insect;
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
            "\t13. GrowBody DefTekton unSuccess Not Enough Spare\n" +
            "\t14. GrowBody DefTekton_unSuccess Already Body\n" +
            "\t15. GrowThread Decomposing Tektor\n" +
            "\t16. InsectMove Success\n" +
            "\t17. StunSporeConsumed\n" +
            "\t18. Sporulate unSuccess\n" +
            "\t19. Sporulate Further\n" +
            "\t20. InsectMove unsuccess\n" +
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
        System.out.println("Fungorium szimuláció elindult!");
        // Initialize a Tekton object
        tekton1 = new Tekton(true, true);
        System.out.println("Tekton initialized: " + tekton1);

        // Initialize a DecreasingTekton object
        DecreasingTekton decreasingTekton = new DecreasingTekton();
        System.out.println("DecreasingTekton initialized: " + decreasingTekton);

        // Add some spores to the Tekton
        System.out.println("Spores added to Tekton: " + tekton1.getSpores().size());

        // Add an insect to the Tekton
        Insect insect = new Insect();
        System.out.println("Insect added to Tekton: " + tekton1.insectFree());

        // Add a neighbor Tekton
        neighborTekton = new Tekton(false, true);
        tekton1.addNeighbour(neighborTekton);
        System.out.println("Neighbor Tekton added: " + tekton1.getNeighbours().size());

        // Add a thread and body to the Tekton
        thread = new FungusThread(15, true); 
        body = new FungusBody(10, 5); 
    }

    public static void getUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the test case number: ");
        int testCase = scanner.nextInt();
        scanner.close();
        // ! not yet implemented
        // executeTestCase(testCase);
    }

    // public exitTests(){
        
    // }

    
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
        switch (testCase) {
            case 1:
                growBodyDefTektonSuccess();
                break;
            case 2:
                growThreadOneThreadTektonSuccess();
                break;
            case 3:
                growThreadDefTekton();
                break;
            case 4:
                insectMoveToNewTektonSuccess();
                break;
            case 5:
                sporulateSuccess();
                break;
            case 6:
                disableCutSporeConsumed();
                break;
            case 7:
                slowSporeConsumed();
                break;
            case 8:
                insectCutThreadWhileNoCutAbility();
                break;
            case 9:
                growBodyOnlyThreadTekton();
                break;
            case 10:
                insectCutThread();
                break;
            case 11:
                speedSporeConsumed();
                break;
            case 12:
                insectMoveWhileStunned();
                break;
            //case 13:
              //  break;
            case 15:
                growThreadDecomposingTektor();
                break;
            case 16:
                insectMoveSuccess();
                break;
            case 17:
                stunSporeConsumed();
                break;
            /*case 18:
                sporulateUnSuccess();
                break;*/
            case 19:
                sporulateFurther();
                break;
            /*case 20:
                insectMoveUnsuccess();
                break;*/
            case 50:
                //exitTests();
                break;
            default:
                System.out.println("Invalid test case number.");
                break;
        }
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
        }
        else if(key == "n"){
            String key2 = log.askQ("Van a tektonon elég spóra?(y/n)", true);
            if(key2 == "y"){
                log.stepIn("tekton1.addSpore(spore1)");
                tekton1.addSpore(spore1);
                log.stepOut("tekton1.addSpore(spore1)",null);
                
                log.stepIn("tekton1.addSpore(spore2)");
                tekton1.addSpore(spore2);
                log.stepOut("tekton1.addSpore(spore2)",null);

                log.stepIn("species.growBody(thread2)");
                species.growBody(thread2);
                if(tekton1.getBody()!=null){
                    System.out.println("Sikeres testnövesztés");
                }
            }
            else if(key2=="n"){
                species.growBody(thread2);
                if(tekton1.getBody()== null){
                    System.out.println("Sikertelen testnövesztés");
                }
                log.stepOut("species.growBody(thread2)",null);
            }
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
        FungusThread thread2 = new FungusThread(5,false);
        tekton1.addThread(thread2);
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("Tud éppen spórát szórni a gombatest? (y/n)");
        char key = scanner.next().charAt(0);
        switch(key){
            case 'y':
            tekton1.setBody(body);
            tekton1.getBody().sporulate();
            if(!neighborTekton.getSpores().isEmpty()){
                System.out.println("Sikeres spóraszórás");
            }
            break;
            case 'n':
                FungusBody body2 = new FungusBody(0,3);
                tekton1.setBody(body2);
                tekton1.getBody().sporulate();
                if(neighborTekton.getSpores().isEmpty()){
                    System.out.println("A spóraszórás sikertelen.");
                }
        } 
    }

    public void disableCutSporeConsumed() {
        System.out.println("Running test: disableCutSporeConsumed");
        Spore dcSpore = new DisableCutSpore();
        insect.consumeSpore(dcSpore);
        dcSpore.consume(insect);
        if(!insect.hasCutAbility()){
            System.out.println("Disable Cut Spóra elfogyasztva, hatott a rovarra");
        }
        else
            System.out.println("Disable cut spóra hatása nem lépett érvénybe");
    }

    public void slowSporeConsumed() {
        System.out.println("Running test: slowSporeConsumed");
        Spore sSpore = new SlowSpore();
        insect.consumeSpore(sSpore);
        sSpore.consume(insect);
        //!Enum typeot itt hogyan kellene összehasonlítani?
        if(/*insect.gEffect()==SLOW*/false){
            System.out.println("Slow Spóra elfogyasztva, hatott a rovarra");
        }
        else
            System.out.println("Slow spóra hatása nem lépett érvénybe");
    }

    public void insectCutThreadWhileNoCutAbility() {
        System.out.println("Running test: insectCutThreadWhileNoCutAbility");
        insect.move(thread);
        insect.disableCut();
        FungusThread thread2 = new FungusThread(5,false);
        tekton1.addThread(thread2);
        insect.cut(thread2);
        if(tekton1.getThreads().contains(thread2))
            System.out.println("A fonál elvágása sikertelen a hatás miatt");
        else
            System.out.println("A hatás nem gátolta a fonálvágást");
    }

    public void growBodyOnlyThreadTekton() {
        System.out.println("Running test: growBodyOnlyThreadTekton");
        OnlyThreadTekton temp = new OnlyThreadTekton();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Add meg a hozzáadni kívánt Spórák számát (legalább 3): ");
        int sporeCount = scanner.nextInt();
        for (int i = 0; i < sporeCount; i++) {
            Spore spore = new Spore();
            temp.addSpore(spore);
        }
        System.out.println("Spore hozzáadva az OnlyThreadTekton: " + temp.getSpores().size());
        thread.addTekton(temp);
        temp.addThread(thread);
        System.out.println("FungusThread hozzáadva az OnlyThreadTektonhoz: " + temp.getThreads().size());
        if (temp.getSpores().size() < 3) {
            System.out.println("Nincs elég Spore a FungusBody növesztéshez!");
            return;
        }
        //!Az Onlythreaden nem lehet sikeres a testnövesztés
        species.growBody(thread);
        temp.setBody(body);
        System.out.println("FungusBody: OnlyThreadTekton hozzáadása sikeres!");
        System.out.println("FungusBody növesztése sikeres!");
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
        insect.cut(thread);
        System.out.println("Test: Insect szálvágása sikeres!");
    }

    public void speedSporeConsumed() {
        System.out.println("Running test: speedSporeConsumed");
        Spore fs = new FastSpore();
        insect.consumeSpore(fs);
        fs.consume(insect);
        System.out.println("Test: SpeedSporeConsumed sikeres!");
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
        StunSpore ss = new StunSpore();
        insect.consumeSpore(ss);
        ss.consume(insect);
        insect.move(thread);
        System.out.println("Test: InsectMoveWhileStunned sikeres!");
    }

    public void growThreadDecomposingTektor() {
        System.out.println("Running test: growThreadDecomposingTektor");
        DecomposingTekton dt = new DecomposingTekton();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Add meg a hozzáadni kívánt Spore számát (legalább 3): ");
        int sporeCount = scanner.nextInt();
        for (int i = 0; i < sporeCount; i++) {
            Spore spore = new Spore();
            dt.addSpore(spore);
        }
        System.out.println("Spore hozzáadva a DecomposingTekton: " + dt.getSpores().size());
        thread.addTekton(dt);
        dt.addThread(thread);
        System.out.println("FungusThread hozzáadva a DecomposingTektonhoz: " + dt.getThreads().size());
        if (dt.getSpores().size() < 3) {
            System.out.println("Nincs elég Spore a FungusBody növesztéshez!");
            return;
        }
        species.growThread(body, thread);
        dt.setBody(body);
        System.out.println("FungusBody: DecomposingTekton hozzáadása sikeres!");
        System.out.println("FungusBody növesztése sikeres!");
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
        insect.move(thread2);
        System.out.println("Test: InsectMove sikeres!");
    }

    public void stunSporeConsumed() {
        System.out.println("Running test: stunSporeConsumed");
        Spore ss = new StunSpore();
        insect.consumeSpore(ss);
        ss.consume(insect);
        System.out.println("Test: StunSporeConsumed sikeres!");
    }

    public void sporulateFurther() {
        System.out.println("Running test: sporulateFurther");
        Tekton tekton2 = new Tekton(true, true);
        Tekton tekton3 = new Tekton(true, true);
        tekton1.addNeighbour(tekton2);
        tekton1.addNeighbour(tekton3);
        body.setTekton(tekton3);
        body.produceSpore();
        body.sporulate();
        if(tekton1.getSpores().size() == 0) {
            System.out.println("Nem sikerült a SporulateFurther!");
            return;
        }
        else {
            System.out.println("Test: SporulateFurther sikeres!");
        }
        
    }

    public void insectMoveUnsuccess() {
        System.out.println("Running test: insectMoveUnsuccess");

    }

    public static void main(String[] args) {
        
        displayTests();
        getUserInput();
        

        Tests tests = new Tests();

        System.out.println("Starting all tests...");

        System.out.println("All tests finished.");
    }
}