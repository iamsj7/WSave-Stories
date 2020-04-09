package com.shaikjaleel.wsave.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shaikjaleel.wsave.R
import com.shaikjaleel.wsave.commoners.K
import com.shaikjaleel.wsave.models.Story
import com.shaikjaleel.wsave.utils.loadUrl
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        val story = intent.getSerializableExtra(K.STORY) as Story
        image.loadUrl(story.path!!)
    }
}
