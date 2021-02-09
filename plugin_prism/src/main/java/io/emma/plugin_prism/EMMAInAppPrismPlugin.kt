package io.emma.plugin_prism

import android.app.Activity
import io.emma.android.EMMA
import io.emma.android.model.EMMANativeAd
import io.emma.android.model.EMMANativeAdField
import io.emma.android.plugins.EMMAInAppPlugin
import io.emma.android.utils.EMMALog

class EMMAInAppPrismPlugin: EMMAInAppPlugin() {
    private val testData = true;

    override fun getType(): String = "emma_plugin_prism"

    override fun show(context: Activity?, nativeAd: EMMANativeAd) {
        if (EMMA.getInstance().sdkBuild < 127) {
            EMMALog.d("This plugin requires EMMA SDK > 4.9.0")
            return
        }

        if (context != null && !context.isFinishing) {
            context.startActivity(EMMAInAppPrismActivity.makeIntent(context, convertNativeAdToPrism(nativeAd)))
            return
        }
        EMMALog.w("Cannot show prism view. Wrong context.")
    }

    private fun createPrismSide(position: Int, containerBlock: Map<String, EMMANativeAdField>): EMMAPrismSide? {
        var image = containerBlock["Main Picture"]
        val cta = containerBlock["CTA"]

        val imageValue = image?.fieldValue
        val ctaValue = cta?.fieldValue

        if (imageValue != null && ctaValue != null
                && imageValue.isNotEmpty() && ctaValue.isNotEmpty()) {
            return EMMAPrismSide(imageValue, ctaValue, position)
        }
        return null
    }


    private fun cyclicSidesForRepresentation(sides: MutableList<EMMAPrismSide>) {
        if (sides.size > 2) {
            val firstPositionSide = sides[0];
            val lastPositionSide = sides[sides.size - 1]

            sides.add(0, lastPositionSide)
            sides.add(sides.size, firstPositionSide)
        }
    }

    private fun convertNativeAdToPrism(nativeAd: EMMANativeAd): EMMAPrism {
        if (testData) {
            return fakeData()
        }
        val sides = mutableListOf<EMMAPrismSide>()
        val campaignId = nativeAd.campaignID
        val openInApp = nativeAd.showOnWebView()

        val content = nativeAd.nativeAdContent
        val container = content["Container"]

        val prismSides = mutableListOf<EMMAPrismSide>()
        container?.fieldContainer?.forEachIndexed { index, containerBlock ->
            val prismSide = createPrismSide(index, containerBlock)
            prismSide?.let {
                prismSides.add(prismSide)
            }
        }

        cyclicSidesForRepresentation(sides)
        return EMMAPrism(campaignId, openInApp, sides)
    }

    private fun fakeData(): EMMAPrism {
        val fakeSides = mutableListOf<EMMAPrismSide>()
        fakeSides.apply {
            add(EMMAPrismSide("https://loremflickr.com/cache/resized/2381_2404158313_e6276190b1_c_370_750_nofilter.jpg", "https://emma.io/", 1))
            add(EMMAPrismSide("https://loremflickr.com/cache/resized/65535_50848154026_e6297c24af_c_370_750_nofilter.jpg", "https://emma.io/", 2))
            add(EMMAPrismSide("https://loremflickr.com/cache/resized/65535_50794109733_bbf35ef8f2_c_370_750_nofilter.jpg", "https://emma.io/", 3))
            add(EMMAPrismSide("https://loremflickr.com/cache/resized/65535_50841437988_a1696e81d3_c_370_750_nofilter.jpg", "https://emma.io/", 4))
            add(EMMAPrismSide("https://loremflickr.com/cache/resized/65535_50455565772_41195f5373_h_370_750_nofilter.jpg", "https://emma.io/", 5))
        }
        cyclicSidesForRepresentation(fakeSides)
        return EMMAPrism(3434343, false, fakeSides)
    }

    override fun dismiss() {
    }
}