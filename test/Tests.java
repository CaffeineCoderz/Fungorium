package test;

public class Tests {
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

    public void growBodyDefTektonUnSuccessNotEnoughSpare() {
        System.out.println("Running test: growBodyDefTektonUnSuccessNotEnoughSpare");
    }

    public void growBodyDefTektonUnSuccessAlreadyBody() {
        System.out.println("Running test: growBodyDefTektonUnSuccessAlreadyBody");
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
        Tests tests = new Tests();

        System.out.println("Starting all tests...");

        tests.growBodyDefTektonSuccess();
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
        tests.growBodyDefTektonUnSuccessNotEnoughSpare();
        tests.growBodyDefTektonUnSuccessAlreadyBody();
        tests.growThreadDecomposingTektor();
        tests.insectMoveSuccess();
        tests.stunSporeConsumed();
        tests.sporulateUnSuccess();
        tests.sporulateFurther();
        tests.insectMoveUnsuccess();

        System.out.println("All tests finished.");
    }
}