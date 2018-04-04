package com.e_gueli.boulderprogress;

import processing.core.PApplet;

public class BoulderDashProgress extends PApplet {
    private static final int cellSize = 50;
    private static final int fieldWidth = 5;
    private static final int fieldHeight = 7;

    private static final int interval = 250;
    private int lastRecordedTime = 0;

    private static final boolean testMode = false;

    private Logic logic = new Logic(fieldWidth, fieldHeight);

    public void settings() {
        //size (cellSize * fieldWidth, cellSize * fieldHeight);
        size(250, 350);
    }

    public void setup() {
        textSize(18);
        textAlign(CENTER, CENTER);
        ellipseMode(CORNER);

        if (testMode) {
            logic.setCellMakingEnabled(false);
            logic.addCell(2, 6);
            logic.addCell(2, 0);
        }
    }


    public void draw() {
        background(0);

        drawField();
        updateField();

    }

    private void drawField() {
        drawFieldBase();

        for (Cell cell : logic.getCells()) {
            if (cell.settled) {
                fill(color(0, 200, 0));
                noStroke();
            } else {
                noFill();
                stroke(255);
            }
            ellipse(cell.x * cellSize, cell.y * cellSize, cellSize, cellSize);
        }
    }

    private void drawFieldBase() {
        stroke(48);
        noFill();

        for (int x = 0; x < fieldWidth; x++) {
            for (int y = 0; y < fieldHeight; y++) {
                ellipse(x * cellSize, y * cellSize, cellSize, cellSize);
            }
        }
    }

    private void updateField() {
        // Iterate if timer ticks
        if (millis() - lastRecordedTime <= interval) {
            return;
        }
        logic.iteration();
        lastRecordedTime = millis();
    }

    public void runMain() {
        main(BoulderDashProgress.class.getName());
    }
}
