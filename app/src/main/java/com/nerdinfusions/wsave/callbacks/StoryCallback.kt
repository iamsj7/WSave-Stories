package com.nerdinfusions.wsave.callbacks

import android.view.View
import com.nerdinfusions.wsave.models.Story

interface StoryCallback {

    fun onStoryClicked(v: View, story: Story)

}