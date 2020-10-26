package com.nanav.weather.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class SimpleAdapter<T, B : ViewBinding>(
    private val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> B,
    private val viewBind: (holder: SimpleAdapter<T, B>.SimpleViewHolder, item: T) -> Unit,
    private val clickListener: ((T) -> Unit)? = null
) : RecyclerView.Adapter<SimpleAdapter<T, B>.SimpleViewHolder>() {
    private val items = mutableListOf<T>()

    fun addItems(
        data: List<T>,
        idsTheSame: ((old: T, new: T) -> Boolean)? = null,
        contentTheSame: ((old: T, new: T) -> Boolean)? = null
    ) {
        changeContent(items + data, idsTheSame, contentTheSame)
    }

    private fun changeContent(
        data: List<T>,
        idsTheSame: ((old: T, new: T) -> Boolean)?,
        contentTheSame: ((old: T, new: T) -> Boolean)?
    ) {
        val idComparator = idsTheSame ?: { lhs, rhs -> lhs === rhs }
        val contentComparator = contentTheSame ?: { lhs, rhs -> lhs == rhs }

        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(old: Int, new: Int) = idComparator(items[old], data[new])

            override fun getOldListSize() = items.size

            override fun getNewListSize() = data.size

            override fun areContentsTheSame(old: Int, new: Int) =
                contentComparator(items[old], data[new])
        })

        items.clear()
        items.addAll(data)
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val binding = bindingInflater.invoke(LayoutInflater.from(parent.context), parent, false)
        return SimpleViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        viewBind(holder, items[position])
    }

    inner class SimpleViewHolder(val layout: B) : RecyclerView.ViewHolder(layout.root) {
        init {
            layout.root.setOnClickListener {
                clickListener?.invoke(items[adapterPosition])
            }
        }
    }
}