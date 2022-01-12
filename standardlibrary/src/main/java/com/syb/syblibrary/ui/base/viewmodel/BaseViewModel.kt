package com.syb.syblibrary.ui.base.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.syb.syblibrary.ui.base.view.BaseView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign


@Suppress("ConvertSecondaryConstructorToPrimary")
@SuppressLint("StaticFieldLeak")
abstract class BaseViewModel<V: BaseView> : AndroidViewModel {

    protected val context: Context

    constructor(application: Application) : super(application) {
        this.context = application
    }

    private var view: V? = null

    private val disposables = CompositeDisposable()

    fun onAttach(baseView: V)
    {
        view = baseView
    }

    fun onDetach()
    {
        disposables.dispose()
        view = null
    }

    fun getView(): V? = view

    fun addDisposable(disposable: Disposable) {
        disposables += disposable
    }

    override fun onCleared() {
        disposables.dispose()
    }

}