# inapp-plugin-prism-android

## Introduction

Plugin for EMMA in-app communication. This plugin shows a prism with 10 sides as maximum. 

You can create your own plugin through native ad engine.

## Installation

> **Warning**
>
> Version 1.3 depends of EMMA SDK 4.14.0 if your project cannot update force package tag to previous version 1.2

For integrate this plugin add the following lines to build.grade:

```groovy
 repositories {
    ....
    maven { url 'https://repo.emma.io/emma' }
}

dependencies {
   ...
   // Use the latest version available
   implementation 'io.emma:eMMaSDK:4.14.0'
   implementation 'io.emma:inapp-plugin-prism:1.3'
}
```

## Integration

Import the library in Application class and add plugin class to SDK.

``` kotlin
import io.emma.android.EMMA
import io.emma.plugin_prism.EMMAInAppPrismPlugin
 
 class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ....
        EMMA.getInstance().startSession(configuration)
        EMMA.getInstance().addInAppPlugins(EMMAInAppPrismPlugin())
    }

```

In an Activity or Fragment request the available prism (If there are many prism avaiable only is shown the latest created).

``` kotlin
import io.emma.android.EMMA
import io.emma.android.model.EMMAInAppRequest
import io.emma.android.model.EMMANativeAdRequest

class HomeActivity: AppCompatActivity() {
	
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val request = EMMANativeAdRequest()
        request.templateId = "emma-plugin-prisma"
        EMMA.getInstance().getInAppMessage(request)
    }
}

```
