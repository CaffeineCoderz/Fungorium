package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fungus.FungusSpecies;
import fungus.FungusThread;
import insect.Insect;
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
    
    //! t1 -> newt <-t2 "Fák gyökerei sem nőnek össze. No para"
    private Boolean canReachThread(Insect insect, FungusThread toThread){
        Integer distance = 2;
        switch (insect.gEffect()) {
            case SLOW:
                distance = 1;
                break;
            case FAST:
                distance = 3;
                break;
            default:
                distance = 2;
                break;
        }
        FungusThread temp = insect.getThread().getPrev();
        for (Integer i = 0; i < distance ; i++) {
            if (temp == toThread) {
                return true;   
            }
            //? a Body-hoz értünk meg kell nézni, hogy ér-e el másik threadet a bodyból
            else if (temp == null) {
                Integer remainingDistance = distance-i-1; //Mivel az hogy rálép a Body-ra az is egy lépés, 
                //szóval Body-ból kijövő fonalak közti váltás az nem 1 hanem 2 lépés
                //0: nem csinál semmit,
                //1: body-ból kinövő threadeket nézi, 
                //2: 1-es és a threadek szomszédai
                if(canReachFromBody(toThread, temp, remainingDistance)) return true;
                break;
            }
            temp = temp.getPrev();
        }

        temp = insect.getThread().getNext();
        for (Integer i = 0; i < distance ; i++) {
            if (temp == toThread) {
                return true;   
            }else if (temp == null) {
                break;
            }
            if (temp.getNext() == null){
                if (toThread.getBody() == temp.getBody()){
                    if(canReachFromBody(toThread, temp, distance-i-1)) return true;
                }
                break;
            }
            temp = temp.getNext();
        }
        return false;
    }

    private Boolean canReachFromBody(FungusThread toThread, FungusThread temp,Integer distance){
        for (FungusThread bodyThreads :temp.getBody().getThreads()) {
            if (bodyThreads == toThread) {
                return true;
            }else{
                Boolean dirChange = false;
                FungusThread bodyThreadtemp;
                //? t3-ből növesztettük a Body-t és a body-ból t1-et és t2-t
                //? t1<- FBody ->t2
                //?        ^
                //?        |
                //?        t3
                if (bodyThreads.getNext() == null && bodyThreads.getPrev() != null) {
                    bodyThreadtemp = bodyThreads.getPrev();
                    dirChange = true;
                }else{
                    bodyThreadtemp = bodyThreads.getNext();
                    dirChange = false;
                }

                for (int j = 0; j < distance; j++) {
                    if(bodyThreadtemp == toThread){
                        return true;
                    }
                    if (dirChange) {
                        bodyThreadtemp = bodyThreadtemp.getPrev();
                    }else
                        bodyThreadtemp = bodyThreadtemp.getNext();
                }
            }
        }
        return false;
    }
}
