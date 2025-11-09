package org.app.common

import io.quarkus.vertx.VertxContextSupport
import io.smallrye.mutiny.Uni
import java.util.function.Supplier

fun <T> execute(uniSupplier: Supplier<Uni<T>>): T {
    return uniSupplier.get().await().indefinitely()
}

fun <T> executeOnVertex(uniSupplier: Supplier<Uni<T>>): T {
    return VertxContextSupport.subscribeAndAwait(uniSupplier)
}

fun fail(uniSupplier: Supplier<Uni<*>>): Throwable {
    return uniSupplier.get().onItemOrFailure().transformToUni { i, t ->
        if (t == null) {
            Uni.createFrom().failure<RuntimeException>(RuntimeException("Uni did not contain a failure."))
        }
        Uni.createFrom().item(t)
    }.await().indefinitely()
}

fun failOnVertex(uniSupplier: Supplier<Uni<*>>): Throwable {
    return VertxContextSupport.subscribeAndAwait {
        uniSupplier.get().onItemOrFailure().transformToUni { i, t ->
            if (t == null) {
                Uni.createFrom().failure<RuntimeException>(RuntimeException("Uni did not contain a failure."))
            }
            Uni.createFrom().item(t)
        }
    }
}