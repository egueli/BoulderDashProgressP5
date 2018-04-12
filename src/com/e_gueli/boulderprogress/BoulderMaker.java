package com.e_gueli.boulderprogress;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoulderMaker {
    private final BoulderPhysics boulderPhysics;
    private final Random rng;

    public BoulderMaker(BoulderPhysics boulderPhysics, Random rng) {
        this.boulderPhysics = boulderPhysics;
        this.rng = rng;
    }

    void makeBoulderAtTop() {
        int fieldWidth = boulderPhysics.getFieldWidth();
        int[] heights = new int[fieldWidth];
        for (int x = 0; x < fieldWidth; x++) {
            int y = boulderPhysics.getFieldHeight() - 1;
            while (y >= 0 && boulderPhysics.boulderAt(x, y) != null) {
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
            boulderPhysics.clear();
            return;
        }

        int i = (int) (random(availableColumns.size()));
        int x = availableColumns.get(i);
        boulderPhysics.addBoulder(x, heights[x]);
    }

    private int random(int max) {
        return rng.nextInt(max);
    }
}
