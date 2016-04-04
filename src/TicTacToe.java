import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TicTacToe extends Game<TicTacToe.Piece[][]> {

    enum Piece {
        O, X, EMPTY
    }

    @Override
    public void initBoard() {
        currentBoard = new Piece[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                currentBoard[i][j] = Piece.EMPTY;
            }
        }
    }

    @Override
    public void printBoard() {
        System.out.println("__________________________");
        for (int i = 0; i < 3; i++) {
            System.out.print("|");
            for (int j = 0; j < 3; j++) {
                char c = ' ';
                if (currentBoard[i][j] == Piece.X) {
                    c = 'X';
                } else if (currentBoard[i][j] == Piece.O) {
                    c = 'O';
                }
                System.out.print("\t" + c + "\t|");
            }
            System.out.println("");
            System.out.println("_________________________");
        }
        System.out.println();
    }

    @Override
    public int evaluateBoard(Piece[][] board) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == Piece.X && board[i][1] == Piece.X && board[i][2] == Piece.X) {
                return Integer.MAX_VALUE;
            }
            if (board[i][0] == Piece.O && board[i][1] == Piece.O && board[i][2] == Piece.O) {
                return Integer.MIN_VALUE;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (board[0][i] == Piece.X && board[1][i] == Piece.X && board[2][i] == Piece.X) {
                return Integer.MAX_VALUE;
            }
            if (board[0][i] == Piece.O && board[1][i] == Piece.O && board[2][i] == Piece.O) {
                return Integer.MIN_VALUE;
            }
        }

        if (board[0][0] == Piece.X && board[1][1] == Piece.X && board[2][2] == Piece.X) {
            return Integer.MAX_VALUE;
        }
        if (board[0][0] == Piece.O && board[1][1] == Piece.O && board[2][2] == Piece.O) {
            return Integer.MIN_VALUE;
        }

        if (board[0][2] == Piece.X && board[1][1] == Piece.X && board[2][0] == Piece.X) {
            return Integer.MAX_VALUE;
        }

        if (board[0][2] == Piece.O && board[1][1] == Piece.O && board[2][0] == Piece.O) {
            return Integer.MIN_VALUE;
        }

        return 0;
    }

    @Override
    public boolean gameOver(Piece[][] board) {
        if (doesHeWin(board, Player.MAXIMIZER) || doesHeWin(board, Player.MINIMIZER)) {
            return true;
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == Piece.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean doesHeWin(Piece[][] board, Player player) {
        Piece piece = player == Player.MAXIMIZER ? Piece.X : Piece.O;
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == piece && board[i][1] == piece && board[i][2] == piece) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (board[0][i] == piece && board[1][i] == piece && board[2][i] == piece) {
                return true;
            }
        }

        if (board[0][0] == piece && board[1][1] == piece && board[2][2] == piece) {
            return true;
        }

        if (board[0][2] == piece && board[1][1] == piece && board[2][0] == piece) {
            return true;
        }

        return false;
    }

    @Override
    public void playHumanMove(Player humanPlayer) {
        //ask user for input
        int i = -1, j = -1;
        Scanner s = new Scanner(System.in);
        boolean invalid = true;
        while (invalid) {
            i = s.nextInt();
            j = s.nextInt();
            if (i < 0 || i > 2 || j < 0 || j > 2 || currentBoard[i][j] != Piece.EMPTY) {
                System.out.println("Invalid move, try again.");
            } else {
                invalid = false;
            }
        }
        currentBoard[i][j] = humanPlayer == Player.MAXIMIZER ? Piece.X : Piece.O;
    }

    @Override
    public List<Piece[][]> getChildren(Piece[][] board, Player player) {
        List<Piece[][]> children = new ArrayList<>(9);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == Piece.EMPTY) {
                    //make a new board and place x here, and insert as child
                    Piece[][] newBoard = new Piece[3][3];
                    for (int x = 0; x < 3; x++) {
                        newBoard[x] = Arrays.copyOf(board[x], 3);
                    }
                    newBoard[i][j] = player == Player.MAXIMIZER ? Piece.X : Piece.O;
                    children.add(newBoard);
                }
            }
        }
        return children;
    }
}
