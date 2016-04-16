import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ConnectFour extends Game<ConnectFour.Board> {

    ConnectFour() {
        super(5);
    }

    @Override
    public void initBoard() {
        currentBoard = new Board();
        currentBoard.init();
    }

    @Override
    public void printBoard() {
        System.out.println("____________________________________________________________");
        for (int i = 0; i < 6; i++) {
            System.out.print("|");
            for (int j = 0; j < 7; j++) {
                char c = ' ';
                if (currentBoard.arr[i][j] == Piece.X) {
                    c = 'X';
                } else if (currentBoard.arr[i][j] == Piece.O) {
                    c = 'O';
                }
                System.out.print("\t" + c + "\t|");
            }
            System.out.println("");
            System.out.println("____________________________________________________________");
        }
        System.out.print("|");
        for (int i = 0; i < 7; i++) {
            System.out.print("\t" + i + "\t|");
        }
        System.out.println();
    }

    @Override
    public boolean gameOver(Board board) {
        boolean full = true;
        for (int j = 0; j < 7; j++) {
            if (board.arr[0][j] == Piece.EMPTY) {
                full = false;
                break;
            }
        }
        return full || doesHeWin(board, Player.MAXIMIZER) || doesHeWin(board, Player.MINIMIZER);
    }

    @Override
    public int evaluateBoard(Board board, Player player) {
        if (gameOver(board)) {
            if (doesHeWin(board, Player.MAXIMIZER)) {
                return Integer.MAX_VALUE;
            }
            if (doesHeWin(board, Player.MINIMIZER)) {
                return Integer.MIN_VALUE;
            }
            return 0;
        } else {
            int score = 0;
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 4; j++) {
                    int count = 0;
                    for (int x = 0; x < 4; x++) {
                        if (board.arr[i][j+x] == Piece.O) {
                            count = -1;
                            break;
                        }
                        if (board.arr[i][j+x] == Piece.X) {
                            count++;
                        }
                    }
                    if (count > 2) {
                        score++;
                    }
                }
            }
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 4; j++) {
                    int count = 0;
                    for (int x = 0; x < 4; x++) {
                        if (board.arr[i][j+x] == Piece.X) {
                            count = -1;
                            break;
                        }
                        if (board.arr[i][j+x] == Piece.O) {
                            count++;
                        }
                    }
                    if (count > 2) {
                        score--;
                    }
                }
            }

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 7; j++) {
                    int count = 0;
                    for (int x = 0; x < 4; x++) {
                        if (board.arr[i+x][j] == Piece.O) {
                            count = -1;
                            break;
                        }
                        if (board.arr[i+x][j] == Piece.X) {
                            count++;
                        }
                    }
                    if (count > 2) {
                        score++;
                    }
                }
            }
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 7; j++) {
                    int count = 0;
                    for (int x = 0; x < 4; x++) {
                        if (board.arr[i+x][j] == Piece.X) {
                            count = -1;
                            break;
                        }
                        if (board.arr[i+x][j] == Piece.O) {
                            count++;
                        }
                    }
                    if (count > 2) {
                        score--;
                    }
                }
            }

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 4; j++) {
                    int count = 0;
                    for (int x = 0; x < 4; x++) {
                        if (board.arr[i+x][j+x] == Piece.O) {
                            count = -1;
                            break;
                        }
                        if (board.arr[i+x][j+x] == Piece.X) {
                            count++;
                        }
                    }
                    if (count > 2) {
                        score++;
                    }
                }
            }
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 4; j++) {
                    int count = 0;
                    for (int x = 0; x < 4; x++) {
                        if (board.arr[i+x][j+x] == Piece.X) {
                            count = -1;
                            break;
                        }
                        if (board.arr[i+x][j+x] == Piece.O) {
                            count++;
                        }
                    }
                    if (count > 2) {
                        score--;
                    }
                }
            }

            for (int i = 3; i < 6; i++) {
                for (int j = 0; j < 4; j++) {
                    int count = 0;
                    for (int x = 0; x < 4; x++) {
                        if (board.arr[i-x][j+x] == Piece.O) {
                            count = -1;
                            break;
                        }
                        if (board.arr[i-x][j+x] == Piece.X) {
                            count++;
                        }
                    }
                    if (count > 2) {
                        score++;
                    }
                }
            }
            for (int i = 3; i < 6; i++) {
                for (int j = 0; j < 4; j++) {
                    int count = 0;
                    for (int x = 0; x < 4; x++) {
                        if (board.arr[i-x][j+x] == Piece.X) {
                            count = -1;
                            break;
                        }
                        if (board.arr[i-x][j+x] == Piece.O) {
                            count++;
                        }
                    }
                    if (count > 2) {
                        score--;
                    }
                }
            }
            return score;
        }
    }

    @Override
    public void playHumanMove(Player humanPlayer) {
        //ask user for input
//        int startX, startY;
        int column;
        int row;
        Piece piece;
        piece = humanPlayer == Player.MAXIMIZER ? Piece.X : Piece.O;
        System.out.println("board value: " + evaluateBoard(currentBoard, humanPlayer));
        Scanner s = new Scanner(System.in);
        while (true) {
//            startX = s.nextInt();
//            startY = s.nextInt();
            column = s.nextInt();
            if (column < 0 || column > 6) {
                System.out.println("Out of bounds, try again.");
                continue;
            }
            Board newBoard = currentBoard.clone();
            row = 0;
            if (currentBoard.arr[row][column] != Piece.EMPTY) {
                System.out.println("Column is filled, try again");
                continue;
            }
            while (row+1 < 6 && currentBoard.arr[row+1][column] == Piece.EMPTY) {
                row++;
            }
            newBoard.arr[row][column] = piece;
            List<Board> children = getChildren(currentBoard, humanPlayer);
            if (!children.contains(newBoard)) {
                System.out.println("illegal move");
                continue;
            }
            break;
        }
        currentBoard.arr[row][column] = piece;
    }

    @Override
    public boolean doesHeWin(Board board, Player player) {
        Piece piece = player == Player.MAXIMIZER ? Piece.X : Piece.O;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                if (board.arr[i][j] == board.arr[i][j+1] && board.arr[i][j+1] == board.arr[i][j+2] && board.arr[i][j+2] == board.arr[i][j+3]) {
                    if (board.arr[i][j] == piece) return true;
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 7; j++) {
                if (board.arr[i][j] == board.arr[i+1][j] && board.arr[i+1][j] == board.arr[i+2][j] && board.arr[i+2][j] == board.arr[i+3][j]) {
                    if (board.arr[i][j] == piece) return true;
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (board.arr[i][j] == board.arr[i+1][j+1] && board.arr[i+1][j+1] == board.arr[i+2][j+2] && board.arr[i+2][j+2] == board.arr[i+3][j+3]) {
                    if (board.arr[i][j] == piece) return true;
                }
            }
        }
        for (int i = 3; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                if (board.arr[i][j] == board.arr[i-1][j+1] && board.arr[i-1][j+1] == board.arr[i-2][j+2] && board.arr[i-2][j+2] == board.arr[i-3][j+3]) {
                    if (board.arr[i][j] == piece) return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<Board> getChildren(Board board, Player player) {
        List<Board> children = new ArrayList<>(7);
        for (int j = 0; j < 7; j++) {
            if (board.arr[0][j] == Piece.EMPTY) {
                int i = 0;
                while (i+1 < 6 && board.arr[i+1][j] == Piece.EMPTY) {
                    i++;
                }
                //make a new board and place x here, and insert as child
                Board newBoard = board.clone();
                newBoard.arr[i][j] = player == Player.MAXIMIZER ? Piece.X : Piece.O;
                children.add(newBoard);
            }
        }
        return children;
    }

    enum Piece {
        X, O, EMPTY
    }

    class Board implements Cloneable{
        Piece[][] arr;

        Board() {
            arr = new Piece[6][7];
        }

        void init() {
            initNormal();
        }

        private void initNormal() {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++) {
                    arr[i][j] = Piece.EMPTY;
                }
            }
        }

        @Override
        public Board clone() {
            Board newBoard = new Board();
            for (int x = 0; x < 6; x++) {
                newBoard.arr[x] = Arrays.copyOf(arr[x], 7);
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
                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 7; j++) {
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
}
