package daniel.lop.io.marvelappstarter.data.model.event

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class EventModelData(
    @SerializedName("results")
    val results: List<EventModel>
): Serializable