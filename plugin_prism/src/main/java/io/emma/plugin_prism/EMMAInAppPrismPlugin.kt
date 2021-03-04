package io.emma.plugin_prism

import android.app.Activity
import io.emma.android.EMMA
import io.emma.android.model.EMMACampaign
import io.emma.android.model.EMMANativeAd
import io.emma.android.model.EMMANativeAdField
import io.emma.android.plugins.EMMAInAppPlugin
import io.emma.android.utils.EMMALog

class EMMAInAppPrismPlugin: EMMAInAppPlugin() {

    override fun getId(): String = "emma-plugin-prism"

    override fun show(context: Activity?, nativeAd: EMMANativeAd) {
        if (EMMA.getInstance().sdkBuild < 127) {
            EMMALog.w("This plugin requires EMMA SDK >= 4.9.0")
            return
        }

        if (context != null && !context.isFinishing) {
            context.startActivity(InAppPrismActivity.makeIntent(context, convertNativeAdToPrism(nativeAd)))
            return
        }
        EMMALog.w("Cannot show prism view. Wrong context")
    }

    private fun createPrismSide(containerBlock: Map<String, EMMANativeAdField>): PrismSide? {
        var image = containerBlock["Main picture"]
        val cta = containerBlock["CTA"]

        val imageValue = image?.fieldValue
        val ctaValue = cta?.fieldValue

        if (imageValue != null && ctaValue != null
                && imageValue.isNotEmpty() && ctaValue.isNotEmpty()) {
            return PrismSide(imageValue, ctaValue)
        }
        EMMALog.w("Invalid values for prism")
        return null
    }


    private fun cyclicSidesForRepresentation(sides: MutableList<PrismSide>) {
        if (sides.size >= 2) {
            val firstPositionSide = sides[0]
            val lastPositionSide = sides[sides.size - 1]

            sides.add(0, lastPositionSide)
            sides.add(sides.size, firstPositionSide)
        }
    }

    private fun EMMANativeAd.toCampaign(): EMMACampaign {
        val campaign = EMMACampaign(EMMACampaign.Type.NATIVEAD)
        campaign.campaignID = campaignID
        campaign.campaignUrl = campaignUrl
        campaign.canClose = canClose
        campaign.params = params
        campaign.times = times
        return campaign
    }

    private fun convertNativeAdToPrism(nativeAd: EMMANativeAd): Prism {
        val openInApp = nativeAd.showOnWebView()

        val content = nativeAd.nativeAdContent
        val container = content["Container"]

        val prismSides = mutableListOf<PrismSide>()
        container?.fieldContainer?.forEach { containerBlock ->
            val prismSide = createPrismSide(containerBlock)
            prismSide?.let {
                prismSides.add(prismSide)
            }
        }

        cyclicSidesForRepresentation(prismSides)
        return Prism(nativeAd.toCampaign(), openInApp, true, prismSides)
    }

    override fun dismiss() {
    }
}