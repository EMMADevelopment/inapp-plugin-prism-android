package io.emma.plugin_prism

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.DialogFragment
import androidx.viewpager2.widget.ViewPager2
import io.emma.android.activities.EMMAWebViewActivity
import io.emma.android.controllers.EMMADeepLinkController
import io.emma.android.model.EMMACampaign
import io.emma.android.plugins.EMMAInAppPlugin
import io.emma.android.utils.EMMALog


internal class PrismDialogFragment: DialogFragment(), View.OnClickListener {
    private lateinit var pager: ViewPager2
    private lateinit var prism: Prism

    enum class PagerSelectionType {
        MANUAL,
        AUTOMATIC
    }

    enum class PagerDirection {
        LEFT,
        RIGHT,
    }

    companion object {
        const val TAG = "emma_inapp_prism_dialog"
    }

    fun addPrism(value: Prism) {
        prism = value
    }

    private inner class CubeTransformer : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {
            val deltaY = 0.5F

            view.pivotX = if (position < 0F) view.width.toFloat() else 0F
            view.pivotY = view.height * deltaY
            view.rotationY = 45F * position
        }
    }

    @Suppress("Deprecation")
    private fun getMetrics(): DisplayMetrics? {
        val metrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val display = activity?.display
            display?.getRealMetrics(metrics)
        } else {
            val display = activity?.windowManager?.defaultDisplay
            display?.getMetrics(metrics)
        }

        if (metrics.heightPixels > 0 && metrics.widthPixels > 0) {
            metrics.heightPixels = (metrics.heightPixels * 0.6).toInt()
            metrics.widthPixels = (metrics.widthPixels * 0.7).toInt()
            return metrics
        }
        return null
    }

    private fun resizeView() {
        val metrics = getMetrics()
        metrics?.let {
            val layoutParams = pager.layoutParams
            layoutParams.height = metrics.heightPixels
            layoutParams.width = metrics.widthPixels
            pager.layoutParams = layoutParams
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_fragment_view, container, false)
        pager = view.findViewById(R.id.pager)
        resizeView()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(0))
        return view
    }

    private fun nextPage(selectionType: PagerSelectionType, direction: PagerDirection? = null) {
        if (pager.currentItem == 0) {
            pager.setCurrentItem(prism.sides.size - 2, false)
        } else if (pager.currentItem == prism.sides.size - 1) {
            pager.setCurrentItem(1, false)
        } else if(selectionType == PagerSelectionType.MANUAL && direction != null) {
            val nextPage =  if (direction == PagerDirection.RIGHT) pager.currentItem + 1 else pager.currentItem - 1
            pager.setCurrentItem(nextPage, 300)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pager.apply {
            adapter = EndlessViewPagerAdapter(
                this@PrismDialogFragment,
                prism.sides,
                getMetrics()
            )
            setCurrentItem(1, false)
            offscreenPageLimit = 1
            setPageTransformer(CubeTransformer())
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    if (state == ViewPager2.SCROLL_STATE_IDLE || state == ViewPager2.SCROLL_STATE_DRAGGING) {
                        nextPage(PagerSelectionType.AUTOMATIC)
                    }
                }
            })
        }
        EMMAInAppPlugin.sendImpression(prism.campaign)
        EMMAInAppPlugin.invokeShownListeners(prism.campaign)
    }

    private fun openBrowserInApp(cta: String) {
        try {
            val campaign = EMMACampaign(EMMACampaign.Type.NATIVEAD)
            campaign.campaignUrl = cta
            val intent = EMMAWebViewActivity.makeIntent(activity, campaign, true)
            activity?.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            EMMALog.e(e)
        }
    }

    private fun openBrowserOutApp(cta: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(cta)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        } catch (e: ActivityNotFoundException) {
            EMMALog.e(e)
            openBrowserInApp(cta)
        }
    }

    private fun ctaAction(cta: String?) {
        cta?.let {
            if (!cta.startsWith("http://") && !cta.startsWith("https://")) {
                val deepLinkController = EMMADeepLinkController(activity)
                deepLinkController.execute(cta)
                dismissAllowingStateLoss()
                return
            }

            if (prism.openInApp) {
                openBrowserInApp(cta)
            } else {
                openBrowserOutApp(cta)
            }

           EMMAInAppPlugin.sendInAppClick(prism.campaign)
        }
    }

    private fun close() {
        EMMAInAppPlugin.invokeCloseListener(prism.campaign)
        dismissAllowingStateLoss()
        activity?.let {
            if (it.isFinishing ||
                    (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && it.isDestroyed)) {
                EMMALog.d("Cannot finish the activity it is finished or destroyed")
                return
            }
            it.finish()
        }
    }

    override fun onClick(view: View?) {
        view?.let {
            when(it.id) {
                R.id.buttonLeft -> nextPage(PagerSelectionType.MANUAL, PagerDirection.LEFT)
                R.id.buttonRight -> nextPage(PagerSelectionType.MANUAL, PagerDirection.RIGHT)
                R.id.buttonCta -> ctaAction(view.tag as? String)
                R.id.buttonClose -> close()
            }
        }
    }

    private fun ViewPager2.setCurrentItem(item: Int, duration: Long) {
        val pxToDrag: Int = width * (item - currentItem)
        val animator = ValueAnimator.ofInt(0, pxToDrag)
        var previousValue = 0
        animator.addUpdateListener { valueAnimator ->
            val currentValue = valueAnimator.animatedValue as Int
            val currentPxToDrag = (currentValue - previousValue).toFloat()
            fakeDragBy(-currentPxToDrag)
            previousValue = currentValue
        }
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) { beginFakeDrag() }
            override fun onAnimationEnd(animation: Animator?) { endFakeDrag() }
            override fun onAnimationCancel(animation: Animator?) { /* Ignored */ }
            override fun onAnimationRepeat(animation: Animator?) { /* Ignored */ }
        })
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.duration = duration
        animator.start()
    }
}