package com.e_gueli.boulderprogress;

import processing.core.PApplet;

import java.util.*;

public class BoulderDashProgress extends PApplet {
    private static final int circleSize = 50;
    private static final int fieldWidth = 5;
    private static final int fieldHeight = 7;

    private Random rng = new Random(0);

    private ArduinoBitmapSender bitmapSender = new ArduinoBitmapSender();

    /**
     * The list of bitmaps, one for each amount of dots/boulders to show.
     * The first item is empty, the next has one boulder, and so on.
     */
    private Deque<BoulderFieldState> stateStack = new LinkedList<>();

    public static void main(String[] args) {
        main(BoulderDashProgress.class.getName());
    }

    public void settings() {
        //size (circleSize * fieldWidth, circleSize * fieldHeight);
        size(250, 350);
    }

    public void setup() {
        textSize(18);
        textAlign(CENTER, CENTER);
        ellipseMode(CORNER);

        bitmapSender.setup(BoulderDashProgress.this);
    }


    public void draw() {
        background(0);

        updateField();
        drawField();
        bitmapSender.sendFieldToArduino(stateStack.getLast());
    }

    private void drawField() {
        drawFieldBase();

        for (Boulder boulder : stateStack.getLast().getBoulders()) {
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
        int numDots = fieldHeight * fieldWidth;
        int dotsToShow = (int) map(mouseY, circleSize * fieldHeight, 0, 0, numDots + 1);

        while (stateStack.size() > dotsToShow + 1) {
            stateStack.removeLast();
        }

        while (stateStack.size() < dotsToShow) {
            pushNewField();
        }
    }

    private void pushNewField() {
        BoulderPhysics physics;
        if (stateStack.isEmpty()) {
            physics = new BoulderPhysics(fieldWidth, fieldHeight, rng);
        } else {
            physics = new BoulderPhysics(stateStack.peekLast(), rng);
            BoulderMaker boulderMaker = new BoulderMaker(physics, rng);

            boulderMaker.makeBoulderAtTop();
            while (!physics.getField().allSettled()) {
                physics.update();
            }
        }

        stateStack.addLast(physics.getField());
    }
}
