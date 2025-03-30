package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fungus.FungusSpecies;
import fungus.Mycologist;
import insect.Entomologist;

public class GameLogic {
    private List<Mycologist> mycologists;
    private List<FungusSpecies> species;

    public GameLogic() {
        mycologists = new ArrayList<Mycologist>();
        species = new ArrayList<FungusSpecies>();
    }

    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the game!");

        while (true) {
            System.out.println("What would you like to do?");
            System.out.println("1. Add a mycologist");
            System.out.println("2. Add a fungus species");
            System.out.println("3. Remove a mycologist");
            System.out.println("4. Remove a fungus species");
            System.out.println("5. Add an entymologist");
            System.out.println("6. Remove an entymologist");
            System.out.println("7. Add an insect species");
            System.out.println("8. Remove an insect species");
            System.out.println("9. Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    //addMycologist();
                    break;
                case 2:
                    // addFungusSpecies();
                    break;
                case 3:
                    // removeMycologist();
                    break;
                case 4:
                    // removeFungusSpecies();
                    break;
                case 5:
                    // addEntymologist();
                    break;
                case 6:
                    // removeEntymologist();
                    break;
                case 7:
                    // addInsectSpecies();
                    break;
                case 8:
                    // removeInsectSpecies();
                    break;
                case 9:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // gombász hozzáadása, úgy hogy egy kiválasztott gombafajtához hozzáadja a gombászt 
    public void addMycologist(Mycologist mycologist, FungusSpecies species) {
        addFungusSpecies(species);
    }

    // gombafaj hozzáadása a játékhoz
    public void addFungusSpecies(FungusSpecies species) {
        
    }

    // gombász eltávolítása, úgy hogy egy kiválasztott gombafajtáról eltávolítja a gombászt
    public void removeMycologist(Mycologist mycologist, FungusSpecies species) {
        removeFungusSpecies(species);
    }

    // gombafaj eltávolítása a játékból
    public void removeFungusSpecies(FungusSpecies species) {
        
    }

}
