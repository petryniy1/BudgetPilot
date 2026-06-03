package com.petryniy1.budgetpilot.presentation.recyclers.operations

import android.view.LayoutInflater
import android.view.ViewGroup
import com.petryniy1.budgetpilot.R
import com.petryniy1.budgetpilot.databinding.ItemOperationBinding
import com.petryniy1.budgetpilot.domain.models.BaseItem
import com.petryniy1.budgetpilot.domain.models.OperationWithMoneyHolder
import com.petryniy1.budgetpilot.presentation.recyclers.BaseViewHolder

class OperationsViewHolder(

    private val binding: ItemOperationBinding,
    private val itemClickListenerOperations: OperationsOnItemListener

) : BaseViewHolder(binding.root) {

    companion object {

        const val VIEW_TYPE_OPERATION = 2

        fun fromParent(
            parent: ViewGroup,
            itemClickListenerOperations: OperationsOnItemListener
        ):
                OperationsViewHolder {
            return OperationsViewHolder(
                ItemOperationBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                ),
                itemClickListenerOperations
            )
        }
    }

    override fun bindViewHolder(item: BaseItem?) {
        val operationItem = item as OperationWithMoneyHolder
        with(binding) {

            itemCategory.text = operationItem.operationEntity.category
            itemTypeOfValue.text = operationItem.accountEntity.name
            itemValue.text = root.context.getString(
                R.string.msg_currency_byn_amount_format,
                item.operationEntity.value / 100f
            )
            itemImageId.setImageResource(operationItem.operationEntity.categoryDrawable)

            itemOperation.setOnClickListener {
                item.operationEntity.id.let { it1 ->
                    itemClickListenerOperations.onItemClickListener(it1)
                }
            }
        }
    }
}