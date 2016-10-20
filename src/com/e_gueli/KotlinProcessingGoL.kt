package com.e_gueli

import processing.core.PApplet
import rx.Observable
import rx.Scheduler
import rx.Subscriber
import rx.Subscription
import rx.functions.Action0
import rx.lang.kotlin.observable
import java.util.*
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

    data class State(val cells: Array<IntArray> = Array(columns, {IntArray(rows)}))

    var stateToDraw = State()

    val DrawEvents: Observable<Long> = Observable.create(DrawOnSubscribe)

    override fun settings() {
        size(200, 200)
    }

    override fun setup() {
        stroke(48)
        var currentState = State()
        initialize(currentState)

        DrawEvents.subscribe {
            currentState = doGoLStep(currentState)
            stateToDraw = currentState
        }

        DrawEvents.subscribe {
            for (x in 0..columns - 1) {
                for (y in 0..rows - 1) {
                    fill(if (stateToDraw.cells[x][y]==1) alive else dead)
                    rect (x * cellSize, y * cellSize, cellSize, cellSize)
                }
            }
        }
    }

    private fun initialize(currentState: State) {
        for (x in 0..columns - 1) {
            for (y in 0..rows - 1) {
                val state = random(100f)
                currentState.cells[x][y] = if (state > probabilityOfAliveAtStart) 0 else 1
            }
        }
    }

    override fun draw() {
        val millis = millis().toLong()
        DrawScheduler.onDraw(millis)
        DrawOnSubscribe.onDraw(millis)
    }

    private fun doGoLStep(previousState : State): State {
        val nextState = State()
        for (x in 0..columns - 1) {
            for (y in 0..rows - 1) {
                nextState.cells[x][y] = previousState.cells[x][y]
            }
        }

        // Visit each cell:
        for (x in 0..columns - 1) {
            for (y in 0..rows - 1) {
                processCell(previousState.cells, nextState.cells, x, y)
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

object DrawScheduler : Scheduler() {
    val worker = DrawWorker()
    override fun createWorker(): Worker {
        return worker
    }

    fun onDraw(currentTime: Long) {
        println("onDraw($currentTime)")
        worker.runEventsAt(currentTime)
    }
}

class DrawWorker : Scheduler.Worker() {

    var unsubscribed = false
    var events: TreeMap<Long, MutableList<Action0>> = TreeMap()
    var currentTime: Long = 0

    override fun schedule(p0: Action0?): Subscription {
        schedule(p0, 0, TimeUnit.MILLISECONDS)
        return this
    }

    override fun schedule(p0: Action0?, p1: Long, p2: TimeUnit?): Subscription {
        if (p2 != TimeUnit.MILLISECONDS)
            throw UnsupportedOperationException("TODO")

        val time = currentTime + p1
        if (p0 != null) {
            val actionsNow: MutableList<Action0>
            if (events.containsKey(time)) {
                actionsNow = events[time]!!
            }
            else {
                actionsNow = ArrayList()
                events.put(time, actionsNow)
            }
            actionsNow.add(p0)
            println("scheduled action $p0 at $time")
        }
        return this
    }

    override fun isUnsubscribed(): Boolean {
        return unsubscribed
    }

    override fun unsubscribe() {
        unsubscribed = true
    }

    fun runEventsAt(time: Long) {
        val pastOrNow = events.keys.filter { it <= time }
        for (t in pastOrNow) {
            println("time = $time, running ${events[t]!!.size} scheduled at time $t")
            events[t]!!.forEach(Action0::call)
            events.remove(t)
        }
        currentTime = time
    }

}

object DrawOnSubscribe : Observable.OnSubscribe<Long> {
    val subscribers = ArrayList<Subscriber<in Long>>()
    override fun call(p0: Subscriber<in Long>?) {
        if (p0 != null)
            subscribers.add(p0)
    }

    fun onDraw(millis: Long) {
        subscribers.forEach { it.onNext(millis) }
    }
}
