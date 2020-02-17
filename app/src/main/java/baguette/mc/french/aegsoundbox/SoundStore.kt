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

import android.content.Context
import java.util.*

object SoundStore {
    @JvmStatic
    fun getAllSounds(context: Context): ArrayList<Sound> {
        val res = context.applicationContext.resources
        val labels = res.obtainTypedArray(R.array.labels)
        val ids = res.obtainTypedArray(R.array.ids)
        val pictureIds = res.obtainTypedArray(R.array.pictureIds)
        val sounds = ArrayList<Sound>()
        for (i in 0 until labels.length()) {
            sounds.add(
                Sound(
                    labels.getString(i),
                    ids.getResourceId(i, -1),
                    pictureIds.getResourceId(i, -1)
                )
            )
        }
        labels.recycle()
        ids.recycle()
        pictureIds.recycle()
        return sounds
    }

    fun getFavoriteSounds(context: Context): ArrayList<Sound> {
        val res = context.applicationContext.resources
        val labels = res.obtainTypedArray(R.array.labels)
        val ids = res.obtainTypedArray(R.array.ids)
        val pictureIds = res.obtainTypedArray(R.array.pictureIds)
        val sounds = ArrayList<Sound>()
        for (i in 0 until labels.length()) {
            val sound = Sound(
                labels.getString(i),
                ids.getResourceId(i, -1),
                pictureIds.getResourceId(i, -1)
            )
            if (sound.getFavorite()) {
                sounds.add(sound)
            }
        }
        labels.recycle()
        ids.recycle()
        pictureIds.recycle()
        return sounds
    }
}