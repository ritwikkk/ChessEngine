package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Square;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BlackPlayer extends Player {

    public BlackPlayer(Board board, Collection<Move> whiteStandardLegalMoves, Collection<Move> blackStandardLegalMoves) {
        super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.Black;
    }

    public Player getOpponent() {
        return this.board.whitePlayer();
    }

    @Override
    public Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>();

        if (this.playerKing.isFirstMove() && !this.isCheck()){
            if(!this.board.getSquare(5).isOccupied() && !this.board.getSquare(6).isOccupied()){
                final Square rookSquare = this.board.getSquare(7);
                if(rookSquare.isOccupied() && rookSquare.getPiece().isFirstMove()) {
                    if(Player.calculateAttacksOnSquare(5, opponentLegals).isEmpty() && Player.calculateAttacksOnSquare(6, opponentLegals).isEmpty() && rookSquare.getPiece().getPieceType().isRook()){
                        kingCastles.add(new Move.CastleKingSide(this.board, this.playerKing, 6, (Rook)rookSquare.getPiece(), rookSquare.getSquareCoordinate(), 5));
                    }
                }
            }
            if(!this.board.getSquare(1).isOccupied() && !this.board.getSquare(2).isOccupied() && !this.board.getSquare(3).isOccupied()){
                final Square rookSquare = this.board.getSquare(0);
                if(rookSquare.isOccupied() && rookSquare.getPiece().isFirstMove() && Player.calculateAttacksOnSquare(2, opponentLegals).isEmpty() && Player.calculateAttacksOnSquare(3, opponentLegals).isEmpty()
                && rookSquare.getPiece().getPieceType().isRook()) {
                    kingCastles.add(new Move.CastleQueenSide(this.board, this.playerKing, 2, (Rook)rookSquare.getPiece(), rookSquare.getSquareCoordinate(), 3));
                }
            }
        }

        return Collections.unmodifiableList(kingCastles);
    }
}
