package com.example.sudoku;
import java.util.ArrayList;

public class Sudoku {

    public static final int GRIDNUM = 9;
    public static final int BOXSIZE = 3;

    private ArrayList<char> sudokuBoardTemp;
    private char[][] sudokuBoard;

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
        int[] emptyPosition;
        for (int i = 0; i < GRIDNUM; i++) {
            for (int j = 0; j < GRIDNUM; j++) {
                if (isAssignedByInput(i, j)) {
                    continue;
                }
                else {
                    emptyPosition = { i, j };
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
        int[] to_try = findEmptyPosition();
        if (to_try.size() <= 0) {
            return true;
        }
        int row = to_try[0];
        int col = to_try[1];
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
}


