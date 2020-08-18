package com.nerdinfusions.statusx.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nerdinfusions.statusx.R
import com.nerdinfusions.statusx.commoners.K
import com.nerdinfusions.statusx.models.Story
import com.nerdinfusions.statusx.utils.loadUrl
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        val story = intent.getSerializableExtra(K.STORY) as Story
        image.loadUrl(story.path!!)
    }
}
