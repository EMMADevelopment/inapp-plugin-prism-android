package io.emma.plugin_prism

import android.app.Activity
import android.content.Context
import android.content.Intent
import io.emma.android.interfaces.EMMARequestListener
import io.emma.android.model.EMMAInAppRequest
import io.emma.android.model.EMMANativeAd
import io.emma.android.utils.EMMALog

class EMMAInappPlugin: EMMAInappPluginInterface {

    override fun getType(): String = "emma_plugin_prism"

    override fun show(context: Context, nativeAd: EMMANativeAd) {
        if (context is Activity && !context.isFinishing) {
            context.startActivity(Intent(context, EMMAInappPrismActivity::class.java))
            return
        }
        EMMALog.w("Cannot show prism view. Wrong context.")
    }

    override fun dismiss() {

    }
}