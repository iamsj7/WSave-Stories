package com.nerdinfusions.statusx.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager

import com.nerdinfusions.statusx.R
import com.nerdinfusions.statusx.adapters.StoriesAdapter
import com.nerdinfusions.statusx.callbacks.StoryCallback
import com.nerdinfusions.statusx.commoners.BaseFragment
import com.nerdinfusions.statusx.commoners.K
import com.nerdinfusions.statusx.commoners.StoryOverview
import com.nerdinfusions.statusx.models.PermissionsEvent
import com.nerdinfusions.statusx.models.Story
import com.nerdinfusions.statusx.utils.RecyclerFormatter
import com.nerdinfusions.statusx.utils.hideView
import com.nerdinfusions.statusx.utils.showView
import kotlinx.android.synthetic.main.fragment_videos.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File

class VideosFragment : BaseFragment(), StoryCallback {
    private lateinit var adapter: StoriesAdapter

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_videos, container, false)
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

        val dir = File(K.WHATSAPP_STORIES)
        if (!dir.exists())
            dir.mkdirs()

        doAsync {
            val files = dir.listFiles { _, s ->
                s.endsWith(".mp4") || s.endsWith(".gif") }

            uiThread {

                if (files?.isNotEmpty() == true) {
                    hasStories()

                    for (file in files.sortedBy { it.lastModified() }.reversed()) {
                        val story = Story(K.TYPE_VIDEO, file.absolutePath)
                        adapter.addStory(story)
                    }

                } else {
                    noStories()
                }

                stopRefreshing()
            }

        }

        val gbdir = File(K.GBWHATSAPP_STORIES)
        if (!gbdir.exists())
            gbdir.mkdirs()

        doAsync {
            val files = gbdir.listFiles { _, s ->
                s.endsWith(".mp4") || s.endsWith(".gif") }

            uiThread {

                if (files?.isNotEmpty() == true) {
                    hasStories()

                    for (file in files.sortedBy { it.lastModified() }.reversed()) {
                        val story = Story(K.TYPE_VIDEO, file.absolutePath)
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
        val overview = StoryOverview(activity!!, story)
        overview.show()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPermissonsEvent(event: PermissionsEvent) {
        if (event.granted) {
            adapter.clearStories()
            loadStories()
        }
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

}
