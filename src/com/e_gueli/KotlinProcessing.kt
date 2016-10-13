package com.e_gueli

import processing.core.PApplet
import kotlin.reflect.jvm.jvmName

const val columns = 8
const val rows = 8

class KotlinP5 : PApplet() {

    var emptyColumn = IntArray(rows)
    var cells = Array(columns, {emptyColumn})
    var cellsBuffer = Array(columns, {emptyColumn})

    override fun settings() {
        size(200, 200)
    }

    override fun setup() {
        stroke(48)
    }

    override fun draw() {
        background(0)
        ellipse(mouseX.toFloat(), mouseY.toFloat(), 20f, 20f)
    }
    fun runMain() {
        main(KotlinP5::class.jvmName)
    }
}