import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class Board extends JPanel {
    static final int SIZE = 8;
    JButton[][] squares = new JButton[SIZE][SIZE];
    JButton selectedPiece = null;
    boolean isWhitePlayer;

    boolean isWhiteTurn = true;

    private static final int MIN_SQUARE_SIZE = 50;
    private int squareSize;

    String[][][] boardState = new String[SIZE][SIZE][2];

    public Board(boolean isWhitePlayer) {
        this.isWhitePlayer = isWhitePlayer;
        setLayout(new GridLayout(SIZE, SIZE));
        squareSize = calculateSquareSize();
        initializeBoard();
        initializeStartingPositions();
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
                squares[row][col].setIcon(icon);
                squares[row][col].setHorizontalTextPosition(SwingConstants.CENTER);
                squares[row][col].setVerticalTextPosition(SwingConstants.BOTTOM);

                add(squares[row][col]);
                squares[row][col].addMouseListener(new SquareClickListener(this));
            }
        }
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
        URL resource = getClass().getResource("/Pieces/" + imageName);
        if (resource != null) {
            try {
                Image image = ImageIO.read(resource);
                if (image != null) {
                    ImageIcon icon = new ImageIcon(image);
                    icon.setDescription("Pieces/" + imageName); // Setting description here
                    return icon;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
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
                    } else {
                        clearHighlights();
                        board.selectedPiece = null;
                    }
                } else {
                    if (clickedSquare.getIcon() != null) {
                        board.selectedPiece = clickedSquare;
                        highlightMoves(clickedRow, clickedCol);
                    }
                }
            } else if (SwingUtilities.isRightMouseButton(e)) {
                displayPieceInformation(clickedSquare, clickedRow, clickedCol);
            }
        }

        private boolean isValidMove(int row, int col) {
            return row >= 0 && row < Board.SIZE && col >= 0 && col < Board.SIZE;
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
            board.boardState[clickedRow][clickedCol][0] = board.boardState[selectedRow][selectedCol][0];
            board.boardState[clickedRow][clickedCol][1] = board.boardState[selectedRow][selectedCol][1];
            board.boardState[selectedRow][selectedCol][0] = null;
            board.boardState[selectedRow][selectedCol][1] = null;

            // Move the piece on the GUI
            clickedSquare.setIcon(movedPieceIcon);
            selectedSquare.setIcon(null);  // Remove the icon from the old square
            board.selectedPiece = null;  // Deselect after move
            board.isWhiteTurn = !board.isWhiteTurn; // Toggle turn only after a successful move

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

                if (isValidMove(newRow, newCol)) {
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
    }

}
