package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardFunctions;
import com.chess.engine.board.Move;
import com.chess.engine.board.Square;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class King extends Piece {
    private static final int[] candidate_move_vector_coordinates = {-9, -8, -7, -1, 1, 7, 8, 9};

    public King(int piecePosition, Alliance pieceAlliance) {
        super(PieceType.King, piecePosition, pieceAlliance);
    }

    @Override
    public List<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int coordinateOffset : candidate_move_vector_coordinates) {
            final int potentialDestination = coordinateOffset + this.piecePosition;
            if(BoardFunctions.isValidSquareCoordinate(potentialDestination)){
                final Square potentialDestinationSquare = board.getSquare(potentialDestination);

                if(isFirstColumnExclusion(this.piecePosition, coordinateOffset) || isEighthColumnExclusion(this.piecePosition, coordinateOffset)) {
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
    public King movePiece(Move move) {
        return new King(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    public String toString(){
        return PieceType.King.toString();
    }
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardFunctions.FirstColumn[currentPosition] && ((candidateOffset == -9) || (candidateOffset == -1) || (candidateOffset == 7));
    }
    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardFunctions.EighthColumn[currentPosition] && ((candidateOffset == -7) || (candidateOffset == 1) || (candidateOffset == 9));
    }
}