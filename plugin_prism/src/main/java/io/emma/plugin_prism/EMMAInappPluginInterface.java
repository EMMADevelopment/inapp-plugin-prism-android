package io.emma.plugin_prism;

import android.content.Context;
import io.emma.android.model.EMMANativeAd;

interface EMMAInappPluginInterface {
    String getType();
    void show(Context context, EMMANativeAd nativeAd);
    void dismiss();
}