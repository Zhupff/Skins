package zhupff.skins

import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

object SkinTask {

    @JvmStatic
    fun skinTextColor(skinWrapper: SkinWrapper, @ColorRes id: Int) {
        val defaultSkin = skinWrapper.dispatcher.getDefaultSkin()
        val targetSkin = skinWrapper.dispatcher.getCurrentSkin().value ?: defaultSkin
        when (val view = skinWrapper.view) {
            is TextView -> {
                view.setTextColor(targetSkin.getColorStateList(id, defaultSkin))
            }
        }
    }

    @JvmStatic
    fun skinHintColor(skinWrapper: SkinWrapper, @ColorRes id: Int) {
        val defaultSkin = skinWrapper.dispatcher.getDefaultSkin()
        val targetSkin = skinWrapper.dispatcher.getCurrentSkin().value ?: defaultSkin
        when (val view = skinWrapper.view) {
            is EditText -> {
                view.setHintTextColor(targetSkin.getColorStateList(id, defaultSkin))
            }
        }
    }

    @JvmStatic
    fun skinForegroundDrawable(skinWrapper: SkinWrapper, @DrawableRes id: Int) {
        val defaultSkin = skinWrapper.dispatcher.getDefaultSkin()
        val targetSkin = skinWrapper.dispatcher.getCurrentSkin().value ?: defaultSkin
        when (val view = skinWrapper.view) {
            is ImageView -> {
                view.setImageDrawable(targetSkin.getDrawable(id, defaultSkin))
            }
        }
    }

    @JvmStatic
    fun skinBackgroundColor(skinWrapper: SkinWrapper, @ColorRes id: Int) {
        val defaultSkin = skinWrapper.dispatcher.getDefaultSkin()
        val targetSkin = skinWrapper.dispatcher.getCurrentSkin().value ?: defaultSkin
        skinWrapper.view.setBackgroundColor(targetSkin.getColor(id, defaultSkin))
    }

    @JvmStatic
    fun skinForegroundTint(skinWrapper: SkinWrapper, @ColorRes id: Int) {
        val defaultSkin = skinWrapper.dispatcher.getDefaultSkin()
        val targetSkin = skinWrapper.dispatcher.getCurrentSkin().value ?: defaultSkin
        when (val view = skinWrapper.view) {
            is ImageView -> {
                view.setColorFilter(targetSkin.getColor(id, defaultSkin))
            }
        }
    }

    @JvmStatic
    fun skinBackgroundTint(skinWrapper: SkinWrapper, @ColorRes id: Int) {
        val defaultSkin = skinWrapper.dispatcher.getDefaultSkin()
        val targetSkin = skinWrapper.dispatcher.getCurrentSkin().value ?: defaultSkin
        skinWrapper.view.backgroundTintList = targetSkin.getColorStateList(id, defaultSkin)
    }
}