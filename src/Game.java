import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public abstract class Game<BoardType> {

    public BoardType currentBoard = null;

    Game() {
        initBoard();
    }

    private Node minChild(ArrayList<Node> children) {
        Node min = children.get(0);
        for (int i = 1; i < children.size(); i++) {
            if (children.get(i).value < min.value) {
                min = children.get(i);
            }
        }
        return min;
    }

    private Node maxChild(ArrayList<Node> children) {
        Node max = children.get(0);
        for (int i = 1; i < children.size(); i++) {
            if (children.get(i).value > max.value) {
                max = children.get(i);
            }
        }
        return max;
    }

    public abstract void initBoard();

    public abstract void printBoard();

    public abstract boolean gameOver(BoardType board);

    public abstract int evaluateBoard(BoardType board, Player player);

    public abstract void playHumanMove(Player humanPlayer);

    public abstract boolean doesHeWin(BoardType board, Player player);

    public void playComputerMove(Player player) {
        long startTime = System.currentTimeMillis();
        final Node root = new Node(currentBoard);
        evaluateNodes(root, player, 4);
        Node child = player == Player.MINIMIZER ? minChild(root.children) : maxChild(root.children);
//        final int[] depth = {4};

//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        Callable<Node> task = () -> {
//            evaluateNodes(root, player, ++depth[0]);
//            return player == Player.MINIMIZER ? minChild(root.children) : maxChild(root.children);
//        };
//
//        while (true) {
//            long timeRemaining = startTime + 5 * 1000 - System.currentTimeMillis();
//            if (timeRemaining <= 0) {
//                break;
//            }
//            Future<Node> future = executor.submit(task);
//            try {
//                Node node = future.get(timeRemaining, TimeUnit.MILLISECONDS);
//                child = node;
//            } catch (InterruptedException | ExecutionException | TimeoutException e) {
//                //do nothing
//            }
//        }
//
//        executor.shutdownNow();
//        System.out.println("depth: " + depth[0]);
        currentBoard = child.board;
    }

    public abstract List<BoardType> getChildren(BoardType board, Player player);

    private void evaluateNodes(Node root, Player player, int depth) {
        if (gameOver(root.board) || depth == 0) {
            root.value = evaluateBoard(root.board, player.opposite());
        } else {
            root.value = player == Player.MAXIMIZER ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            root.children = new ArrayList<>();

            for (BoardType childBoard : getChildren(root.board, player)) {
                Node newChild = new Node(childBoard);
                newChild.alpha = root.alpha;
                newChild.beta = root.beta;
                evaluateNodes(newChild, player.opposite(), depth-1);
                root.children.add(newChild);
                if (player == Player.MAXIMIZER) {
                    root.value = Math.max(root.value, newChild.value);
                    root.alpha = Math.max(root.alpha, newChild.value);
                } else {
                    root.value = Math.min(root.value, newChild.value);
                    root.beta = Math.min(root.beta, newChild.value);
                }
                if (root.beta <= root.alpha) {
                    break;
                }
            }
        }
    }

    public void play() {
        Player humanPlayer = Player.MAXIMIZER;
        Player player = Player.MAXIMIZER;
        printBoard();
        while (!gameOver(currentBoard)) {
            if (player == humanPlayer) {
                playHumanMove(humanPlayer);
            } else {
                long startTime = System.currentTimeMillis();
                playComputerMove(humanPlayer.opposite());
                long elapsed = System.currentTimeMillis() - startTime;
                System.out.println("Move took " + elapsed + " ms");
                printBoard();
            }
            player = player.opposite();
        }
        if (doesHeWin(currentBoard, humanPlayer)) {
            System.out.println("YOU WIN!");
        } else if (doesHeWin(currentBoard, humanPlayer.opposite())) {
            System.out.println("YOU LOSE!");
        } else {
            System.out.println("YOU DRAW!");
        }
    }

    class Node {
        BoardType board;
        int value;
        int alpha;
        int beta;
        ArrayList<Node> children;

        Node(BoardType board) {
            this.board = board;
            this.value = 0;
            this.children = null;
            alpha = Integer.MIN_VALUE;
            beta = Integer.MAX_VALUE;
        }
    }


}
