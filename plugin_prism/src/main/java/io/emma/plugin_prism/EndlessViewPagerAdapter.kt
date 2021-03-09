package io.emma.plugin_prism

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import android.util.DisplayMetrics
import io.emma.android.controllers.EMMAImageController

internal class EndlessViewPagerAdapter(
        private val clickContext: PrismDialogFragment,
        private val data: List<PrismSide>,
        private val metrics: DisplayMetrics?
) : RecyclerView.Adapter<EndlessViewPagerAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val backgroundImage: ImageView = view.findViewById(R.id.backgroundImage)
        private val buttonRight: ImageView = view.findViewById(R.id.buttonRight)
        private val buttonLeft: ImageView = view.findViewById(R.id.buttonLeft)
        private val buttonCta: ImageView =  view.findViewById(R.id.buttonCta)
        private val buttonClose: ImageView = view.findViewById(R.id.buttonClose)

        fun bind(prismSide: PrismSide) {
            applyMetrics(backgroundImage)

            EMMAImageController.loadImage(itemView.context, prismSide.image, backgroundImage)
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

    private fun applyMetrics(view: ImageView) {
        metrics?.let {
            val layoutParams = view.layoutParams
            layoutParams.height = it.heightPixels
            layoutParams.width = it.widthPixels
            view.layoutParams = layoutParams
        }
    }
}