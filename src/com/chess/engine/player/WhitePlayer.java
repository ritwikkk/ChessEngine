package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Square;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class WhitePlayer extends Player {
    public WhitePlayer(Board board, Collection<Move> whiteStandardLegalMoves, Collection<Move> blackStandardLegalMoves) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.White;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer() ;
    }

    public Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>();

        if (this.playerKing.isFirstMove() && !this.isCheck()){
            if(!this.board.getSquare(61).isOccupied() && !this.board.getSquare(62).isOccupied()){
                final Square rookSquare = this.board.getSquare(63);
                if(rookSquare.isOccupied() && rookSquare.getPiece().isFirstMove()) {
                    if(Player.calculateAttacksOnSquare(61, opponentLegals).isEmpty() && Player.calculateAttacksOnSquare(62, opponentLegals).isEmpty() && rookSquare.getPiece().getPieceType().isRook()){
                        kingCastles.add(new Move.CastleKingSide(this.board, this.playerKing, 62, (Rook)rookSquare.getPiece(), rookSquare.getSquareCoordinate(), 61));
                    }
                }
            }
            if(!this.board.getSquare(59).isOccupied() && !this.board.getSquare(58).isOccupied() && !this.board.getSquare(57).isOccupied()){
                final Square rookSquare = this.board.getSquare(56);
                if(rookSquare.isOccupied() && rookSquare.getPiece().isFirstMove() && Player.calculateAttacksOnSquare(58, opponentLegals).isEmpty() && Player.calculateAttacksOnSquare(59, opponentLegals).isEmpty()
                        && rookSquare.getPiece().getPieceType().isRook()) {
                    kingCastles.add(new Move.CastleQueenSide(this.board, this.playerKing, 58, (Rook)rookSquare.getPiece(), rookSquare.getSquareCoordinate(), 59));
                }
            }
        }

        return Collections.unmodifiableList(kingCastles);
    }
}
