package com.shaikjaleel.wsave.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import cn.jzvd.JZVideoPlayer
import com.shaikjaleel.wsave.R
import com.shaikjaleel.wsave.commoners.BaseActivity
import com.shaikjaleel.wsave.fragments.ImagesFragment
import com.shaikjaleel.wsave.fragments.SavedFragment
import com.shaikjaleel.wsave.fragments.VideosFragment
import com.shaikjaleel.wsave.utils.PagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast


class MainActivity : BaseActivity(), TabLayout.OnTabSelectedListener {
    private var doubleBackToExit = false

    companion object {
        private const val IMAGES = "IMAGES"
        private const val VIDEOS = "VIDEOS"
        private const val SAVED = "SAVED"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        if (!storagePermissionGranted()) requestStoragePermission()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.settings) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_name)

        setupViewPager()
        setupTabs()
    }

    private fun setupViewPager() {
        val adapter = PagerAdapter(supportFragmentManager, this)
        val images = ImagesFragment()
        val videos = VideosFragment()
        val saved = SavedFragment()

        adapter.addAllFrags(images, videos, saved)
        adapter.addAllTitles(IMAGES, VIDEOS, SAVED)

        viewpager.adapter = adapter
        viewpager.offscreenPageLimit = 2
        viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))

    }

    private fun setupTabs() {
        tabs.setupWithViewPager(viewpager)
        tabs.addOnTabSelectedListener(this)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        viewpager.setCurrentItem(tab!!.position, true)
    }

    override fun onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return
        }

        if (doubleBackToExit) {
            super.onBackPressed()
        } else {
            toast("Please tap back again to exit")

            doubleBackToExit = true

            Handler().postDelayed({doubleBackToExit = false }, 1500)
        }
    }

    override fun onPause() {
        super.onPause()
        JZVideoPlayer.releaseAllVideos()
    }

}
