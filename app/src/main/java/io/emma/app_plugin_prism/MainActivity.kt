package io.emma.app_plugin_prism

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import io.emma.android.model.EMMANativeAd
import io.emma.android.model.EMMANativeAdField
import io.emma.plugin_prism.EMMAInAppPrismPlugin

class MainActivity : AppCompatActivity() {

    private fun createFakeNativeAd(): EMMANativeAd {
        val nativeAd = EMMANativeAd()
        nativeAd.campaignID = 123456789
        nativeAd.setShowOn(0)

        val side1Image = EMMANativeAdField()
        side1Image.fieldName  = "Main picture"
        side1Image.fieldValue = "https://i.picsum.photos/id/586/300/700.jpg?hmac=TKBELClTbUvaXq5NHUpCVnnhssZ3tYTSLTYBi6rPo5Q"

        val side1Cta = EMMANativeAdField()
        side1Cta.fieldName  = "CTA"
        side1Cta.fieldValue = "emmaio://settings"

        val side2Image = EMMANativeAdField()
        side2Image.fieldName = "Main picture"
        side2Image.fieldValue = "https://i.picsum.photos/id/666/300/700.jpg?hmac=mXEaSU1_1gEAtK3z-beAT7GyWGt8oYsa34QOXLBx-qY"

        val side2Cta = EMMANativeAdField()
        side2Cta.fieldName = "CTA"
        side2Cta.fieldValue = "https://emma.io"

        val container = EMMANativeAdField()
        container.fieldName = "container"
        container.fieldContainer = mutableListOf(
            mapOf(side1Image.fieldName to side1Image, side1Cta.fieldName to side1Cta),
            mapOf(side2Image.fieldName to side2Image, side2Cta.fieldName to side2Cta)
        )
        nativeAd.setFields(mapOf("container" to container))

        return nativeAd
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.showPrism).setOnClickListener {
            val plugin = EMMAInAppPrismPlugin()
            plugin.show(this, createFakeNativeAd())
        }
    }
}