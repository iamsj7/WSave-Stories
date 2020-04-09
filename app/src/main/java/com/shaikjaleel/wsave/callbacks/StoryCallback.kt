package com.shaikjaleel.wsave.callbacks

import android.view.View
import com.shaikjaleel.wsave.models.Story

interface StoryCallback {

    fun onStoryClicked(v: View, story: Story)

}