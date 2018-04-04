package com.e_gueli.boulderprogress;

import java.util.ArrayList;
import java.util.List;

public class CellMaker {
    private int waitCount;
    private final Logic logic;

    public CellMaker(Logic logic) {
        this.logic = logic;
    }

    void iteration() {
        makeNewCells();
    }

    private void makeNewCells() {
        boolean allSettled = true;
        for (Cell cell : logic.getCells()) {
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


        int fieldWidth = logic.getFieldWidth();
        int[] heights = new int[fieldWidth];
        for (int x = 0; x < fieldWidth; x++) {
            int y = logic.getFieldHeight() - 1;
            while (y >= 0 && logic.cellAt(x, y) != null) {
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
            logic.clear();
            return;
        }

        int i = (int) (random(availableColumns.size()));
        int x = availableColumns.get(i);
        logic.addCell(x, heights[x]);
    }

    private double random(int max) {
        return Math.random() * max;
    }
}
