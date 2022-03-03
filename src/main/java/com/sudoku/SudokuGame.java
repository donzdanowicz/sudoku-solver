package com.sudoku;

import java.util.*;

public class SudokuGame {
    public SudokuBoard board = new SudokuBoard(new SudokuRow(new SudokuElement()));
    private String usersNumbers;
    private boolean notZero = false;
    private final List<SudokuGuess> sudokuGuesses = new ArrayList<>();
    Scanner scan = new Scanner(System.in);
    private boolean isSudokuIncorrect = false;
    private static final int MIN_INDEX = 0;
    private static final int MAX_INDEX = 8;
    private static final String SUDOKU = "SUDOKU";
    private static final String COMMA = ",";
    private static final String THERE_ALREADY_WAS = "There already was such an element";
    private static final String POSSIBLE_VALUES_EMPTY= "getPossibleValues is empty";
    private static final String VALUE_ALREADY_EXISTS = "This value already exists in a row, column or block " +
            "it can't be chosen, because it would cause a paradox.";
    private static final String FIRST_TWO_NUMBERS = "First two numbers are x (row) and y (column) - board coordinates. "
            + "Third number is a value.";
    private static final String TYPE_IN = "Type in 3 numbers (from 1 to 9) " +
            "separated by comma (without space) e.g. 3,5,5";
    private static final String DIFFICULTY = "Difficulty of sudoku: ";
    private static final String EASY = "Easy.";
    private static final String MEDIUM = "Medium.";
    private static final String HARD = "Hard.";
    private static final String KILLER = "Killer.";
    private static final String SUDOKU_INCORRECT = "Sudoku is incorrect.";
    private static final String SUDOKU_SOLVED = "Sudoku solved.";
    private static final String WRONG_KEYS = "Wrong keys. Type in SUDOKU to solve sudoku \n" +
            "or 3 numbers (from 1 to 9) separated by comma e.g. 3,5,5";
    private static final String SOMETHING_WRONG = "Something went wrong: ";
    private static final String PARADOX = "Paradox.";
    private static final String BEST_ELEMENTS_LIST_IS_EMPTY = "Best elements to choose list is empty";
    private static final String TOO_MUCH_ITERATIONS = "n = 999. Too much iterations!";
    private static final String THIS_VALUE_WOULD_CAUSE_PARADOX = "Using this value would cause a paradox";
    private static final String DO_YOU_WANT_TO_PLAY_AGAIN = "Do you want to play again? Type in YES or NO";
    private static final String YES = "YES";

    public boolean isSudoku() {
        return SUDOKU.equalsIgnoreCase(usersNumbers);
    }

    public boolean usersKeysAreCorrect() {
        if (isSudoku()) {
            return true;
        } else if (usersNumbers.length() == 5) {
            String digits = String.valueOf(usersNumbers.charAt(0)) + usersNumbers.charAt(2) + usersNumbers.charAt(4);
            boolean areDigits = digits.chars().allMatch(Character::isDigit);
            if(areDigits) {
                notZero = Integer.parseInt(usersNumbers.substring(0, 1)) > 0
                        && Integer.parseInt(usersNumbers.substring(2, 3)) > 0
                        && Integer.parseInt(usersNumbers.substring(4, 5)) > 0;
            }
            return areDigits && notZero && COMMA.equals(usersNumbers.substring(1, 2))
                    && COMMA.equals(usersNumbers.substring(3, 4));
        }
        return false;
    }

    public void setBoard(SudokuBoard board) {
        this.board = board;
    }

    public void serveNewEntry(int x, int y, int value) {

        SudokuElement element = getBoard().getRows().get(x).getElements()
                .get(y);
        if(element.getPossibleValues().contains(value)
                && isThereAParadox(x, y, value)
                && element.getValue() == -1) {
            element.setValue(value);
            removePossibleValue(x,y,value);
            element.getPossibleValues().removeAll(element.getPossibleValues());
        } else if (element.getValue() != -1) {
            System.out.println(THERE_ALREADY_WAS);
        } else if (element.getPossibleValues().size() == 0) {
            System.out.println(POSSIBLE_VALUES_EMPTY);
        } else {
            System.out.println(VALUE_ALREADY_EXISTS);
        }
    }

    public void solveSudoku() {
        for (int y = MIN_INDEX; y <= MAX_INDEX; y++) {
            for (int x = MIN_INDEX; x <= MAX_INDEX; x++) {
                SudokuElement element = board.getRows().get(x).getElements().get(y);
                if (element.getValue() == -1) {
                    if (element.getPossibleValues().size() == 1) {
                        element.setValue(element.getPossibleValues().stream().findFirst().orElse(-1));
                        element.getPossibleValues().removeAll(element.getPossibleValues());
                        removePossibleValue(x, y, element.getValue());
                    }
                } else {
                    element.getPossibleValues().removeAll(element.getPossibleValues());
                }
            }
        }
    }

    public int howManyChangesInPossibleValues(SudokuBoard board) {
        int possibleValuesCounter = 0;
        for(int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                possibleValuesCounter += board.getRows().get(i).getElements().get(j).getPossibleValues().size();
            }
        }
        return possibleValuesCounter;
    }

    public int findMinimumCountOfPossibleValues() {
        List<SudokuElement> allElementsWithoutValue = new ArrayList<>();
        for (int y = MIN_INDEX; y <= MAX_INDEX; y++) {
            for (int x = MIN_INDEX; x <= MAX_INDEX; x++) {
                SudokuElement element = board.getRows().get(x).getElements().get(y);
                if (element.getValue() == -1)
                    allElementsWithoutValue.add(board.getRows().get(x).getElements().get(y));
            }
        }

        return allElementsWithoutValue.stream()
                .map(t -> t.getPossibleValues().size())
                .filter(v -> v != 0)
                .min(Integer::compare)
                .get();
    }

    public int getXAndYOfAnElement(SudokuElement element, int xOY1) {
        int[] xy = new int[2];
        for (int y = MIN_INDEX; y <= MAX_INDEX; y++) {
            for (int x = MIN_INDEX; x <= MAX_INDEX; x++) {
                SudokuElement foundElement = board.getRows().get(x).getElements().get(y);

                if (element.equals(foundElement)) {
                    xy[0] = x;
                    xy[1] = y;
                }
            }
        }
        return xy[xOY1];
    }

    public void addToBackTrackList(SudokuBoard deepCopy, int guessedX, int guessedY, int guessedValue) {
        SudokuGuess sudokuGuess = new SudokuGuess(deepCopy, guessedX, guessedY, guessedValue);
        sudokuGuess.addToListOfGuessedPossibleValues(getBoard()
                .getRows().get(guessedX).getElements().get(guessedY).getPossibleValues());
        sudokuGuess.removeFromListOfGuessedPossibleValues(guessedValue);
        sudokuGuesses.add(sudokuGuess);
    }

    public void instructions() {
        System.out.println(FIRST_TWO_NUMBERS);
        System.out.println(TYPE_IN);

        System.out.println(getBoard());
    }

    public void evaluateDifficulty(int n) {
        if (n < 10) {
            System.out.println(DIFFICULTY + EASY);
        } else if (n < 50) {
            System.out.println(DIFFICULTY + MEDIUM);
        } else if (n < 100){
            System.out.println(DIFFICULTY + HARD);
        } else {
            System.out.println(DIFFICULTY + KILLER);
        }
    }

    public void wrongKeys() {
        System.out.println(WRONG_KEYS);
    }

    public void findBestElementToChoose() {

        int bestValue = 10;
        int bestX = 10;
        int bestY = 10;
        int j = 0;
        int k;
        boolean isNotParadox = false;
        SudokuElement bestElementToChoose;
        SudokuBoard deepClonedBoard = null;

        try {
            deepClonedBoard = getBoard().deepCopy();
        } catch (CloneNotSupportedException e) {
            System.out.println(SOMETHING_WRONG + e);
        }

        while (!isNotParadox && j <= 9) {

            if (!bestElementsToChoose().isEmpty()) {
                bestElementToChoose = bestElementsToChoose().get(j);
                List<Integer> getListOfPossibleValues =
                        new ArrayList<>(bestElementToChoose.getPossibleValues());
                boolean internalIsNotParadox = false;
                k = 0;

                while (!internalIsNotParadox && k <= 9) {
                    bestValue = getListOfPossibleValues.get(k);
                    bestX = getXAndYOfAnElement(bestElementToChoose, 0);
                    bestY = getXAndYOfAnElement(bestElementToChoose, 1);

                    if (isThereOnlyOnePossibleValue(bestX, bestY, bestValue)) {
                        System.out.println(PARADOX);
                        k++;
                    } else {
                        internalIsNotParadox = true;
                        isNotParadox = true;
                    }
                }
                j++;

            } else {
                System.out.println(BEST_ELEMENTS_LIST_IS_EMPTY);
                isNotParadox = true;

            }
        }

        addToBackTrackList(deepClonedBoard, bestX, bestY, bestValue);
        serveNewEntry(bestX, bestY, bestValue);
    }

    public void findDifferentLastGuessedValue() {
        setBoard(getSudokuGuesses().get(getSudokuGuesses().size() - 1).getDeepCopy());
        int lastGuessedX = getSudokuGuesses().get(getSudokuGuesses().size() - 1).getGuessedX();
        int lastGuessedY = getSudokuGuesses().get(getSudokuGuesses().size() - 1).getGuessedY();
        List<Integer> lastGuessedPossibleValues = getSudokuGuesses().get(getSudokuGuesses().size() - 1)
                .getGuessedPossibleValues();

        if (lastGuessedPossibleValues.size() > 0) {
            int differentLastGuessedValue = lastGuessedPossibleValues.get(0);
            serveNewEntry(lastGuessedX, lastGuessedY, differentLastGuessedValue);
            getSudokuGuesses().get(getSudokuGuesses().size() - 1)
                    .removeFromListOfGuessedPossibleValues(differentLastGuessedValue);
        } else {
            getSudokuGuesses().remove(getSudokuGuesses().size() - 1);
        }
    }

    public void gameCore() {
        setSudokuIncorrect(false);
        instructions();

        while (!isSudoku()) {

            setUsersNumbers(scan.nextLine());

            if (usersKeysAreCorrect() && !isSudoku()) {

                serveNewEntry(getUsersX(), getUsersY(), getUsersValue());
                System.out.println(getBoard());

            } else if (isSudoku()) {

                ifIsSudoku();

            } else {
                wrongKeys();
            }
        }

    }

    public void ifIsSudoku() {
        setSudokuIncorrect(false);
        int globalBefore = howManyChangesInPossibleValues(getBoard());
        int globalAfter = howManyChangesInPossibleValues(getBoard());
        int n = 0;

        while (globalBefore != 0 && globalAfter != 0 && n < 1000) {
            globalBefore = howManyChangesInPossibleValues(getBoard());
            globalAfter = howManyChangesInPossibleValues(getBoard());
            n++;

            int before = howManyChangesInPossibleValues(getBoard());
            int after = 1;

            while (after != before) {

                before = howManyChangesInPossibleValues(getBoard());
                solveSudoku();
                isThereOnlyOneElementWithAllPossibilities();
                after = howManyChangesInPossibleValues(getBoard());

            }

            if (isThereAnElementWithNoValueThatHasNoPossibleValues() && n > 1) {

                findDifferentLastGuessedValue();
                after = 0;

            }

            if (after != 0) {
                findBestElementToChoose();
            }

            if (n == 999) {
                System.out.println(TOO_MUCH_ITERATIONS);
            }

            if(isThereAnElementWithNoValueThatHasNoPossibleValues()) {
                globalBefore = 1;
                globalAfter = 1;
            }

            if(isThereAnElementWithNoValueThatHasNoPossibleValues() && getSudokuGuesses().size() == 0) {
                globalBefore = 0;
                globalAfter = 0;
                setSudokuIncorrect(true);
            }

        }

        if (isSudokuIncorrect()) {
            System.out.println(SUDOKU_INCORRECT);
        } else {

            System.out.println(SUDOKU_SOLVED);
            System.out.println(getBoard());

            evaluateDifficulty(n);
        }
    }

    public List<SudokuElement> bestElementsToChoose() {
        Set<SudokuElement> bestElementsToChoose = new HashSet<>();
        List<Integer> rowList = new ArrayList<>();
        List<Integer> columnList = new ArrayList<>();

        int minValue = findMinimumCountOfPossibleValues();

        for (int y = MIN_INDEX; y <= MAX_INDEX; y++) {
            for (int x = MIN_INDEX; x <= MAX_INDEX; x++) {
                SudokuElement element = board.getRows().get(x).getElements().get(y);
                    if(element.getValue() == -1) {
                        rowList.add(x);
                        columnList.add(y);
                    }
            }
        }

        Map<Integer, Integer> mostFrequentRowMap = new HashMap<>();
        Map<Integer, Integer> mostFrequentColumnMap = new HashMap<>();

        for (int i = 0; i < 9; i++) {
            mostFrequentRowMap.put(i, Collections.frequency(rowList, i));
            mostFrequentColumnMap.put(i, Collections.frequency(columnList, i));
        }

        while (true) if (!mostFrequentRowMap.values().remove(0)) break;
        while (true) if (!mostFrequentColumnMap.values().remove(0)) break;

        int mostFrequentRow = Collections.min(mostFrequentRowMap.entrySet(), Map.Entry.comparingByValue()).getKey();
        int rowFrequency = Collections.frequency(rowList, mostFrequentRow);
        int mostFrequentColumn = Collections.min(mostFrequentColumnMap.entrySet(), Map.Entry.comparingByValue()).getKey();
        int columnFrequency = Collections.frequency(columnList, mostFrequentColumn);

        while (bestElementsToChoose.size() == 0 & minValue <= 9) {
            for (int y = MIN_INDEX; y <= MAX_INDEX; y++) {
                for (int x = MIN_INDEX; x <= MAX_INDEX; x++) {
                    if (rowFrequency <= columnFrequency & rowFrequency > 0) {
                        SudokuElement element1 = board.getRows().get(mostFrequentRow).getElements().get(y);
                        if (element1.getPossibleValues().size() == minValue) {
                            bestElementsToChoose.add(element1);
                        }
                    } else if (rowFrequency > columnFrequency || columnFrequency > 0) {
                        SudokuElement element2 = board.getRows().get(x).getElements().get(mostFrequentColumn);
                        if (element2.getPossibleValues().size() == minValue) {
                            bestElementsToChoose.add(element2);
                        }
                    } else {
                        SudokuElement element = board.getRows().get(x).getElements().get(y);
                        if (element.getValue() == -1 & element.getPossibleValues().size() == minValue
                                & element.getPossibleValues().size() > 0)
                        {
                            bestElementsToChoose.add(element);
                        }
                    }
                }
            }
            if(bestElementsToChoose.isEmpty()) {
                minValue++;
            }
        }

        return new ArrayList<>(bestElementsToChoose);
    }

    public void isThereOnlyOneElementWithAllPossibilities() {
        int howManyElementsWithAllPossibilities = 0;
        int xOfElement = 0;
        int yOfElement = 0;
        for (int y = MIN_INDEX; y <= MAX_INDEX; y++) {
            for (int x = MIN_INDEX; x <= MAX_INDEX; x++) {
                SudokuElement element = board.getRows().get(x).getElements().get(y);
                if (element.getPossibleValues().size() == 9) {
                    xOfElement = x;
                    yOfElement = y;
                    howManyElementsWithAllPossibilities++;
                }
            }
        }
        if(howManyElementsWithAllPossibilities == 1) {
            SudokuElement element2 = board.getRows().get(xOfElement).getElements().get(yOfElement);
            element2.setValue(xOfElement + 1);
            element2.getPossibleValues().removeAll(element2.getPossibleValues());
            removePossibleValue(xOfElement, yOfElement, element2.getValue());
        }
    }

    public boolean isThereAnElementWithNoValueThatHasNoPossibleValues() {
        for (int y = MIN_INDEX; y <= MAX_INDEX; y++) {
            for (int x = MIN_INDEX; x <= MAX_INDEX; x++) {
                SudokuElement element = board.getRows().get(x).getElements().get(y);
                if (element.getValue() == -1 && element.getPossibleValues().size() == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isThereAParadox(int row, int column, int value) {
        if (isThereOnlyOnePossibleValueForThisElement(row, column, value)) {
            return true;
        } else return !isThereOnlyOnePossibleValue(row, column, value);
    }

    public boolean isThereOnlyOnePossibleValueForThisElement(int row, int column, int value) {
        return board.getRows().get(row).getElements().get(column).getPossibleValues().size() == 1
                && board.getRows().get(row).getElements().get(column).getPossibleValues().contains(value);
    }

    public boolean isThereOnlyOnePossibleValue(int row, int column, int value) {
        for( int x = MIN_INDEX; x <= MAX_INDEX; x++) {
            if(board.getRows().get(row).getElements().get(x).getPossibleValues().size() == 1
               && board.getRows().get(row).getElements().get(x).getPossibleValues().contains(value)
            || board.getRows().get(x).getElements().get(column).getPossibleValues().size() == 1
                    && board.getRows().get(x).getElements().get(column).getPossibleValues().contains(value)) {
                System.out.println(THIS_VALUE_WOULD_CAUSE_PARADOX);
                return true;
            }
        }

        return false;
    }
    
    public void removePossibleValue(int row, int column, int value) {
        int rowBlock = (row)/3;
        int columnBlock = (column)/3;

        board.getRows().get(row).getElements().get(column).getPossibleValues()
                .removeAll(board.getRows().get(row).getElements().get(column).getPossibleValues());

        for(int x = MIN_INDEX; x <= MAX_INDEX; x++) {
            board.getRows().get(row).getElements().get(x).removePossibleValue(value);
        }

        for(int x = MIN_INDEX; x <= MAX_INDEX; x++) {
            board.getRows().get(x).getElements().get(column).removePossibleValue(value);
        }

        for(int x = rowBlock * 3; x <= rowBlock * 3 + 2; x++) {
            for (int y = columnBlock * 3; y <= columnBlock * 3 + 2; y++) {
                board.getRows().get(x).getElements().get(y).removePossibleValue(value);
            }
        }
    }

    public SudokuBoard getBoard() {
        return board;
    }

    public void setUsersNumbers(String usersNumbers) {
        this.usersNumbers = usersNumbers;
    }

    public int getUsersX() {
        return Integer.parseInt(usersNumbers.substring(0, 1)) - 1;
    }

    public int getUsersY() {
        return Integer.parseInt(usersNumbers.substring(2, 3)) - 1;
    }

    public int getUsersValue() {
        return Integer.parseInt(usersNumbers.substring(4, 5));
    }

    public List<SudokuGuess> getSudokuGuesses() {
        return sudokuGuesses;
    }

    public boolean resolveSudoku() {
        System.out.println(DO_YOU_WANT_TO_PLAY_AGAIN);
        String playersChoice = scan.nextLine();
        return !YES.equalsIgnoreCase(playersChoice);
    }

    public void setSudokuIncorrect(boolean sudokuIncorrect) {
        isSudokuIncorrect = sudokuIncorrect;
    }

    public boolean isSudokuIncorrect() {
        return isSudokuIncorrect;
    }
}
