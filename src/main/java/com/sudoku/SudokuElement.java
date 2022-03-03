package com.sudoku;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SudokuElement {
    public static int EMPTY = -1;
    private int value = EMPTY;
    private int x;
    private int y;
    private Set<Integer> possibleValues = new HashSet<>(Arrays.asList(1,2,3,4,5,6,7,8,9));

    public void removePossibleValue(Integer value) {
        possibleValues.remove(value);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Set<Integer> getPossibleValues() {
        return possibleValues;
    }

    public void setPossibleValues(Set<Integer> possibleValues) {
        this.possibleValues = possibleValues;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
