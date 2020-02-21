package baguette.mc.french.aegsoundbox.utils

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import baguette.mc.french.aegsoundbox.R
import baguette.mc.french.aegsoundbox.R.drawable
import com.futuremind.recyclerviewfastscroll.SectionTitleProvider
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class SoundAdapterK(soundArray:ArrayList<Sound>, context: Context) :
    RecyclerView.Adapter<SoundAdapterK.ViewHolder>(),
    SectionTitleProvider, Filterable {


    private var sounds:ArrayList<Sound>
    private var soundsFiltered: ArrayList<Sound>
    private var shouldShowFavsOnly = false
    private var context:Context

    init{
        sounds = soundArray
        soundsFiltered = soundArray
        this.context = context
    }

    override fun getItemCount(): Int {
        return  soundsFiltered.size
    }

    fun getItem(position: Int): String {
        return  soundsFiltered[position].name
    }

    fun onlyShowFavorites() {
        shouldShowFavsOnly = true
        for (sound in ArrayList(soundsFiltered))
        {
            if (!sound.getFavorite())
            {
                notifyItemRemoved(soundsFiltered.indexOf(sound))
                soundsFiltered.remove(sound)
            }
        }
    }

    fun showAllSounds(context:Context) {
        shouldShowFavsOnly = false
        soundsFiltered =
            SoundStore.getAllSounds(context)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int): ViewHolder {
        // create a new view
        val v = LayoutInflater.from(parent.context).inflate(R.layout.sound_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position:Int) {
        holder.title.text = soundsFiltered[position].name
        holder.itemView.setOnClickListener(object: View.OnClickListener {


            @Subscribe(threadMode = ThreadMode.MAIN)
            fun onEvent(done:String) {
                holder.setNormalColors()
                EventBus.getDefault().unregister(this)
            }
            override fun onClick(view:View) {
                holder.setPlayingColors()
                if (EventBus.getDefault().isRegistered(this))
                {
                    return
                }
                EventBus.getDefault().register(this)
                EventBus.getDefault().post(soundsFiltered[holder.adapterPosition])
            }
        })
        val isFavorite = soundsFiltered[position].getFavorite()
        holder.favButton.setImageResource(if (isFavorite)
            drawable.ic_red_tomato
        else
            drawable.ic_grey_tomato)
        holder.favButton.setOnClickListener { v ->
            val newFavStatus = !soundsFiltered[holder.adapterPosition].getFavorite()
            soundsFiltered[holder.adapterPosition].setFavorite(newFavStatus)
            if (newFavStatus) {
                (v as ImageButton).setImageResource(drawable.ic_red_tomato)
                v.setContentDescription(v.getContext().getString(R.string.fav_desc))
            } else {
                (v as ImageButton).setImageResource(drawable.ic_grey_tomato)
                v.setContentDescription(v.getContext().getString(R.string.not_fav_desc))
            }
            if (shouldShowFavsOnly) {
                // Remove from the list.
                soundsFiltered.remove(soundsFiltered[holder.adapterPosition])
                notifyItemRemoved(holder.adapterPosition)
            }
            notifyDataSetChanged()
        }
        GlideApp.with(context)
            .load(soundsFiltered[position].pictureId)
            .centerCrop()
            .into(holder.imageView)
    }

    class ViewHolder(v:View): RecyclerView.ViewHolder(v) {
        var title:TextView = v.findViewById(R.id.title) as TextView
        var favButton:ImageButton = v.findViewById(R.id.fav_button) as ImageButton
        var imageView:ImageView = v.findViewById(R.id.imageView) as ImageView
        private var accentColor:Int = 0
        init{

            accentColor = ContextCompat.getColor(itemView.context,
                R.color.colorAccent
            )
        }
        fun setNormalColors() {
            (itemView as CardView).setCardBackgroundColor(accentColor)
            title.setTextColor(Color.WHITE)
            favButton.clearColorFilter()

            imageView.clearAnimation()
        }
        fun setPlayingColors() {
            (itemView as CardView).setCardBackgroundColor(ContextCompat.getColor(itemView.context,
                R.color.colorImagePlayingSound
            ))
            title.setTextColor(ContextCompat.getColor(itemView.context,
                R.color.colorTextPlayingSound
            ))
//            favButton.setColorFilter(accentColor)

            val rotateAnimation =  RotateAnimation(0.0F, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)

            rotateAnimation.interpolator = LinearInterpolator()
            rotateAnimation.duration = 500
            rotateAnimation.repeatCount = Animation.INFINITE

            imageView.startAnimation(rotateAnimation)
        }
    }

    override fun getSectionTitle(position: Int): String {
        //this String will be shown in a bubble for specified position
        return getItem(position).substring(0, 1)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    soundsFiltered = sounds
                } else {
                    val filteredList = ArrayList<Sound>()
                    for (sound in sounds) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (sound.name.toLowerCase().contains(charString.toLowerCase())
                        ) {
                            filteredList.add(sound)
                        }
                    }

                    soundsFiltered = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = soundsFiltered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                soundsFiltered = filterResults.values as ArrayList<Sound>
                notifyDataSetChanged()
            }
        }
    }
}