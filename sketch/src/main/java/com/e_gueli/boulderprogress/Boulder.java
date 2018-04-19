package com.e_gueli.boulderprogress;

class Boulder {
    final int x;
    final int y;
    final boolean settled;
    final int id;
    private static int nextId = 0;

    Boulder(int x, int y) {
        this(x, y, false, nextId++);
    }

    private Boulder(int x, int y, boolean settled, int id) {
        this.x = x;
        this.y = y;
        this.settled = settled;
        this.id = id;
    }

    Boulder cloneSettled() {
        return new Boulder(x, y, true, id);
    }

    Boulder cloneDown(int horizontalOffset) {
        return new Boulder(x + horizontalOffset, y + 1, false, id);
    }
}
