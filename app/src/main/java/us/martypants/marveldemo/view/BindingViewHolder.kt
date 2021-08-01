package us.martypants.marvel.view

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BindingViewHolder<T : ViewDataBinding?> internal constructor(val binding: T) :
    RecyclerView.ViewHolder(
        binding!!.root
    )