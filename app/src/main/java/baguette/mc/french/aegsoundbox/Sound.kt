/*
 * Copyright (C) 2014 Caleb Sabatini
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package baguette.mc.french.aegsoundbox

import baguette.mc.french.aegsoundbox.FavStore.Companion.instance

class Sound(var name: String, var resourceId: Int, var pictureId: Int) {
    private var favorite: Boolean

    fun getFavorite(): Boolean {
        return favorite
    }

    fun setFavorite(favorite: Boolean) {
        this.favorite = favorite
        instance!!.setSoundFavorited(name, favorite)
    }

    override fun toString(): String {
        return name
    }

    init {
        favorite = instance!!.isSoundFavorited(name)
    }
}