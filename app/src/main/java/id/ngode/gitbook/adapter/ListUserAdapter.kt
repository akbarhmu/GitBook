package id.ngode.gitbook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import id.ngode.gitbook.databinding.ItemRowUserBinding
import id.ngode.gitbook.model.UserResponse

class ListUserAdapter(private val listUser: ArrayList<UserResponse>) : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    private lateinit var onUserClickCallback: OnUserClickCallback

    class ListViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    fun setOnUserClickCallback(onUserClickCallback: OnUserClickCallback) {
        this.onUserClickCallback = onUserClickCallback
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]
        holder.binding.apply {
            tvUsername.text = user.login
            tvLink.text = user.htmlUrl
            Glide.with(root)
                .load(user.avatarUrl)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
                .into(ivPhoto)
        }

        holder.itemView.setOnClickListener {
            onUserClickCallback.onUserClicked(listUser[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    interface OnUserClickCallback {
        fun onUserClicked(data: UserResponse)
    }
}