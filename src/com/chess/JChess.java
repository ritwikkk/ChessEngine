package com.chess;
import com.chess.engine.board.Board;
import com.chess.engine.gui.Table;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class JChess {
    public static void main(String[] args) {
        Board board = Board.createChessBoard();
        System.out.println(board);

        Table table = new Table();
    }
}