package com.gelostech.whatsappstories.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gelostech.whatsappstories.R
import com.gelostech.whatsappstories.commoners.K
import com.gelostech.whatsappstories.models.Story
import com.gelostech.whatsappstories.utils.loadUrl
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        val story = intent.getSerializableExtra(K.STORY) as Story
        image.loadUrl(story.path!!)
    }
}
