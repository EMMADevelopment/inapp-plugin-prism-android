package io.emma.plugin_prism

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import io.emma.android.controllers.EMMAImageController

class EMMAEndlessViewPagerAdapter(
        private val context: Context,
        private val clickContext: EMMAPrismDialogFragment,
        private val data: List<EMMAPrismSide>,
) : RecyclerView.Adapter<EMMAEndlessViewPagerAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val backgroundImage: ImageView = view.findViewById(R.id.backgroundImage)
        private val buttonRight: ImageView = view.findViewById(R.id.buttonRight)
        private val buttonLeft: ImageView = view.findViewById(R.id.buttonLeft)
        private val buttonCta: ImageView =  view.findViewById(R.id.buttonCta)
        private val buttonClose: Button = view.findViewById(R.id.buttonClose)

        // temporal text to see the position on recyclerview
        private val textView: TextView =  view.findViewById(R.id.text)

        fun bind(prismSide: EMMAPrismSide) {
            EMMAImageController.loadImage(context, prismSide.image, backgroundImage)
            buttonLeft.setOnClickListener(clickContext)
            buttonRight.setOnClickListener(clickContext)
            buttonCta.setOnClickListener(clickContext)
            buttonClose.setOnClickListener(clickContext)
            buttonCta.tag = prismSide.cta
            // temporal test assignation
            textView.text = "${prismSide.pos}"
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.adapter_prism_side, parent, false))
    }
}