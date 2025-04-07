package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fungus.FungusSpecies;
import insect.InsectSpecies;

public class GameLogic {
    private List<FungusSpecies> fungusSpecies;
    private List<InsectSpecies> insectSpecies;

    public GameLogic() {
        fungusSpecies = new ArrayList<FungusSpecies>();
        insectSpecies = new ArrayList<InsectSpecies>();
    }

    /**
     * Starts the game and provides a text-based interface for the player.
     * 
     * This method continuously displays a menu with options for the player to
     * interact with the game. The player can add or remove mycologists, fungus
     * species, insectSpecies, and insect species. The player can also choose to
     * exit the game. The method reads the user's input and calls the appropriate
     * methods to perform the selected actions.
     */
    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the game!");

        while (true) {
            System.out.println("What would you like to do?");
            System.out.println("1. Add a fungusSpecies");
            System.out.println("2. Add a fungus species");
            System.out.println("3. Remove a fungusSpecies");
            System.out.println("4. Remove a fungus species");
            System.out.println("5. Add an entymologist");
            System.out.println("6. Remove an entymologist");
            System.out.println("7. Add an insect species");
            System.out.println("8. Remove an insect species");
            System.out.println("9. Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    //addFungusSpecies();
                    break;
                case 2:
                    // addFungusSpecies();
                    break;
                case 3:
                    // removeFungusSpecies();
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

    /**
     * Adds a fungusSpecies to the list of mycologists.
     * 
     * @param fungusSpecies The fungusSpecies to be added.
     */
    public void addFungusSpecies(FungusSpecies fungusSpecies) {
        this.fungusSpecies.add(fungusSpecies);
    }

    /**
     * Removes a fungusSpecies from the list of mycologists.
     * 
     * @param fungusSpecies The fungusSpecies to be removed.
     */
    public void removeFungusSpecies(FungusSpecies fungusSpecies) {
        this.fungusSpecies.remove(fungusSpecies);
    }

    /**
     * Adds an insectSpecies to the list of insectSpecies.
     * 
     * @param insectSpecies The insectSpecies to be added.
     */
    public void addInsectSpecies(InsectSpecies insectSpecies) {
        this.insectSpecies.add(insectSpecies);
    }

    /**
     * Removes an insectSpecies from the list of insectSpecies.
     * 
     * @param insectSpecies The insectSpecies to be removed.
     */
    public void removeInsectSpecies(InsectSpecies insectSpecies) {
        this.insectSpecies.remove(insectSpecies);
    }
}
