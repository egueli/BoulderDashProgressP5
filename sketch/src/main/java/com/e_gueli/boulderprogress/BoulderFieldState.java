package com.e_gueli.boulderprogress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoulderFieldState {
    private final int fieldWidth;
    private final int fieldHeight;
    private final List<Boulder> boulders;

    BoulderFieldState(int fieldWidth, int fieldHeight) {
        this(fieldWidth, fieldHeight, new ArrayList<>());
    }

    BoulderFieldState(int fieldWidth, int fieldHeight, List<Boulder> boulders) {
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        this.boulders = Collections.unmodifiableList(boulders);
    }

    List<Boulder> getBoulders() {
        return boulders;
    }

    Boulder boulderAt(int x, int y) {
        for (Boulder boulder : boulders) {
            if (boulder.x == x && boulder.y == y) {
                return boulder;
            }
        }
        return null;
    }

    boolean[][] toArrayOfSettled() {
        boolean[][] output = new boolean[fieldWidth][fieldHeight];
        for (Boulder boulder : boulders) {
            if (boulder.settled) {
                output[boulder.x][boulder.y] = true;
            }
        }
        return output;
    }

    boolean allSettled() {
        for (Boulder boulder : getBoulders()) {
            if (!boulder.settled) {
                return false;
            }
        }
        return true;
    }

    public int getWidth() {
        return fieldWidth;
    }

    public int getHeight() {
        return fieldHeight;
    }

    @Override
    public String toString() {
        return "BoulderFieldState{" +
                "ihc=" + System.identityHashCode(this) +
                ", boulders.size=" + boulders.size() +
                '}';
    }
}
