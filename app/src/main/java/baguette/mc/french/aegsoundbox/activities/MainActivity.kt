package baguette.mc.french.aegsoundbox.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import baguette.mc.french.aegsoundbox.R
import baguette.mc.french.aegsoundbox.utils.FavStore
import baguette.mc.french.aegsoundbox.utils.SoundPlayer
import baguette.mc.french.aegsoundbox.utils.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
import com.google.android.material.textfield.TextInputEditText


class MainActivity : AppCompatActivity() {

    private var soundPlayer: SoundPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        val viewpager = findViewById<View>(R.id.viewpager) as ViewPager2
        val tabs = findViewById<View>(R.id.tabs) as TabLayout
        val txtSearchSounds = findViewById<View>(R.id.txtSearchSounds) as TextInputEditText


        setSupportActionBar(toolbar)
//        toolbar.setLogo(R.mipmap.ic_cucumber)
        toolbar.title = ""

        FavStore.init(
            getPreferences(
                Context.MODE_PRIVATE
            )
        )

        viewpager.adapter = ViewPagerAdapter(this)

        TabLayoutMediator(tabs, viewpager,
            TabConfigurationStrategy { tab, position ->
                if(position == 0) {
                    tab.text = "ALL SOUNDS"
                } else {
                    tab.text = "FAVORITES"
                }

            }).attach()

//        txtSearchSounds.addTextChangedListener(object : TextWatcher {
//
//            override fun afterTextChanged(s: Editable) {
//                (grid.adapter as SoundAdapterK).filter.filter(s)
//            }
//
//            override fun beforeTextChanged(s: CharSequence, start: Int,
//                                           count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence, start: Int,
//                                       before: Int, count: Int) {
//                (grid.adapter as SoundAdapterK).filter.filter(s)
//            }
//        })

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
