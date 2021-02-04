package io.emma.plugin_prism

import android.app.Activity
import io.emma.android.model.EMMANativeAd
import io.emma.android.utils.EMMALog

class EMMAInappPrismPlugin: EMMAInappPluginInterface {

    override fun getType(): String = "emma_plugin_prism"

    override fun show(context: Activity, nativeAd: EMMANativeAd) {
        if (!context.isFinishing) {
            context.startActivity(EMMAInappPrismActivity.makeIntent(context))
            return
        }
        EMMALog.w("Cannot show prism view. Wrong context.")
    }

    override fun dismiss() {
    }
}