package com.chess.engine.board;

import com.chess.engine.pieces.Piece;
import java.util.*;

public abstract class Square {
    protected final int squareCoordinate;

    private static final Map<Integer, EmptySquare> EMPTY_SQUARES = createAllPossibleEmptyTiles();

    private static Map<Integer, EmptySquare> createAllPossibleEmptyTiles(){
        final Map<Integer, EmptySquare> empty_squares_map = new HashMap<>();
        for(int i = 0; i < 64; i++){
            empty_squares_map.put(i, new EmptySquare(i));
        }
        return Collections.unmodifiableMap(empty_squares_map);
    }
    private Square(int squareCoordinate){
        this.squareCoordinate = squareCoordinate;
    }

    public static Square createSquare(final int squareCoordinate, final Piece piece){
        return piece != null ? new OccupiedSquare(squareCoordinate, piece) : EMPTY_SQUARES.get(squareCoordinate);
    }
    public abstract boolean isOccupied();

    public abstract Piece getPiece();

    public abstract int getSquareCoordinate();

    public static final class OccupiedSquare extends Square {
        private final Piece pieceOnTile;
        public OccupiedSquare(int coordinate, Piece pieceOnTile) {
            super(coordinate);
            this.pieceOnTile = pieceOnTile;
        }

        public boolean isOccupied(){
            return true;
        }

        public Piece getPiece(){
            return this.pieceOnTile;
        }

        @Override
        public int getSquareCoordinate() {
            return this.squareCoordinate;
        }

        public String toString(){
            return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase() : getPiece().toString();
        }
    }

    public static final class EmptySquare extends Square {
        public EmptySquare(final int coordinate){
            super(coordinate);
        }

        public boolean isOccupied(){
            return false;
        }

        public Piece getPiece(){
            return null;
        }

        @Override
        public int getSquareCoordinate() {
            return this.squareCoordinate;
        }

        public String toString(){
            return "-";
        }
    }
}