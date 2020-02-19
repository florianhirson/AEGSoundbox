package baguette.mc.french.aegsoundbox

import android.content.Context
import android.os.Bundle
import com.google.android.material.textfield.TextInputEditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.futuremind.recyclerviewfastscroll.FastScroller
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.AbsListView
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.android.material.switchmaterial.SwitchMaterial


class MainActivity : AppCompatActivity() {

    private var soundPlayer: SoundPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        val txtSearchSounds = findViewById<View>(R.id.txtSearchSounds) as TextInputEditText
        val grid = findViewById<View>(R.id.grid_view) as RecyclerView

        setSupportActionBar(toolbar)
//        toolbar.setLogo(R.mipmap.ic_cucumber)
        toolbar.title = ""

        FavStore.init(getPreferences(Context.MODE_PRIVATE))


        grid.layoutManager = StaggeredGridLayoutManager(
            resources.getInteger(R.integer.num_cols),
            StaggeredGridLayoutManager.VERTICAL
        )
        grid.adapter = SoundAdapterK(SoundStore.getAllSounds(this), this@MainActivity)

        grid.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                when (newState) {
                    AbsListView.OnScrollListener.SCROLL_STATE_FLING -> {
                        // Do something
                        txtSearchSounds.clearFocus()
                    }
                    AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL -> {
                        // Do something
                        txtSearchSounds.clearFocus()
                    }
                    else -> {
                        // Do something
                        txtSearchSounds.clearFocus()
                    }
                }
            }
        })

        val fastScroller = findViewById<View>(R.id.fastscroll) as FastScroller
        fastScroller.setRecyclerView(grid)

        val favSwitch = findViewById<View>(R.id.fav_switch) as SwitchMaterial
        favSwitch.isChecked = FavStore.instance?.showFavorites!!

        if (favSwitch.isChecked) {
            (grid.adapter as SoundAdapterK).onlyShowFavorites()
        } else {
            (grid.adapter as SoundAdapterK).showAllSounds(this@MainActivity)
        }
        favSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            txtSearchSounds.clearFocus()
            if (isChecked) {
                (grid.adapter as SoundAdapterK).onlyShowFavorites()
            } else {
                (grid.adapter as SoundAdapterK).showAllSounds(this@MainActivity)
            }
            FavStore.instance?.showFavorites = isChecked
        }



        txtSearchSounds.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                (grid.adapter as SoundAdapterK).filter.filter(s)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                (grid.adapter as SoundAdapterK).filter.filter(s)
            }
        })
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
