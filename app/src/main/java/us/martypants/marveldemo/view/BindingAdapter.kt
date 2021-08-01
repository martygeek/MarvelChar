package us.martypants.marvel.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BindingAdapter<T : ViewDataBinding?> : RecyclerView.Adapter<BindingViewHolder<T>> {
    private val mLayoutResourceIds: IntArray

    constructor(layoutResourceId: Int) {
        mLayoutResourceIds = IntArray(1)
        mLayoutResourceIds[0] = layoutResourceId
    }

    constructor(resourceIds: IntArray) {
        mLayoutResourceIds = resourceIds
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<T> {
        val binding = createBindingForViewType(parent, viewType)
        return BindingViewHolder(binding)
    }

    private fun createBindingForViewType(parent: ViewGroup, viewType: Int): T {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            mLayoutResourceIds[viewType],
            parent,
            false
        )
    }

    override fun onBindViewHolder(holder: BindingViewHolder<T>, position: Int) {
        val binding = holder.binding
        updateBinding(binding, position)
    }

    protected abstract fun updateBinding(binding: T, position: Int)
}