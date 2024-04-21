package daniel.lop.io.marvelappstarter.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import daniel.lop.io.marvelappstarter.data.model.series.SerieModel
import daniel.lop.io.marvelappstarter.databinding.ItemSerieBinding

class SerieAdapter: RecyclerView.Adapter<SerieAdapter.SerieViewHolder>(){

    inner class SerieViewHolder(val binding: ItemSerieBinding):
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<SerieModel>(){

        override fun areItemsTheSame(oldItem: SerieModel, newItem: SerieModel): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: SerieModel, newItem: SerieModel): Boolean {
            return oldItem.id == newItem.id && oldItem.title == newItem.title && oldItem.description == newItem.description &&
                    oldItem.thumbnailModel.path == newItem.thumbnailModel.path && oldItem.thumbnailModel.extension == newItem.thumbnailModel.extension
        }
    }

    private val differ = AsyncListDiffer(this, differCallBack)

    var series: List<SerieModel>
    get() = differ.currentList
    set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SerieViewHolder {
        return SerieViewHolder(
            ItemSerieBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = series.size

    override fun onBindViewHolder(holder: SerieViewHolder, position: Int) {
        var serie = series[position]
        holder.binding.apply {
            tvNameSerie.text = serie.title
            tvDescriptionSerie.text = serie.description
            Glide.with(holder.itemView.context)
                .load(serie.thumbnailModel.path + "." + serie.thumbnailModel.extension)
                .into(imgSerie)
        }
    }
}


