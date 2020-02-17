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
import android.media.MediaPlayer
import android.util.Log
import de.greenrobot.event.EventBus
import java.io.IOException

class SoundPlayer(context: Context) {
    private var mPlayer: MediaPlayer? = null
    private val mContext: Context

    fun onEvent(sound: Sound) {
        playSound(sound)
    }

    fun playSound(sound: Sound) {
        val resource = sound.resourceId
        if (mPlayer != null) {
            if (mPlayer!!.isPlaying) mPlayer!!.stop()
            mPlayer!!.reset()
            try {
                val afd = mContext.resources.openRawResourceFd(resource) ?: return
                mPlayer!!.setDataSource(
                    afd.fileDescriptor,
                    afd.startOffset,
                    afd.length
                )
                afd.close()
                mPlayer!!.prepare()
            } catch (e: IOException) {
                Log.e(TAG, e.message)
            } catch (e: IllegalArgumentException) {
                Log.e(TAG, e.message)
            } catch (e: SecurityException) {
                Log.e(TAG, e.message)
            }
        } else {
            mPlayer = MediaPlayer.create(mContext, resource)
        }
        mPlayer!!.start()
        mPlayer!!.setOnCompletionListener { EventBus.getDefault().post("Done") }
    }

    fun release() {
        EventBus.getDefault().unregister(this)
        if (mPlayer != null) {
            mPlayer!!.release()
            mPlayer = null
        }
    }

    companion object {
        private const val TAG = "SoundPlayer"
    }

    init {
        EventBus.getDefault().register(this)
        mContext = context.applicationContext
    }
}