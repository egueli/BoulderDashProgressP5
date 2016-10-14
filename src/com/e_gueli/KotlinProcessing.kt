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
                if (state > probabilityOfAliveAtStart) {
                    cells[x][y] = 0
                }
                else {
                    cells[x][y] = 1
                }
            }
        }
    }

    override fun draw() {
        for (x in IntRange(0, columns - 1)) {
            for (y in IntRange(0, rows - 1)) {
                if (cells[x][y]==1) {
                    fill(alive)
                }
                else {
                    fill(dead)
                }
                rect (x * cellSize, y * cellSize, cellSize, cellSize)
            }
        }
    }

    fun runMain() {
        main(KotlinP5::class.jvmName)
    }
}