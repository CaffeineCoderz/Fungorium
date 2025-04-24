package interfaces;

import commands.CommandProcessor;

public interface iControl {
    /**
     * Adds the specified amount to the score.
     *
     * @param x The amount to add to the score.
     */
    void addScore(Integer x);

    /**
     * Decreases the score by the specified amount.
     *
     * @param x The amount to subtract from the score.
     */
    void decreaseScore(Integer x);

    /**
     * Handles the elapsed time for a given round.
     *
     */
    void timeElapsed();

    /**
     * Handles the elapsed time for a given round.
     *
     */
    void timeElapsed(CommandProcessor commandProcessor);
}