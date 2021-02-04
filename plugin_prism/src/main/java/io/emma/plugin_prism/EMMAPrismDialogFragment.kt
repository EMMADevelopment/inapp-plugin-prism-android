package io.emma.plugin_prism

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager


class EMMAPrismDialogFragment: DialogFragment(), ViewPager.OnPageChangeListener {
    private var currentPage : Int = 0
    private var mPreviousPosition : Int = 0
    private var mIsEndOfCycle = false

    private lateinit var pagerAdapter : ScreenSlidePagerAdapter
    private lateinit var pager: ViewPager
    private val fragmentList = mutableListOf<Fragment>()

    fun clearFragments() = fragmentList.clear()
    fun addFragment(fragment: Fragment) = fragmentList.add(fragment)
    fun addFragments(fragments: List<Fragment>) =fragmentList.addAll(fragments)

    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment = fragmentList[position % fragmentList.size]
        override fun getCount(): Int = fragmentList.size
    }

    private inner class CubeTransformer : ViewPager.PageTransformer {
        override fun transformPage(view: View, position: Float) {
            val deltaY = 0.5F

            view.pivotX = if (position < 0F) view.width.toFloat() else 0F
            view.pivotY = view.height * deltaY
            view.rotationY = 45F * position
        }
    }

    private fun resizeView() {
        val height =  dialog?.window?.windowManager?.defaultDisplay?.height
        val width =  dialog?.window?.windowManager?.defaultDisplay?.width
        height?.let {
            val layoutParams = pager.layoutParams
            layoutParams.height = (height * 0.7).toInt()
            width?.let {
                layoutParams.width = (width * 0.7).toInt()
            }
            pager.layoutParams = layoutParams
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.emma_prism_content_main, container, false)
        pager = view.findViewById(R.id.pager)
        resizeView()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(0))
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagerAdapter = ScreenSlidePagerAdapter(childFragmentManager)
        pager.apply {
            adapter = pagerAdapter
            currentItem = 0
            setPageTransformer(true, CubeTransformer())
        }
    }

    fun selectPage(position : Int){
        pager.currentItem = position
    }

    override fun onPageScrollStateChanged(state: Int) {
        val pageCount = pager.adapter?.count
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            if (mPreviousPosition == currentPage && !mIsEndOfCycle) {
                if (currentPage == 0) {
                    pageCount?.minus(1)?.let { pager.setCurrentItem(it, true) }
                } else {
                    pager.setCurrentItem(0, true)
                }
                mIsEndOfCycle = true
            } else {
                mIsEndOfCycle = false
            }
            mPreviousPosition = currentPage
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        currentPage = position
    }
}