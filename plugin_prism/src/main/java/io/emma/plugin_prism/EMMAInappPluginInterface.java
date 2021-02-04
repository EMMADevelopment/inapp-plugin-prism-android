package io.emma.plugin_prism;

import android.app.Activity;
import io.emma.android.model.EMMANativeAd;

interface EMMAInappPluginInterface {
    String getType();
    void show(Activity activity, EMMANativeAd nativeAd);
    void dismiss();
}