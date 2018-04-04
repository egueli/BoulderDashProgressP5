package com.e_gueli.boulderprogress;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class Logic {
    private List<Cell> cells;
    private final int fieldWidth;
    private final int fieldHeight;

    Logic(int fieldWidth, int fieldHeight) {
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
    }

    int getFieldWidth() {
        return fieldWidth;
    }

    int getFieldHeight() {
        return fieldHeight;
    }

    Iterable<Cell> getCells() {
        return cells;
    }

    void addCell(int x, int y) {
        if (cellAt(x, y) != null) {
            throw new IllegalStateException(String.format("there is already a cell at (%d, %d)", x, y));
        }

        cells.add(new Cell(x, y));
    }

    void clear() {
        cells.clear();
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

        // Cells with an unstable base (i.e. with no left and no right cell below)
        // will fall at a random side
        if (!baseLeft && !baseRight) {
            int fallDirection = ((int) random(2) == 0) ? -1 : 1;
            System.out.println("random fall at direction " + fallDirection);
            return cell.cloneDown(fallDirection);
        }

        // Cells with an half-unstable base (i.e. with missing left or right cell below)
        // will fall to the empty side
        if (!baseLeft) {
            return cell.cloneDown(-1);
        }
        else {
            return cell.cloneDown(1);
        }
    }

    Cell cellAt(int x, int y) {
        for (Cell cell : cells) {
            if (cell.x == x && cell.y == y) {
                return cell;
            }
        }
        return null;
    }

    private double random(int max) {
        return Math.random() * max;
    }
}
