package com.chess.engine.board;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public class Move {
    final Board board;
    final Piece movedPiece;
    final int destinationCoord;

    public static final Move NullMove = new NullMove();

    private Move(final Board board, final Piece movedPiece, final int destinationCoord){
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoord = destinationCoord;
    }

    public int getCurrentCoordinate(){
        return this.getMovedPiece().getPiecePosition();
    }

    @Override
    public int hashCode(){
        int result = 1;
        result = 31 * result + this.destinationCoord;
        result = 31 * result + this.movedPiece.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object other){
        if (this == other){
            return true;
        }
        if (!(other instanceof Move)) {
            return false;
        }
        final Move otherMove = (Move) other;
        return (this.getDestinationCoordinate() == ((Move) other).getDestinationCoordinate() &&
                this.getMovedPiece().equals(otherMove.getMovedPiece()));
    }

    public boolean isAttack(){
        return false;
    }
    public boolean isCastlingMove(){
        return false;
    }
    public Piece getAttackedPiece(){
        return null;
    }
    public Piece getMovedPiece(){
        return this.movedPiece;
    }

    public int getDestinationCoordinate() {
        return this.destinationCoord;
    }

    public Board execute() {
        final Board.Builder builder = new Board.Builder();
        for(final Piece piece : this.board.currentPlayer().getActivePieces()) {
            if (!this.movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }
        for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
        return builder.build();
    }

    public static final class MajorMove extends Move{
        public MajorMove(final Board board, final Piece movedPiece, final int destinationCoord) {
            super(board, movedPiece, destinationCoord);
        }
    }


    public static class AttackMove extends Move{
        final Piece attackedPiece;
        public AttackMove(final Board board, final Piece movedPiece, final int destinationCoord, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoord);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public Board execute(){
            return null;
        }
        public boolean isAttack(){
            return true;
        }
        public Piece getAttackedPiece(){
            return this.attackedPiece;
        }
        public int hashCode(){
            return this.attackedPiece.hashCode() + super.hashCode();
        }
        public boolean equals(final Object other){
            if (this == other){
                return true;
            }
            if (!(other instanceof AttackMove)) {
                return false;
            }
            final AttackMove otherAttackMove = (AttackMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }
    }

    public static final class PawnMove extends Move {

        private PawnMove(final Board board, final Piece movedPiece, final int destinationCoord) {
            super(board, movedPiece, destinationCoord);
        }

    }

    public static final class PawnAttackMove extends AttackMove {

        private PawnAttackMove(final Board board, final Piece movedPiece, final int destinationCoord, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoord, attackedPiece);
        }

    }

    public static final class EnPassantMove extends AttackMove {

        private EnPassantMove(final Board board, final Piece movedPiece, final int destinationCoord, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoord, attackedPiece);
        }

    }

    public static final class PawnJump extends Move {

        private PawnJump(final Board board, final Piece movedPiece, final int destinationCoord) {
            super(board, movedPiece, destinationCoord);
        }

        public Board execute(){
            final Board.Builder boardBuilder = new Board.Builder();
            for(final Piece piece: this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    boardBuilder.setPiece(piece);
                }
            }
            for (final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()){
                boardBuilder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn)this.movedPiece.movePiece(this);
            boardBuilder.setPiece(movedPawn);
            boardBuilder.setEnPassantPawn(movedPawn);
            boardBuilder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return boardBuilder.build();
        }
    }

    static abstract class CastleMove extends Move{
        protected final Rook castleRook;
        protected final int castleRookStartingPosition;
        protected final int castleRookDestination;
        public CastleMove(final Board board, final Piece movedPiece, final int destinationCoord, final Rook castleRook, final int castleRookStartingPosition, final int castleRookDestination) {
            super(board, movedPiece, destinationCoord);
            this.castleRook = castleRook;
            this.castleRookDestination = castleRookDestination;
            this.castleRookStartingPosition = castleRookStartingPosition;
        }

        public Rook getCastleRook(){
            return this.castleRook;
        }
        @Override
        public boolean isCastlingMove(){
            return true;
        }

        public Board execute(){
            final Board.Builder boardBuilder = new Board.Builder();
            for(final Piece piece: this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)){
                    boardBuilder.setPiece(piece);
                }
            }
            for (final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()){
                boardBuilder.setPiece(piece);
            }
            boardBuilder.setPiece(this.movedPiece.movePiece(this));
            boardBuilder.setPiece(new Rook(this.castleRookDestination, this.castleRook.getPieceAlliance()));
            boardBuilder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return boardBuilder.build();
        }
    }

    public static final class CastleKingSide extends CastleMove {
        public CastleKingSide(Board board, Piece movedPiece, int destinationCoord, final Rook castleRook, final int castleRookStartingPosition, final int castleRookDestination) {
            super(board, movedPiece, destinationCoord, castleRook, castleRookStartingPosition, castleRookDestination);
        }

        public String toString(){
            return "o-o";
        }
    }

    public static final class CastleQueenSide extends CastleMove {

        public CastleQueenSide(Board board, Piece movedPiece, int destinationCoord, final Rook castleRook, final int castleRookStartingPosition, final int castleRookDestination) {
            super(board, movedPiece, destinationCoord, castleRook, castleRookStartingPosition, castleRookDestination);
        }

        public String toString(){
            return "o-o-o";
        }
    }

    public static final class NullMove extends Move {

        public NullMove() {
            super(null, null, -1);
        }

        public Board execute() {
            throw new RuntimeException("cannot execute null move");
        }
    }

    public static class MoveFactory {
        private MoveFactory(){
            throw new RuntimeException("Not Instantiable");
        }

        public static Move createMove(final Board board, final int currentCoordinate, final int destinationCoord){
            for (final Move move : board.getAllLegalMoves()) {
                if(move.getCurrentCoordinate() == currentCoordinate && move.destinationCoord == destinationCoord) {
                    return move;
                }
            }
            return NullMove;
        }
    }
}
