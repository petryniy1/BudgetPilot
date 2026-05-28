package com.petryniy1.budgetpilot.presentation.recyclers.operations

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.petryniy1.budgetpilot.domain.models.BaseItem
import com.petryniy1.budgetpilot.domain.models.HeadItem
import com.petryniy1.budgetpilot.domain.models.OperationWithMoneyHolder
import com.petryniy1.budgetpilot.presentation.recyclers.BaseViewHolder
import com.petryniy1.budgetpilot.presentation.recyclers.HeadViewHolder

class OperationsAdapter(

    private val itemClickListenerOperations: OperationsOnItemListener

) : RecyclerView.Adapter<BaseViewHolder>() {

    private var items: List<BaseItem?> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        if (viewType == HeadViewHolder.VIEW_TYPE_HEAD) HeadViewHolder.createViewHolder(parent)
        else OperationsViewHolder.fromParent(parent, itemClickListenerOperations)


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindViewHolder(items[position])
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is HeadItem -> HeadViewHolder.VIEW_TYPE_HEAD
            is OperationWithMoneyHolder -> OperationsViewHolder.VIEW_TYPE_OPERATION
            else -> {0}
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(data: List<BaseItem?>) {
        items = data
        notifyDataSetChanged()
    }
}