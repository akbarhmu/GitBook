package id.ngode.gitbook.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.ngode.gitbook.fragment.FollowersFragment
import id.ngode.gitbook.fragment.FollowingFragment

class DetailUserPagerAdapter(activity: AppCompatActivity, private val username: String?) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        val mFollowingFragment = FollowingFragment()
        val mFollowersFragment = FollowersFragment()

        val mBundleFollowing = Bundle()
        mBundleFollowing.putString(FollowingFragment.EXTRA_USERNAME, username)
        mFollowingFragment.arguments = mBundleFollowing

        val mBundleFollowers = Bundle()
        mBundleFollowers.putString(FollowersFragment.EXTRA_USERNAME, username)
        mFollowersFragment.arguments = mBundleFollowers

        when (position) {
            0 -> fragment = mFollowingFragment
            1 -> fragment = mFollowersFragment
        }
        return fragment as Fragment
    }

}