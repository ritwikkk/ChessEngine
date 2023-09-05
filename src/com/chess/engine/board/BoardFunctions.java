package com.chess.engine.board;

public class BoardFunctions {

    public static final boolean[] FirstColumn = initColumn(0);
    public static final boolean[] SecondColumn = initColumn(1);
    public static final boolean[] SeventhColumn = initColumn(6);
    public static final boolean[] EighthColumn = initColumn(7);

    public static final boolean[] EighthRank = initRow(0);
    public static final boolean[] SeventhRank = initRow(8);
    public static final boolean[] SixthRank = initRow(16);
    public static final boolean[] FifthRank = initRow(24);
    public static final boolean[] FourthRank = initRow(32);
    public static final boolean[] ThirdRank = initRow(40);
    public static final boolean[] SecondRank = initRow(48);
    public static final boolean[] FirstRank = initRow(56);

    private static boolean[] initColumn(int columnNumber) {
        final boolean[] column = new boolean[64];
        while(columnNumber < 64) {
            column[columnNumber] = true;
            columnNumber += 8;
        }
        return column;
    }

    private static boolean[] initRow(int rowNumber) {
        final boolean[] row = new boolean[64];
        while(rowNumber < 64) {
            row[rowNumber] = true;
            rowNumber += 1;
        }
        return row;
    }
    private BoardFunctions() {
        throw new RuntimeException("No instantiation");
    }
    public static boolean isValidSquareCoordinate(int coordinate){
        return (coordinate >=0) && (coordinate < 64);
    }
}
