package zhupff.skins

import androidx.lifecycle.LiveData

interface SkinDispatcher<S : Skin> {

    fun getDefaultSkin(): S

    fun getCurrentSkin(): LiveData<S>
}