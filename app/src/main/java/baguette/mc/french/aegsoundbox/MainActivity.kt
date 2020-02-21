package baguette.mc.french.aegsoundbox

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
import androidx.viewpager2.widget.ViewPager2
import com.futuremind.recyclerviewfastscroll.FastScroller
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.textfield.TextInputEditText


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

        (grid.layoutManager as StaggeredGridLayoutManager).gapStrategy = GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS

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

        val tabLayout = findViewById<View>(R.id.tabLayout) as TabLayout

        if(FavStore.instance?.showFavorites!!) {
            tabLayout.getTabAt(1)?.select()
        }


        if (tabLayout.selectedTabPosition == 1) {
            (grid.adapter as SoundAdapterK).onlyShowFavorites()
        } else {
            (
                    grid.adapter as SoundAdapterK).showAllSounds(this@MainActivity)
        }

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                txtSearchSounds.clearFocus()
                if (tabLayout.selectedTabPosition == 1) {
                    (grid.adapter as SoundAdapterK).onlyShowFavorites()
                } else {
                    (grid.adapter as SoundAdapterK).showAllSounds(this@MainActivity)
                }
                FavStore.instance?.showFavorites = tabLayout.selectedTabPosition == 1
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })



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
