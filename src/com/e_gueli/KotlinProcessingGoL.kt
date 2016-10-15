package com.e_gueli

import processing.core.PApplet
import rx.Observable
import java.util.concurrent.TimeUnit
import kotlin.reflect.jvm.jvmName

const val columns = 40
const val rows = 40
const val probabilityOfAliveAtStart = 15
const val cellSize:Float = 5f

class KotlinProcessingGameOfLife : PApplet() {

    internal var alive = color(0, 200, 0)
    internal var dead = color(0)

    // Variables for timer
    internal var interval:Long = 100


    var currentState = Array(columns, {IntArray(rows)})

    override fun settings() {
        size(200, 200)
    }

    override fun setup() {
        stroke(48)

        for (x in 0..columns - 1) {
            for (y in 0..rows - 1) {
                val state = random(100f)
                currentState[x][y] = if (state > probabilityOfAliveAtStart) 0 else 1
            }
        }

        noLoop()
        Observable
                .interval(interval, TimeUnit.MILLISECONDS)
                .subscribe {
                    currentState = doGoLStep(currentState)
                    redraw()
                }

    }

    override fun draw() {
        for (x in 0..columns - 1) {
            for (y in 0..rows - 1) {
                fill(if (currentState[x][y]==1) alive else dead)
                rect (x * cellSize, y * cellSize, cellSize, cellSize)
            }
        }
    }

    private fun doGoLStep(previousState : Array<IntArray>): Array<IntArray> {
        val nextState = Array(columns, {IntArray(rows)})
        for (x in 0..columns - 1) {
            for (y in 0..rows - 1) {
                nextState[x][y] = previousState[x][y]
            }
        }

        // Visit each cell:
        for (x in 0..columns - 1) {
            for (y in 0..rows - 1) {
                processCell(previousState, nextState, x, y)
            }
        }

        return nextState
    }

    private fun processCell(currentState: Array<IntArray>, nextState: Array<IntArray>, x: Int, y: Int) {
        var neighbours = 0
        for (xx in -1..1) {
            val nx = wrap(x + xx, 0, columns-1)
            for (yy in -1..1) {
                val ny = wrap(y + yy, 0, rows-1)
                if (nx == x && ny == y) continue
                if (currentState[nx][ny] == 1) {
                    neighbours++ // Check alive neighbours and count them
                } // End of if
                // End of if
            } // End of yy loop
        } //End of xx loop

        // We've checked the neigbours: apply rules!
        if (currentState[x][y] == 1) { // The cell is alive: kill it if necessary
            if (neighbours < 2 || neighbours > 3) {
                nextState[x][y] = 0 // Die unless it has 2 or 3 neighbours
            }
        } else { // The cell is dead: make it live if necessary
            if (neighbours == 3) {
                nextState[x][y] = 1 // Only if it has 3 neighbours
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
        main(KotlinProcessingGameOfLife::class.jvmName)
    }
}