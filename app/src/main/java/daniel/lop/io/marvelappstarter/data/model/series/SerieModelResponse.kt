package daniel.lop.io.marvelappstarter.data.model.series

import com.google.gson.annotations.SerializedName
import daniel.lop.io.marvelappstarter.data.model.series.SerieModelData
import java.io.Serializable

data class SerieModelResponse(
    @SerializedName("data")
    val data: SerieModelData
): Serializable
