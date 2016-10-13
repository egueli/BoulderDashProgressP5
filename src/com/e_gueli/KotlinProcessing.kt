package com.e_gueli

import processing.core.PApplet
import kotlin.reflect.jvm.jvmName

const val columns = 8
const val rows = 8
const val probabilityOfAliveAtStart = 15

class KotlinP5 : PApplet() {

    var emptyColumn = IntArray(rows)
    var cells = Array(columns, {emptyColumn})
    var cellsBuffer = Array(columns, {emptyColumn})

    override fun settings() {
        size(200, 200)
    }

    override fun setup() {
        stroke(48)
        background(0)

        for (x in IntRange(0, columns - 1)) {
            for (y in IntRange(0, rows - 1)) {
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

    }

    fun runMain() {
        main(KotlinP5::class.jvmName)
    }
}