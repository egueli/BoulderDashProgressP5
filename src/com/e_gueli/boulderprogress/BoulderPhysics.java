package com.e_gueli.boulderprogress;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

class BoulderPhysics {
    private BoulderFieldState field;
    private final int fieldWidth;
    private final int fieldHeight;
    private final Random rng;

    BoulderPhysics(int fieldWidth, int fieldHeight, Random rng) {
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        this.rng = rng;
        clear();
    }

    void update() {
        List<Boulder> newBoulders = new LinkedList<>();

        for (Boulder boulder : field.getBoulders()) {
            newBoulders.add(updateBoulder(boulder));
        }

        field = new BoulderFieldState(fieldWidth, fieldHeight, newBoulders);
    }

    int getFieldWidth() {
        return fieldWidth;
    }

    int getFieldHeight() {
        return fieldHeight;
    }

    BoulderFieldState getField() {
        return field;
    }

    void addBoulder(int x, int y) {
        if (field.boulderAt(x, y) != null) {
            throw new IllegalStateException(String.format("there is already a boulder at (%d, %d)", x, y));
        }

        List<Boulder> newBoulders = new ArrayList<Boulder>(field.getBoulders());
        newBoulders.add(new Boulder(x, y));
        field = new BoulderFieldState(fieldWidth, fieldHeight, newBoulders);
    }

    void clear() {
        field = new BoulderFieldState(fieldWidth, fieldHeight);
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

        Boulder boulderBottomLeft = field.boulderAt(boulder.x - 1, boulder.y + 1);
        boolean baseLeft = boulder.x == 0 || (boulderBottomLeft != null && boulderBottomLeft.settled);
        Boulder boulderBottomCenter = field.boulderAt(boulder.x, boulder.y + 1);
        boolean baseCenter = boulderBottomCenter != null && boulderBottomCenter.settled;
        Boulder boulderBottomRight = field.boulderAt(boulder.x + 1, boulder.y + 1);
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


    private int random(int max) {
        return rng.nextInt(max);
    }
}
