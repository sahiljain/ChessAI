import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        int[][] board = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = -1;
            }
        }
        Node root = new Node(board);
        generateChildrenForNode(root, true);
        evaluateNodes(root, true);
        long endTime = System.currentTimeMillis();
        System.out.println("Board generation took " + (endTime - startTime) + " ms");
        playMove(root, true);
    }

    //-1 is no one, 1 is x, 0 is o
    //-1 is o wins, 0 is draw, x is 1 wins

    private static void playMove(Node root, boolean xTurn) {
        printBoard(root.board);
        if (root.children.isEmpty()) {
            return;
        }
        if (xTurn) {
            //ask user for input
            int i, j;
            Scanner s = new Scanner(System.in);
            i = s.nextInt();
            j = s.nextInt();
            for (Node child : root.children) {
                if (child.board[i][j] == 1) {
                    playMove(child, false);
                    return;
                }
            }
        } else {
            //pick the child with the smallest value
            int goal = minValue(root.children);
            for (Node child : root.children) {
                if (goal == child.value) {
                    playMove(child, true);
                    return;
                }
            }
        }
    }

    private static void printBoard(int[][] board) {
        System.out.println("__________________________");
        for (int i = 0; i < 3; i++) {
            System.out.print("|");
            for (int j = 0; j < 3; j++) {
                System.out.print("\t" + board[i][j] + "\t");
            }
            System.out.println("|");
            System.out.println("_________________________");
        }
        System.out.println();
    }

    private static void evaluateNodes(Node root, boolean xTurn) {
        if (root.children.isEmpty()) {
            boolean xWins = doesHeWin(root.board, 1);
            boolean oWins = doesHeWin(root.board, 0);
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
        if (doesHeWin(root.board, 1) || doesHeWin(root.board, 0)) {
            return;
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (root.board[i][j] == -1) {
                    //make a new board and place x here, and insert as child
                    int[][] newBoard = new int[3][3];
                    for (int x = 0; x < 3; x++) {
                        newBoard[x] = Arrays.copyOf(root.board[x], 3);
                    }
                    newBoard[i][j] = xTurn ? 1 : 0;
                    Node newChild = new Node(newBoard);
                    generateChildrenForNode(newChild, !xTurn);
                    children.add(newChild);
                }
            }
        }
    }

    static boolean doesHeWin(int[][] board, int player) {
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
        int[][] board;
        int value;
        ArrayList<Node> children;

        Node(int[][] board) {
            this.board = board;
            this.value = 0;
            this.children = null;
        }
    }
}
