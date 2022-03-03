package com.sudoku;

import com.sudoku.prototype.Prototype;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SudokuBoard extends Prototype<SudokuBoard> {
    private List<SudokuRow> rows = new ArrayList<>();
    public final static int MIN_INDEX = 0;
    public final static int MAX_INDEX = 8;

    public SudokuBoard(SudokuRow row) {
    }

    public void addBoardElements() {
        for(int i = 0; i < 9; i++) {
            rows.add(new SudokuRow(new SudokuElement()));
            rows.get(i).addElements();

        }
    }

    public List<SudokuRow> getRows() {
        return rows;
    }

    public String toString() {
        String result = "";
        result += "-----------------------------------------\n";
        for (int y = MIN_INDEX; y <= MAX_INDEX; y++) {

                result += "||" ;

            for (int x = MIN_INDEX; x <= MAX_INDEX; x++) {
                if (getRows().get(y).getElements().get(x).getValue() == -1) {
                    result += "   ";
                } else {
                    result += " " + getRows().get(y).getElements().get(x).getValue() + " ";
                }

                if (x == 2 || x == 5 || x == 8) {
                    result += "||";
                } else {
                    result += "|";
                }



            }
            if (y == 2 || y == 5 || y == 8) {
                result += "\n-----------------------------------------\n";
            } else {
                result += "\n";
            }
        }
        return result;
    }

    public SudokuBoard deepCopy() throws CloneNotSupportedException {
        SudokuBoard clonedBoard = super.clone();
        clonedBoard.rows = new ArrayList<>();
        for (SudokuRow theRow : rows) {
            SudokuRow clonedList = new SudokuRow(new SudokuElement());
            for (SudokuElement element : theRow.getElements()) {
                SudokuElement sudokuElement = new SudokuElement();
                sudokuElement.setValue(element.getValue());
                Set<Integer> clonedPossibleValues = new HashSet<>();
                clonedPossibleValues.addAll(element.getPossibleValues());
                sudokuElement.setPossibleValues(clonedPossibleValues);
                clonedList.getElements().add(sudokuElement);
                }

            clonedBoard.getRows().add(clonedList);
        }
        return clonedBoard;
    }

}
