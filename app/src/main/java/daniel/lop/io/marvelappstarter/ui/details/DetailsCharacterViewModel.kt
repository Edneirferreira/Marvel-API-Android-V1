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

    fun fetch(characterId: Int) = viewModelScope.launch{
        safeFetch(characterId)
    }

    fun fetchEvent(characterId: Int) = viewModelScope.launch{
        safeFetch(characterId)
    }

    fun fetchSeries(characterId: Int) = viewModelScope.launch{
        safeFetch(characterId)
    }


    private suspend fun safeFetch(characterId: Int){
        _details_comics.value = ResourceState.Loading()
        try {
            val response = repository.getComics(characterId)
            _details_comics.value = handResponse(response)
        }catch (t: Throwable){
            when(t){
                is IOException -> _details_comics.value =
                    ResourceState.Error("Erro de rede ou de conexão com a internet")
                else -> _details_comics.value = ResourceState.Error("Erro de conversão")
            }
        }
        _details_events.value = ResourceState.Loading()
        try {
            val response = repository.getEvents(characterId)
            _details_events.value = handResponseEvents(response)
        }catch (t: Throwable){
            when(t){
                is IOException -> _details_events.value =
                    ResourceState.Error("Erro de rede ou conexão com a internet")
                else -> _details_events.value = ResourceState.Error("Erro de conversão")
            }
        }
        _details_series.value = ResourceState.Loading()
        try {
            val response = repository.getSeries(characterId)
            _details_series.value = handResponseSeries(response)
        }catch (t: Throwable){
            when(t){
                is IOException -> _details_series.value =
                    ResourceState.Error("Erro de rede ou de conexão com a internet")
                else -> _details_series.value = ResourceState.Error("Erro de conversão")
            }
        }
    }

    private fun handResponse(response: Response<ComicModelResponse>): ResourceState<ComicModelResponse> {
        if (response.isSuccessful){
            response.body()?.let { values ->
                return ResourceState.Success(values)
            }
        }
        return ResourceState.Error(response.message())
    }
    private fun handResponseEvents(response: Response<EventModelResponse>): ResourceState<EventModelResponse> {
        if (response.isSuccessful){
            response.body()?.let { values ->
                return ResourceState.Success(values)
            }
        }
        return ResourceState.Error(response.message())
    }

    private fun handResponseSeries(response: Response<SerieModelResponse>): ResourceState<SerieModelResponse> {
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