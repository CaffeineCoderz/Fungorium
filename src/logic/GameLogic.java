package logic;

import java.util.Scanner;

public class GameLogic {
    public void displayTests() {
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

    public void getUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the test case number: ");
        int testCase = scanner.nextInt();
        scanner.close();
        // ! not yet implemented
        // executeTestCase(testCase);
    }

    /* 
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
                growBodyDefTektonUnSuccessNotEnoughSpare();
                break;
            case 14:
                growBodyDefTektonUnSuccessAlreadyBody();
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
            default:
                System.out.println("Invalid test case number.");
                break;
        }
    }
    */
}
