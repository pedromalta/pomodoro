package net.clubedocomputador.pomodoro.features.principal

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PrincipalViewPagerAdapter(
        private val context: Context,
        fragmentManager: FragmentManager,
        private val fragments: Array<Fragment>) : FragmentPagerAdapter(fragmentManager) {


    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val fragment = fragments[position]
        if (fragment is PrincipalTabbedView) {
            return fragment.getTabTitle(context)
        }
        return super.getPageTitle(position)
    }
}