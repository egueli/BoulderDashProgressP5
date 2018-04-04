package com.e_gueli.boulderprogress;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

public class BoulderDashProgress extends PApplet {


    // Size of cells
    final int cellSize = 50;
    final int fieldWidth = 5;
    final int fieldHeight = 7;

    final int interval = 250;
    int lastRecordedTime = 0;
    int frameNumber = 0;


    List<Cell> cells;

    // Pause
    boolean pause = false;

    public void settings() {
        //size (cellSize * fieldWidth, cellSize * fieldHeight);
        size(250, 350);
    }

    public void setup() {

        // Instantiate arrays
        cells = new LinkedList<Cell>();

        textSize(18);
        textAlign(CENTER, CENTER);
        ellipseMode(CORNER);
    }


    public void draw() {
        background(0);

        for (int x = 0; x < fieldWidth; x++) {
            for (int y = 0; y < fieldHeight; y++) {
                Cell cell = cellAt(x, y);
                if (cell != null) {
                    if (cell.settled) {
                        fill(color(0, 200, 0));
                        noStroke();
                    } else {
                        noFill();
                        stroke(255);
                    }
                } else {
                    stroke(48);
                    noFill();
                }
                ellipse(x * cellSize, y * cellSize, cellSize, cellSize);

            }
        }
        // Iterate if timer ticks
        if (millis() - lastRecordedTime > interval) {
            if (!pause) {
                iteration();
                lastRecordedTime = millis();
            }
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

    void iteration() { // When the clock ticks
        List<Cell> newCells = new LinkedList<Cell>();

        for (Cell cell : cells) {
            newCells.add(updateCell(cell));
        }

        cells = newCells;

        makeNewCells();

        frameNumber++;
    }

    Cell updateCell(Cell cell) {
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
        return cell.cloneDown(fallDirection);
    }

    int waitCount;

    void makeNewCells() {
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

        List<Integer> availableColumns = new ArrayList<Integer>();
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

    public void runMain() {
        main(BoulderDashProgress.class.getName());
    }
}
