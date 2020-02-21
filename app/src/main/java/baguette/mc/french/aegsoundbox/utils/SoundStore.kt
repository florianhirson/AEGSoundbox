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
package baguette.mc.french.aegsoundbox.utils

import android.annotation.SuppressLint
import android.content.Context
import baguette.mc.french.aegsoundbox.R
import java.util.*


object SoundStore {
    @SuppressLint("ResourceType")
    @JvmStatic
    fun getAllSounds(context: Context): ArrayList<Sound> {
        val res = context.applicationContext.resources
        val soundsArray = res.obtainTypedArray(R.array.sounds)

        val soundsList = ArrayList<Sound>()
        for (i in 0 until soundsArray.length()) {
            val soundId = soundsArray.getResourceId(i, 0)

            // Get the properties of one object
            val rawSound = context.resources.obtainTypedArray(soundId)

            soundsList.add(
                Sound(
                    rawSound.getString(0),
                    rawSound.getResourceId(1, -1),
                    rawSound.getResourceId(2, -1)
                )
            )

            rawSound.recycle()
        }

        soundsArray.recycle()
        return soundsList
    }

    @SuppressLint("ResourceType")
    fun getFavoriteSounds(context: Context): ArrayList<Sound> {


        val res = context.applicationContext.resources
        val soundsArray = res.obtainTypedArray(R.array.sounds)

        val soundsList = ArrayList<Sound>()
        for (i in 0 until soundsArray.length()) {
            val soundId = soundsArray.getResourceId(i, 0)

            // Get the properties of one object
            val rawSound = context.resources.obtainTypedArray(soundId)

            val sound  =
                Sound(
                    rawSound.getString(0),
                    rawSound.getResourceId(1, -1),
                    rawSound.getResourceId(2, -1)
                )

            if (sound.getFavorite()) {
                soundsList.add(sound)
            }

            rawSound.recycle()
        }

        soundsArray.recycle()
        return soundsList
    }
}