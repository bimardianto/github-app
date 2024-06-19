package com.development.githubuser.ui.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class DetailAdapter(fa: FragmentActivity, private val fragment: MutableList<FollowFragment>) :
    FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = fragment.size

    override fun createFragment(position: Int): Fragment = fragment[position]

}