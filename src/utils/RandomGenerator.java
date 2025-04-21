package utils;
import sporeTypes.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;


//!
//! 
//!
//? Amúgy ez mehet akár GameLogicba nem?
public class RandomGenerator {
    private static final Map<Class<? extends Spore>, Double> sporeProbabilities = new LinkedHashMap<>();
    private static double totalProbability = 0.0;
    private static final Random random = new Random();

    // Alapértelmezett valószínűségek beállítása
    static {
        setDefaultProbabilities();
    }

    private static void setDefaultProbabilities() {
        sporeProbabilities.put(Spore.class, 0.30);
        sporeProbabilities.put(StunSpore.class, 0.15);
        sporeProbabilities.put(FastSpore.class, 0.15);
        sporeProbabilities.put(SlowSpore.class, 0.);
        sporeProbabilities.put(DisableCutSpore.class, 0.15);
        sporeProbabilities.put(MultiplyInsectSpore.class, 0.10);
        calculateTotalProbability();
    }

    private static void calculateTotalProbability() {
        totalProbability = sporeProbabilities.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    public static Integer generateRandomNumber(Integer min, Integer max) {
        return (int)(Math.random() * ((max - min) + 1)) + min;
    }

    /**
     * Generates a random Spore instance based on configured probabilities
     */
    public static Spore generateRandomSpore() {
        double randomValue = random.nextDouble() * totalProbability;
        double cumulativeProbability = 0.0;

        for (Map.Entry<Class<? extends Spore>, Double> entry : sporeProbabilities.entrySet()) {
            cumulativeProbability += entry.getValue();
            if (randomValue <= cumulativeProbability) {
                try {
                    return entry.getKey().getDeclaredConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    throw new RuntimeException("Failed to create Spore instance: " + entry.getKey().getSimpleName(), e);
                }
            }
        }

        // Fallback - should normally never reach here if probabilities are configured correctly
        return new Spore();
    }

    /**
     * Updates the probability for a specific Spore type
     */
    public static void setSporeProbability(Class<? extends Spore> sporeClass, double probability) {
        if (probability < 0) {
            throw new IllegalArgumentException("Probability cannot be negative");
        }
        sporeProbabilities.put(sporeClass, probability);
        calculateTotalProbability();
    }

    /**
     * Gets all currently configured Spore types with their probabilities
     */
    public static Map<Class<? extends Spore>, Double> getSporeProbabilities() {
        return Collections.unmodifiableMap(sporeProbabilities);
    }

    /**
     * Adds a new Spore type with its probability
     */
    public static void addSporeType(Class<? extends Spore> sporeClass, double probability) {
        setSporeProbability(sporeClass, probability);
    }

    /**
     * Resets all probabilities to default values
     */
    public static void resetToDefaultProbabilities() {
        sporeProbabilities.clear();
        setDefaultProbabilities();
    }

    //! Ha akarunk, akkor ide tudunk a komplextesztekhez csinálni egy olyan függvény, amitől előre meghatározott spóra típusokat szór a gombatest. 
}
