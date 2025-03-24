package utils;

public class RandomGenerator {
    /**
     * Generates a random number between two values.
     * 
     * @param min the minimum value of the random number
     * @param max the maximum value of the random number
     */
    public static Integer generateRandomNumber(Integer min, Integer max){
        return (int)(Math.random() * ((max - min) + 1)) + min;
    }
}
