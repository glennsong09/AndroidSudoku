package com.example.sudoku;

public class SudokuSolver {

    public static final int GRIDNUM = 9;
    public static final int BOXSIZE = 3;

    private char[][] sudokuBoard;

    public char[][] getSudokuBoard() {
        return sudokuBoard;
    }

    public int getSudokuBoardValue(int rowPos, int colPos) {
        return sudokuBoard[rowPos][colPos];
    }

    public boolean isAssignedByInput(int rowPosition, int colPosition) {
        if (sudokuBoard[rowPosition][colPosition] == '_') {
            return false;
        }
        else {
            return true;
        }
    }

    public int[] findEmptyPosition() {
        int[] emptyPosition = new int[2];
        for (int i = 0; i < GRIDNUM; i++) {
            for (int j = 0; j < GRIDNUM; j++) {
                if (isAssignedByInput(i, j)) {
                    continue;
                }
                else {
                    emptyPosition[0] = i;
                    emptyPosition[1] = j;
                    return emptyPosition;
                }

            }
        }
        return emptyPosition;
    }

    public boolean checkNumbersAllowedInPositionHorizontalVertical(int rowPosition, int colPosition, char toCheck) {
        boolean allowed_horizontal = true;
        boolean allowed_vertical = true;
        for (int row = 0; row < GRIDNUM; row++) {
            if (sudokuBoard[row][colPosition] == toCheck) {
                allowed_vertical = false;
            }
        }
        for (int col = 0; col < GRIDNUM; col++) {
            if (sudokuBoard[rowPosition][col] == toCheck) {
                allowed_horizontal = false;
            }
        }
        return allowed_horizontal && allowed_vertical;
    }

    public boolean checkNumbersAllowedInPositionBox(int rowPosition, int colPosition, char toCheck) {
        boolean allowedBox = true;
        int boxStartRowPos = rowPosition / BOXSIZE;
        int boxStartColPos = colPosition / BOXSIZE;
        for (int row = boxStartRowPos; row < boxStartRowPos + BOXSIZE; row++) {
            for (int col = boxStartColPos; col < boxStartColPos + BOXSIZE; col++) {
                if (sudokuBoard[row][col] == toCheck) {
                    allowedBox = false;
                }
            }
        }
        return allowedBox;
    }

    public boolean solveBoard() {
        int[] toTry = findEmptyPosition();
        if (toTry.length <= 0) {
            return true;
        }
        int row = toTry[0];
        int col = toTry[1];
        for (char num = '1'; num <= '9'; num++) {
            if (checkNumbersAllowedInPositionHorizontalVertical(row, col, num) && checkNumbersAllowedInPositionBox(row, col, num)) {
                sudokuBoard[row][col] = num;
                if (solveBoard()) {
                    return true;
                }
                sudokuBoard[row][col] = '_';
            }
        }
        return false;
    }

    public boolean checkHorizontalVertialBoard() {
        int counter = 0;
        for (int i = 0; i < GRIDNUM; i++) {
            for (int j = 1; j < GRIDNUM; j++) {
                if (sudokuBoard[counter][i] == sudokuBoard[j][counter]) {
                    return false;
                }
                if (sudokuBoard[i][counter] == sudokuBoard[counter][j]) {
                    return false;
                }
                counter++;
            }
        }
        return true;
    }


    public boolean checkBoxesBoard(int baseRow, int baseCol, int squareSize) {
        boolean[] found = new boolean[GRIDNUM];
        for (int row = baseRow; row < (baseRow + squareSize); ++row) {
            for (int col = baseCol; col < (baseCol + squareSize); ++col) {
                int index = sudokuBoard[row][col] - 1;
                if (!found[index]) {
                    found[index] = true;
                }
                else {
                    return false;
                }
            }
        }
        return true;
    }
}


