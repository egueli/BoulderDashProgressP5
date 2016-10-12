How I did it
============

1. Download IntelliJ
2. Create new Java project
3. Create lib directory
4. copy core.jar there
5. Right-click lib directory, "Add as library..."
6. Copy-paste the example code:

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

7. Tools > Kotlin > Configure Kotlin in project
8. Add the runner class:

    fun main(args: Array<String>) {
        KotlinP5().runMain()
    }
    
9. Start and enjoy :)