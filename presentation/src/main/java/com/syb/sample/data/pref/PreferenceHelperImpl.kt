package com.syb.sample.data.pref

import android.content.Context
import com.syb.syblibrary.data.local.pref.BasePreferenceHelperImpl

class PreferenceHelperImpl : BasePreferenceHelperImpl, PreferenceHelper {

    constructor(context: Context, appPrefFileName: String): super(context, appPrefFileName)

}