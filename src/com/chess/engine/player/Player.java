package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class Player {
    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    protected final boolean isCheck;
    Player (final Board board, final Collection<Move> legalMoves, final Collection<Move> opponentMoves){
        this.board = board;
        this.playerKing = establishKing();
        legalMoves.addAll(calculateKingCastles(legalMoves, opponentMoves));
        this.legalMoves = Collections.unmodifiableCollection(legalMoves);;
        this.isCheck = !Player.calculateAttacksOnSquare(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
    }

    protected static Collection<Move> calculateAttacksOnSquare(int piecePosition, Collection<Move> opponentMoves) {
        final List<Move> attackMoves = new ArrayList<>();
        for (final Move move: opponentMoves) {
            if(move.getDestinationCoordinate() == piecePosition) {
                attackMoves.add(move);
            }
        }
        return Collections.unmodifiableList(attackMoves);
    }

    public King getPlayerKing() {
        return this.playerKing;
    }
    private King establishKing() {
        for ( final Piece piece : getActivePieces()){
            if (piece.getPieceType().isKing()){
                return (King) piece;
            }
        }
        throw new RuntimeException("Not Valid Board");
    }

    public Collection<Move> getLegalMoves() {
        return this.legalMoves;
    }
    public boolean isMoveLegal(Move move){
        return this.legalMoves.contains(move);
    }

    public boolean isCheck(){
        return this.isCheck;
    }
    public boolean isCheckmate(){
        return this.isCheck && !hasEscapeMoves();
    }

    private boolean hasEscapeMoves() {
        for ( final Move move : this.legalMoves){
            final MoveTransition moveTransition = makeMove(move);
            if (moveTransition.getMoveStatus().isDone()){
                return true;
            }
        }
        return false;
    }

    public boolean isStalemate(){
        return !this.isCheck && !hasEscapeMoves();
    }
    public boolean isCastle(){
        return false;
    }

    public MoveTransition makeMove(final Move move){
        if(!isMoveLegal(move)) {
            return new MoveTransition(this.board, move, MoveStatus.IllegalMove);
        }
        final Board transitionBoard = move.execute();
        final Collection<Move> kingAttacks = Player.calculateAttacksOnSquare(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(), transitionBoard.currentPlayer().getLegalMoves());
        if(!kingAttacks.isEmpty()) {
            return new MoveTransition(this.board, move, MoveStatus.LeavesPlayerInCheck);
        }
        return new MoveTransition(transitionBoard, move, MoveStatus.Done);
    }

    public abstract Collection<Piece> getActivePieces();

    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
    public abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals);
}
