package baguette.mc.french.aegsoundbox

import android.content.Context

import android.graphics.Color

import android.support.v7.widget.CardView

import android.support.v7.widget.RecyclerView

import android.view.LayoutInflater

import android.view.View

import android.view.ViewGroup

import android.widget.ImageButton

import android.widget.ImageView

import android.widget.TextView

import com.bumptech.glide.Glide
import com.futuremind.recyclerviewfastscroll.SectionTitleProvider
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class SoundAdapterK(soundArray:ArrayList<Sound>, context: Context) :RecyclerView.Adapter<SoundAdapterK.ViewHolder>(),
    SectionTitleProvider {
    private var sounds:ArrayList<Sound>
    private var shouldShowFavsOnly = false
    private var context:Context

    init{
        sounds = soundArray
        this.context = context
    }

    override fun getItemCount(): Int {
        return  sounds.size
    }

    fun getItem(position: Int): String {
        return  sounds[position].name
    }

    fun onlyShowFavorites() {
        shouldShowFavsOnly = true
        for (sound in ArrayList(sounds))
        {
            if (!sound.getFavorite())
            {
                notifyItemRemoved(sounds.indexOf(sound))
                sounds.remove(sound)
            }
        }
    }

    fun showAllSounds(context:Context) {
        shouldShowFavsOnly = false
        sounds = SoundStore.getAllSounds(context)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int): ViewHolder {
        // create a new view
        val v = LayoutInflater.from(parent.context).inflate(R.layout.sound_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder:ViewHolder, position:Int) {
        holder.title.text = sounds[position].name
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
                EventBus.getDefault().post(sounds[holder.adapterPosition])
            }
        })
        val isFavorite = sounds.get(position).getFavorite()
        holder.favButton.setImageResource(if (isFavorite)
            R.drawable.ic_favorite_white_24dp
        else
            R.drawable
                .ic_favorite_outline_white_24dp)
        holder.favButton.setOnClickListener { v ->
            val newFavStatus = !sounds.get(holder.adapterPosition).getFavorite()
            sounds.get(holder.adapterPosition).setFavorite(newFavStatus)
            if (newFavStatus) {
                (v as ImageButton).setImageResource(R.drawable.ic_favorite_white_24dp)
                v.setContentDescription(v.getContext().getString(R.string.fav_desc))
            } else {
                (v as ImageButton).setImageResource(R.drawable.ic_favorite_outline_white_24dp)
                v.setContentDescription(v.getContext().getString(R.string.not_fav_desc))
            }
            if (shouldShowFavsOnly) {
                // Remove from the list.
                sounds.remove(sounds.get(holder.adapterPosition))
                notifyItemRemoved(holder.adapterPosition)
            }
        }
        Glide.with(context)
            .load(sounds[position].pictureId)
            .centerCrop()
            .into(holder.imageView)
    }
    class ViewHolder(v:View):RecyclerView.ViewHolder(v) {
        var title:TextView = v.findViewById(R.id.title) as TextView
        var favButton:ImageButton = v.findViewById(R.id.fav_button) as ImageButton
        var imageView:ImageView = v.findViewById(R.id.imageView) as ImageView
        private var accentColor:Int = 0
        init{
            accentColor = itemView.context.resources.getColor(R.color.colorAccent)
        }
        fun setNormalColors() {
            (itemView as CardView).setCardBackgroundColor(accentColor)
            title.setTextColor(Color.WHITE)
            favButton.clearColorFilter()
        }
        fun setPlayingColors() {
            (itemView as CardView).setCardBackgroundColor(Color.WHITE)
            title.setTextColor(accentColor)
            favButton.setColorFilter(accentColor)
        }
    }

    override fun getSectionTitle(position: Int): String {
        //this String will be shown in a bubble for specified position
        return getItem(position).substring(0, 1)
    }
}