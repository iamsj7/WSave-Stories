package com.shaikjaleel.wsave.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager

import com.shaikjaleel.wsave.R
import com.shaikjaleel.wsave.adapters.StoriesAdapter
import com.shaikjaleel.wsave.callbacks.StoryCallback
import com.shaikjaleel.wsave.commoners.AppUtils.isImage
import com.shaikjaleel.wsave.commoners.AppUtils.isVideo
import com.shaikjaleel.wsave.commoners.BaseFragment
import com.shaikjaleel.wsave.commoners.K
import com.shaikjaleel.wsave.commoners.StoryOverview
import com.shaikjaleel.wsave.models.PermissionsEvent
import com.shaikjaleel.wsave.models.RefreshStoriesEvent
import com.shaikjaleel.wsave.models.Story
import com.shaikjaleel.wsave.utils.RecyclerFormatter
import com.shaikjaleel.wsave.utils.hideView
import com.shaikjaleel.wsave.utils.showView
import kotlinx.android.synthetic.main.fragment_saved.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File

class SavedFragment : BaseFragment(), StoryCallback {
    private lateinit var adapter: StoriesAdapter

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        loadStories()
    }

    private fun initViews() {
        rv.setHasFixedSize(true)
        rv.layoutManager = GridLayoutManager(activity!!, 3)
        rv.addItemDecoration(RecyclerFormatter.GridItemDecoration(activity!!, 3, 5))
        rv.itemAnimator = DefaultItemAnimator()
        (rv.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false

        adapter = StoriesAdapter(this, activity!!)
        rv.adapter = adapter

        refresh.setOnRefreshListener {
            adapter.clearStories()
            loadStories()
        }
    }

    private fun loadStories() {
        if (!storagePermissionGranted()) {
            toast(getString(R.string.message_permissions_required))
            stopRefreshing()
            return
        }

        val dir = File(K.SAVED_STORIES)
        if (!dir.exists())
            dir.mkdirs()

        doAsync {
            val files = dir.listFiles { _, s ->
                s.endsWith(".png") || s.endsWith(".jpg") || s.endsWith(".jpeg") || s.endsWith(".mp4") || s.endsWith(".gif") }

            uiThread {

                if (files?.isNotEmpty() == true) {
                    hasStories()

                    for (file in files.sortedBy { it.lastModified() }.reversed()) {
                        var story = Story()

                        if (isImage(file)) {
                            story = Story(K.TYPE_IMAGE, file.absolutePath)
                        } else if (isVideo(file)) {
                            story = Story(K.TYPE_VIDEO, file.absolutePath)
                        }

                        adapter.addStory(story)
                    }

                } else {
                    noStories()
                }

                stopRefreshing()
            }

        }

    }

    private fun noStories() {
        rv?.hideView()
        empty?.showView()
    }

    private fun hasStories() {
        empty?.hideView()
        rv?.showView()
    }

    private fun stopRefreshing() {
        if (refresh.isRefreshing) refresh.isRefreshing = false
    }

    override fun onStoryClicked(v: View, story: Story) {
        val overview = StoryOverview(activity!!, story, true)
        overview.show()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPermissonsEvent(event: PermissionsEvent) {
        if (event.granted) {
            adapter.clearStories()
            loadStories()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshStoriesEvent(event: RefreshStoriesEvent) {
        adapter.clearStories()
        loadStories()
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

}
