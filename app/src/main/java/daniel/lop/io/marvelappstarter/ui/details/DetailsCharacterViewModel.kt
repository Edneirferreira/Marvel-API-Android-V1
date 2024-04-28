package daniel.lop.io.marvelappstarter.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import daniel.lop.io.marvelappstarter.data.model.character.CharacterModel
import daniel.lop.io.marvelappstarter.data.model.comic.ComicModelResponse
import daniel.lop.io.marvelappstarter.data.model.event.EventModelResponse
import daniel.lop.io.marvelappstarter.data.model.series.SerieModelResponse
import daniel.lop.io.marvelappstarter.repository.MarvelRepository
import daniel.lop.io.marvelappstarter.ui.state.ResourceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DetailsCharacterViewModel @Inject constructor(
    private val repository: MarvelRepository
): ViewModel(){

    private val _details_comics =
        MutableStateFlow<ResourceState<ComicModelResponse>>(ResourceState.Loading())
    val detailsComic: StateFlow<ResourceState<ComicModelResponse>> = _details_comics

    private val _details_events =
        MutableStateFlow<ResourceState<EventModelResponse>>(ResourceState.Loading())
    val detailsEvents: StateFlow<ResourceState<EventModelResponse>> = _details_events

    private val _details_series =
        MutableStateFlow<ResourceState<SerieModelResponse>>(ResourceState.Loading())
    val detailsSeries: StateFlow<ResourceState<SerieModelResponse>> = _details_series

    fun fetchDetails(characterId: Int) = viewModelScope.launch {
        try {
            // Primeiro, definimos todos os estados como Loading
            _details_comics.value = ResourceState.Loading()
            _details_events.value = ResourceState.Loading()
            _details_series.value = ResourceState.Loading()

            // Em seguida, fazemos todas as chamadas de API
            val comicsResponse = repository.getComics(characterId)
            val eventsResponse = repository.getEvents(characterId)
            val seriesResponse = repository.getSeries(characterId)

            // Lidamos com as respostas individualmente
            _details_comics.value = handleResponse(comicsResponse)
            _details_events.value = handleResponseEvents(eventsResponse)
            _details_series.value = handleResponseSeries(seriesResponse)
        } catch (t: Throwable) {
            // Em caso de erro, tratamos todos os fluxos de dados da mesma forma
            val errorMessage = when (t) {
                is IOException -> "Erro de rede ou de conexão com a internet"
                else -> "Erro de conversão"
            }
            _details_comics.value = ResourceState.Error(errorMessage)
            _details_events.value = ResourceState.Error(errorMessage)
            _details_series.value = ResourceState.Error(errorMessage)
        }
    }

    private fun handleResponse(response: Response<ComicModelResponse>): ResourceState<ComicModelResponse> {
        if (response.isSuccessful){
            response.body()?.let { values ->
                return ResourceState.Success(values)
            }
        }
        return ResourceState.Error(response.message())
    }
    private fun handleResponseEvents(response: Response<EventModelResponse>): ResourceState<EventModelResponse> {
        if (response.isSuccessful){
            response.body()?.let { values ->
                return ResourceState.Success(values)
            }
        }
        return ResourceState.Error(response.message())
    }

    private fun handleResponseSeries(response: Response<SerieModelResponse>): ResourceState<SerieModelResponse> {
        if (response.isSuccessful){
            response.body()?.let { values ->
                return ResourceState.Success(values)
            }
        }
        return ResourceState.Error(response.message())
    }
    fun insert(characterModel: CharacterModel) = viewModelScope.launch {
        repository.insert(characterModel)
    }
}