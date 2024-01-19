package io.emma.plugin_prism

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import io.emma.android.utils.EMMALog

internal class InAppPrismActivity: FragmentActivity()  {

    companion object {
        const val PRISM = "prism"

        fun makeIntent(context: Activity, prism: Prism): Intent {
            val intent = Intent(context, InAppPrismActivity::class.java)
            intent.putExtra(PRISM, prism)
            return intent
        }
    }

    private fun getPrism(): Prism? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(PRISM, Prism::class.java)
        } else {
            intent.getSerializableExtra(PRISM) as? Prism
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emma_prism)

        val prism = getPrism()
        if (prism != null && prism.sides.isNotEmpty()) {
            val bundle = Bundle()
            bundle.putSerializable(PRISM, prism)
            PrismDialogFragment().apply {
                isCancelable = prism.canClose
                arguments = bundle
                show(supportFragmentManager, PrismDialogFragment.TAG)
            }
        } else {
            EMMALog.w("Invalid prism or empty content. Skipping show")
            finish()
        }
    }
}