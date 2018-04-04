package com.e_gueli.boulderprogress;

import processing.core.PApplet;

public class BoulderDashProgress extends PApplet {
    private static final int cellSize = 50;
    private static final int fieldWidth = 5;
    private static final int fieldHeight = 7;

    private static final int interval = 250;
    private int lastRecordedTime = 0;

    private BdpLogic bdpLogic = new BdpLogic(fieldWidth, fieldHeight);

    public void settings() {
        //size (cellSize * fieldWidth, cellSize * fieldHeight);
        size(250, 350);
    }

    public void setup() {
        textSize(18);
        textAlign(CENTER, CENTER);
        ellipseMode(CORNER);
    }


    public void draw() {
        background(0);

        drawField();
        updateField();

    }

    private void drawField() {
        drawFieldBase();

        for (Cell cell : bdpLogic.getCells()) {
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
        bdpLogic.iteration();
        lastRecordedTime = millis();
    }

    public void runMain() {
        main(BoulderDashProgress.class.getName());
    }
}
