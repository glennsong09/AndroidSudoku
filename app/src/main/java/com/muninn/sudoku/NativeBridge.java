package com.muninn.sudoku;

public class NativeBridge {

    public static native int doNativeAction(char[] gameData);

    static {
        System.loadLibrary("native-lib");
    }

}
