package daniel.lop.io.marvelappstarter.data.model.series

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SerieModelData(
    @SerializedName("results")
    val results: List<SerieModel>
): Serializable
