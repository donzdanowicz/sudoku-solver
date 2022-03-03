package com.sudoku;

import java.util.ArrayList;
import java.util.List;

public class SudokuRow {
    private SudokuElement element;
    private final List<SudokuElement> elements = new ArrayList<>();

    public SudokuRow(SudokuElement element) {
        this.element = element;
    }

    public void addElements() {
        for(int i = 0; i < 9; i++) {
            elements.add(new SudokuElement());
        }
    }

    public List<SudokuElement> getElements() {
        return elements;
    }
}
