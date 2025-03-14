package TektonTypes;

public class OnlyThreadTekton extends Tekton{
    // ! Ezt valahogy override-olni kéne, canGrowBody sztem protected kéne legyen és akk nem kéne overridolni
    public void override setGrowBody() {
        canGrowBody = false;
    }
}
