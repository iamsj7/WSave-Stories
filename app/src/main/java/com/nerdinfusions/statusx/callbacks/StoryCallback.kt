package com.nerdinfusions.statusx.callbacks

import android.view.View
import com.nerdinfusions.statusx.models.Story

interface StoryCallback {

    fun onStoryClicked(v: View, story: Story)

}