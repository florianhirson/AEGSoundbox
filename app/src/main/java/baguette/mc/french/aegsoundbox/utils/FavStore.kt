package baguette.mc.french.aegsoundbox.utils

import android.content.SharedPreferences

/**
 * Created by kkelly on 11/1/15.
 */
class FavStore private constructor(private val prefs: SharedPreferences) {
    fun setSoundFavorited(soundName: String?, shouldFavorite: Boolean) {
        prefs.edit().putBoolean(soundName, shouldFavorite).apply()
    }

    fun isSoundFavorited(soundName: String?): Boolean {
        return prefs.getBoolean(soundName, false)
    }

    var showFavorites: Boolean
        get() = prefs.getBoolean("switch", false)
        set(showFavorites) {
            prefs.edit().putBoolean("switch", showFavorites).apply()
        }

    companion object {
        @JvmStatic
        var instance: FavStore? = null
            private set

        fun init(prefs: SharedPreferences) {
            instance =
                FavStore(prefs)
        }

    }

}