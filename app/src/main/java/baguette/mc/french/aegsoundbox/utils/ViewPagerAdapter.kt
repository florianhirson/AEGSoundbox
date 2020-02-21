package baguette.mc.french.aegsoundbox.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import baguette.mc.french.aegsoundbox.fragments.TabFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val CARD_ITEM_SIZE = 2

    override fun getItemCount(): Int {
        return CARD_ITEM_SIZE
    }

    override fun createFragment(position: Int): Fragment {
        if(position == 0) {
            return TabFragment.newInstance(false)!!
        } else {
            return TabFragment.newInstance(true)!!
        }

    }

}
