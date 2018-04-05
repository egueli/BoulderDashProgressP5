package com.e_gueli.boulderprogress;

import java.util.ArrayList;
import java.util.List;

public class BoulderMaker {
    private int waitCount;
    private final BoulderField boulderField;

    public BoulderMaker(BoulderField boulderField) {
        this.boulderField = boulderField;
    }

    void iteration() {
        makeNewCells();
    }

    private void makeNewCells() {
        boolean allSettled = true;
        for (Boulder boulder : boulderField.getBoulders()) {
            if (!boulder.settled) {
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


        int fieldWidth = boulderField.getFieldWidth();
        int[] heights = new int[fieldWidth];
        for (int x = 0; x < fieldWidth; x++) {
            int y = boulderField.getFieldHeight() - 1;
            while (y >= 0 && boulderField.cellAt(x, y) != null) {
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
            boulderField.clear();
            return;
        }

        int i = (int) (random(availableColumns.size()));
        int x = availableColumns.get(i);
        boulderField.addCell(x, heights[x]);
    }

    private double random(int max) {
        return Math.random() * max;
    }
}
