package com.emplk.realestatemanager.ui.utils

class EquatableCallback(private val callback: () -> Unit) {

    operator fun invoke() {
        callback.invoke()
    }

    override fun equals(other: Any?): Boolean = if (other is EquatableCallback) {
        true
    } else {
        super.equals(other)
    }

    override fun hashCode(): Int = 2051656923
}

class EquatableCallbackWithParam<T>(private val callback: (T) -> Unit) {

    operator fun invoke(data: T) {
        callback.invoke(data)
    }

    override fun equals(other: Any?): Boolean = if (other is EquatableCallbackWithParam<*>) {
        true
    } else {
        super.equals(other)
    }

    override fun hashCode(): Int = 1211656923
}

class EquatableCallbackWithParams<A, B>(private val callback: (A, B) -> Unit) {

    operator fun invoke(a: A, b: B) {
        callback.invoke(a, b)
    }

    override fun equals(other: Any?): Boolean = if (other is EquatableCallbackWithParams<*, *>) {
        true
    } else {
        super.equals(other)
    }

    override fun hashCode(): Int = 264586923
}
