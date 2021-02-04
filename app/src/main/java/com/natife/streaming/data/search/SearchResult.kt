package com.natife.streaming.data.search

data class SearchResult (
        val id: Int,
        val name: String,
        val type: Type,
        val image: String,
        val placeholder: String,
        val sport: Int,
        val gender: Int
        ){
    enum class Type{
        PLAYER,
        TEAM,
        TOURNAMENT
    }
}