//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Scanner;
//
//public class RooksMain {
//
//    enum Piece {
//        WHITE_ROOK, WHITE_KING, BLACK_ROOK, BLACK_KING, EMPTY
//    }
//
//    enum Player {
//        WHITE, BLACK
//    }
//
//    public static boolean isWhite(Piece piece) {
//        return piece == Piece.WHITE_KING || piece == Piece.WHITE_ROOK;
//    }
//
//    public static Player opposite(Player player) {
//        return player == Player.WHITE ? Player.BLACK : Player.WHITE;
//    }
//
//    public static boolean isBlack(Piece piece) {
//        return piece == Piece.BLACK_KING || piece == Piece.BLACK_ROOK;
//    }
//
////    public static void main(String[] args) {
////        Piece[][] board = new Piece[8][8];
////        for (int i = 0; i < 8; i++) {
////            for (int j = 0; j < 8; j++) {
////                board[i][j] = Piece.EMPTY;
////            }
////        }
////
////        board[0][0] = Piece.BLACK_KING;
////        board[7][7] = Piece.WHITE_KING;
////
////        board[1][0] = Piece.BLACK_ROOK;
////        board[0][1] = Piece.BLACK_ROOK;
////
////        board[7][6] = Piece.WHITE_ROOK;
////        board[6][7] = Piece.WHITE_ROOK;
////
////        Player player = Player.WHITE;
////        printBoard(board);
////        while (!gameOver(board)) {
////            if (player == Player.WHITE) {
////                board = playHumanMove(board);
////            } else {
////                board = playComputerMove(board);
////                printBoard(board);
////            }
////            player = player == Player.WHITE ? Player.BLACK : Player.WHITE;
////        }
////        if (doesHeWin(board, Player.WHITE)) {
////            System.out.println("YOU WIN!");
////        } else if (doesHeWin(board, Player.BLACK)) {
////            System.out.println("YOU LOSE!");
////        } else {
////            System.out.println("YOU DRAW!");
////        }
////    }
//
//    private static Piece[][] playComputerMove(Piece[][] board) {
//        Node root = new Node(board);
//        generateChildrenForNode(root, Player.BLACK, 0);
//        evaluateNodes(root, Player.BLACK);
//        Node child = minChild(root.children);
//        return child.board;
//    }
//
////    private static void printBoard(Piece[][] board) {
////        System.out.println("__________________________________________________________________");
////        for (int i = 0; i < 8; i++) {
////            System.out.print("|");
////            for (int j = 0; j < 8; j++) {
////                String c = " ";
////                if (board[i][j] == Piece.BLACK_KING) {
////                    c = "BK";
////                } else if (board[i][j] == Piece.BLACK_ROOK) {
////                    c = "BR";
////                } else if (board[i][j] == Piece.WHITE_KING) {
////                    c = "WK";
////                } else if (board[i][j] == Piece.WHITE_ROOK) {
////                    c = "WR";
////                }
////                System.out.print("\t" + c + "\t|");
////            }
////            System.out.println("");
////            System.out.println("__________________________________________________________________");
////        }
////        System.out.println();
////    }
//
//    private static void evaluateNodes(Node root, Player player) {
//        if (root.children.isEmpty()) {
//            boolean whiteWins = doesHeWin(root.board, Player.WHITE);
//            boolean blackWins = doesHeWin(root.board, Player.BLACK);
//            boolean draw = !whiteWins && !blackWins;
//            if (draw) {
//                root.value = evaluateBoard(root.board);
//            } else {
//                root.value = whiteWins ? 10000 : -10000;
//            }
//        } else {
//            for (Node child : root.children) {
//                evaluateNodes(child, opposite(player));
//            }
//            if (player == Player.WHITE) {
//                root.value = maxValue(root.children);
//            } else {
//                root.value = minValue(root.children);
//            }
//        }
//    }
//
//    private static int minValue(ArrayList<Node> children) {
//        int min = children.get(0).value;
//        for (int i = 1; i < children.size(); i++) {
//            min = Math.min(min, children.get(i).value);
//        }
//        return min;
//    }
//
//    private static Node minChild(ArrayList<Node> children) {
//        Node min = children.get(0);
//        for (int i = 1; i < children.size(); i++) {
//            if (children.get(i).value < min.value) {
//                min = children.get(i);
//            }
//        }
//        return min;
//    }
//
//    private static Node maxChild(ArrayList<Node> children) {
//        Node max = children.get(0);
//        for (int i = 1; i < children.size(); i++) {
//            if (children.get(i).value > max.value) {
//                max = children.get(i);
//            }
//        }
//        return max;
//    }
//
//    private static int maxValue(ArrayList<Node> children) {
//        int max = children.get(0).value;
//        for (int i = 1; i < children.size(); i++) {
//            max = Math.max(max, children.get(i).value);
//        }
//        return max;
//    }
//
//    private static void generateChildrenForNode(Node root, Player player, int level) {
//        root.children = new ArrayList<>();
//        if (level == 3 || doesHeWin(root.board, Player.WHITE) || doesHeWin(root.board, Player.BLACK)) {
//            return;
//        }
//        // come up with all moves for player
//        Piece kingPiece;
//        if (player == Player.WHITE) {
//            kingPiece = Piece.WHITE_KING;
//        } else {
//            kingPiece = Piece.BLACK_KING;
//        }
//        // come up with all movements of the king
//        int kingX = -1, kingY = -1;
//        outerloop:
//        for (int i = 0; i < 8; i++) {
//            for (int j = 0; j < 8; j++) {
//                if (root.board[i][j] == kingPiece) {
//                    kingX = i;
//                    kingY = j;
//                    break outerloop;
//                }
//            }
//        }
//        if (kingX < 7 && root.board[kingX+1][kingY] == Piece.EMPTY) {
//            Piece[][] newBoard = copyBoard(root.board);
//            newBoard[kingX][kingY] = Piece.EMPTY;
//            newBoard[kingX+1][kingY] = kingPiece;
//            Node newChild = new Node(newBoard);
//            generateChildrenForNode(newChild, opposite(player), level+1);
//            root.children.add(newChild);
//        }
//        if (kingX > 0 && root.board[kingX-1][kingY] == Piece.EMPTY) {
//            Piece[][] newBoard = copyBoard(root.board);
//            newBoard[kingX][kingY] = Piece.EMPTY;
//            newBoard[kingX-1][kingY] = kingPiece;
//            Node newChild = new Node(newBoard);
//            generateChildrenForNode(newChild, opposite(player), level+1);
//            root.children.add(newChild);
//        }
//        if (kingY < 7 && root.board[kingX][kingY+1] == Piece.EMPTY) {
//            Piece[][] newBoard = copyBoard(root.board);
//            newBoard[kingX][kingY] = Piece.EMPTY;
//            newBoard[kingX][kingY+1] = kingPiece;
//            Node newChild = new Node(newBoard);
//            generateChildrenForNode(newChild, opposite(player), level+1);
//            root.children.add(newChild);
//        }
//        if (kingY > 0 && root.board[kingX][kingY-1] == Piece.EMPTY) {
//            Piece[][] newBoard = copyBoard(root.board);
//            newBoard[kingX][kingY] = Piece.EMPTY;
//            newBoard[kingX][kingY-1] = kingPiece;
//            Node newChild = new Node(newBoard);
//            generateChildrenForNode(newChild, opposite(player), level+1);
//            root.children.add(newChild);
//        }
//    }
//
//    static Piece[][] copyBoard(Piece[][] board) {
//        Piece[][] newBoard = new Piece[8][8];
//        for (int x = 0; x < 8; x++) {
//            newBoard[x] = Arrays.copyOf(board[x], 8);
//        }
//        return newBoard;
//    }
//
//    static class Node {
//        Piece[][] board;
//        int value;
//        ArrayList<Node> children;
//
//        Node(Piece[][] board) {
//            this.board = board;
//            this.value = 0;
//            this.children = null;
//        }
//    }
//}
