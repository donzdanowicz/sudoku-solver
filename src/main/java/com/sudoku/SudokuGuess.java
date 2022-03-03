package com.sudoku;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SudokuGuess {
    private final SudokuBoard deepCopy;
    private final int guessedX;
    private final int guessedY;
    private final int guessedValue;
    private final List<Integer> guessedPossibleValues = new ArrayList<>();

    public SudokuGuess(SudokuBoard deepCopy, int guessedX, int guessedY, int guessedValue) {
        this.deepCopy = deepCopy;
        this.guessedX = guessedX;
        this.guessedY = guessedY;
        this.guessedValue = guessedValue;
    }

    public void addToListOfGuessedPossibleValues(Set<Integer> possibleValues) {
        guessedPossibleValues.addAll(possibleValues);
    }

    public void removeFromListOfGuessedPossibleValues(Integer guessedValue) {
        guessedPossibleValues.remove(guessedValue);
    }

    public List<Integer> getGuessedPossibleValues() {
        return guessedPossibleValues;
    }

    public SudokuBoard getDeepCopy() {
        return deepCopy;
    }

    public int getGuessedX() {
        return guessedX;
    }

    public int getGuessedY() {
        return guessedY;
    }

    public int getGuessedValue() {
        return guessedValue;
    }
}
