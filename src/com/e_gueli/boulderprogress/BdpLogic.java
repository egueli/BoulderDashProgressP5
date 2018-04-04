package com.e_gueli.boulderprogress;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class BdpLogic {
    private List<Cell> cells;
    private final int fieldWidth;
    private final int fieldHeight;
    private int waitCount;

    BdpLogic(int fieldWidth, int fieldHeight) {
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        cells = new LinkedList<>();
    }

    void iteration() {
        List<Cell> newCells = new LinkedList<>();

        for (Cell cell : cells) {
            newCells.add(updateCell(cell));
        }

        cells = newCells;

        makeNewCells();
    }

    Iterable<Cell> getCells() {
        return cells;
    }

    private Cell updateCell(Cell cell) {
        // Settled cells stay as they are.
        if (cell.settled) {
            return cell;
        }

        // Cells at the bottom are settled.
        if (cell.y == fieldHeight - 1) {
            return cell.cloneSettled();
        }

        // From this below, the cell is going to fall.

        Cell cellBottomLeft = cellAt(cell.x - 1, cell.y + 1);
        boolean baseLeft = cell.x == 0 || (cellBottomLeft != null && cellBottomLeft.settled);
        Cell cellBottomCenter = cellAt(cell.x, cell.y + 1);
        boolean baseCenter = cellBottomCenter != null && cellBottomCenter.settled;
        Cell cellBottomRight = cellAt(cell.x + 1, cell.y + 1);
        boolean baseRight = cell.x == fieldWidth - 1 || (cellBottomRight != null && cellBottomRight.settled);

        // Cells with a stable base stay still
        if (baseLeft && baseCenter && baseRight) {
            return cell.cloneSettled();
        }

        // Cells with nothing below will fall
        if (!baseCenter) {
            return cell.cloneDown(0);
        }

        // Cells with an half-unstable base (i.e. with missing left or right cell below)
        // will fall to the empty side
        if (!baseLeft) {
            return cell.cloneDown(-1);
        }
        if (!baseRight) {
            return cell.cloneDown(1);
        }
        // Cells with an unstable base (i.e. with no left and no right cell below)
        // will fall at a random side
        int fallDirection = ((int) random(2) == 0) ? -1 : 1;
        System.out.println("random fall at direction " + fallDirection);
        return cell.cloneDown(fallDirection);
    }

    private Cell cellAt(int x, int y) {
        for (Cell cell : cells) {
            if (cell.x == x && cell.y == y) {
                return cell;
            }
        }
        return null;
    }

    private void makeNewCells() {
        boolean allSettled = true;
        for (Cell cell : cells) {
            if (!cell.settled) {
                allSettled = false;
                break;
            }
        }

        if (!allSettled) {
            waitCount = 5;
            return;
        }

        waitCount--;
        if (waitCount > 0) return;


        int[] heights = new int[fieldWidth];
        for (int x = 0; x < fieldWidth; x++) {
            int y = fieldHeight - 1;
            while (y >= 0 && cellAt(x, y) != null) {
                y--;
            }
            heights[x] = y;
        }

        List<Integer> availableColumns = new ArrayList<>();
        for (int x = 0; x < fieldWidth; x++) {
            if (heights[x] >= 0) {
                availableColumns.add(x);
            }
        }

        if (availableColumns.isEmpty()) {
            cells.clear();
            return;
        }

        int i = (int) (random(availableColumns.size()));
        int x = availableColumns.get(i);
        cells.add(new Cell(x, heights[x]));
    }

    private double random(int max) {
        return Math.random() * max;
    }
}
