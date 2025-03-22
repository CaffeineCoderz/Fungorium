package test;

import fungus.FungusBody;
import fungus.FungusThread;
import insect.Insect;
import java.util.Scanner;
import logic.GameLogic;
import sporeTypes.Spore;
import tektonTypes.DecreasingTekton;
import tektonTypes.Tekton;

public class Tests {
    
    private Tekton tekton1;
    private DecreasingTekton Dtekton;
    private Tekton neighborTekton;
    private Spore spore1;
    private Spore spore2;
    private Spore spore3;
    private FungusBody body;
    private FungusThread thread;


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
    public void setup(){
        GameLogic gameLogic = new GameLogic();

        System.out.println("Fungorium szimuláció elindult!");
        // Initialize a Tekton object
        tekton1 = new Tekton(true, true);
        System.out.println("Tekton initialized: " + tekton1);

        // Initialize a DecreasingTekton object
        DecreasingTekton decreasingTekton = new DecreasingTekton();
        System.out.println("DecreasingTekton initialized: " + decreasingTekton);

        // Add some spores to the Tekton
        spore1 = new Spore();
        spore2 = new Spore();
        tekton1.addSpore(spore1);
        tekton1.addSpore(spore2);
        System.out.println("Spores added to Tekton: " + tekton1.getSpores().size());

        // Add an insect to the Tekton
        Insect insect = new Insect();
        tekton1.addInsect(insect);
        System.out.println("Insect added to Tekton: " + tekton1.hasInsect());

        // Add a neighbor Tekton
        neighborTekton = new Tekton(false, true);
        tekton1.addNeighbour(neighborTekton);
        System.out.println("Neighbor Tekton added: " + tekton1.getNeighbours().size());

        // Add a thread and body to the Tekton
        thread = new FungusThread(15, true); 
        thread.addTekton(tekton1); 
        body = new FungusBody(10, 5); 
        body.addThread(thread); 
        body.setTekton(tekton1);
        tekton1.addThread(thread);
        tekton1.setBody(body);
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
            case 13:
                break;
            case 15:
                growThreadDecomposingTektor();
                break;
            case 16:
                insectMoveSuccess();
                break;
            case 17:
                stunSporeConsumed();
                break;
            case 18:
                sporulateUnSuccess();
                break;
            case 19:
                sporulateFurther();
                break;
            case 20:
                insectMoveUnsuccess();
                break;
            case 50:
                //exitTests();
                break;
            default:
                System.out.println("Invalid test case number.");
                break;
        }
    }
    

    public void growBodyDefTektonSuccess() {
        System.out.println("Running test: growBodyDefTektonSuccess");

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

    public static void main(String[] args) {
        
        displayTests();
        getUserInput();
        

        Tests tests = new Tests();

        System.out.println("Starting all tests...");

        System.out.println("All tests finished.");
    }
}