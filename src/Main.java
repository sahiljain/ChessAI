import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    enum Player {
        MAXIMIZER, MINIMIZER;

        public Player opposite() {
            return this == MAXIMIZER ? MINIMIZER : MAXIMIZER;
        }
    }

    enum Piece {
        O, X, EMPTY
    }

    public static void main(String[] args) {
        Piece[][] board = new Piece[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = Piece.EMPTY;
            }
        }
        Player humanPlayer = Player.MAXIMIZER;
        Player player = humanPlayer;
        printBoard(board);
        while (!gameOver(board)) {
            if (player == Player.MAXIMIZER) {
                board = playHumanMove(board);
            } else {
                long startTime = System.currentTimeMillis();
                board = playComputerMove(board);
                long elapsed = System.currentTimeMillis() - startTime;
                System.out.println("Move took " + elapsed + " ms");
                printBoard(board);
            }
            player = player.opposite();
        }
        if (doesHeWin(board, humanPlayer)) {
            System.out.println("YOU WIN!");
        } else if (doesHeWin(board, humanPlayer.opposite())) {
            System.out.println("YOU LOSE!");
        } else {
            System.out.println("YOU DRAW!");
        }
    }

    private static boolean gameOver(Piece[][] board) {
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

    private static Piece[][] playComputerMove(Piece[][] board) {
        Node root = new Node(board);
        evaluateNodes(root, false);
        Node child = minChild(root.children);
        return child.board;
    }

    private static Piece[][] playHumanMove(Piece[][] board) {
        //ask user for input
        int i = -1, j = -1;
        Scanner s = new Scanner(System.in);
        boolean invalid = true;
        while (invalid) {
            i = s.nextInt();
            j = s.nextInt();
            if (i < 0 || i > 2 || j < 0 || j > 2 || board[i][j] != Piece.EMPTY) {
                System.out.println("Invalid move, try again.");
            } else {
                invalid = false;
            }
        }
        board[i][j] = Piece.X;
        return board;
    }

    //-1 is o wins, 0 is draw, x is 1 wins

    private static void printBoard(Piece[][] board) {
        System.out.println("__________________________");
        for (int i = 0; i < 3; i++) {
            System.out.print("|");
            for (int j = 0; j < 3; j++) {
                char c = ' ';
                if (board[i][j] == Piece.X) {
                    c = 'X';
                } else if (board[i][j] == Piece.O) {
                    c = 'O';
                }
                System.out.print("\t" + c + "\t|");
            }
            System.out.println("");
            System.out.println("_________________________");
        }
        System.out.println();
    }

    private static void evaluateNodes(Node root, boolean xTurn) {
        if (gameOver(root.board)) {
            boolean xWins = doesHeWin(root.board, Player.MAXIMIZER);
            boolean oWins = doesHeWin(root.board, Player.MINIMIZER);
            boolean draw = !xWins && !oWins;
            if (draw) {
                root.value = 0;
            } else {
                root.value = xWins ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            }
        } else {
            root.value = xTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            root.children = new ArrayList<>();
            outerloop:
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (root.board[i][j] == Piece.EMPTY) {
                        //make a new board and place x here, and insert as child
                        Piece[][] newBoard = new Piece[3][3];
                        for (int x = 0; x < 3; x++) {
                            newBoard[x] = Arrays.copyOf(root.board[x], 3);
                        }
                        newBoard[i][j] = xTurn ? Piece.X : Piece.O;
                        Node newChild = new Node(newBoard);
                        newChild.alpha = root.alpha;
                        newChild.beta = root.beta;
                        evaluateNodes(newChild, !xTurn);
                        root.children.add(newChild);
                        if (xTurn) {
                            root.value = Math.max(root.value, newChild.value);
                            root.alpha = Math.max(root.alpha, newChild.value);
                        } else {
                            root.value = Math.min(root.value, newChild.value);
                            root.beta = Math.min(root.beta, newChild.value);
                        }
                        if (root.beta <= root.alpha) {
                            break outerloop;
                        }
                    }
                }
            }
        }
    }

    private static int minValue(ArrayList<Node> children) {
        int min = children.get(0).value;
        for (int i = 1; i < children.size(); i++) {
            min = Math.min(min, children.get(i).value);
        }
        return min;
    }

    private static Node minChild(ArrayList<Node> children) {
        Node min = children.get(0);
        for (int i = 1; i < children.size(); i++) {
            if (children.get(i).value < min.value) {
                min = children.get(i);
            }
        }
        return min;
    }

    private static Node maxChild(ArrayList<Node> children) {
        Node max = children.get(0);
        for (int i = 1; i < children.size(); i++) {
            if (children.get(i).value > max.value) {
                max = children.get(i);
            }
        }
        return max;
    }

    private static int maxValue(ArrayList<Node> children) {
        int max = children.get(0).value;
        for (int i = 1; i < children.size(); i++) {
            max = Math.max(max, children.get(i).value);
        }
        return max;
    }

    static boolean doesHeWin(Piece[][] board, Player player) {
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

    static class Node {
        Piece[][] board;
        int value;
        int alpha;
        int beta;
        ArrayList<Node> children;

        Node(Piece[][] board) {
            this.board = board;
            this.value = 0;
            this.children = null;
            alpha = Integer.MIN_VALUE;
            beta = Integer.MAX_VALUE;
        }
    }
}
