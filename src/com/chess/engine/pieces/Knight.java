package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Square;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.chess.engine.board.BoardFunctions;


public class Knight extends Piece {
    private final static int[] potentialMoves = {-17, -15, -10, -6, 6, 10, 15, 17};
    public Knight(final int piecePosition, final Alliance pieceAlliance){
        super(PieceType.Knight, piecePosition,pieceAlliance);
    }

    public List<Move> calculateLegalMoves(final Board board) {
        int potentialDestination;
        final List<Move> legalMoves = new ArrayList<>();
        for (final int current : potentialMoves) {
            potentialDestination = this.piecePosition + current;
            if(BoardFunctions.isValidSquareCoordinate(potentialDestination)) {
                final Square potentialDestinationSquare = board.getSquare(potentialDestination);
                if(isFirstColumnExclusion(this.piecePosition, current) || isSecondColumnExclusion(this.piecePosition, current) || isSeventhColumnExclusion(this.piecePosition, current) || isEightColumnExclusion(this.piecePosition, current)) {
                    continue;
                }
                if(!potentialDestinationSquare.isOccupied()) {
                    legalMoves.add(new Move.MajorMove(board, this, potentialDestination));
                } else {
                    final Piece pieceAtDestination = potentialDestinationSquare.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                    if(this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new Move.AttackMove(board, this, potentialDestination, pieceAtDestination));
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }
    @Override
    public Knight movePiece(Move move) {
        return new Knight(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    public String toString(){
        return PieceType.Knight.toString();
    }
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardFunctions.FirstColumn[currentPosition] && ((candidateOffset == -17) || (candidateOffset == -10) || (candidateOffset == 6) || (candidateOffset == 15));
    }
    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardFunctions.SecondColumn[currentPosition] && ((candidateOffset == -10) || (candidateOffset == 6));
    }
    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardFunctions.SeventhColumn[currentPosition] && ((candidateOffset == 10) || (candidateOffset == -6));
    }
    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardFunctions.EighthColumn[currentPosition] && ((candidateOffset == 17) || (candidateOffset == 10) || (candidateOffset == -6) || (candidateOffset == -15));
    }
}
