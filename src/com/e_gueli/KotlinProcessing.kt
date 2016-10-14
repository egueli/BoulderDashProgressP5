package com.e_gueli

import processing.core.PApplet
import kotlin.reflect.jvm.jvmName

const val columns = 40
const val rows = 40
const val probabilityOfAliveAtStart = 15
const val cellSize:Float = 5f

class KotlinP5 : PApplet() {

    internal var alive = color(0, 200, 0)
    internal var dead = color(0)

    // Variables for timer
    internal var interval = 100
    internal var lastRecordedTime = 0


    var cells = Array(columns, {IntArray(rows)})
    var cellsBuffer = Array(columns, {IntArray(rows)})

    override fun settings() {
        size(200, 200)
    }

    override fun setup() {
        stroke(48)
        background(0)

        for (x in 0..columns-1) {
            for (y in 0..rows-1) {
                var state = random(100f)
                cells[x][y] = if (state > probabilityOfAliveAtStart) 0 else 1
            }
        }
    }

    override fun draw() {
        for (x in 0..columns - 1) {
            for (y in 0..rows - 1) {
                fill(if (cells[x][y]==1) alive else dead)
                rect (x * cellSize, y * cellSize, cellSize, cellSize)
            }
        }

        // Iterate if timer ticks
        if (millis() - lastRecordedTime > interval) {
            iteration()
            lastRecordedTime = millis()
        }

    }

    private fun iteration() {
        // Save cells to buffer (so we opeate with one array keeping the other intact)
        for (x in 0..columns - 1) {
            for (y in 0..rows - 1) {
                cellsBuffer[x][y] = cells[x][y]
            }
        }

        // Visit each cell:
        for (x in 0..columns - 1) {
            for (y in 0..rows - 1) {
                processCell(x, y)
            }
        }

    }

    private fun processCell(x: Int, y: Int) {
        var neighbours = 0
        for (xx in -1..1) {
            val nx = wrap(x + xx, 0, columns-1)
            for (yy in -1..1) {
                val ny = wrap(y + yy, 0, rows-1)
                if (nx == x && ny == y) continue
                if (cellsBuffer[nx][ny] == 1) {
                    neighbours++ // Check alive neighbours and count them
                } // End of if
                // End of if
            } // End of yy loop
        } //End of xx loop

        // We've checked the neigbours: apply rules!
        if (cellsBuffer[x][y] == 1) { // The cell is alive: kill it if necessary
            if (neighbours < 2 || neighbours > 3) {
                cells[x][y] = 0 // Die unless it has 2 or 3 neighbours
            }
        } else { // The cell is dead: make it live if necessary
            if (neighbours == 3) {
                cells[x][y] = 1 // Only if it has 3 neighbours
            }
        } // End of if
    }

    private fun wrap(i: Int, min: Int, max: Int): Int {
        var out = i
        val span = max - min + 1
        while (out < min) out += span
        while (out > max) out -= span
        return out
    }

    fun runMain() {
        main(KotlinP5::class.jvmName)
    }
}