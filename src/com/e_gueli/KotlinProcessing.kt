package com.e_gueli

import processing.core.PApplet
import kotlin.reflect.jvm.jvmName

class KotlinP5 : PApplet() {
    override fun settings() {
        size(200, 200)
    }
    override fun draw() {
        background(0)
        ellipse(mouseX.toFloat(), mouseY.toFloat(), 20f, 20f)
    }
    fun runMain() {
        main(KotlinP5::class.jvmName)
    }
}