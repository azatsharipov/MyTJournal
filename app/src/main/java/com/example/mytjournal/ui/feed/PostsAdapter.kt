package com.example.mytjournal.ui.feed

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mytjournal.R
import com.example.mytjournal.data.model.Post
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView

class PostsAdapter(var posts: MutableList<Post>) :
    RecyclerView.Adapter<PostsAdapter.VideoPostsViewHolder>() {

    lateinit var player: SimpleExoPlayer

/*
    class PostsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvTitle: TextView = view.findViewById(R.id.tv_post_item_title)
        var ivContent: ImageView = view.findViewById(R.id.iv_post_item)
    }
*/

    class VideoPostsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvTitle: TextView = view.findViewById(R.id.tv_videopost_item_title)
        var vvContent: PlayerView = view.findViewById(R.id.vv_videopost_item)
        lateinit var mediaItem: MediaItem
        lateinit var player: SimpleExoPlayer
        var volume: Float = 0f
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoPostsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.videopost_item, parent, false) as View
        return VideoPostsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onViewAttachedToWindow(holder: VideoPostsViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.mediaItem = MediaItem.fromUri(Uri.parse(posts[holder.position].cover?.url))
        holder.player.setMediaItem(holder.mediaItem)
        holder.player.setPlayWhenReady(true);
        holder.player.seekTo(0)
        holder.vvContent.hideController()
        holder.vvContent.setControllerVisibilityListener {
            if (holder.player.volume == 0f)
                holder.player.volume = holder.volume
            else {
                holder.volume = holder.player.volume
                holder.player.volume = 0f
            }
        }
        holder.volume = holder.player.volume
        holder.player.volume = 0f
        holder.player.prepare();
        holder.vvContent.player = holder.player
    }

    override fun onViewDetachedFromWindow(holder: VideoPostsViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.player.release()
    }

    override fun onBindViewHolder(holder: VideoPostsViewHolder, position: Int) {
        holder.tvTitle.setText(posts[position].title)
        holder.player = SimpleExoPlayer.Builder(holder.itemView.context).build()
        /*
        holder.mediaItem = MediaItem.fromUri(Uri.parse(posts[position].cover?.url))
        holder.player.setMediaItem(holder.mediaItem)

         */
        /*
        holder.player.setPlayWhenReady(true);
        holder.player.seekTo(0)
        holder.player.prepare();
        */
        holder.vvContent.player = holder.player
    }
}