package com.chess.engine.board;
import com.chess.engine.Alliance;
import com.chess.engine.pieces.*;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

import java.util.*;

public class Board {
    private final List<Square> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;

    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;

    private final Player currentPlayer;
    private Board(Builder builder){
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.White);
        this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.Black);

        final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);

        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
    }


    public Player currentPlayer() {
        return this.currentPlayer;
    }
    public Player whitePlayer(){
        return this.whitePlayer;
    }

    public Player blackPlayer(){
        return this.blackPlayer;
    }

    public Collection<Piece> getWhitePieces(){
        return this.whitePieces;
    }
    public Collection<Piece> getBlackPieces(){
        return this.blackPieces;
    }

    public String toString(){
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 64; i++) {
            final String squareText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", squareText));
            if((i+1)%8 == 0) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces){
        final List<Move> legalMoves = new ArrayList<>();
        for (final Piece piece: pieces) {
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return legalMoves;
    }

    private static Collection<Piece> calculateActivePieces(final List<Square> gameBoard, final Alliance alliance){
        final List<Piece> activePieces = new ArrayList<>();
        for(final Square square: gameBoard){
            if (square.isOccupied()) {
                final Piece piece = square.getPiece();
                if(piece.getPieceAlliance() == alliance){
                    activePieces.add(piece);
                }
            }
        }
        return Collections.unmodifiableList(activePieces);
    }
    public Square getSquare (int squareCoordinate){
        return gameBoard.get(squareCoordinate);
    }
    private static List<Square> createGameBoard(final Builder builder){
        final Square[] squares = new Square[64];
        for(int i = 0; i < 64; i++) {
            squares[i] = Square.createSquare(i, builder.boardConfig.get(i));
        }
        return Collections.unmodifiableList(List.of(squares));
    }

    public static Board createChessBoard(){
        final Builder builder = new Builder();
        builder.setPiece(new Rook(0, Alliance.Black));
        builder.setPiece(new Knight(1, Alliance.Black));
        builder.setPiece(new Bishop(2, Alliance.Black));
        builder.setPiece(new Queen(3, Alliance.Black));
        builder.setPiece(new King(4, Alliance.Black));
        builder.setPiece(new Bishop(5, Alliance.Black));
        builder.setPiece(new Knight(6, Alliance.Black));
        builder.setPiece(new Rook(7, Alliance.Black));
        builder.setPiece(new Pawn(8, Alliance.Black));
        builder.setPiece(new Pawn(9, Alliance.Black));
        builder.setPiece(new Pawn(10, Alliance.Black));
        builder.setPiece(new Pawn(11, Alliance.Black));
        builder.setPiece(new Pawn(12, Alliance.Black));
        builder.setPiece(new Pawn(13, Alliance.Black));
        builder.setPiece(new Pawn(14, Alliance.Black));
        builder.setPiece(new Pawn(15, Alliance.Black));

        builder.setPiece(new Rook(63, Alliance.White));
        builder.setPiece(new Knight(62, Alliance.White));
        builder.setPiece(new Bishop(61, Alliance.White));
        builder.setPiece(new Queen(59, Alliance.White));
        builder.setPiece(new King(60, Alliance.White));
        builder.setPiece(new Bishop(58, Alliance.White));
        builder.setPiece(new Knight(57, Alliance.White));
        builder.setPiece(new Rook(56, Alliance.White));
        builder.setPiece(new Pawn(55, Alliance.White));
        builder.setPiece(new Pawn(54, Alliance.White));
        builder.setPiece(new Pawn(53, Alliance.White));
        builder.setPiece(new Pawn(52, Alliance.White));
        builder.setPiece(new Pawn(51, Alliance.White));
        builder.setPiece(new Pawn(50, Alliance.White));
        builder.setPiece(new Pawn(49, Alliance.White));
        builder.setPiece(new Pawn(48, Alliance.White));

        builder.setMoveMaker(Alliance.White);
        return builder.build();
    }

    public Iterable<Move> getAllLegalMoves() {
        List<Move> allLegalMoves = new ArrayList<>();
        allLegalMoves.addAll(this.whitePlayer.getLegalMoves());
        allLegalMoves.addAll(this.blackPlayer.getLegalMoves());
        return Collections.unmodifiableList(allLegalMoves);
    }

    public static class Builder {
        Map<Integer, Piece> boardConfig;
        Alliance nextMoveMaker;
        Pawn enPassantPawn;
        public Builder(){
            this.boardConfig = new HashMap<>();
        }
        public Builder setPiece(final Piece piece){
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }
        public Builder setMoveMaker(final Alliance nextMoveMaker){
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }
        public Board build(){
            return new Board(this);
        }

        public void setEnPassantPawn(Pawn movedPawn) {
            this.enPassantPawn = enPassantPawn;
        }
    }
}
