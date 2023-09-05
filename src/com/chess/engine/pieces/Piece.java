package com.chess.engine.pieces;
import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import java.util.*;

public abstract class Piece {
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final PieceType pieceType;
    protected final boolean isFirstMove;

    private final int  cachedHashCode;

    public Piece(final PieceType pieceType, final int piecePosition, final Alliance pieceAlliance) {
        this.pieceType = pieceType;
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
        this.isFirstMove = false;
        this.cachedHashCode = computeHashCode(); 
    }

    public int hashCode() {
        return this.cachedHashCode;
    }

    @Override
    public boolean equals(final Object other){
        if (this == other){
            return true;
        }
        if (!(other instanceof Piece)) {
            return false;
        }
        final Piece otherPiece = (Piece) other;
        return (pieceAlliance == otherPiece.getPieceAlliance()) && (piecePosition == otherPiece.getPiecePosition())
                && (pieceType == otherPiece.getPieceType()) && (isFirstMove == otherPiece.isFirstMove());
    }

    private int computeHashCode(){
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;

    }
    public PieceType getPieceType() {
        return pieceType;
    }

    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }
    public boolean isFirstMove(){
        return isFirstMove;
    }
    public abstract List<Move> calculateLegalMoves(final Board board);

    public abstract Piece movePiece(Move move);

    public int getPiecePosition() {
        return piecePosition;
    }

    public enum PieceType {
        Pawn("P") {
            @Override
            public boolean isKing() {
                return false;
            }
            public boolean isRook() {
                return false;
            }
        },
        Bishop("B") {
            @Override
            public boolean isKing() {
                return false;
            }
            public boolean isRook() {
                return false;
            }
        },
        Knight("N") {
            @Override
            public boolean isKing() {
                return false;
            }
            public boolean isRook() {
                return false;
            }
        },
        Queen("Q") {
            @Override
            public boolean isKing() {
                return false;
            }
            public boolean isRook() {
                return false;
            }
        },
        Rook("R") {
            @Override
            public boolean isKing() {
                return false;
            }
            public boolean isRook() {
                return true;
            }
        },
        King("K") {
            @Override
            public boolean isKing() {
                return true;
            }
            public boolean isRook() {
                return false;
            }
        };

        private String pieceName;
        PieceType (final String pieceName) {
            this.pieceName = pieceName;
        }

        public abstract boolean isKing();
        public abstract boolean isRook();

        public String toString(){
            return this.pieceName;
        }

    }
}