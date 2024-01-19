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
        side1Image.fieldValue = "https://i.imgur.com/W3FHL99.jpeg"

        val side1Cta = EMMANativeAdField()
        side1Cta.fieldName  = "CTA"
        side1Cta.fieldValue = "emmaio://settings"

        val side2Image = EMMANativeAdField()
        side2Image.fieldName = "Main picture"
        side2Image.fieldValue = "https://i.imgur.com/cQP38OS.jpeg"

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