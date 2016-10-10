package com.e_gueli;

import processing.core.PApplet;

@SuppressWarnings("WeakerAccess")
public class Main extends PApplet {

    // Size of cells
    int cellSize = 5;

    int rows = 40;
    int columns = 40;

    // How likely for a cell to be alive at start (in percentage)
    float probabilityOfAliveAtStart = 15;

    // Variables for timer
    int interval = 100;
    int lastRecordedTime = 0;

    // Colors for active/inactive cells
    int alive = color(0, 200, 0);
    int dead = color(0);

    // Array of cells
    int[][] cells;
    // Buffer to record the state of the cells and use this while changing the others in the interations
    int[][] cellsBuffer;

    // Pause
    boolean pause = false;

    public void settings() {
        size(640, 360);
        noSmooth();
    }

    public void setup() {
        // Instantiate arrays
        cells = new int[columns][rows];
        cellsBuffer = new int[columns][rows];

        // This stroke will draw the background grid
        stroke(48);


        // Initialization of cells
        for (int x=0; x<columns; x++) {
            for (int y=0; y<rows; y++) {
                float state = random (100);
                if (state > probabilityOfAliveAtStart) {
                    state = 0;
                }
                else {
                    state = 1;
                }
                cells[x][y] = (int)Math.floor(state); // Save state of each cell
            }
        }
        background(0); // Fill in black in case cells don't cover all the windows
    }

    public void draw() {

        //Draw grid
        for (int x=0; x<columns; x++) {
            for (int y=0; y<rows; y++) {
                if (cells[x][y]==1) {
                    fill(alive); // If alive
                }
                else {
                    fill(dead); // If dead
                }
                rect (x*cellSize, y*cellSize, cellSize, cellSize);
            }
        }
        // Iterate if timer ticks
        if (millis()-lastRecordedTime>interval) {
            if (!pause) {
                iteration();
                lastRecordedTime = millis();
            }
        }
    }



    void iteration() { // When the clock ticks
        // Save cells to buffer (so we opeate with one array keeping the other intact)
        for (int x=0; x<columns; x++) {
            for (int y=0; y<rows; y++) {
                cellsBuffer[x][y] = cells[x][y];
            }
        }

        // Visit each cell:
        for (int x=0; x<columns; x++) {
            for (int y=0; y<rows; y++) {
                // And visit all the neighbours of each cell
                int neighbours = 0; // We'll count the neighbours
                for (int xx=x-1; xx<=x+1;xx++) {
                    for (int yy=y-1; yy<=y+1;yy++) {
                        if (((xx>=0)&&(xx<columns))&&((yy>=0)&&(yy<rows))) { // Make sure you are not out of bounds
                            if (!((xx==x)&&(yy==y))) { // Make sure to to check against self
                                if (cellsBuffer[xx][yy]==1){
                                    neighbours ++; // Check alive neighbours and count them
                                }
                            } // End of if
                        } // End of if
                    } // End of yy loop
                } //End of xx loop
                // We've checked the neigbours: apply rules!
                if (cellsBuffer[x][y]==1) { // The cell is alive: kill it if necessary
                    if (neighbours < 2 || neighbours > 3) {
                        cells[x][y] = 0; // Die unless it has 2 or 3 neighbours
                    }
                }
                else { // The cell is dead: make it live if necessary
                    if (neighbours == 3 ) {
                        cells[x][y] = 1; // Only if it has 3 neighbours
                    }
                } // End of if
            } // End of y loop
        } // End of x loop
    } // End of function

    public static void main(String... args){
        PApplet.main(Main.class.getName());
    }

}