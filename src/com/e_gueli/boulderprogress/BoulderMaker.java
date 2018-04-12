package com.e_gueli.boulderprogress;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoulderMaker {
    private final BoulderField boulderField;
    private final Random rng;

    public BoulderMaker(BoulderField boulderField, Random rng) {
        this.boulderField = boulderField;
        this.rng = rng;
    }

    void makeBoulderAtTop() {
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

    private int random(int max) {
        return rng.nextInt(max);
    }
}
