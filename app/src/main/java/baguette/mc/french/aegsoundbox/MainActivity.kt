package baguette.mc.french.aegsoundbox

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.SwitchCompat
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.CompoundButton

class MainActivity : AppCompatActivity() {

    private var soundPlayer: SoundPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        toolbar.setLogo(R.mipmap.ic_cucumber)
        toolbar.title = ""

        FavStore.init(getPreferences(Context.MODE_PRIVATE))

        val grid = findViewById<View>(R.id.grid_view) as RecyclerView
        grid.layoutManager = StaggeredGridLayoutManager(
            resources.getInteger(R.integer.num_cols),
            StaggeredGridLayoutManager.VERTICAL
        )
        grid.adapter = SoundAdapter(SoundStore.getAllSounds(this), this@MainActivity)

        val favSwitch = findViewById<View>(R.id.fav_switch) as SwitchCompat
        favSwitch.isChecked = FavStore.getInstance().getShowFavorites()
        if (favSwitch.isChecked) {
            (grid.adapter as SoundAdapter).onlyShowFavorites()
        } else {
            (grid.adapter as SoundAdapter).showAllSounds(this@MainActivity)
        }
        favSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                (grid.adapter as SoundAdapter).onlyShowFavorites()
            } else {
                (grid.adapter as SoundAdapter).showAllSounds(this@MainActivity)
            }
            FavStore.getInstance().setShowFavorites(isChecked)
        }
    }

    public override fun onResume() {
        super.onResume()
        soundPlayer = SoundPlayer(this)
    }

    public override fun onPause() {
        super.onPause()
        soundPlayer?.release()
    }
}
