package com.shaikjaleel.wsave.activities

import android.os.Bundle
import cn.jzvd.JZVideoPlayer
import cn.jzvd.JZVideoPlayerStandard
import com.shaikjaleel.wsave.R
import com.shaikjaleel.wsave.commoners.BaseActivity
import com.shaikjaleel.wsave.commoners.K
import com.shaikjaleel.wsave.models.Story
import com.shaikjaleel.wsave.utils.loadUrl
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
