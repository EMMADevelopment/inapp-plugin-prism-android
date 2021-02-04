package io.emma.plugin_prism

import android.graphics.Color
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.emma.android.controllers.EMMAImageController
import io.emma.android.interfaces.EMMALoadImageInterface


class EMMAInappPrismFragment: Fragment(){
    companion object {
        private const val DURATION: Long = 500
        private const val DIRECTION_ARG = "direction"

        fun newInstance(): EMMAInappPrismFragment {
            val prismFragment = EMMAInappPrismFragment()
            prismFragment.arguments = Bundle()
            Log.d("DEBUG","paso por constructor")
            return prismFragment
        }
    }


    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_prism_side, container, false)
        val background = view.findViewById<ImageView>(R.id.background)
        background.setBackgroundResource(android.R.color.white)
        //EMMAImageController.loadImage(activity, "https://loremflickr.com/350/750", background)

        Glide.with(activity!!)
                .load("https://loremflickr.com/370/750")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .skipMemoryCache(true)
                .into(background);

        //view.findViewById<ImageView>(R.id.buttonLeft).setOnClickListener(this)
        //view.findViewById<ImageView>(R.id.buttonRight).setOnClickListener(this)

        return view
    }

    /*override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        val direction = arguments?.getSerializable(DIRECTION_ARG) as? Direction
        when (direction) {
            Direction.UP -> return CubeAnimation.create(
                Direction.UP,
                enter,
                DURATION
            )
            Direction.DOWN -> return CubeAnimation.create(
                Direction.DOWN,
                enter,
                DURATION
            )
            Direction.LEFT -> return CubeAnimation.create(
                Direction.LEFT,
                enter,
                DURATION
            )
            Direction.RIGHT -> return CubeAnimation.create(
                Direction.RIGHT,
                enter,
                DURATION
            )
            else -> return null
        }
    }

    private fun buttonPressed(direction: Direction) {
        arguments!!.putSerializable(DIRECTION_ARG, direction)
        val ft: FragmentTransaction = fragmentManager!!.beginTransaction()
        //ft.replace(R.id.layout_main, newInstance(direction))
        ft.commit()
    }

    override fun onClick(view: View?) {
        print(view)
       when(view?.id) {
           R.id.buttonLeft -> buttonPressed(Direction.LEFT)
           R.id.buttonRight -> buttonPressed(Direction.RIGHT)
           R.id.buttonCta -> print("click CTA")
       }
    }*/
}