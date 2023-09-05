package com.chess.engine.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardFunctions;
import com.chess.engine.board.Move;
import com.chess.engine.board.Square;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table {
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private Board chessBoard;

    private Square sourceSquare;
    private Square destinationSquare;
    public Piece humanMovedPiece;
    private static String defaultPieceImagesPath = "/Users/ritwiknigam/IdeaProjects/ChessEngine/art/";
    private final static Dimension outerFrameDimension = new Dimension(600, 600);
    private final static Dimension BoardPanelDimension = new Dimension(400,350);
    private final static Dimension SquarePanelDimensions = new Dimension(10, 10);
    private final Color lightSquareColor = Color.decode("#FFFACD");
    private final Color darkSquareColor = Color.decode("#593E1A");
    public Table() {
        this.gameFrame = new JFrame("JChess");
        this.gameFrame.setLayout(new BorderLayout());

        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(outerFrameDimension);
        this.chessBoard = Board.createChessBoard();

        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);

        this.gameFrame.setVisible(true);
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open PGN");
            }
        });
        fileMenu.add(openPGN);
        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);
        return fileMenu;

    }


    private class BoardPanel extends JPanel {
        final java.util.List<SquarePanel> boardSquares;

        BoardPanel() {
            super(new GridLayout(8,8));
            this.boardSquares = new ArrayList<>();
            for(int i = 0; i < 64; i++){
                final SquarePanel squarePanel = new SquarePanel(this, i);
                this.boardSquares.add(squarePanel);
                add(squarePanel);
            }
            setPreferredSize(BoardPanelDimension);
            validate();
        }


        public void drawBoard(final Board board) {
            removeAll();
            for(final SquarePanel squarePanel : boardSquares){
                squarePanel.drawSquare(board);
                add(squarePanel);
            }
            validate();
            repaint();
        }
    }

    private class SquarePanel extends JPanel {
        private final int squareId;

        SquarePanel( final BoardPanel boardPanel, final int squareId) {
            super(new GridBagLayout());
            this.squareId = squareId;
            setPreferredSize(SquarePanelDimensions);
            assignSquareColor();
            assignPieceIcon(chessBoard);

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if(isRightMouseButton(e)){
                        sourceSquare = null;
                        destinationSquare = null;
                        humanMovedPiece = null;
                    } else if(isLeftMouseButton(e)){
                        if(sourceSquare == null) {
                            sourceSquare = chessBoard.getSquare(squareId);
                            humanMovedPiece = sourceSquare.getPiece();
                            if (humanMovedPiece == null) {
                                sourceSquare = null;
                            }
                        } else {
                            destinationSquare = chessBoard.getSquare(squareId);
                            final Move move = Move.MoveFactory.createMove(chessBoard, sourceSquare.getSquareCoordinate(), destinationSquare.getSquareCoordinate());
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                            if(transition.getMoveStatus().isDone()) {
                                chessBoard = transition.getTransitionBoard();
                            }
                            sourceSquare = null;
                            destinationSquare = null;
                            humanMovedPiece = null;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                boardPanel.drawBoard(chessBoard);
                            }
                        });
                    }
                }

                @Override
                public void mousePressed(final MouseEvent e) {

                }

                @Override
                public void mouseReleased(final MouseEvent e) {

                }

                @Override
                public void mouseEntered(final MouseEvent e) {

                }

                @Override
                public void mouseExited(final MouseEvent e) {

                }
            });

            validate();
        }

        public void drawSquare(final Board board){
            assignSquareColor();
            assignPieceIcon(board);
            validate();
            repaint();
        }
        private void assignPieceIcon(final Board board){
            this.removeAll();
            if(board.getSquare(this.squareId).isOccupied()){
                try {
                    final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath + board.getSquare(squareId).getPiece().getPieceAlliance().toString().substring(0, 1) + board.getSquare(squareId).getPiece().toString() + ".gif"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void assignSquareColor() {
            boolean isLight = ((this.squareId + this.squareId / 8) % 2 == 0);
            setBackground(isLight ? lightSquareColor : darkSquareColor);
        }
    }
}
