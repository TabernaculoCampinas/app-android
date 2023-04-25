package br.org.tabernaculocampinas.extension

import android.os.Binder

data class LocalBinder<T>(
    val service: T
) : Binder()