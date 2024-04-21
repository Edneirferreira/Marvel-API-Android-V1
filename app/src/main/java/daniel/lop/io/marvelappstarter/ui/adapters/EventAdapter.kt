package daniel.lop.io.marvelappstarter.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import daniel.lop.io.marvelappstarter.data.model.event.EventModel
import daniel.lop.io.marvelappstarter.databinding.ItemEventBinding

class EventAdapter: RecyclerView.Adapter<EventAdapter.EventViewHolder>(){

    inner class EventViewHolder(val binding: ItemEventBinding):
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<EventModel>(){

        override fun areItemsTheSame(oldItem: EventModel, newItem: EventModel): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: EventModel, newItem: EventModel): Boolean {
            return oldItem.id == newItem.id && oldItem.title == newItem.title && oldItem.description == newItem.description &&
                    oldItem.thumbnailModel.path == newItem.thumbnailModel.path && oldItem.thumbnailModel.extension == newItem.thumbnailModel.extension
        }
    }

    private val differ = AsyncListDiffer(this, differCallBack)

    var events: List<EventModel>
    get() = differ.currentList
    set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            ItemEventBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        var event = events[position]
        holder.binding.apply {
            tvNameEvent.text = event.title
            tvDescriptionEvent.text = event.description
            Glide.with(holder.itemView.context)
                .load(event.thumbnailModel.path + "." + event.thumbnailModel.extension)
                .into(imgEvent)
        }
    }
}


