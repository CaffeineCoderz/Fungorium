package pkg.utils;

public class RandomGenerator {
    public static Integer generateRandomNumber(Integer min, Integer max){
        return (int)(Math.random() * ((max - min) + 1)) + min;
    }
}
