package com.e_gueli

import rx.Observable
import rx.Subscriber
import rx.lang.kotlin.fold
import rx.lang.kotlin.observable
import java.util.*

fun main(args: Array<String>) {
    observable<String> { subscriber ->
        subscriber.onNext("H")
        subscriber.onNext("e")
        subscriber.onNext("l")
        subscriber.onNext("")
        subscriber.onNext("l")
        subscriber.onNext("o")
        subscriber.onCompleted()
    }.filter { it.isNotEmpty() }
            .fold (StringBuilder()) { sb, e -> sb.append(e) }
            .map { it.toString() }
            .subscribe { println(it) }

    Observable.create(Observable.OnSubscribe<Int> { subscriber ->
        for (i in 1..5)
            subscriber.onNext(i)

        subscriber.onCompleted()
    }).subscribe( ::println )
}