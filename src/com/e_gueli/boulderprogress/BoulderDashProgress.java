package com.e_gueli.boulderprogress;

import processing.core.PApplet;

import java.util.Random;

public class BoulderDashProgress extends PApplet {
    private static final int circleSize = 50;
    private static final int fieldWidth = 5;
    private static final int fieldHeight = 7;

    private static final int interval = 250;
    private int lastRecordedTime = 0;

    private static final boolean testMode = false;

    private Random rng = new Random(0);
    private BoulderPhysics physics = new BoulderPhysics(fieldWidth, fieldHeight, rng);
    private BoulderMaker boulderMaker = new BoulderMaker(physics, rng);

    private int settledFor = 0;

    public void settings() {
        //size (circleSize * fieldWidth, circleSize * fieldHeight);
        size(250, 350);
    }

    public void setup() {
        textSize(18);
        textAlign(CENTER, CENTER);
        ellipseMode(CORNER);

        if (testMode) {
            physics.addBoulder(2, 6);
            physics.addBoulder(2, 0);
        }
    }


    public void draw() {
        background(0);

        drawField();
        updateField();

    }

    private void drawField() {
        drawFieldBase();

        for (Boulder boulder : physics.getField().getBoulders()) {
            if (boulder.settled) {
                fill(color(0, 200, 0));
                noStroke();
            } else {
                noFill();
                stroke(255);
            }
            ellipse(boulder.x * circleSize, boulder.y * circleSize, circleSize, circleSize);
        }
    }

    private void drawFieldBase() {
        stroke(48);
        noFill();

        for (int x = 0; x < fieldWidth; x++) {
            for (int y = 0; y < fieldHeight; y++) {
                ellipse(x * circleSize, y * circleSize, circleSize, circleSize);
            }
        }
    }

    private void updateField() {
        // Iterate if timer ticks
        if (millis() - lastRecordedTime <= interval) {
            return;
        }
        if (!physics.getField().allSettled()) {
            physics.update();
            settledFor = 0;
        }
        else {
            if (!testMode) {
                settledFor++;
                if (settledFor >= 5) {
                    boulderMaker.makeBoulderAtTop();
                }
            }
        }
        lastRecordedTime = millis();
    }

    public void runMain() {
        main(BoulderDashProgress.class.getName());
    }
}
