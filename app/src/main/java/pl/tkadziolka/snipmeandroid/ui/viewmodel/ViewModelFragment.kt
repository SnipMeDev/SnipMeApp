package pl.tkadziolka.snipmeandroid.ui.viewmodel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import kotlin.reflect.KClass

abstract class ViewModelFragment<T : ViewModel>(private val clazz: KClass<T>) : Fragment() {
    protected lateinit var viewModel: T

    abstract val layout: Int

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(layout, container, false)

    protected abstract fun onViewCreated()
    protected abstract fun observeViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = getViewModel(null, clazz)
        observeViewModel()
        onViewCreated()
    }
}

fun <T> LiveData<T>.observeNotNull(owner: LifecycleOwner, callback: (T) -> Unit) {
    observe(owner, Observer { callback(it!!) })
}