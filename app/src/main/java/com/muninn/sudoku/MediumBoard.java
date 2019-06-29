package com.muninn.sudoku;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MediumBoard extends AppCompatActivity {

    public static final int GRIDNUM = 9;
    public static final int BOXSIZE = 3;
    public static final int CHARLIMIT = 1;
    private char[][] sudokuBoard = new char[9][9];
    private char[][] completedBoard = new char[9][9];
    private boolean[][] canModify = new boolean[9][9];
    private TextView[] textArray = new TextView[GRIDNUM * GRIDNUM];
    private EditText[] editArray = new EditText[GRIDNUM * GRIDNUM];
    private ArrayList<Integer> allowedNums = new ArrayList<Integer>();
    private final String PUZZLEONE = "_____5__24_6_3951_1____8____749_2__1_5__6__2_8__3_476____5____4_4928_1_37__4_____";
    private final String PUZZLETWO = "_7_8_4__2_1__2__5_4__6_5___24___3_____3_6_8_____5___14___2_6__1_9__5__8_6__1_8_7_";
    private final String PUZZLETHREE = "_____7__483_1___9_____8_6____3274_____5_6_9_____3594____9_3_____7___6_481__7_____";
    private final String PUZZLEFOUR = "2_5__4____38__7______58_1__6____2_3___7___2___1_7____4__6_51______9__57____4__8_9";
    private final String PUZZLECOMPLETE = "53467891267219534819834256785976142342685379171392485696153728428741963534528617_";

    private class NumericKeyBoardTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return source;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medium_board);
        configureMenuButton();
        configureCheckButton();

        allowedNums.add(1);
        allowedNums.add(2);
        allowedNums.add(3);
        allowedNums.add(4);
        allowedNums.add(5);
        allowedNums.add(6);
        allowedNums.add(7);
        allowedNums.add(8);
        allowedNums.add(9);

        generateBoardFromPreexistingPuzzles(PUZZLEONE);

        //generateMediumBoard();
        /*
        while (!solveBoard()) {
            setContentView(R.layout.activity_loading);
            generateEasyBoard();
        }
        setContentView(R.layout.activity_board_new);
        */

        setInitialTextEditArrays();
        setCharLimit();
        setCanModify();
        setEditable();
    }

    public void configureMenuButton() {
        Button buttonMenu = findViewById(R.id.boardMainMenuButton);
        buttonMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MediumBoard.this, MainActivity.class));
            } });
    }

    public void configureCheckButton() {
        Button buttonCheck = findViewById(R.id.checkBoardButton);
        buttonCheck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setFinalBoard();
                if (checkSudokuStatus(completedBoard)) {
                    startActivity(new Intent(MediumBoard.this, FinalScreen.class));
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "Your solution is incorrect!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            } });
    }


    @SuppressWarnings("unchecked")
    public void generateMediumBoard() {
        int count = 0;
        for (int i = 0; i < GRIDNUM; i++) {
            for (int j = 0; j < GRIDNUM; j++) {
                sudokuBoard[i][j] = ' ';
            }
        }
        for (int i = 0; i < GRIDNUM; i++) {
            ArrayList<Integer> copy = (ArrayList<Integer>) allowedNums.clone();
            Collections.shuffle(copy);
            for (int j = 0; j < GRIDNUM; j++) {
                if (count >= 4) {
                    count = 0;
                    break;
                }
                if (Math.random() < 0.4) {
                    sudokuBoard[i][j] = copy.get(0).toString().charAt(0);
                    copy.remove(0);
                    count++;
                    Collections.shuffle(copy);
                }
            }
        }
    }

    public void generateBoardFromPreexistingPuzzles(String puzzle) {
        int count = 0;
        for (int i = 0; i < GRIDNUM; i++) {
            for (int j = 0; j < GRIDNUM; j++) {
                if (puzzle.charAt(count) == '_') {
                    sudokuBoard[i][j] = ' ';
                } else {
                    sudokuBoard[i][j] = puzzle.charAt(count);
                }
                count++;
            }
        }
    }

    private boolean checkSudokuStatus(char[][] grid) {
        for (int i = 0; i < 9; i++) {

            char[] row = new char[9];
            char[] square = new char[9];
            char[] column = grid[i].clone();

            for (int j = 0; j < 9; j ++) {
                row[j] = grid[j][i];
                square[j] = grid[(i / 3) * 3 + j / 3][i * 3 % 9 + j % 3];
            }
            if (!(validate(column) && validate(row) && validate(square)))
                return false;
        }
        return true;
    }

    private boolean validate(char[] check) {
        char i = '1';
        Arrays.sort(check);
        for (char number : check) {
            if (number != i)
                return false;
            i++;
        }
        return true;
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
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public void setCanModify() {
        for (int i = 0; i < GRIDNUM; i++) {
            for (int j = 0; j < GRIDNUM; j++) {
                if (sudokuBoard[i][j] == ' ') {
                    canModify[i][j] = true;
                } else {
                    canModify[i][j] = false;
                }

            }
        }
    }

    public void setEditable() {
        int count = 0;
        for (int i = 0; i < GRIDNUM; i++) {
            for (int j = 0; j < GRIDNUM; j++) {
                if (!canModify[i][j]) {
                    editArray[count].setEnabled(false);
                    editArray[count].setVisibility(View.INVISIBLE);
                }
                count++;
            }
        }
    }

    public void setFinalBoard() {
        int count = 0;
        char[][] boardCopy = new char[GRIDNUM][GRIDNUM];
        for (int i = 0; i < sudokuBoard.length; i++) {
            boardCopy[i] = sudokuBoard[i].clone();
        }
        for (int i = 0; i < GRIDNUM; i++) {
            for (int j = 0; j < GRIDNUM; j++) {
                if (canModify[i][j]) {
                    if (editArray[count].getText().toString().length() == 0) {
                        boardCopy[i][j] = ' ';
                    } else {
                        boardCopy[i][j] = editArray[count].getText().toString().charAt(0);
                    }
                }
                count++;
            }
        }
        completedBoard = boardCopy;
    }

    public void setCharLimit() {
        for (int i = 0; i < (GRIDNUM*GRIDNUM); i++) {
            editArray[i].setFilters(new InputFilter[] {new InputFilter.LengthFilter(CHARLIMIT)});
            editArray[i].setInputType(InputType.TYPE_CLASS_NUMBER);
            editArray[i].setTransformationMethod(new MediumBoard.NumericKeyBoardTransformationMethod());
        }
    }

    public void setInitialTextEditArrays() {
        TextView textView00 = findViewById(R.id.board00);
        textView00.setText(String.valueOf(sudokuBoard[0][0]));
        textArray[0] = textView00;

        EditText editView00 = (EditText) findViewById(R.id.edit00);
        editArray[0] = editView00;

        TextView textView01 = (TextView) findViewById(R.id.board01);
        textView01.setText(String.valueOf(sudokuBoard[0][1]));
        textArray[1] = textView01;

        EditText editView01 = (EditText) findViewById(R.id.edit01);
        editArray[1] = editView01;

        TextView textView02 = (TextView) findViewById(R.id.board02);
        textView02.setText(String.valueOf(sudokuBoard[0][2]));
        textArray[2] = textView02;

        EditText editView02 = (EditText) findViewById(R.id.edit02);
        editArray[2] = editView02;

        TextView textView03 = (TextView) findViewById(R.id.board03);
        textView03.setText(String.valueOf(sudokuBoard[0][3]));
        textArray[3] = textView03;

        EditText editView03 = (EditText) findViewById(R.id.edit03);
        editArray[3] = editView03;

        TextView textView04 = (TextView) findViewById(R.id.board04);
        textView04.setText(String.valueOf(sudokuBoard[0][4]));
        textArray[4] = textView04;

        EditText editView04 = (EditText) findViewById(R.id.edit04);
        editArray[4] = editView04;

        TextView textView05 = (TextView) findViewById(R.id.board05);
        textView05.setText(String.valueOf(sudokuBoard[0][5]));
        textArray[5] = textView05;

        EditText editView05 = (EditText) findViewById(R.id.edit05);
        editArray[5] = editView05;

        TextView textView06 = (TextView) findViewById(R.id.board06);
        textView06.setText(String.valueOf(sudokuBoard[0][6]));
        textArray[6] = textView06;

        EditText editView06 = (EditText) findViewById(R.id.edit06);
        editArray[6] = editView06;

        TextView textView07 = (TextView) findViewById(R.id.board07);
        textView07.setText(String.valueOf(sudokuBoard[0][7]));
        textArray[7] = textView07;

        EditText editView07 = (EditText) findViewById(R.id.edit07);
        editArray[7] = editView07;

        TextView textView08 = (TextView) findViewById(R.id.board08);
        textView08.setText(String.valueOf(sudokuBoard[0][8]));
        textArray[8] = textView08;

        EditText editView08 = (EditText) findViewById(R.id.edit08);
        editArray[8] = editView08;

        TextView textView10 = (TextView) findViewById(R.id.board10);
        textView10.setText(String.valueOf(sudokuBoard[1][0]));
        textArray[9] = textView10;

        EditText editView10 = (EditText) findViewById(R.id.edit10);
        editArray[9] = editView10;

        TextView textView11 = (TextView) findViewById(R.id.board11);
        textView11.setText(String.valueOf(sudokuBoard[1][1]));
        textArray[10] = textView11;

        EditText editView11 = (EditText) findViewById(R.id.edit11);
        editArray[10] = editView11;

        TextView textView12 = (TextView) findViewById(R.id.board12);
        textView12.setText(String.valueOf(sudokuBoard[1][2]));
        textArray[11] = textView12;

        EditText editView12 = (EditText) findViewById(R.id.edit12);
        editArray[11] = editView12;

        TextView textView13 = (TextView) findViewById(R.id.board13);
        textView13.setText(String.valueOf(sudokuBoard[1][3]));
        textArray[12] = textView13;

        EditText editView13 = (EditText) findViewById(R.id.edit13);
        editArray[12] = editView13;

        TextView textView14 = (TextView) findViewById(R.id.board14);
        textView14.setText(String.valueOf(sudokuBoard[1][4]));
        textArray[13] = textView14;

        EditText editView14 = (EditText) findViewById(R.id.edit14);
        editArray[13] = editView14;

        TextView textView15 = (TextView) findViewById(R.id.board15);
        textView15.setText(String.valueOf(sudokuBoard[1][5]));
        textArray[14] = textView15;

        EditText editView15 = (EditText) findViewById(R.id.edit15);
        editArray[14] = editView15;

        TextView textView16 = (TextView) findViewById(R.id.board16);
        textView16.setText(String.valueOf(sudokuBoard[1][6]));
        textArray[15] = textView16;

        EditText editView16 = (EditText) findViewById(R.id.edit16);
        editArray[15] = editView16;

        TextView textView17 = (TextView) findViewById(R.id.board17);
        textView17.setText(String.valueOf(sudokuBoard[1][7]));
        textArray[16] = textView17;

        EditText editView17 = (EditText) findViewById(R.id.edit17);
        editArray[16] = editView17;

        TextView textView18 = (TextView) findViewById(R.id.board18);
        textView18.setText(String.valueOf(sudokuBoard[1][8]));
        textArray[17] = textView18;

        EditText editView18 = (EditText) findViewById(R.id.edit18);
        editArray[17] = editView18;

        TextView textView20 = (TextView) findViewById(R.id.board20);
        textView20.setText(String.valueOf(sudokuBoard[2][0]));
        textArray[18] = textView20;

        EditText editView20 = (EditText) findViewById(R.id.edit20);
        editArray[18] = editView20;

        TextView textView21 = (TextView) findViewById(R.id.board21);
        textView21.setText(String.valueOf(sudokuBoard[2][1]));
        textArray[19] = textView21;

        EditText editView21 = (EditText) findViewById(R.id.edit21);
        editArray[19] = editView21;

        TextView textView22 = (TextView) findViewById(R.id.board22);
        textView22.setText(String.valueOf(sudokuBoard[2][2]));
        textArray[20] = textView22;

        EditText editView22 = (EditText) findViewById(R.id.edit22);
        editArray[20] = editView22;

        TextView textView23 = (TextView) findViewById(R.id.board23);
        textView23.setText(String.valueOf(sudokuBoard[2][3]));
        textArray[21] = textView23;

        EditText editView23 = (EditText) findViewById(R.id.edit23);
        editArray[21] = editView23;

        TextView textView24 = (TextView) findViewById(R.id.board24);
        textView24.setText(String.valueOf(sudokuBoard[2][4]));
        textArray[22] = textView24;

        EditText editView24 = (EditText) findViewById(R.id.edit24);
        editArray[22] = editView24;

        TextView textView25 = (TextView) findViewById(R.id.board25);
        textView25.setText(String.valueOf(sudokuBoard[2][5]));
        textArray[23] = textView25;

        EditText editView25 = (EditText) findViewById(R.id.edit25);
        editArray[23] = editView25;

        TextView textView26 = (TextView) findViewById(R.id.board26);
        textView26.setText(String.valueOf(sudokuBoard[2][6]));
        textArray[24] = textView26;

        EditText editView26 = (EditText) findViewById(R.id.edit26);
        editArray[24] = editView26;

        TextView textView27 = (TextView) findViewById(R.id.board27);
        textView27.setText(String.valueOf(sudokuBoard[2][7]));
        textArray[25] = textView27;

        EditText editView27 = (EditText) findViewById(R.id.edit27);
        editArray[25] = editView27;

        TextView textView28 = (TextView) findViewById(R.id.board28);
        textView28.setText(String.valueOf(sudokuBoard[2][8]));
        textArray[26] = textView28;

        EditText editView28 = (EditText) findViewById(R.id.edit28);
        editArray[26] = editView28;

        TextView textView30 = (TextView) findViewById(R.id.board30);
        textView30.setText(String.valueOf(sudokuBoard[3][0]));
        textArray[27] = textView30;

        EditText editView30 = (EditText) findViewById(R.id.edit30);
        editArray[27] = editView30;

        TextView textView31 = (TextView) findViewById(R.id.board31);
        textView31.setText(String.valueOf(sudokuBoard[3][1]));
        textArray[28] = textView31;

        EditText editView31 = (EditText) findViewById(R.id.edit31);
        editArray[28] = editView31;

        TextView textView32 = (TextView) findViewById(R.id.board32);
        textView32.setText(String.valueOf(sudokuBoard[3][2]));
        textArray[29] = textView32;

        EditText editView32 = (EditText) findViewById(R.id.edit32);
        editArray[29] = editView32;

        TextView textView33 = (TextView) findViewById(R.id.board33);
        textView33.setText(String.valueOf(sudokuBoard[3][3]));
        textArray[30] = textView33;

        EditText editView33 = (EditText) findViewById(R.id.edit33);
        editArray[30] = editView33;

        TextView textView34 = (TextView) findViewById(R.id.board34);
        textView34.setText(String.valueOf(sudokuBoard[3][4]));
        textArray[31] = textView34;

        EditText editView34 = (EditText) findViewById(R.id.edit34);
        editArray[31] = editView34;

        TextView textView35 = (TextView) findViewById(R.id.board35);
        textView35.setText(String.valueOf(sudokuBoard[3][5]));
        textArray[32] = textView35;

        EditText editView35 = (EditText) findViewById(R.id.edit35);
        editArray[32] = editView35;

        TextView textView36 = (TextView) findViewById(R.id.board36);
        textView36.setText(String.valueOf(sudokuBoard[3][6]));
        textArray[33] = textView36;

        EditText editView36 = (EditText) findViewById(R.id.edit36);
        editArray[33] = editView36;

        TextView textView37 = (TextView) findViewById(R.id.board37);
        textView37.setText(String.valueOf(sudokuBoard[3][7]));
        textArray[34] = textView37;

        EditText editView37 = (EditText) findViewById(R.id.edit37);
        editArray[34] = editView37;

        TextView textView38 = (TextView) findViewById(R.id.board38);
        textView38.setText(String.valueOf(sudokuBoard[3][8]));
        textArray[35] = textView38;

        EditText editView38 = (EditText) findViewById(R.id.edit38);
        editArray[35] = editView38;

        TextView textView40 = (TextView) findViewById(R.id.board40);
        textView40.setText(String.valueOf(sudokuBoard[4][0]));
        textArray[36] = textView40;

        EditText editView40 = (EditText) findViewById(R.id.edit40);
        editArray[36] = editView40;

        TextView textView41 = (TextView) findViewById(R.id.board41);
        textView41.setText(String.valueOf(sudokuBoard[4][1]));
        textArray[37] = textView41;

        EditText editView41 = (EditText) findViewById(R.id.edit41);
        editArray[37] = editView41;

        TextView textView42 = (TextView) findViewById(R.id.board42);
        textView42.setText(String.valueOf(sudokuBoard[4][2]));
        textArray[38] = textView42;

        EditText editView42 = (EditText) findViewById(R.id.edit42);
        editArray[38] = editView42;

        TextView textView43 = (TextView) findViewById(R.id.board43);
        textView43.setText(String.valueOf(sudokuBoard[4][3]));
        textArray[39] = textView43;

        EditText editView43 = (EditText) findViewById(R.id.edit43);
        editArray[39] = editView43;

        TextView textView44 = (TextView) findViewById(R.id.board44);
        textView44.setText(String.valueOf(sudokuBoard[4][4]));
        textArray[40] = textView44;

        EditText editView44 = (EditText) findViewById(R.id.edit44);
        editArray[40] = editView44;

        TextView textView45 = (TextView) findViewById(R.id.board45);
        textView45.setText(String.valueOf(sudokuBoard[4][5]));
        textArray[41] = textView45;

        EditText editView45 = (EditText) findViewById(R.id.edit45);
        editArray[41] = editView45;

        TextView textView46 = (TextView) findViewById(R.id.board46);
        textView46.setText(String.valueOf(sudokuBoard[4][6]));
        textArray[42] = textView46;

        EditText editView46 = (EditText) findViewById(R.id.edit46);
        editArray[42] = editView46;

        TextView textView47 = (TextView) findViewById(R.id.board47);
        textView47.setText(String.valueOf(sudokuBoard[4][7]));
        textArray[43] = textView47;

        EditText editView47 = (EditText) findViewById(R.id.edit47);
        editArray[43] = editView47;

        TextView textView48 = (TextView) findViewById(R.id.board48);
        textView48.setText(String.valueOf(sudokuBoard[4][8]));
        textArray[44] = textView48;

        EditText editView48 = (EditText) findViewById(R.id.edit48);
        editArray[44] = editView48;

        TextView textView50 = (TextView) findViewById(R.id.board50);
        textView50.setText(String.valueOf(sudokuBoard[5][0]));
        textArray[45] = textView50;

        EditText editView50 = (EditText) findViewById(R.id.edit50);
        editArray[45] = editView50;

        TextView textView51 = (TextView) findViewById(R.id.board51);
        textView51.setText(String.valueOf(sudokuBoard[5][1]));
        textArray[46] = textView51;

        EditText editView51 = (EditText) findViewById(R.id.edit51);
        editArray[46] = editView51;

        TextView textView52 = (TextView) findViewById(R.id.board52);
        textView52.setText(String.valueOf(sudokuBoard[5][2]));
        textArray[47] = textView52;

        EditText editView52 = (EditText) findViewById(R.id.edit52);
        editArray[47] = editView52;

        TextView textView53 = (TextView) findViewById(R.id.board53);
        textView53.setText(String.valueOf(sudokuBoard[5][3]));
        textArray[48] = textView53;

        EditText editView53 = (EditText) findViewById(R.id.edit53);
        editArray[48] = editView53;

        TextView textView54 = (TextView) findViewById(R.id.board54);
        textView54.setText(String.valueOf(sudokuBoard[5][4]));
        textArray[49] = textView54;

        EditText editView54 = (EditText) findViewById(R.id.edit54);
        editArray[49] = editView54;

        TextView textView55 = (TextView) findViewById(R.id.board55);
        textView55.setText(String.valueOf(sudokuBoard[5][5]));
        textArray[50] = textView55;

        EditText editView55 = (EditText) findViewById(R.id.edit55);
        editArray[50] = editView55;

        TextView textView56 = (TextView) findViewById(R.id.board56);
        textView56.setText(String.valueOf(sudokuBoard[5][6]));
        textArray[51] = textView56;

        EditText editView56 = (EditText) findViewById(R.id.edit56);
        editArray[51] = editView56;

        TextView textView57 = (TextView) findViewById(R.id.board57);
        textView57.setText(String.valueOf(sudokuBoard[5][7]));
        textArray[52] = textView57;

        EditText editView57 = (EditText) findViewById(R.id.edit57);
        editArray[52] = editView57;

        TextView textView58 = (TextView) findViewById(R.id.board58);
        textView58.setText(String.valueOf(sudokuBoard[5][8]));
        textArray[53] = textView58;

        EditText editView58 = (EditText) findViewById(R.id.edit58);
        editArray[53] = editView58;

        TextView textView60 = (TextView) findViewById(R.id.board60);
        textView60.setText(String.valueOf(sudokuBoard[6][0]));
        textArray[54] = textView60;

        EditText editView60 = (EditText) findViewById(R.id.edit60);
        editArray[54] = editView60;

        TextView textView61 = (TextView) findViewById(R.id.board61);
        textView61.setText(String.valueOf(sudokuBoard[6][1]));
        textArray[55] = textView61;

        EditText editView61 = (EditText) findViewById(R.id.edit61);
        editArray[55] = editView61;

        TextView textView62 = (TextView) findViewById(R.id.board62);
        textView62.setText(String.valueOf(sudokuBoard[6][2]));
        textArray[56] = textView62;

        EditText editView62 = (EditText) findViewById(R.id.edit62);
        editArray[56] = editView62;

        TextView textView63 = (TextView) findViewById(R.id.board63);
        textView63.setText(String.valueOf(sudokuBoard[6][3]));
        textArray[57] = textView63;

        EditText editView63 = (EditText) findViewById(R.id.edit63);
        editArray[57] = editView63;

        TextView textView64 = (TextView) findViewById(R.id.board64);
        textView64.setText(String.valueOf(sudokuBoard[6][4]));
        textArray[58] = textView64;

        EditText editView64 = (EditText) findViewById(R.id.edit64);
        editArray[58] = editView64;

        TextView textView65 = (TextView) findViewById(R.id.board65);
        textView65.setText(String.valueOf(sudokuBoard[6][5]));
        textArray[59] = textView65;

        EditText editView65 = (EditText) findViewById(R.id.edit65);
        editArray[59] = editView65;

        TextView textView66 = (TextView) findViewById(R.id.board66);
        textView66.setText(String.valueOf(sudokuBoard[6][6]));
        textArray[60] = textView66;

        EditText editView66 = (EditText) findViewById(R.id.edit66);
        editArray[60] = editView66;

        TextView textView67 = (TextView) findViewById(R.id.board67);
        textView67.setText(String.valueOf(sudokuBoard[6][7]));
        textArray[61] = textView67;

        EditText editView67 = (EditText) findViewById(R.id.edit67);
        editArray[61] = editView67;

        TextView textView68 = (TextView) findViewById(R.id.board68);
        textView68.setText(String.valueOf(sudokuBoard[6][8]));
        textArray[62] = textView68;

        EditText editView68 = (EditText) findViewById(R.id.edit68);
        editArray[62] = editView68;

        TextView textView70 = (TextView) findViewById(R.id.board70);
        textView70.setText(String.valueOf(sudokuBoard[7][0]));
        textArray[63] = textView70;

        EditText editView70 = (EditText) findViewById(R.id.edit70);
        editArray[63] = editView70;

        TextView textView71 = (TextView) findViewById(R.id.board71);
        textView71.setText(String.valueOf(sudokuBoard[7][1]));
        textArray[64] = textView71;

        EditText editView71 = (EditText) findViewById(R.id.edit71);
        editArray[64] = editView71;

        TextView textView72 = (TextView) findViewById(R.id.board72);
        textView72.setText(String.valueOf(sudokuBoard[7][2]));
        textArray[65] = textView72;

        EditText editView72 = (EditText) findViewById(R.id.edit72);
        editArray[65] = editView72;

        TextView textView73 = (TextView) findViewById(R.id.board73);
        textView73.setText(String.valueOf(sudokuBoard[7][3]));
        textArray[66] = textView73;

        EditText editView73 = (EditText) findViewById(R.id.edit73);
        editArray[66] = editView73;

        TextView textView74 = (TextView) findViewById(R.id.board74);
        textView74.setText(String.valueOf(sudokuBoard[7][4]));
        textArray[67] = textView74;

        EditText editView74 = (EditText) findViewById(R.id.edit74);
        editArray[67] = editView74;

        TextView textView75 = (TextView) findViewById(R.id.board75);
        textView75.setText(String.valueOf(sudokuBoard[7][5]));
        textArray[68] = textView75;

        EditText editView75 = (EditText) findViewById(R.id.edit75);
        editArray[68] = editView75;

        TextView textView76 = (TextView) findViewById(R.id.board76);
        textView76.setText(String.valueOf(sudokuBoard[7][6]));
        textArray[69] = textView76;

        EditText editView76 = (EditText) findViewById(R.id.edit76);
        editArray[69] = editView76;

        TextView textView77 = (TextView) findViewById(R.id.board77);
        textView77.setText(String.valueOf(sudokuBoard[7][7]));
        textArray[70] = textView77;

        EditText editView77 = (EditText) findViewById(R.id.edit77);
        editArray[70] = editView77;

        TextView textView78 = (TextView) findViewById(R.id.board78);
        textView78.setText(String.valueOf(sudokuBoard[7][8]));
        textArray[71] = textView78;

        EditText editView78 = (EditText) findViewById(R.id.edit78);
        editArray[71] = editView78;

        TextView textView80 = (TextView) findViewById(R.id.board80);
        textView80.setText(String.valueOf(sudokuBoard[8][0]));
        textArray[72] = textView80;

        EditText editView80 = (EditText) findViewById(R.id.edit80);
        editArray[72] = editView80;

        TextView textView81 = (TextView) findViewById(R.id.board81);
        textView81.setText(String.valueOf(sudokuBoard[8][1]));
        textArray[73] = textView81;

        EditText editView81 = (EditText) findViewById(R.id.edit81);
        editArray[73] = editView81;

        TextView textView82 = (TextView) findViewById(R.id.board82);
        textView82.setText(String.valueOf(sudokuBoard[8][2]));
        textArray[74] = textView82;

        EditText editView82 = (EditText) findViewById(R.id.edit82);
        editArray[74] = editView82;

        TextView textView83 = (TextView) findViewById(R.id.board83);
        textView83.setText(String.valueOf(sudokuBoard[8][3]));
        textArray[75] = textView83;

        EditText editView83 = (EditText) findViewById(R.id.edit83);
        editArray[75] = editView83;

        TextView textView84 = (TextView) findViewById(R.id.board84);
        textView84.setText(String.valueOf(sudokuBoard[8][4]));
        textArray[76] = textView84;

        EditText editView84 = (EditText) findViewById(R.id.edit84);
        editArray[76] = editView84;

        TextView textView85 = (TextView) findViewById(R.id.board85);
        textView85.setText(String.valueOf(sudokuBoard[8][5]));
        textArray[77] = textView85;

        EditText editView85 = (EditText) findViewById(R.id.edit85);
        editArray[77] = editView85;

        TextView textView86 = (TextView) findViewById(R.id.board86);
        textView86.setText(String.valueOf(sudokuBoard[8][6]));
        textArray[78] = textView86;

        EditText editView86 = (EditText) findViewById(R.id.edit86);
        editArray[78] = editView86;

        TextView textView87 = (TextView) findViewById(R.id.board87);
        textView87.setText(String.valueOf(sudokuBoard[8][7]));
        textArray[79] = textView87;

        EditText editView87 = (EditText) findViewById(R.id.edit87);
        editArray[79] = editView87;

        TextView textView88 = (TextView) findViewById(R.id.board88);
        textView88.setText(String.valueOf(sudokuBoard[8][8]));
        textArray[80] = textView88;

        EditText editView88 = (EditText) findViewById(R.id.edit88);
        editArray[80] = editView88;
    }
}
