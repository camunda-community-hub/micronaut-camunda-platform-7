package info.novatec.micronaut.camunda.bpm.feature.test

import io.micronaut.context.annotation.Factory
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.mockito.Mockito.*
import javax.inject.Named
import javax.inject.Singleton

@Factory
class MyDelegateFactory {
    @Singleton
    @Named("myDelegate")
    fun myDelegate(): JavaDelegate {
        return mock(JavaDelegate::class.java)
    }
}