package games.chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;

public class Board extends JPanel {
    static final int SIZE = 8;
    JButton[][] squares = new JButton[SIZE][SIZE];
    JButton selectedPiece = null;
    JButton castlingShortButton = new JButton("Castle Short");
    JButton castlingLongButton = new JButton("Castle Long");
    JProgressBar evaluationBar = new JProgressBar(0, 100); // Evaluation bar

    boolean isWhitePlayer;
    boolean isWhiteTurn = true;
    boolean whiteKingInCheck = false;
    boolean blackKingInCheck = false;
    boolean whiteKingMoved = false;
    boolean blackKingMoved = false;
    boolean whiteCastled = false;
    boolean blackCastled = false;
    boolean[] whiteRooksMoved = {false, false}; // Left and Right Rooks
    boolean[] blackRooksMoved = {false, false}; // Left and Right Rooks

    private static final int MIN_SQUARE_SIZE = 50;
    private int squareSize;

    String[][][] boardState = new String[SIZE][SIZE][2];

    public Board(boolean isWhitePlayer) {
        this.isWhitePlayer = isWhitePlayer;
        setLayout(new GridBagLayout());
        setBackground(new Color(43, 43, 43)); // Dark background color
        squareSize = calculateSquareSize();
        initializeBoard();
        initializeStartingPositions();
        initializeControlButtons();
    }

    @Override
    public Dimension getPreferredSize() {
        int preferredSize = squareSize * SIZE;
        return new Dimension(preferredSize, preferredSize);
    }

    private void initializeBoard() {
        int startRow, endRow, increment;
        if (isWhitePlayer) {
            startRow = 0;
            endRow = SIZE;
            increment = 1;
        } else {
            startRow = SIZE - 1;
            endRow = -1;
            increment = -1;
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = SIZE;
        gbc.gridheight = SIZE;
        gbc.fill = GridBagConstraints.BOTH;
        JPanel boardPanel = new JPanel(new GridLayout(SIZE, SIZE));
        boardPanel.setBackground(new Color(43, 43, 43)); // Dark background color
        add(boardPanel, gbc);

        for (int row = startRow; row != endRow; row += increment) {
            for (int col = 0; col < SIZE; col++) {
                squares[row][col] = new JButton();
                if ((row + col) % 2 == 0) {
                    squares[row][col].setBackground(new Color(222, 184, 135));
                } else {
                    squares[row][col].setBackground(new Color(139, 69, 19));
                }

                String imageName = getChessPieceImageName(row, col);
                ImageIcon icon = createImageIcon(imageName);
                squares[row][col].setFocusPainted(false);
                squares[row][col].setBorderPainted(false);
                squares[row][col].setIcon(icon);
                squares[row][col].setHorizontalTextPosition(SwingConstants.CENTER);
                squares[row][col].setVerticalTextPosition(SwingConstants.BOTTOM);

                boardPanel.add(squares[row][col]);
                squares[row][col].addMouseListener(new SquareClickListener(this));
            }
        }
    }

    private void initializeControlButtons() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = SIZE;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;

        JPanel controlPanel = new JPanel(new GridLayout(0, 1));
        controlPanel.setBackground(new Color(43, 43, 43));
        add(controlPanel, gbc);

        customizeButton(castlingShortButton);
        customizeButton(castlingLongButton);

        castlingShortButton.addActionListener(e -> performCastling(false));
        castlingLongButton.addActionListener(e -> performCastling(true));

        controlPanel.add(castlingShortButton);
        controlPanel.add(castlingLongButton);

        evaluationBar.setStringPainted(true);
        evaluationBar.setForeground(Color.WHITE);
        updateEvaluationBar();
        controlPanel.add(evaluationBar);

        updateCastlingButtons();
    }

    private void customizeButton(JButton button) {
        button.setBackground(new Color(60, 63, 65));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
    }

    private void updateCastlingButtons() {
        if (isWhiteTurn) {
            castlingShortButton.setEnabled(!whiteCastled && canCastle(false));
            castlingLongButton.setEnabled(!whiteCastled && canCastle(true));
        } else {
            castlingShortButton.setEnabled(!blackCastled && canCastle(false));
            castlingLongButton.setEnabled(!blackCastled && canCastle(true));
        }
    }

    private boolean canCastle(boolean isLong) {
        if (isWhiteTurn) {
            if (whiteKingMoved || whiteKingInCheck) return false;
            if (isLong) {
                return !whiteRooksMoved[0] && areSquaresEmptyAndSafe(7, 1, 3) && boardState[7][0][1] != null && boardState[7][0][1].equals("Rook");
            } else {
                return !whiteRooksMoved[1] && areSquaresEmptyAndSafe(7, 5, 6) && boardState[7][7][1] != null && boardState[7][7][1].equals("Rook");
            }
        } else {
            if (blackKingMoved || blackKingInCheck) return false;
            if (isLong) {
                return !blackRooksMoved[0] && areSquaresEmptyAndSafe(0, 1, 3) && boardState[0][0][1] != null && boardState[0][0][1].equals("Rook");
            } else {
                return !blackRooksMoved[1] && areSquaresEmptyAndSafe(0, 5, 6) && boardState[0][7][1] != null && boardState[0][7][1].equals("Rook");
            }
        }
    }

    private boolean areSquaresEmptyAndSafe(int row, int colStart, int colEnd) {
        for (int col = colStart; col <= colEnd; col++) {
            if (boardState[row][col][0] != null || isUnderAttack(row, col, isWhiteTurn ? "black" : "white")) {
                return false;
            }
        }
        return true;
    }

    private void promotePawn(int row, int col, String newPiece, JDialog promotionDialog) {
        String color = boardState[row][col][0];
        boardState[row][col][1] = newPiece;
        String imageName = color + newPiece + ".png";
        squares[row][col].setIcon(createImageIcon(imageName));
        promotionDialog.dispose();
    }

    private void performCastling(boolean isLong) {
        if (isWhiteTurn) {
            if (isLong) {
                if (!whiteRooksMoved[0] && areSquaresEmptyAndSafe(7, 1, 3)) {
                    // White Queen-side castling
                    movePiece(7, 4, 7, 2);
                    movePiece(7, 0, 7, 3);
                    whiteCastled = true;
                }
            } else {
                if (!whiteRooksMoved[1] && areSquaresEmptyAndSafe(7, 5, 6)) {
                    // White King-side castling
                    movePiece(7, 4, 7, 6);
                    movePiece(7, 7, 7, 5);
                    whiteCastled = true;
                }
            }
        } else {
            if (isLong) {
                if (!blackRooksMoved[0] && areSquaresEmptyAndSafe(0, 1, 3)) {
                    // Black Queen-side castling
                    movePiece(0, 4, 0, 2);
                    movePiece(0, 0, 0, 3);
                    blackCastled = true;
                }
            } else {
                if (!blackRooksMoved[1] && areSquaresEmptyAndSafe(0, 5, 6)) {
                    // Black King-side castling
                    movePiece(0, 4, 0, 6);
                    movePiece(0, 7, 0, 5);
                    blackCastled = true;
                }
            }
        }
        updateKingInCheckStatus();
        isWhiteTurn = !isWhiteTurn; // Toggle turn only after a successful move
        updateCastlingButtons();
    }

    private void movePiece(int startRow, int startCol, int endRow, int endCol) {
        JButton startSquare = squares[startRow][startCol];
        JButton endSquare = squares[endRow][endCol];
        ImageIcon pieceIcon = (ImageIcon) startSquare.getIcon();

        endSquare.setIcon(pieceIcon);
        startSquare.setIcon(null);

        boardState[endRow][endCol][0] = boardState[startRow][startCol][0];
        boardState[endRow][endCol][1] = boardState[startRow][startCol][1];
        boardState[startRow][startCol][0] = null;
        boardState[startRow][startCol][1] = null;

        updateEvaluationBar(); // Update evaluation bar after the move

        // Check for pawn promotion
        if ((endRow == 0 || endRow == 7) && "Pawn".equals(boardState[endRow][endCol][1])) {
            showPromotionOptions(endRow, endCol);
        }
    }

    private void showPromotionOptions(int row, int col) {
        JDialog promotionDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Pawn Promotion", true);
        promotionDialog.setLayout(new FlowLayout());
        promotionDialog.setSize(300, 200);
        promotionDialog.setLocationRelativeTo(this);

        JButton queenButton = new JButton(createImageIcon(boardState[row][col][0] + "Queen.png"));
        queenButton.addActionListener(e -> promotePawn(row, col, "Queen", promotionDialog));

        JButton rookButton = new JButton(createImageIcon(boardState[row][col][0] + "Rook.png"));
        rookButton.addActionListener(e -> promotePawn(row, col, "Rook", promotionDialog));

        JButton bishopButton = new JButton(createImageIcon(boardState[row][col][0] + "Bishop.png"));
        bishopButton.addActionListener(e -> promotePawn(row, col, "Bishop", promotionDialog));

        JButton knightButton = new JButton(createImageIcon(boardState[row][col][0] + "Knight.png"));
        knightButton.addActionListener(e -> promotePawn(row, col, "Knight", promotionDialog));

        promotionDialog.add(queenButton);
        promotionDialog.add(rookButton);
        promotionDialog.add(bishopButton);
        promotionDialog.add(knightButton);

        promotionDialog.setVisible(true);
    }

    private int calculateSquareSize() {
        int width = getWidth();
        int height = getHeight();
        int smallerDimension = Math.min(width, height);
        return Math.max(smallerDimension / SIZE, MIN_SQUARE_SIZE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        squareSize = calculateSquareSize();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                squares[row][col].setPreferredSize(new Dimension(squareSize, squareSize));
            }
        }
        revalidate();
    }

    private void initializeStartingPositions() {
        // Initialize starting positions for black pieces
        for (int col = 0; col < SIZE; col++) {
            boardState[0][col][0] = "black";
            switch (col) {
                case 0:
                case 7:
                    boardState[0][col][1] = "Rook";
                    break;
                case 1:
                case 6:
                    boardState[0][col][1] = "Knight";
                    break;
                case 2:
                case 5:
                    boardState[0][col][1] = "Bishop";
                    break;
                case 3:
                    boardState[0][col][1] = "Queen";
                    break;
                case 4:
                    boardState[0][col][1] = "King";
                    break;
            }
            boardState[1][col][0] = "black";
            boardState[1][col][1] = "Pawn";
        }

        // Initialize starting positions for white pieces
        for (int col = 0; col < SIZE; col++) {
            boardState[7][col][0] = "white";
            switch (col) {
                case 0:
                case 7:
                    boardState[7][col][1] = "Rook";
                    break;
                case 1:
                case 6:
                    boardState[7][col][1] = "Knight";
                    break;
                case 2:
                case 5:
                    boardState[7][col][1] = "Bishop";
                    break;
                case 3:
                    boardState[7][col][1] = "Queen";
                    break;
                case 4:
                    boardState[7][col][1] = "King";
                    break;
            }
            boardState[6][col][0] = "white";
            boardState[6][col][1] = "Pawn";
        }
    }

    private String getChessPieceImageName(int row, int col) {
        switch (row) {
            case 0:
                switch (col) {
                    case 0:
                    case 7:
                        return "blackRook.png";
                    case 1:
                    case 6:
                        return "blackKnight.png";
                    case 2:
                    case 5:
                        return "blackBishop.png";
                    case 3:
                        return "blackQueen.png";
                    case 4:
                        return "blackKing.png";
                }
                break;
            case 1:
                return "blackPawn.png";
            case 6:
                return "whitePawn.png";
            case 7:
                switch (col) {
                    case 0:
                    case 7:
                        return "whiteRook.png";
                    case 1:
                    case 6:
                        return "whiteKnight.png";
                    case 2:
                    case 5:
                        return "whiteBishop.png";
                    case 3:
                        return "whiteQueen.png";
                    case 4:
                        return "whiteKing.png";
                }
                break;
        }
        return "";
    }

    private ImageIcon createImageIcon(String imageName) {
        URL resource = getClass().getResource("/games/chess/Pieces/" + imageName);
        if (resource != null) {
            try {
                Image image = ImageIO.read(resource);
                if (image != null) {
                    ImageIcon icon = new ImageIcon(image);
                    icon.setDescription("Pieces/" + imageName);
                    return icon;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void updateKingInCheckStatus() {
        whiteKingInCheck = false;
        blackKingInCheck = false;
        highlightKingIfUnderAttack();
    }

    private void highlightKingIfUnderAttack() {
        Point whiteKingPos = findKing("white");
        Point blackKingPos = findKing("black");

        // Check if white king is under attack
        whiteKingInCheck = whiteKingPos != null && isUnderAttack(whiteKingPos.x, whiteKingPos.y, "black");
        if (whiteKingInCheck) {
            squares[whiteKingPos.x][whiteKingPos.y].setBackground(Color.YELLOW);
        }

        // Check if black king is under attack
        blackKingInCheck = blackKingPos != null && isUnderAttack(blackKingPos.x, blackKingPos.y, "white");
        if (blackKingInCheck) {
            squares[blackKingPos.x][blackKingPos.y].setBackground(Color.YELLOW);
        }
    }

    private Point findKing(String color) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (color.equals(boardState[row][col][0]) && "King".equals(boardState[row][col][1])) {
                    return new Point(row, col);
                }
            }
        }
        return null;
    }

    private boolean isUnderAttack(int row, int col, String attackerColor) {
        return isUnderAttack(row, col, attackerColor, boardState);
    }

    private boolean isUnderAttack(int row, int col, String attackerColor, String[][][] boardState) {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (attackerColor.equals(boardState[r][c][0])) {
                    if (canMoveTo(r, c, row, col, boardState)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean canMoveTo(int startRow, int startCol, int endRow, int endCol, String[][][] boardState) {
        String pieceType = boardState[startRow][startCol][1];
        switch (pieceType) {
            case "Pawn":
                return canPawnMoveTo(startRow, startCol, endRow, endCol, boardState[startRow][startCol][0], boardState);
            case "Rook":
                return canRookMoveTo(startRow, startCol, endRow, endCol, boardState);
            case "Knight":
                return canKnightMoveTo(startRow, startCol, endRow, endCol);
            case "Bishop":
                return canBishopMoveTo(startRow, startCol, endRow, endCol, boardState);
            case "Queen":
                return canQueenMoveTo(startRow, startCol, endRow, endCol, boardState);
            case "King":
                return canKingMoveTo(startRow, startCol, endRow, endCol);
            default:
                return false;
        }
    }

    private boolean canPawnMoveTo(int startRow, int startCol, int endRow, int endCol, String color, String[][][] boardState) {
        int direction = color.equals("white") ? -1 : 1;
        if (startCol == endCol) {
            if (endRow == startRow + direction) {
                return boardState[endRow][endCol][0] == null;
            } else if ((startRow == 1 && direction == 1) || (startRow == 6 && direction == -1)) {
                return endRow == startRow + 2 * direction && boardState[endRow][endCol][0] == null;
            }
        } else if (Math.abs(startCol - endCol) == 1) {
            return endRow == startRow + direction && boardState[endRow][endCol][0] != null && !color.equals(boardState[endRow][endCol][0]);
        }
        return false;
    }

    private boolean canRookMoveTo(int startRow, int startCol, int endRow, int endCol, String[][][] boardState) {
        if (startRow != endRow && startCol != endCol) {
            return false;
        }
        int rowStep = Integer.compare(endRow, startRow);
        int colStep = Integer.compare(endCol, startCol);
        for (int r = startRow + rowStep, c = startCol + colStep; r != endRow || c != endCol; r += rowStep, c += colStep) {
            if (boardState[r][c][0] != null) {
                return false;
            }
        }
        return true;
    }

    private boolean canKnightMoveTo(int startRow, int startCol, int endRow, int endCol) {
        int rowDiff = Math.abs(startRow - endRow);
        int colDiff = Math.abs(startCol - endCol);
        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }

    private boolean canBishopMoveTo(int startRow, int startCol, int endRow, int endCol, String[][][] boardState) {
        if (Math.abs(startRow - endRow) != Math.abs(startCol - endCol)) {
            return false;
        }
        int rowStep = Integer.compare(endRow, startRow);
        int colStep = Integer.compare(endCol, startCol);
        for (int r = startRow + rowStep, c = startCol + colStep; r != endRow && c != endCol; r += rowStep, c += colStep) {
            if (boardState[r][c][0] != null) {
                return false;
            }
        }
        return true;
    }

    private boolean canQueenMoveTo(int startRow, int startCol, int endRow, int endCol, String[][][] boardState) {
        return canRookMoveTo(startRow, startCol, endRow, endCol, boardState) || canBishopMoveTo(startRow, startCol, endRow, endCol, boardState);
    }

    private boolean canKingMoveTo(int startRow, int startCol, int endRow, int endCol) {
        int rowDiff = Math.abs(startRow - endRow);
        int colDiff = Math.abs(startCol - endCol);
        return rowDiff <= 1 && colDiff <= 1;
    }

    private String[][][] copyBoardState() {
        String[][][] copy = new String[SIZE][SIZE][2];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                copy[row][col][0] = boardState[row][col][0];
                copy[row][col][1] = boardState[row][col][1];
            }
        }
        return copy;
    }

    private void updateEvaluationBar() {
        int evaluation = evaluateBoard();
        evaluationBar.setValue(evaluation);
        if (evaluation > 50) {
            evaluationBar.setString("White +" + (evaluation - 50));
        } else if (evaluation < 50) {
            evaluationBar.setString("Black +" + (50 - evaluation));
        } else {
            evaluationBar.setString("Equal");
        }
    }

    private int evaluateBoard() {
        int whiteScore = 0;
        int blackScore = 0;

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (boardState[row][col][0] != null) {
                    int pieceValue = getPieceValue(boardState[row][col][1]);
                    if (boardState[row][col][0].equals("white")) {
                        whiteScore += pieceValue;
                    } else {
                        blackScore += pieceValue;
                    }
                }
            }
        }

        int totalScore = whiteScore - blackScore + 50; // Adjust to fit 0-100 range
        return Math.min(100, Math.max(0, totalScore)); // Clamp between 0 and 100
    }

    private int getPieceValue(String piece) {
        switch (piece) {
            case "Pawn":
                return 1;
            case "Bishop":
            case "Knight":
                return 3;
            case "Rook":
                return 5;
            case "Queen":
                return 9;
            default:
                return 0;
        }
    }

    private class SquareClickListener extends MouseAdapter {
        private final Board board;

        public SquareClickListener(Board board) {
            this.board = board;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            JButton clickedSquare = (JButton) e.getSource();
            int clickedRow = -1, clickedCol = -1;

            // Find the row and column of the clicked square
            for (int row = 0; row < Board.SIZE; row++) {
                for (int col = 0; col < Board.SIZE; col++) {
                    if (board.squares[row][col] == clickedSquare) {
                        clickedRow = row;
                        clickedCol = col;
                        break;
                    }
                }
                if (clickedRow != -1) break;
            }

            if (SwingUtilities.isLeftMouseButton(e)) {
                if (board.selectedPiece != null) {
                    Color squareColor = clickedSquare.getBackground();
                    if (squareColor.equals(Color.RED) || squareColor.equals(Color.BLUE)) {
                        moveSelectedPiece(clickedSquare, clickedRow, clickedCol);
                        clearHighlights();
                        highlightKingIfUnderAttack();
                    } else {
                        clearHighlights();
                        board.selectedPiece = null;
                    }
                } else {
                    if (clickedSquare.getIcon() != null && isTurnValid(clickedRow, clickedCol)) {
                        board.selectedPiece = clickedSquare;
                        highlightMoves(clickedRow, clickedCol);
                    }
                }
            } else if (SwingUtilities.isRightMouseButton(e)) {
                displayPieceInformation(clickedSquare, clickedRow, clickedCol);
            }
        }

        private boolean isTurnValid(int row, int col) {
            String pieceColor = board.boardState[row][col][0];
            return (board.isWhiteTurn && "white".equals(pieceColor)) || (!board.isWhiteTurn && "black".equals(pieceColor));
        }

        private boolean isValidMove(int row, int col) {
            return row >= 0 && row < Board.SIZE && col >= 0 && col < Board.SIZE;
        }

        private boolean isLegalMoveWhenInCheck(int startRow, int startCol, int endRow, int endCol) {
            // Check if the move is legal when the king is in check
            boolean isWhite = board.isWhiteTurn;
            Point kingPos = findKing(isWhite ? "white" : "black");

            // If moving the king, check if the new position is safe
            if (board.boardState[startRow][startCol][1].equals("King")) {
                return !isUnderAttack(endRow, endCol, isWhite ? "black" : "white");
            }

            // Simulate the move and check if it resolves the check
            String[][][] boardStateCopy = copyBoardState();
            boardStateCopy[endRow][endCol][0] = boardStateCopy[startRow][startCol][0];
            boardStateCopy[endRow][endCol][1] = boardStateCopy[startRow][startCol][1];
            boardStateCopy[startRow][startCol][0] = null;
            boardStateCopy[startRow][startCol][1] = null;

            return !isUnderAttack(kingPos.x, kingPos.y, isWhite ? "black" : "white", boardStateCopy);
        }

        private boolean isLegalMove(int startRow, int startCol, int endRow, int endCol) {
            boolean isWhite = board.isWhiteTurn;
            Point kingPos = findKing(isWhite ? "white" : "black");

            // Check if the piece being moved is a king
            if (board.boardState[startRow][startCol][1].equals("King")) {
                // Simulate the move and check if it resolves the check
                String[][][] boardStateCopy = copyBoardState();
                boardStateCopy[endRow][endCol][0] = boardStateCopy[startRow][startCol][0];
                boardStateCopy[endRow][endCol][1] = boardStateCopy[startRow][startCol][1];
                boardStateCopy[startRow][startCol][0] = null;
                boardStateCopy[startRow][startCol][1] = null;

                return !isUnderAttack(endRow, endCol, isWhite ? "black" : "white", boardStateCopy);
            } else {
                // Simulate the move and check if it exposes the king to potential pin
                String[][][] boardStateCopy = copyBoardState();
                boardStateCopy[endRow][endCol][0] = boardStateCopy[startRow][startCol][0];
                boardStateCopy[endRow][endCol][1] = boardStateCopy[startRow][startCol][1];
                boardStateCopy[startRow][startCol][0] = null;
                boardStateCopy[startRow][startCol][1] = null;

                return !isPotentialPin(startRow, startCol, endRow, endCol, isWhite ? "black" : "white", kingPos, boardStateCopy);
            }
        }

        private boolean isPotentialPin(int startRow, int startCol, int endRow, int endCol, String attackerColor, Point kingPos, String[][][] boardState) {
            // Simulate the move to the end position
            String[][][] boardStateCopy = copyBoardState();
            boardStateCopy[endRow][endCol][0] = boardStateCopy[startRow][startCol][0];
            boardStateCopy[endRow][endCol][1] = boardStateCopy[startRow][startCol][1];
            boardStateCopy[startRow][startCol][0] = null;
            boardStateCopy[startRow][startCol][1] = null;

            // Check if the move exposes the king to a potential pin
            for (int r = 0; r < Board.SIZE; r++) {
                for (int c = 0; c < Board.SIZE; c++) {
                    if (attackerColor.equals(boardStateCopy[r][c][0])) {
                        if (canMoveTo(r, c, kingPos.x, kingPos.y, boardStateCopy)) {
                            return true; // Move exposes the king to potential pin
                        }
                    }
                }
            }
            return false;

        }

        private void moveSelectedPiece(JButton clickedSquare, int clickedRow, int clickedCol) {
            JButton selectedSquare = board.selectedPiece;
            ImageIcon movedPieceIcon = (ImageIcon) selectedSquare.getIcon();

            // Get the name of the piece
            String pieceName = getPieceName(movedPieceIcon);

            // Update the boardState array
            int selectedRow = -1, selectedCol = -1;
            for (int row = 0; row < Board.SIZE; row++) {
                for (int col = 0; col < Board.SIZE; col++) {
                    if (board.squares[row][col] == selectedSquare) {
                        selectedRow = row;
                        selectedCol = col;
                        break;
                    }
                }
                if (selectedRow != -1) break;
            }

            // Check if move is legal when the king is in check or pinned
            if ((whiteKingInCheck || blackKingInCheck) && !isLegalMoveWhenInCheck(selectedRow, selectedCol, clickedRow, clickedCol)) {
                System.out.println("Illegal move, king is in check or pinned");
                return; // Illegal move
            }

            // Check if the move is a king move or rook move to disable castling appropriately
            if (board.boardState[selectedRow][selectedCol][1].equals("King")) {
                if (board.isWhiteTurn) {
                    whiteKingMoved = true;
                } else {
                    blackKingMoved = true;
                }
            } else if (board.boardState[selectedRow][selectedCol][1].equals("Rook")) {
                if (board.isWhiteTurn) {
                    if (selectedCol == 0) {
                        whiteRooksMoved[0] = true;
                    } else if (selectedCol == 7) {
                        whiteRooksMoved[1] = true;
                    }
                } else {
                    if (selectedCol == 0) {
                        blackRooksMoved[0] = true;
                    } else if (selectedCol == 7) {
                        blackRooksMoved[1] = true;
                    }
                }
            }

            // Check if the move is legal considering pins
            if (!isLegalMove(selectedRow, selectedCol, clickedRow, clickedCol)) {
                System.out.println("Illegal move, piece is pinned");
                return; // Illegal move
            }

            board.boardState[clickedRow][clickedCol][0] = board.boardState[selectedRow][selectedCol][0];
            board.boardState[clickedRow][clickedCol][1] = board.boardState[selectedRow][selectedCol][1];
            board.boardState[selectedRow][selectedCol][0] = null;
            board.boardState[selectedRow][selectedCol][1] = null;

            // Move the piece on the GUI
            clickedSquare.setIcon(movedPieceIcon);
            selectedSquare.setIcon(null);  // Remove the icon from the old square
            board.selectedPiece = null;  // Deselect after move
            board.isWhiteTurn = !board.isWhiteTurn; // Toggle turn only after a successful move

            // Check if the king is still in check after the move
            updateKingInCheckStatus();

            // Update the castling button
            updateCastlingButtons();

            // Update the evaluation bar
            updateEvaluationBar();

            // Check for pawn promotion
            if ((clickedRow == 0 || clickedRow == 7) && "Pawn".equals(board.boardState[clickedRow][clickedCol][1])) {
                showPromotionOptions(clickedRow, clickedCol);
            }

            // Check for checkmate
            checkForCheckmate();

            // Output the message
            System.out.println("Moved " + pieceName + " to: (" + clickedRow + ", " + clickedCol + ")");
            System.out.println("Turn: " + (board.isWhiteTurn ? "White's turn" : "Black's turn"));
        }

        private void highlightMoves(int row, int col) {
            System.out.println("Highlighting moves for row: " + row + ", col: " + col);
            clearHighlights();
            String pieceType = board.boardState[row][col][1];
            if (pieceType == null) {
                // No piece on this square
                System.out.println("No piece on this square");
                return;
            }
            switch (pieceType) {
                case "Pawn":
                    highlightPawnMoves(row, col);
                    break;
                case "Rook":
                    highlightRookMoves(row, col);
                    break;
                case "Bishop":
                    highlightBishopMoves(row, col);
                    break;
                case "Knight":
                    highlightKnightMoves(row, col);
                    break;
                case "Queen":
                    // Rook-like and Bishop-like moves
                    highlightRookMoves(row, col);
                    highlightBishopMoves(row, col);
                    break;
                case "King":
                    highlightKingMoves(row, col);
                    break;
            }
        }

        private void highlightPawnMoves(int row, int col) {
            String pieceColor = board.boardState[row][col][0];
            int direction = pieceColor.equals("white") ? -1 : 1;

            // Standard move forward
            if (isValidMove(row + direction, col) && board.squares[row + direction][col].getIcon() == null) {
                board.squares[row + direction][col].setBackground(Color.RED);
                System.out.println("Standard move forward");
                // First move can go two squares if the first move is clear
                if ((row == 6 && pieceColor.equals("white")) || (row == 1 && pieceColor.equals("black"))) {
                    if (isValidMove(row + 2 * direction, col) && board.squares[row + 2 * direction][col].getIcon() == null) {
                        board.squares[row + 2 * direction][col].setBackground(Color.RED);
                        System.out.println("First move, two squares forward");
                    }
                }
            }

            // Capture moves for pawn: diagonal capture
            int[] offsets = {-1, 1};
            for (int offset : offsets) {
                int targetRow = row + direction;
                int targetCol = col + offset;
                if (isValidMove(targetRow, targetCol)) {
                    JButton targetSquare = board.squares[targetRow][targetCol];
                    if (targetSquare.getIcon() != null) {
                        String targetColor = board.boardState[targetRow][targetCol][0];
                        if (!pieceColor.equals(targetColor)) { // Ensure the piece is of the opposite color
                            targetSquare.setBackground(Color.BLUE); // Highlight for capture
                            System.out.println("Highlight for capture at row: " + targetRow + ", col: " + targetCol);
                        }
                    }
                }
            }
        }

        private void highlightRookMoves(int row, int col) {
            String pieceColor = board.boardState[row][col][0];
            int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; // Right, Down, Left, Up

            for (int[] direction : directions) {
                int r = row + direction[0];
                int c = col + direction[1];
                while (isValidMove(r, c) && board.squares[r][c].getIcon() == null) {
                    board.squares[r][c].setBackground(Color.RED);
                    r += direction[0];
                    c += direction[1];
                }
                if (isValidMove(r, c) && !board.boardState[r][c][0].equals(pieceColor)) {
                    board.squares[r][c].setBackground(Color.BLUE);
                }
            }
        }

        private void highlightBishopMoves(int row, int col) {
            String pieceColor = board.boardState[row][col][0];
            int[][] directions = {{1, 1}, {1, -1}, {-1, -1}, {-1, 1}}; // Diagonal directions

            for (int[] direction : directions) {
                int r = row + direction[0];
                int c = col + direction[1];
                while (isValidMove(r, c) && board.squares[r][c].getIcon() == null) {
                    board.squares[r][c].setBackground(Color.RED);
                    r += direction[0];
                    c += direction[1];
                }
                if (isValidMove(r, c) && !board.boardState[r][c][0].equals(pieceColor)) {
                    board.squares[r][c].setBackground(Color.BLUE);
                }
            }
        }

        private void highlightKnightMoves(int row, int col) {
            int[] rowMoves = {2, 1, -1, -2, -2, -1, 1, 2};
            int[] colMoves = {1, 2, 2, 1, -1, -2, -2, -1};
            String pieceColor = board.boardState[row][col][0];

            for (int i = 0; i < 8; i++) {
                int r = row + rowMoves[i];
                int c = col + colMoves[i];
                if (isValidMove(r, c)) {
                    JButton targetSquare = board.squares[r][c];
                    if (targetSquare.getIcon() == null || !board.boardState[r][c][0].equals(pieceColor)) {
                        targetSquare.setBackground(targetSquare.getIcon() == null ? Color.RED : Color.BLUE);
                    }
                }
            }
        }

        private void highlightKingMoves(int row, int col) {
            int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
            int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

            for (int i = 0; i < 8; i++) {
                int newRow = row + dx[i];
                int newCol = col + dy[i];

                if (isValidMove(newRow, newCol) && !isUnderAttack(newRow, newCol, board.isWhiteTurn ? "black" : "white")) {
                    JButton targetSquare = board.squares[newRow][newCol];
                    if (targetSquare.getIcon() == null) {
                        targetSquare.setBackground(Color.RED);
                    } else {
                        String targetColor = board.boardState[newRow][newCol][0];
                        if (!board.boardState[row][col][0].equals(targetColor)) {
                            targetSquare.setBackground(Color.BLUE);
                        }
                    }
                }
            }
        }

        private void displayPieceInformation(JButton clickedSquare, int clickedRow, int clickedCol) {
            ImageIcon icon = (ImageIcon) clickedSquare.getIcon();
            String pieceName = getPieceName(icon);
            String message = pieceName.isEmpty() ? "No piece" : "Piece: " + pieceName;
            System.out.println(message);
            System.out.println("Coordinates: (" + clickedRow + ", " + clickedCol + ")");
        }

        private void clearHighlights() {
            for (int row = 0; row < Board.SIZE; row++) {
                for (int col = 0; col < Board.SIZE; col++) {
                    if ((row + col) % 2 == 0) {
                        board.squares[row][col].setBackground(new Color(222, 184, 135)); // Light square color
                    } else {
                        board.squares[row][col].setBackground(new Color(139, 69, 19)); // Dark square color
                    }
                }
            }
            // Restore king's yellow highlight if it is in check
            if (whiteKingInCheck) {
                Point whiteKingPos = findKing("white");
                if (whiteKingPos != null) {
                    board.squares[whiteKingPos.x][whiteKingPos.y].setBackground(Color.YELLOW);
                }
            }
            if (blackKingInCheck) {
                Point blackKingPos = findKing("black");
                if (blackKingPos != null) {
                    board.squares[blackKingPos.x][blackKingPos.y].setBackground(Color.YELLOW);
                }
            }
        }

        private String getPieceName(ImageIcon icon) {
            if (icon != null) {
                String description = icon.getDescription(); // Ensure this matches the set description exactly.
                int lastSlashIndex = description.lastIndexOf('/');
                int extensionIndex = description.lastIndexOf('.');
                if (lastSlashIndex != -1 && extensionIndex != -1) {
                    return description.substring(lastSlashIndex + 1, extensionIndex);
                }
            }
            return "No piece";
        }

        private void checkForCheckmate() {
            if (isCheckmate("white")) {
                JOptionPane.showMessageDialog(board, "Checkmate! Black wins!");
            } else if (isCheckmate("black")) {
                JOptionPane.showMessageDialog(board, "Checkmate! White wins!");
            }
        }

        private boolean isCheckmate(String color) {
            // Find the king of the given color
            Point kingPos = findKing(color);
            if (kingPos == null) {
                return false; // No king found, not a valid state
            }

            // Check if the king is in check
            if (!isKingInCheck(kingPos.x, kingPos.y, color)) {
                return false;
            }

            // Get all legal moves for the given color
            List<Point> legalMoves = getAllLegalMoves(color);

            // If there are no legal moves left, it's checkmate
            return legalMoves.isEmpty();
        }

        private List<Point> getAllLegalMoves(String color) {
            List<Point> legalMoves = new ArrayList<>();
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    if (color.equals(boardState[row][col][0])) {
                        List<Point> pieceMoves = getLegalMoves(row, col);
                        legalMoves.addAll(pieceMoves);
                    }
                }
            }
            return legalMoves;
        }

        private List<Point> getLegalMoves(int row, int col) {
            List<Point> legalMoves = new ArrayList<>();
            String pieceType = boardState[row][col][1];
            if (pieceType == null) {
                return legalMoves;
            }
            switch (pieceType) {
                case "Pawn":
                    addPawnMoves(row, col, legalMoves);
                    break;
                case "Rook":
                    addRookMoves(row, col, legalMoves);
                    break;
                case "Bishop":
                    addBishopMoves(row, col, legalMoves);
                    break;
                case "Knight":
                    addKnightMoves(row, col, legalMoves);
                    break;
                case "Queen":
                    addRookMoves(row, col, legalMoves);
                    addBishopMoves(row, col, legalMoves);
                    break;
                case "King":
                    addKingMoves(row, col, legalMoves);
                    break;
            }
            return legalMoves;
        }

        private void addPawnMoves(int row, int col, List<Point> legalMoves) {
            String pieceColor = boardState[row][col][0];
            int direction = pieceColor.equals("white") ? -1 : 1;

            // Standard move forward
            if (isValidMove(row + direction, col) && board.squares[row + direction][col].getIcon() == null) {
                if (isLegalMove(row, col, row + direction, col)) {
                    legalMoves.add(new Point(row + direction, col));
                }
                // First move can go two squares if the first move is clear
                if ((row == 6 && pieceColor.equals("white")) || (row == 1 && pieceColor.equals("black"))) {
                    if (isValidMove(row + 2 * direction, col) && board.squares[row + 2 * direction][col].getIcon() == null) {
                        if (isLegalMove(row, col, row + 2 * direction, col)) {
                            legalMoves.add(new Point(row + 2 * direction, col));
                        }
                    }
                }
            }

            // Capture moves for pawn: diagonal capture
            int[] offsets = {-1, 1};
            for (int offset : offsets) {
                int targetRow = row + direction;
                int targetCol = col + offset;
                if (isValidMove(targetRow, targetCol)) {
                    JButton targetSquare = board.squares[targetRow][targetCol];
                    if (targetSquare.getIcon() != null) {
                        String targetColor = boardState[targetRow][targetCol][0];
                        if (!pieceColor.equals(targetColor)) { // Ensure the piece is of the opposite color
                            if (isLegalMove(row, col, targetRow, targetCol)) {
                                legalMoves.add(new Point(targetRow, targetCol));
                            }
                        }
                    }
                }
            }
        }

        private void addRookMoves(int row, int col, List<Point> legalMoves) {
            String pieceColor = boardState[row][col][0];
            int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; // Right, Down, Left, Up

            for (int[] direction : directions) {
                int r = row + direction[0];
                int c = col + direction[1];
                while (isValidMove(r, c) && board.squares[r][c].getIcon() == null) {
                    if (isLegalMove(row, col, r, c)) {
                        legalMoves.add(new Point(r, c));
                    }
                    r += direction[0];
                    c += direction[1];
                }
                if (isValidMove(r, c) && !boardState[r][c][0].equals(pieceColor)) {
                    if (isLegalMove(row, col, r, c)) {
                        legalMoves.add(new Point(r, c));
                    }
                }
            }
        }

        private void addBishopMoves(int row, int col, List<Point> legalMoves) {
            String pieceColor = boardState[row][col][0];
            int[][] directions = {{1, 1}, {1, -1}, {-1, -1}, {-1, 1}}; // Diagonal directions

            for (int[] direction : directions) {
                int r = row + direction[0];
                int c = col + direction[1];
                while (isValidMove(r, c) && board.squares[r][c].getIcon() == null) {
                    if (isLegalMove(row, col, r, c)) {
                        legalMoves.add(new Point(r, c));
                    }
                    r += direction[0];
                    c += direction[1];
                }
                if (isValidMove(r, c) && !boardState[r][c][0].equals(pieceColor)) {
                    if (isLegalMove(row, col, r, c)) {
                        legalMoves.add(new Point(r, c));
                    }
                }
            }
        }

        private void addKnightMoves(int row, int col, List<Point> legalMoves) {
            int[] rowMoves = {2, 1, -1, -2, -2, -1, 1, 2};
            int[] colMoves = {1, 2, 2, 1, -1, -2, -2, -1};
            String pieceColor = boardState[row][col][0];

            for (int i = 0; i < 8; i++) {
                int r = row + rowMoves[i];
                int c = col + colMoves[i];
                if (isValidMove(r, c)) {
                    JButton targetSquare = board.squares[r][c];
                    if (targetSquare.getIcon() == null || !boardState[r][c][0].equals(pieceColor)) {
                        if (isLegalMove(row, col, r, c)) {
                            legalMoves.add(new Point(r, c));
                        }
                    }
                }
            }
        }

        private void addKingMoves(int row, int col, List<Point> legalMoves) {
            int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
            int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

            for (int i = 0; i < 8; i++) {
                int newRow = row + dx[i];
                int newCol = col + dy[i];

                if (isValidMove(newRow, newCol) && !isUnderAttack(newRow, newCol, board.isWhiteTurn ? "black" : "white")) {
                    JButton targetSquare = board.squares[newRow][newCol];
                    if (targetSquare.getIcon() == null) {
                        if (isLegalMove(row, col, newRow, newCol)) {
                            legalMoves.add(new Point(newRow, newCol));
                        }
                    } else {
                        String targetColor = board.boardState[newRow][newCol][0];
                        if (!board.boardState[row][col][0].equals(targetColor)) {
                            if (isLegalMove(row, col, newRow, newCol)) {
                                legalMoves.add(new Point(newRow, newCol));
                            }
                        }
                    }
                }
            }
        }

        private boolean isKingInCheck(int row, int col, String color) {
            String opponentColor = color.equals("white") ? "black" : "white";
            return isUnderAttack(row, col, opponentColor);
        }
    }
}
