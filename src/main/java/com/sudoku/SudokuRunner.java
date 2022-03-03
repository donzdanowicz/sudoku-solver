package com.sudoku;

public class SudokuRunner {
    public static void main(String[] args) {
        boolean gameFinished = false;

        while (!gameFinished) {
            SudokuGame theGame = new SudokuGame();
            theGame.getBoard().addBoardElements();
            theGame.gameCore();
            gameFinished = theGame.resolveSudoku();

        }
    }
}
