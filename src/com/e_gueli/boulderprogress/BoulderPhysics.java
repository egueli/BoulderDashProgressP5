package com.e_gueli.boulderprogress;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

class BoulderPhysics {
    private List<Boulder> boulders;
    private final int fieldWidth;
    private final int fieldHeight;
    private final Random rng;

    BoulderPhysics(int fieldWidth, int fieldHeight, Random rng) {
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        this.rng = rng;
        boulders = new LinkedList<>();
    }

    void update() {
        List<Boulder> newBoulders = new LinkedList<>();

        for (Boulder boulder : boulders) {
            newBoulders.add(updateBoulder(boulder));
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

    void addBoulder(int x, int y) {
        if (boulderAt(x, y) != null) {
            throw new IllegalStateException(String.format("there is already a boulder at (%d, %d)", x, y));
        }

        boulders.add(new Boulder(x, y));
    }

    void clear() {
        boulders.clear();
    }

    private Boulder updateBoulder(Boulder boulder) {
        // Settled boulders stay as they are.
        if (boulder.settled) {
            return boulder;
        }

        // Boulders at the bottom are settled.
        if (boulder.y == fieldHeight - 1) {
            return boulder.cloneSettled();
        }

        // From this below, the boulder is going to fall.

        Boulder boulderBottomLeft = boulderAt(boulder.x - 1, boulder.y + 1);
        boolean baseLeft = boulder.x == 0 || (boulderBottomLeft != null && boulderBottomLeft.settled);
        Boulder boulderBottomCenter = boulderAt(boulder.x, boulder.y + 1);
        boolean baseCenter = boulderBottomCenter != null && boulderBottomCenter.settled;
        Boulder boulderBottomRight = boulderAt(boulder.x + 1, boulder.y + 1);
        boolean baseRight = boulder.x == fieldWidth - 1 || (boulderBottomRight != null && boulderBottomRight.settled);

        // Boulders with a stable base stay still
        if (baseLeft && baseCenter && baseRight) {
            return boulder.cloneSettled();
        }

        // Boulders with nothing below will fall
        if (!baseCenter) {
            return boulder.cloneDown(0);
        }

        // Boulders with an unstable base (i.e. with no left and no right boulder below)
        // will fall at a random side
        if (!baseLeft && !baseRight) {
            int fallDirection = (random(2) == 0) ? -1 : 1;
            System.out.println("random fall at direction " + fallDirection);
            return boulder.cloneDown(fallDirection);
        }

        // Boulders with an half-unstable base (i.e. with missing left or right boulder below)
        // will fall to the empty side
        if (!baseLeft) {
            return boulder.cloneDown(-1);
        }
        else {
            return boulder.cloneDown(1);
        }
    }

    Boulder boulderAt(int x, int y) {
        for (Boulder boulder : boulders) {
            if (boulder.x == x && boulder.y == y) {
                return boulder;
            }
        }
        return null;
    }

    private int random(int max) {
        return rng.nextInt(max);
    }

    boolean allSettled() {
        for (Boulder boulder : getBoulders()) {
            if (!boulder.settled) {
                return false;
            }
        }
        return true;
    }
}
