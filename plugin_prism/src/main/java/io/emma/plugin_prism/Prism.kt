package io.emma.plugin_prism

import io.emma.android.model.EMMACampaign
import java.io.Serializable

internal data class Prism(val campaign: EMMACampaign, val openInApp: Boolean, val canClose: Boolean, val sides: List<PrismSide>): Serializable