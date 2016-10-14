package com.e_gueli

import rx.lang.kotlin.fold
import rx.lang.kotlin.observable

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
}