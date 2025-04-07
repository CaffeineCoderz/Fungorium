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
     * Choose player type
     */
    public void playerchoosing() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the game!");
        Integer input = 0;
        while (input > 4) {        
            System.out.println("How many players are there? (Atleast 4 player needed):");
            input = scanner.nextInt();
        }
        
        int MycoCount = 0;
        int EntoCount =0;
        for(Integer i=0; i < input; i++){
            System.out.println("Player "+ i+1 +"\nChoose between Entomologists[1] and Mycologist[2]: (1/2)");
            Integer key = scanner.nextInt();
            if (key == 1) {
                if (EntoCount > Math.round(input/2)) {
                    System.out.println("Too many entomologist. You have to choose Mycologist");
                    i--;
                }
            }else if(key == 2){
                if (EntoCount > Math.round(input/2)) {
                    System.out.println("Too many Mycologist. You have to choose Entomologist");
                    i--;
                }
            }
        }
    }
    /**
     * 
     */
    public void initBaseMap(){

    }
    
}
