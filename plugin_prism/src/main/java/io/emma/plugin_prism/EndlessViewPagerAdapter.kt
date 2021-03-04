package io.emma.plugin_prism

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

import io.emma.android.controllers.EMMAImageController

internal class EndlessViewPagerAdapter(
        private val context: Context,
        private val clickContext: PrismDialogFragment,
        private val data: List<PrismSide>,
) : RecyclerView.Adapter<EndlessViewPagerAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val backgroundImage: ImageView = view.findViewById(R.id.backgroundImage)
        private val buttonRight: ImageView = view.findViewById(R.id.buttonRight)
        private val buttonLeft: ImageView = view.findViewById(R.id.buttonLeft)
        private val buttonCta: ImageView =  view.findViewById(R.id.buttonCta)
        private val buttonClose: ImageView = view.findViewById(R.id.buttonClose)

        fun bind(prismSide: PrismSide) {
            EMMAImageController.loadImage(context, prismSide.image, backgroundImage)
            buttonCta.setOnClickListener(clickContext)
            buttonClose.setOnClickListener(clickContext)
            buttonCta.tag = prismSide.cta

            if (data.size > 1) {
                buttonLeft.setOnClickListener(clickContext)
                buttonRight.setOnClickListener(clickContext)
                buttonLeft.visibility = View.VISIBLE
                buttonRight.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.adapter_prism_side, parent, false))
    }
}