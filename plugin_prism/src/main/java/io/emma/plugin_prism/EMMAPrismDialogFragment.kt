package io.emma.plugin_prism

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewpager2.widget.ViewPager2
import io.emma.android.activities.EMMAWebViewActivity
import io.emma.android.controllers.EMMADeepLinkController
import io.emma.android.model.EMMACampaign
import io.emma.android.utils.EMMALog


class EMMAPrismDialogFragment: DialogFragment(), View.OnClickListener {
    private lateinit var pager: ViewPager2
    private lateinit var prism: EMMAPrism

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

    fun addPrism(value: EMMAPrism) {
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
            return metrics
        }
        return null
    }

    private fun resizeView() {
        val metrics = getMetrics()
        metrics?.let {
            val layoutParams = pager.layoutParams
            layoutParams.height = (metrics.heightPixels * 0.6).toInt()
            layoutParams.width = (metrics.widthPixels * 0.7).toInt()
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
            pager.setCurrentItem(nextPage, true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pager.apply {
            adapter = EMMAEndlessViewPagerAdapter(
                context,
                this@EMMAPrismDialogFragment,
                prism.sides
            )
            setCurrentItem(1, false)
            setPageTransformer(CubeTransformer())
            offscreenPageLimit = 1
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    if (state == ViewPager2.SCROLL_STATE_IDLE || state == ViewPager2.SCROLL_STATE_DRAGGING) {
                        nextPage(PagerSelectionType.AUTOMATIC)
                    }
                }
            })
        }
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
                dismiss()
                return
            }

            if (prism.openInApp) {
                openBrowserInApp(cta)
            } else {
                openBrowserOutApp(cta)
            }
        }
    }

    override fun onClick(view: View?) {
        view?.let {
            when(it.id) {
                R.id.buttonLeft -> nextPage(PagerSelectionType.MANUAL, PagerDirection.LEFT)
                R.id.buttonRight -> nextPage(PagerSelectionType.MANUAL, PagerDirection.RIGHT)
                R.id.buttonCta -> ctaAction(view.tag as? String)
            }
        }
    }
}