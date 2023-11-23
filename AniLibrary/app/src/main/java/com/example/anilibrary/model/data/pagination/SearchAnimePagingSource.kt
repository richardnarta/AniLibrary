package com.example.anilibrary.model.data.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.anilibrary.model.data.pojo.AnimeData
import com.example.anilibrary.model.data.pojo.AnimeNode
import com.example.anilibrary.model.network.ApiConfig

class SearchAnimePagingSource(private val query:String): PagingSource<Int, AnimeNode>() {
    override fun getRefreshKey(state: PagingState<Int, AnimeNode>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(10)?:anchorPage?.nextKey?.minus(10)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AnimeNode> {
        return try {
            val currentOffset = params.key?:0
            val response = ApiConfig.getApiService().getSearchAnime(query,currentOffset)
            val dataAnime = response.body()?.data as List<AnimeData>
            val responseData = mutableListOf<AnimeNode>()
            for(data in dataAnime){
                responseData.add(data.node)
            }

            LoadResult.Page(
                data = responseData,
                prevKey = if(currentOffset == 0) null else -10,
                nextKey = currentOffset.plus(10)
            )
        }catch (e:Exception){
            LoadResult.Error(e)
        }
    }

}