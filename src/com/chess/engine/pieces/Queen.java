package com.chess.engine.pieces;
import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardFunctions;
import com.chess.engine.board.Move;
import com.chess.engine.board.Square;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Queen extends Piece {
    private final static int[] candidate_move_vector_coordinates = {-9, -8, -7, -1, 1, 7, 8, 9};
    public Queen(int piecePosition, Alliance pieceAlliance){
        super(PieceType.Queen, piecePosition, pieceAlliance);
    }

    @Override
    public List<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for(final int coordinateOffset: candidate_move_vector_coordinates){
            int potentialDestination = this.piecePosition;
            while(BoardFunctions.isValidSquareCoordinate(potentialDestination)) {
                if(isFirstColumnExclusion(potentialDestination, coordinateOffset) || (isEighthColumnExclusion(potentialDestination, coordinateOffset))){
                    break;
                }

                potentialDestination += coordinateOffset;
                if(BoardFunctions.isValidSquareCoordinate(potentialDestination)){
                    final Square potentialDestinationSquare = board.getSquare(potentialDestination);
                    if(!potentialDestinationSquare.isOccupied()) {
                        legalMoves.add(new Move.MajorMove(board, this, potentialDestination));
                    } else {
                        final Piece pieceAtDestination = potentialDestinationSquare.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                        if(this.pieceAlliance != pieceAlliance) {
                            legalMoves.add(new Move.AttackMove(board, this, potentialDestination, pieceAtDestination));
                        }
                        break;
                    }

                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }

    public String toString(){
        return PieceType.Queen.toString();
    }
    @Override
    public Queen movePiece(Move move) {
        return new Queen(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardFunctions.FirstColumn[currentPosition] && ((candidateOffset == -9) || (candidateOffset == -1) || (candidateOffset == 7));
    }
    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardFunctions.EighthColumn[currentPosition] && ((candidateOffset == -7) || (candidateOffset == 1) || (candidateOffset == 9));
    }
}
