package pl.tkadziolka.snipmeandroid.ui.share

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_item_user_share.view.*
import pl.tkadziolka.snipmeandroid.R
import pl.tkadziolka.snipmeandroid.domain.share.ShareUser
import pl.tkadziolka.snipmeandroid.util.extension.inflate
import pl.tkadziolka.snipmeandroid.util.extension.loadWithFallback

typealias ShareActionListener = (ShareUser) -> Unit

class ShareUserAdapter(
    private val shareListener: ShareActionListener
) : ListAdapter<ShareUser, ShareUserAdapter.ShareUserHolder>(SHARE_DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareUserHolder =
        ShareUserHolder(parent.inflate(R.layout.view_item_user_share), shareListener)

    override fun onBindViewHolder(holder: ShareUserHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ShareUserHolder(
        view: View,
        private val shareListener: ShareActionListener
    ) : RecyclerView.ViewHolder(view) {
        fun bind(shareUser: ShareUser) = with(itemView) {
            shareUserName.text = shareUser.user.login
            setupShareAction(shareUser)
            shareUserImage.loadWithFallback(shareUser.user.photo)
        }

        private fun View.setupShareAction(user: ShareUser) {
            with(shareUserAction) {
                val shared = user.isShared
                text = getActionText(shared, context)
                isEnabled = shared.not()
                setOnClickListener { shareListener(user) }
            }
        }

        private fun getActionText(shared: Boolean, context: Context) =
            if (shared)
                context.getString(R.string.shared)
            else
                context.getString(R.string.share)
    }
}

private val SHARE_DIFF = object : DiffUtil.ItemCallback<ShareUser>() {

    override fun areItemsTheSame(oldItem: ShareUser, newItem: ShareUser): Boolean =
        oldItem.user.login == newItem.user.login

    override fun areContentsTheSame(oldItem: ShareUser, newItem: ShareUser): Boolean =
        oldItem == newItem
}