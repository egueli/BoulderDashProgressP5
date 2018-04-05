package com.e_gueli.boulderprogress;

import java.util.LinkedList;
import java.util.List;

class BoulderField {
    private List<Boulder> boulders;
    private final int fieldWidth;
    private final int fieldHeight;

    BoulderField(int fieldWidth, int fieldHeight) {
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        boulders = new LinkedList<>();
    }

    void iteration() {
        List<Boulder> newBoulders = new LinkedList<>();

        for (Boulder boulder : boulders) {
            newBoulders.add(updateCell(boulder));
        }

        boulders = newBoulders;
    }

    int getFieldWidth() {
        return fieldWidth;
    }

    int getFieldHeight() {
        return fieldHeight;
    }

    Iterable<Boulder> getBoulders() {
        return boulders;
    }

    void addCell(int x, int y) {
        if (cellAt(x, y) != null) {
            throw new IllegalStateException(String.format("there is already a cell at (%d, %d)", x, y));
        }

        boulders.add(new Boulder(x, y));
    }

    void clear() {
        boulders.clear();
    }

    private Boulder updateCell(Boulder boulder) {
        // Settled boulders stay as they are.
        if (boulder.settled) {
            return boulder;
        }

        // Cells at the bottom are settled.
        if (boulder.y == fieldHeight - 1) {
            return boulder.cloneSettled();
        }

        // From this below, the boulder is going to fall.

        Boulder boulderBottomLeft = cellAt(boulder.x - 1, boulder.y + 1);
        boolean baseLeft = boulder.x == 0 || (boulderBottomLeft != null && boulderBottomLeft.settled);
        Boulder boulderBottomCenter = cellAt(boulder.x, boulder.y + 1);
        boolean baseCenter = boulderBottomCenter != null && boulderBottomCenter.settled;
        Boulder boulderBottomRight = cellAt(boulder.x + 1, boulder.y + 1);
        boolean baseRight = boulder.x == fieldWidth - 1 || (boulderBottomRight != null && boulderBottomRight.settled);

        // Cells with a stable base stay still
        if (baseLeft && baseCenter && baseRight) {
            return boulder.cloneSettled();
        }

        // Cells with nothing below will fall
        if (!baseCenter) {
            return boulder.cloneDown(0);
        }

        // Cells with an unstable base (i.e. with no left and no right boulder below)
        // will fall at a random side
        if (!baseLeft && !baseRight) {
            int fallDirection = ((int) random(2) == 0) ? -1 : 1;
            System.out.println("random fall at direction " + fallDirection);
            return boulder.cloneDown(fallDirection);
        }

        // Cells with an half-unstable base (i.e. with missing left or right boulder below)
        // will fall to the empty side
        if (!baseLeft) {
            return boulder.cloneDown(-1);
        }
        else {
            return boulder.cloneDown(1);
        }
    }

    Boulder cellAt(int x, int y) {
        for (Boulder boulder : boulders) {
            if (boulder.x == x && boulder.y == y) {
                return boulder;
            }
        }
        return null;
    }

    private double random(int max) {
        return Math.random() * max;
    }
}
