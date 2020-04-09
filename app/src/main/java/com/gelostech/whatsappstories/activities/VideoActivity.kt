package com.gelostech.whatsappstories.activities

import android.os.Bundle
import androidx.databinding.DataBindingUtil.setContentView
import cn.jzvd.JZVideoPlayer
import cn.jzvd.JZVideoPlayerStandard
import com.gelostech.whatsappstories.R
import com.gelostech.whatsappstories.commoners.BaseActivity
import com.gelostech.whatsappstories.commoners.K
import com.gelostech.whatsappstories.models.Story
import com.gelostech.whatsappstories.utils.loadUrl
import kotlinx.android.synthetic.main.activity_video.*

class VideoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        val story = intent.getSerializableExtra(K.STORY) as Story

        video.setUp(story.path, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "")
        video.thumbImageView.loadUrl(story.path!!)
    }

    override fun onPause() {
        super.onPause()
        JZVideoPlayer.releaseAllVideos()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        JZVideoPlayer.backPress()
    }
}
