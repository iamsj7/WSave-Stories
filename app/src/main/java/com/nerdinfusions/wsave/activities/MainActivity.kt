package com.nerdinfusions.wsave.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import cn.jzvd.JZVideoPlayer
import com.google.android.material.tabs.TabLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.nerdinfusions.wsave.R
import com.nerdinfusions.wsave.SettingsActivity
import com.nerdinfusions.wsave.fragments.ImagesFragment
import com.nerdinfusions.wsave.fragments.SavedFragment
import com.nerdinfusions.wsave.fragments.VideosFragment
import com.nerdinfusions.wsave.models.PermissionsEvent
import com.nerdinfusions.wsave.utils.PagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {
    private var doubleBackToExit = false
	private lateinit var firebaseAnalytics: FirebaseAnalytics

    companion object {
        private const val IMAGES = "IMAGES"
        private const val VIDEOS = "VIDEOS"
        private const val SAVED = "SAVED"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
		// Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
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

            Handler().postDelayed({ doubleBackToExit = false }, 1500)
        }
    }

    override fun onPause() {
        super.onPause()
        JZVideoPlayer.releaseAllVideos()
    }

    // User hasn't requested storage permission; request them to allow
    fun requestStoragePermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        EventBus.getDefault().post(PermissionsEvent(true))
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        toast("Storage permission is required!")
                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()
    }

    // Check if user has granted storage permission
    fun storagePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

}
