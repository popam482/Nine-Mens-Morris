package GameLogic;

public class MillChecker {


    public static boolean checkMill(FullBoard board, int nodeIndex) {
        SquareNode node = board.getNode(nodeIndex);
        String color = node.getColor();

        if ("GRAY".equals(color) || color == null) {
            return false;
        }

        int level = node.getLevel();
        int position = node.getPositionInSquare();

        System.out.println("Checking mill for position " + nodeIndex +
                " (level=" + level + ", pos=" + position + ", color=" + color + ")");


        if (checkHorizontalMill(board, level, position, color)) {
            System.out.println("  -> HORIZONTAL MILL FOUND!");
            return true;
        }


        if (position == 1 || position == 3 || position == 5 || position == 7) {
            if (checkVerticalMill(board, position, color)) {
                System.out.println("  -> VERTICAL MILL FOUND!");
                return true;
            }
        }

        return false;
    }

    /**
      Possible mills:
      0-1-2 (top horizontal)
      2-3-4 (right vertical)
      4-5-6 (bottom horizontal)
      6-7-0 (left vertical)
     */
    private static boolean checkHorizontalMill(FullBoard board, int level, int position, String color) {
        int baseIndex = level * 8;


        // Top side: 0-1-2
        if ((position == 0 || position == 1 || position == 2) &&
                checkThreeInRow(board, baseIndex + 0, baseIndex + 1, baseIndex + 2, color)) {
            return true;
        }

        // Right side: 2-3-4
        if ((position == 2 || position == 3 || position == 4) &&
                checkThreeInRow(board, baseIndex + 2, baseIndex + 3, baseIndex + 4, color)) {
            return true;
        }

        // Bottom side: 4-5-6
        if ((position == 4 || position == 5 || position == 6) &&
                checkThreeInRow(board, baseIndex + 4, baseIndex + 5, baseIndex + 6, color)) {
            return true;
        }

        // Left side: 6-7-0
        if ((position == 6 || position == 7 || position == 0) &&
                checkThreeInRow(board, baseIndex + 6, baseIndex + 7, baseIndex + 0, color)) {
            return true;
        }

        return false;
    }


    private static boolean checkVerticalMill(FullBoard board, int position, String color) {
        int outerIndex = position;           // 0-7
        int middleIndex = 8 + position;      // 8-15
        int innerIndex = 16 + position;      // 16-23

        return checkThreeInRow(board, outerIndex, middleIndex, innerIndex, color);
    }


    private static boolean checkThreeInRow(FullBoard board, int idx1, int idx2, int idx3, String color) {
        SquareNode node1 = board.getNode(idx1);
        SquareNode node2 = board.getNode(idx2);
        SquareNode node3 = board.getNode(idx3);

        boolean result = color.equals(node1.getColor()) &&
                color.equals(node2.getColor()) &&
                color.equals(node3.getColor());

        if (result) {
            System.out.println("  Mill found at positions: " + idx1 + "-" + idx2 + "-" + idx3);
        }

        return result;
    }


    public static boolean canRemovePiece(FullBoard board, int nodeIndex, String opponentColor) {
        SquareNode node = board.getNode(nodeIndex);

        if (!opponentColor.equals(node.getColor())) {
            return false;
        }

        if (checkMill(board, nodeIndex)) {
            boolean hasNonMill = hasNonMillPieces(board, opponentColor);
            System.out.println("  Piece at " + nodeIndex + " is in mill. Can remove: " + !hasNonMill);
            return !hasNonMill;
        }

        return true;
    }


    private static boolean hasNonMillPieces(FullBoard board, String color) {
        for (int i = 0; i < 24; i++) {
            SquareNode node = board.getNode(i);
            if (color.equals(node.getColor())) {
                if (!checkMill(board, i)) {
                    System.out.println("  Found non-mill piece at " + i);
                    return true;
                }
            }
        }
        return false;
    }


    public static void printBoardStatus(FullBoard board) {
        System.out.println("\n=== BOARD STATUS ===");
        for (int i = 0; i < 24; i++) {
            SquareNode node = board.getNode(i);
            String color = node.getColor();
            if (!"GRAY".equals(color)) {
                boolean inMill = checkMill(board, i);
                System.out.println("Position " + i + ": " + color + (inMill ? " [IN MILL]" : ""));
            }
        }
        System.out.println("===================\n");
    }
}