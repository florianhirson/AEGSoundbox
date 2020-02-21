package baguette.mc.french.aegsoundbox.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import baguette.mc.french.aegsoundbox.R
import baguette.mc.french.aegsoundbox.utils.SoundAdapterK
import baguette.mc.french.aegsoundbox.utils.SoundStore
import com.futuremind.recyclerviewfastscroll.FastScroller
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_main.*


class TabFragment : Fragment() {

    lateinit var grid : RecyclerView
    private val ARG_COUNT = "param1"
    var shouldShowFavsOnly : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            shouldShowFavsOnly = arguments!!.getBoolean(ARG_COUNT)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_tab, container, false)
        val txtSearchSound = activity?.findViewById(R.id.txtSearchSounds) as TextInputEditText

        grid = view.findViewById<View>(R.id.grid_view) as RecyclerView

        grid.layoutManager = StaggeredGridLayoutManager(
            resources.getInteger(R.integer.num_cols),
            StaggeredGridLayoutManager.VERTICAL
        )

        if(shouldShowFavsOnly) {
            grid.adapter = SoundAdapterK(
                SoundStore.getFavoriteSounds(this.context!!), this.context!!
            )
        } else {
            grid.adapter = SoundAdapterK(
                SoundStore.getAllSounds(this.context!!), this.context!!
            )
        }


        grid.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                when (newState) {
                    AbsListView.OnScrollListener.SCROLL_STATE_FLING -> {
                        // Do something
                        txtSearchSound.clearFocus()
                    }
                    AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL -> {
                        // Do something
                        txtSearchSound.clearFocus()
                    }
                    else -> {
                        // Do something
                        txtSearchSound.clearFocus()
                    }
                }
            }
        })

        val fastScroller = view.findViewById<View>(R.id.fastscroll) as FastScroller
        fastScroller.setRecyclerView(grid)

        return view
    }

    companion object {
        fun newInstance(shouldShowFavsOnly: Boolean?): TabFragment? {
            val ARG_COUNT = "param1"
            val fragment = TabFragment()
            val args = Bundle()
            args.putBoolean(ARG_COUNT, shouldShowFavsOnly!!)
            fragment.arguments = args
            return fragment
        }
    }

}
