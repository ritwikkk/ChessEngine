package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardFunctions;
import com.chess.engine.board.Move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pawn extends Piece{
    private static final int[] candidate_move_vector_coordinates = {7, 8, 9, 16};
    public Pawn(int piecePosition, Alliance pieceAlliance){
        super(PieceType.Pawn, piecePosition, pieceAlliance);
    }
    @Override
    public List<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int coordinateOffset: candidate_move_vector_coordinates){
            int potentialDestination = this.piecePosition + (this.pieceAlliance.getDirection() * coordinateOffset);
            if(!BoardFunctions.isValidSquareCoordinate(potentialDestination)) {
                continue;
            }
            if (coordinateOffset == 8 && !board.getSquare(potentialDestination).isOccupied()){
                legalMoves.add(new Move.MajorMove(board, this, potentialDestination));
            } else if(coordinateOffset == 16 && this.isFirstMove &&
                    (BoardFunctions.SeventhRank[piecePosition]) && this.getPieceAlliance().isBlack() ||
                    (BoardFunctions.SecondRank[piecePosition] && this.getPieceAlliance().isWhite())){
                final int behindPotentialDestination = this.piecePosition + this.pieceAlliance.getDirection() * 8;
                if(!board.getSquare(behindPotentialDestination).isOccupied() && !board.getSquare(potentialDestination).isOccupied()) {
                    legalMoves.add(new Move.MajorMove(board, this, potentialDestination));
                }
            } else if ((coordinateOffset == 7) && !((BoardFunctions.EighthColumn[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                    (BoardFunctions.FirstColumn[this.piecePosition] && this.pieceAlliance.isBlack()))){
                if(board.getSquare(potentialDestination).isOccupied()) {
                    final Piece pieceOnSquare = board.getSquare(potentialDestination).getPiece();
                    if(this.pieceAlliance != pieceOnSquare.getPieceAlliance()) {
                        legalMoves.add(new Move.MajorMove(board, this, potentialDestination));
                    }
                }
            } else if ((coordinateOffset == 9) && !((BoardFunctions.FirstColumn[piecePosition] && this.getPieceAlliance().isWhite()) || ((BoardFunctions.EighthColumn[piecePosition] && this.getPieceAlliance().isBlack())))){
                legalMoves.add(new Move.MajorMove(board, this, potentialDestination));
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }
    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    public String toString(){
        return PieceType.Pawn.toString();
    }
}
