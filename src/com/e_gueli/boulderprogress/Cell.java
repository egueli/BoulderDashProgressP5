package com.e_gueli.boulderprogress;

class Cell {
    final int x;
    final int y;
    final boolean settled;
    final int id;
    private static int nextId = 0;

    Cell(int x, int y) {
        this(x, y, false, nextId++);
    }

    private Cell(int x, int y, boolean settled, int id) {
        this.x = x;
        this.y = y;
        this.settled = settled;
        this.id = id;
    }

    Cell cloneSettled() {
        return new Cell(x, y, true, id);
    }

    Cell cloneDown(int horizontalOffset) {
        return new Cell(x + horizontalOffset, y + 1, false, id);
    }
}
