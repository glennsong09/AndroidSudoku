package com.muninn.sudoku;

public class NativeBridge {

    public static native int doNativeAction(char[] gameData);

    static {
        System.loadLibrary("native-lib");
    }

    public static int checkSolvable(char[][] gameData) {
        int count = 0;
        char[] toSet = new char[gameData.length * gameData[0].length];
        for (int i = 0; i < gameData.length; i++) {
            for (int j = 0; j < gameData[0].length; j++) {
                toSet[count] = gameData[i][j];
                count++;
            }
        }
        return doNativeAction(toSet);
    }
}
