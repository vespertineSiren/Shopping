package com.example.shopapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopapp.R
import com.example.shopapp.extensions.centsToDollarsFormat
import com.example.shopapp.models.product.Product

class ProductListAdapter(
    personList: MutableList<Product> = mutableListOf(),
    onItemClicked: ((Product) -> Unit)? = null
) : BaseRecyclerAdapter<Product>(personList, onItemClicked) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_list, parent, false)
        )

    class ViewHolder(view: View) : BaseViewHolder<Product>(view) {

        private val textProductName: AppCompatTextView =
            view.findViewById(R.id.rowProductName)

        private val textProductBrand: AppCompatTextView =
            view.findViewById(R.id.rowProductBrand)

        private val textProductShippingPrice: AppCompatTextView =
            view.findViewById(R.id.rowProductShippingPrice)

        private val textProductStyle: AppCompatTextView =
            view.findViewById(R.id.rowProductStyle)

        private val buttonProductDesc: Button =
            view.findViewById(R.id.rowProductDesc)

        private val imageProduct: ImageView =
            view.findViewById(R.id.rowProductImage)

        override fun onBind(data: Product, listener: ((Product) -> Unit)?) {

            Glide.with(imageProduct.context)
                .load("https://picsum.photos/100?random=${data.id}")
                .fitCenter()
                .into(imageProduct)

            textProductName.text = view.context.getString(
                R.string.row_product_name,
                data.productName
            )

            textProductBrand.text = view.context.getString(
                R.string.row_product_brand,
                data.brand
            )

            textProductShippingPrice.text = view.context.getString(
                R.string.row_product_shipping_price,
                data.shippingPrice.centsToDollarsFormat()
            )

            textProductStyle.text = view.context.getString(
                R.string.row_product_style,
                data.style
            )

            buttonProductDesc.setOnClickListener {
                listener?.invoke(data)
            }
        }
    }

    //  TODO: Implement Diffutil in future, instead of expensive notifyDataSetChanged()
    fun setProducts(data: List<Product>) {
        if (masterList.size > 0) masterList.clear()
        masterList.addAll(data)
        notifyDataSetChanged()
    }
}
