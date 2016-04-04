import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Chess extends Game<Chess.Board> {

    class Board implements Cloneable{
        Piece[][] arr;

        Board() {
            arr = new Piece[8][8];
        }

        void init() {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (i == 1) {
                        arr[i][j] = Piece.BLACK_PAWN;
                    } else if (i == 6) {
                        arr[i][j] = Piece.WHITE_PAWN;
                    } else {
                        arr[i][j] = Piece.EMPTY;
                    }
                }
            }

            arr[0][0] = Piece.BLACK_ROOK;
            arr[0][1] = Piece.BLACK_KNIGHT;
            arr[0][2] = Piece.BLACK_BISHOP;
            arr[0][3] = Piece.BLACK_KING;
            arr[0][4] = Piece.BLACK_QUEEN;
            arr[0][5] = Piece.BLACK_BISHOP;
            arr[0][6] = Piece.BLACK_KNIGHT;
            arr[0][7] = Piece.BLACK_ROOK;

            arr[7][0] = Piece.WHITE_ROOK;
            arr[7][1] = Piece.WHITE_KNIGHT;
            arr[7][2] = Piece.WHITE_BISHOP;
            arr[7][3] = Piece.WHITE_KING;
            arr[7][4] = Piece.WHITE_QUEEN;
            arr[7][5] = Piece.WHITE_BISHOP;
            arr[7][6] = Piece.WHITE_KNIGHT;
            arr[7][7] = Piece.WHITE_ROOK;
        }

        @Override
        public Board clone() {
            Board newBoard = new Board();
            for (int x = 0; x < 8; x++) {
                newBoard.arr[x] = Arrays.copyOf(arr[x], 8);
            }
            return newBoard;
        }

        @Override
        public boolean equals(Object obj) {
            try {
                Board that = (Board) obj;
                if (this.arr == null || that.arr == null) {
                    return this.arr == that.arr;
                }
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (this.arr[i][j] != that.arr[i][j]) {
                            return false;
                        }
                    }
                }
                return true;
            } catch (ClassCastException e) {
                return false;
            }
        }
    }

    @Override
    public void initBoard() {
        currentBoard = new Board();
        currentBoard.init();
    }

    @Override
    public void printBoard() {
        System.out.println("__________________________________________________________________");
        for (int i = 0; i < 8; i++) {
            System.out.print("|");
            for (int j = 0; j < 8; j++) {
                String c = " ";
                if (currentBoard.arr[i][j] == Piece.BLACK_KING) {
                    c = "BK";
                } else if (currentBoard.arr[i][j] == Piece.BLACK_ROOK) {
                    c = "BR";
                } else if (currentBoard.arr[i][j] == Piece.WHITE_KING) {
                    c = "WK";
                } else if (currentBoard.arr[i][j] == Piece.WHITE_ROOK) {
                    c = "WR";
                } else if (currentBoard.arr[i][j] == Piece.WHITE_BISHOP) {
                    c = "WB";
                } else if (currentBoard.arr[i][j] == Piece.BLACK_BISHOP) {
                    c = "BB";
                } else if (currentBoard.arr[i][j] == Piece.BLACK_QUEEN) {
                    c = "BQ";
                } else if (currentBoard.arr[i][j] == Piece.WHITE_QUEEN) {
                    c = "WQ";
                } else if (currentBoard.arr[i][j] == Piece.WHITE_KNIGHT) {
                    c = "WKn";
                } else if (currentBoard.arr[i][j] == Piece.BLACK_KNIGHT) {
                    c = "BKn";
                } else if (currentBoard.arr[i][j] == Piece.WHITE_PAWN) {
                    c = "WP";
                } else if (currentBoard.arr[i][j] == Piece.BLACK_PAWN) {
                    c = "BP";
                }
                System.out.print("\t" + c + "\t|");
            }
            System.out.println("");
            System.out.println("__________________________________________________________________");
        }
        System.out.println();
    }

    @Override
    public boolean gameOver(Board board) {
        return doesHeWin(board, Player.MAXIMIZER) || doesHeWin(board, Player.MINIMIZER);
    }

    @Override
    public int evaluateBoard(Board board, Player player) {
        //white material - black material
        int whiteValue = 0;
        int blackValue = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board.arr[i][j];
                if (piece == Piece.WHITE_KING) {
                    whiteValue += 100000;
                } else if (piece == Piece.BLACK_KING) {
                    blackValue += 100000;
                } else if (piece == Piece.WHITE_ROOK) {
                    whiteValue += 5;
                } else if (piece == Piece.BLACK_ROOK) {
                    blackValue += 5;
                } else if (piece == Piece.WHITE_BISHOP) {
                    whiteValue += 3;
                } else if (piece == Piece.BLACK_BISHOP) {
                    blackValue += 3;
                } else if (piece == Piece.BLACK_QUEEN) {
                    blackValue += 9;
                } else if (piece == Piece.WHITE_QUEEN) {
                    whiteValue += 9;
                } else if (piece == Piece.BLACK_KNIGHT) {
                    blackValue += 3;
                } else if (piece == Piece.WHITE_KNIGHT) {
                    whiteValue += 3;
                } else if (piece == Piece.BLACK_PAWN) {
                    blackValue += 1;
                } else if (piece == Piece.WHITE_PAWN) {
                    whiteValue += 1;
                }
            }
        }
        return player == Player.MAXIMIZER ? whiteValue - blackValue : whiteValue - blackValue;
    }

    @Override
    public void playHumanMove(Player humanPlayer) {
        //ask user for input
        int startX, startY, endX, endY;
        Piece piece;
        Scanner s = new Scanner(System.in);
        while (true) {
            startX = s.nextInt();
            startY = s.nextInt();
            endX = s.nextInt();
            endY = s.nextInt();
            if (startX < 0 || startX > 7 || startY < 0 || startY > 7 || endX < 0 || endX > 7 || endY < 0 || endY > 7) {
                System.out.println("Out of bounds, try again.");
                continue;
            }
            piece = currentBoard.arr[startX][startY];
            if (piece == Piece.EMPTY || !currentBoard.arr[endX][endY].canBeReplacedBy(piece)) {
                System.out.println("No piece available, try again.");
                continue;
            }
            if (piece.isBlack()) {
                System.out.println("Can't move black, try again.");
                continue;
            }
            //TODO make it valid for each piece
            break;
        }
        currentBoard.arr[startX][startY] = Piece.EMPTY;
        currentBoard.arr[endX][endY] = piece;
    }

    @Override
    public boolean doesHeWin(Board board, Player player) {
        boolean hasBlackKing = false;
        boolean hasWhiteKing = false;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.arr[i][j] == Piece.WHITE_KING) {
                    hasWhiteKing = true;
                } else if (board.arr[i][j] == Piece.BLACK_KING) {
                    hasBlackKing = true;
                }
            }
        }

        if (player == Player.MAXIMIZER) {
            return !hasBlackKing;
        } else {
            return !hasWhiteKing;
        }
    }

    @Override
    public List<Board> getChildren(Board board, Player player) {
        List<Board> children = new ArrayList<>(9);

        Piece kingPiece;
        Piece rookPiece;
        Piece bishopPiece;
        Piece queenPiece;
        Piece knightPiece;
        Piece pawnPiece;
        if (player == Player.MAXIMIZER) {
            kingPiece = Piece.WHITE_KING;
            rookPiece = Piece.WHITE_ROOK;
            bishopPiece = Piece.WHITE_BISHOP;
            queenPiece = Piece.WHITE_QUEEN;
            knightPiece = Piece.WHITE_KNIGHT;
            pawnPiece = Piece.WHITE_PAWN;
        } else {
            kingPiece = Piece.BLACK_KING;
            rookPiece = Piece.BLACK_ROOK;
            bishopPiece = Piece.BLACK_BISHOP;
            queenPiece = Piece.BLACK_QUEEN;
            knightPiece = Piece.BLACK_KNIGHT;
            pawnPiece = Piece.BLACK_PAWN;
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.arr[i][j] == kingPiece) {
                    moveKingAround(kingPiece, i, j, board, children);
                } else if (board.arr[i][j] == rookPiece) {
                    moveRookAround(rookPiece, i, j, board, children);
                } else if (board.arr[i][j] == bishopPiece) {
                    moveBishopAround(bishopPiece, i, j, board, children);
                } else if (board.arr[i][j] == queenPiece) {
                    moveQueenAround(queenPiece, i, j, board, children);
                } else if (board.arr[i][j] == knightPiece) {
                    moveKnightAround(knightPiece, i, j, board, children);
                } else if (board.arr[i][j] == pawnPiece) {
                    movePawnAround(pawnPiece, i, j, board, children);
                }
            }
        }
        return children;
    }

    private void movePawnAround(Piece pawnPiece, int i, int j, Board board, List<Board> children) {
        if (pawnPiece.isBlack() && i < 7 && board.arr[i+1][j] == Piece.EMPTY) {
            Board newBoard = board.clone();
            newBoard.arr[i][j] = Piece.EMPTY;
            newBoard.arr[i+1][j] = pawnPiece;
            children.add(newBoard);
        } else if (pawnPiece.isWhite() && i > 0 && board.arr[i-1][j] == Piece.EMPTY) {
            Board newBoard = board.clone();
            newBoard.arr[i][j] = Piece.EMPTY;
            newBoard.arr[i-1][j] = pawnPiece;
            children.add(newBoard);
        }
    }

    private void moveQueenAround(Piece queenPiece, int i, int j, Board board, List<Board> children) {
        moveRookAround(queenPiece, i, j, board, children);
        moveBishopAround(queenPiece, i, j, board, children);
    }

    private void moveBishopAround(Piece bishopPiece, int i, int j, Board board, List<Board> children) {
        for (int newI = i, newJ = j; newI>0 && newJ>0;) {
            if (board.arr[newI-1][newJ-1].canBeReplacedBy(bishopPiece)) {
                Board newBoard = board.clone();
                newBoard.arr[i][j] = Piece.EMPTY;
                newBoard.arr[newI-1][newJ-1] = bishopPiece;
//                if (!equals(newBoard, previousComputerBoard3)) {
                children.add(newBoard);
//                }
            }
            if (board.arr[newI-1][newJ-1] != Piece.EMPTY) {
                //attack here, and then stop trying
                break;
            }
            newI--;newJ--;
        }
        for (int newI = i, newJ = j; newI<7 && newJ<7;) {
            if (board.arr[newI+1][newJ+1].canBeReplacedBy(bishopPiece)) {
                Board newBoard = board.clone();
                newBoard.arr[i][j] = Piece.EMPTY;
                newBoard.arr[newI+1][newJ+1] = bishopPiece;
//                if (!equals(newBoard, previousComputerBoard3)) {
                children.add(newBoard);
//                }
            }
            if (board.arr[newI+1][newJ+1] != Piece.EMPTY) {
                //attack here, and then stop trying
                break;
            }
            newI++;newJ++;
        }
        for (int newI = i, newJ = j; newI>0 && newJ<7;) {
            if (board.arr[newI-1][newJ+1].canBeReplacedBy(bishopPiece)) {
                Board newBoard = board.clone();
                newBoard.arr[i][j] = Piece.EMPTY;
                newBoard.arr[newI-1][newJ+1] = bishopPiece;
//                if (!equals(newBoard, previousComputerBoard3)) {
                children.add(newBoard);
//                }
            }
            if (board.arr[newI-1][newJ+1] != Piece.EMPTY) {
                //attack here, and then stop trying
                break;
            }
            newI--;newJ++;
        }
        for (int newI = i, newJ = j; newI<7 && newJ>0;) {
            if (board.arr[newI+1][newJ-1].canBeReplacedBy(bishopPiece)) {
                Board newBoard = board.clone();
                newBoard.arr[i][j] = Piece.EMPTY;
                newBoard.arr[newI+1][newJ-1] = bishopPiece;
//                if (!equals(newBoard, previousComputerBoard3)) {
                children.add(newBoard);
//                }
            }
            if (board.arr[newI+1][newJ-1] != Piece.EMPTY) {
                //attack here, and then stop trying
                break;
            }
            newI++;newJ--;
        }
    }

    private void moveRookAround(Piece rookPiece, int i, int j, Board board, List<Board> children) {
        //move it left (i--)
        for (int newI = i; newI>0; newI--) {
            if (board.arr[newI-1][j].canBeReplacedBy(rookPiece)) {
                Board newBoard = board.clone();
                newBoard.arr[i][j] = Piece.EMPTY;
                newBoard.arr[newI-1][j] = rookPiece;
//                if (!equals(newBoard, previousComputerBoard3)) {
                    children.add(newBoard);
//                }
            }
            if (board.arr[newI-1][j] != Piece.EMPTY) {
                //attack here, and then stop trying
                break;
            }
        }
        for (int newI = i; newI<7; newI++) {
            if (board.arr[newI+1][j].canBeReplacedBy(rookPiece)) {
                Board newBoard = board.clone();
                newBoard.arr[i][j] = Piece.EMPTY;
                newBoard.arr[newI+1][j] = rookPiece;
                children.add(newBoard);
            }
            if (board.arr[newI+1][j] != Piece.EMPTY) {
                //attack here, and then stop trying
                break;
            }
        }
        for (int newJ = j; newJ>0; newJ--) {
            if (board.arr[i][newJ-1].canBeReplacedBy(rookPiece)) {
                Board newBoard = board.clone();
                newBoard.arr[i][j] = Piece.EMPTY;
                newBoard.arr[i][newJ-1] = rookPiece;
//                if (!equals(newBoard, previousComputerBoard3)) {
                    children.add(newBoard);
//                }
            }
            if (board.arr[i][newJ-1] != Piece.EMPTY) {
                //attack here, and then stop trying
                break;
            }
        }
        for (int newJ = j; newJ<7; newJ++) {
            if (board.arr[i][newJ+1].canBeReplacedBy(rookPiece)) {
                Board newBoard = board.clone();
                newBoard.arr[i][j] = Piece.EMPTY;
                newBoard.arr[i][newJ+1] = rookPiece;
//                if (!equals(newBoard, previousComputerBoard3)) {
                    children.add(newBoard);
//                }
            }
            if (board.arr[i][newJ+1] != Piece.EMPTY) {
                //attack here, and then stop trying
                break;
            }
        }
    }

    private void moveKingAround(Piece kingPiece, int i, int j, Board board, List<Board> children) {
        movePieceAround(kingPiece, i, j, i +1, j, board, children);
        movePieceAround(kingPiece, i, j, i -1, j, board, children);
        movePieceAround(kingPiece, i, j, i, j +1, board, children);
        movePieceAround(kingPiece, i, j, i, j -1, board, children);
        movePieceAround(kingPiece, i, j, i -1, j -1, board, children);
        movePieceAround(kingPiece, i, j, i -1, j +1, board, children);
        movePieceAround(kingPiece, i, j, i +1, j -1, board, children);
        movePieceAround(kingPiece, i, j, i +1, j +1, board, children);
    }

    private void moveKnightAround(Piece knightPiece, int i, int j, Board board, List<Board> children) {
        movePieceAround(knightPiece, i, j, i+2, j+1, board, children);
        movePieceAround(knightPiece, i, j, i+2, j-1, board, children);
        movePieceAround(knightPiece, i, j, i-2, j+1, board, children);
        movePieceAround(knightPiece, i, j, i-2, j-1, board, children);
        movePieceAround(knightPiece, i, j, i-1, j+2, board, children);
        movePieceAround(knightPiece, i, j, i+1, j+2, board, children);
        movePieceAround(knightPiece, i, j, i-1, j-2, board, children);
        movePieceAround(knightPiece, i, j, i+1, j-2, board, children);
    }

    private void movePieceAround(Piece piece, int oldX, int oldY, int newX, int newY, Board board, List<Board> children) {
        if (newX < 7 && newX >= 0 && newY < 7 && newY >= 0) {
            if (board.arr[newX][newY] == Piece.EMPTY || board.arr[newX][newY].canBeReplacedBy(piece)) {
                Board newBoard = board.clone();
                newBoard.arr[oldX][oldY] = Piece.EMPTY;
                newBoard.arr[newX][newY] = piece;
                children.add(newBoard);
            }
        }
    }

    enum Piece {
        WHITE_ROOK, WHITE_BISHOP, WHITE_KING, WHITE_QUEEN, WHITE_KNIGHT, WHITE_PAWN, BLACK_ROOK, BLACK_BISHOP, BLACK_KING, BLACK_QUEEN, BLACK_KNIGHT, BLACK_PAWN, EMPTY;

        public boolean isBlack() {
            return this == BLACK_KING || this == BLACK_ROOK || this == BLACK_BISHOP || this == BLACK_QUEEN || this == BLACK_KNIGHT || this == BLACK_PAWN;
        }

        public boolean isWhite() {
            return this == WHITE_KING || this == WHITE_ROOK || this == WHITE_BISHOP || this == WHITE_QUEEN || this == WHITE_KNIGHT || this == WHITE_PAWN;
        }

        public boolean canBeReplacedBy(Piece replacer) {
            return this == EMPTY || this.isWhite() && replacer.isBlack() || this.isBlack() && replacer.isWhite();
        }
    }
}