package learn.daniel.rickAndMortyNew.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import learn.daniel.rickAndMortyNew.network.response.GetCharacterByIdResponse
import androidx.paging.PageKeyedDataSource
import learn.daniel.rickAndMortyNew.repository.CharactersRepository

class CharactersDataSource(
    private val coroutineScope:CoroutineScope,
    private val repository: CharactersRepository
): PageKeyedDataSource<Int, GetCharacterByIdResponse>(){
    override fun loadInitial(
        params: LoadInitialParams<Int> ,
        callback: LoadInitialCallback<Int, GetCharacterByIdResponse>
    ) {
        coroutineScope.launch {
            val page = repository.getCharactersPage(1)

            if (page == null){
                callback.onResult(emptyList() ,null,null)
                return@launch
            }
            callback.onResult(page.results,null,getPageIndexFromNext(page.info.next))
        }
    }
    override fun loadAfter(
        params: LoadParams<Int> ,
        callback: LoadCallback<Int , GetCharacterByIdResponse>
    ) {
        coroutineScope.launch {
            val page = repository.getCharactersPage(params.key)

            if (page == null) {
                callback.onResult(emptyList() , null)
                return@launch
            }
            callback.onResult(page.results, getPageIndexFromNext(page.info.next))
        }
        }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int , GetCharacterByIdResponse>
    ) {
        TODO("Not yet implemented")
    }
    private fun getPageIndexFromNext(next: String?):Int?{
        return next?.split("?page=")?.get(1)?.toInt()
    }
}