import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    enum Piece {
        X, O, EMPTY
    }

    public static void main(String[] args) {
        Piece[][] board = new Piece[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = Piece.EMPTY;
            }
        }

        boolean xTurn = true;
        printBoard(board);
        while (!gameOver(board)) {
            if (xTurn) {
                board = playHumanMove(board);
            } else {
                long startTime = System.currentTimeMillis();
                board = playComputerMove(board);
                long elapsed = System.currentTimeMillis() - startTime;
                System.out.println("Move took " + elapsed + " ms");
                printBoard(board);
            }
            xTurn = !xTurn;
        }
        if (doesHeWin(board, Piece.X)) {
            System.out.println("YOU WIN!");
        } else if (doesHeWin(board, Piece.O)) {
            System.out.println("YOU LOSE!");
        } else {
            System.out.println("YOU DRAW!");
        }
    }

    private static boolean gameOver(Piece[][] board) {
        if (doesHeWin(board, Piece.X) || doesHeWin(board, Piece.O)) {
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
        generateChildrenForNode(root, false);
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
        if (root.children.isEmpty()) {
            boolean xWins = doesHeWin(root.board, Piece.X);
            boolean oWins = doesHeWin(root.board, Piece.O);
            boolean draw = !xWins && !oWins;
            if (draw) {
                root.value = 0;
            } else {
                root.value = xWins ? 1 : -1;
            }
        } else {
            for (Node child : root.children) {
                evaluateNodes(child, !xTurn);
            }
            if (xTurn) {
                root.value = maxValue(root.children);
            } else {
                root.value = minValue(root.children);
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

    private static int maxValue(ArrayList<Node> children) {
        int max = children.get(0).value;
        for (int i = 1; i < children.size(); i++) {
            max = Math.max(max, children.get(i).value);
        }
        return max;
    }

    private static void generateChildrenForNode(Node root, boolean xTurn) {
        ArrayList<Node> children = new ArrayList<>();
        root.children = children;
        if (doesHeWin(root.board, Piece.X) || doesHeWin(root.board, Piece.O)) {
            return;
        }
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
                    generateChildrenForNode(newChild, !xTurn);
                    children.add(newChild);
                }
            }
        }
    }

    static boolean doesHeWin(Piece[][] board, Piece player) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                return true;
            }
        }

        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }

        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true;
        }

        return false;
    }

    static class Node {
        Piece[][] board;
        int value;
        ArrayList<Node> children;

        Node(Piece[][] board) {
            this.board = board;
            this.value = 0;
            this.children = null;
        }
    }
}
