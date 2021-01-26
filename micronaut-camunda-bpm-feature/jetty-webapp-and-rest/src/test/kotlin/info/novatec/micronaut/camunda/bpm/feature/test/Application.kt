package info.novatec.micronaut.camunda.bpm.feature.test

import io.micronaut.runtime.Micronaut.*

fun main(args: Array<String>) {
    build()
        .args(*args)
        .packages("info.novatec.micronaut.camunda.bpm.feature.test")
        .start()
}
