package com.example.githubuser.ui.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuser.ui.detail.TabFollowFragment

class SectionPagerAdapter(activity: AppCompatActivity, private var login: String) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment = TabFollowFragment()
        val bundle = Bundle()
        when (position) {
            0 -> bundle.putString(
                TabFollowFragment.TAB_SECTION_NUMBER,
                TabFollowFragment.TAB_FOLLOWER
            )
            1 -> bundle.putString(
                TabFollowFragment.TAB_SECTION_NUMBER,
                TabFollowFragment.TAB_FOLLOWING
            )
        }
        bundle.putString(TabFollowFragment.LOGIN, login)
        fragment.arguments = bundle
        return fragment
    }
}