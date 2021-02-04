package io.emma.plugin_prism

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Insets
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager

class EMMAInappPrismActivity: FragmentActivity()  {

    private var dialogFragment : EMMAPrismDialogFragment? = null

    companion object {
        fun makeIntent(context: Activity): Intent {
            return Intent(context, EMMAInappPrismActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emma_prism)
        dialogFragment = EMMAPrismDialogFragment().apply {
            addFragments(createFragments())
            isCancelable = false
        }

        dialogFragment?.show(supportFragmentManager, "emma_inapp_prism_dialog")
    }

    private fun createFragments(): List<EMMAInappPrismFragment> {
        val fragments = mutableListOf<EMMAInappPrismFragment>()
        fragments.add(EMMAInappPrismFragment.newInstance())
        fragments.add(EMMAInappPrismFragment.newInstance())
        fragments.add(EMMAInappPrismFragment.newInstance())
        fragments.add(EMMAInappPrismFragment.newInstance())
        fragments.add(EMMAInappPrismFragment.newInstance())
        return fragments
    }

    private fun getScreenHeight(window: Window): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = window.windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            val insetsHeight = insets.top + insets.bottom
            return windowMetrics.bounds.height() - insetsHeight
        } else {
            val displayMetrics = DisplayMetrics()
            window.windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.heightPixels
        }
    }

    /*private inner class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int = 5

        override fun getItem(position: Int): Fragment {
            return EMMAInappPrismFragment.newInstance(null)
        }
    }

   private inner class CubeTransformer : ViewPager.PageTransformer {
        override fun transformPage(view: View, position: Float) {
            val deltaY = 0.5F

            view.pivotX = if (position < 0F) view.width.toFloat() else 0F
            view.pivotY = view.height * deltaY
            view.rotationY = 45F * position
        }
    }*/
}