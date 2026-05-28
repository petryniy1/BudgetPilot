package com.petryniy1.budgetpilot.presentation.recyclers.moneyholder

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.petryniy1.budgetpilot.domain.models.MoneyHolder

class MoneyHolderAdapter(

    private val itemClickListenerMoneyHolder: MoneyHolderOnItemListener

) : RecyclerView.Adapter<MoneyHolderViewHolder>() {

    private var items: List<MoneyHolder?> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoneyHolderViewHolder =
        MoneyHolderViewHolder.fromParent(parent, itemClickListenerMoneyHolder)

    override fun onBindViewHolder(holder: MoneyHolderViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(data: List<MoneyHolder?>) {
        items = data
        notifyDataSetChanged()
    }
}