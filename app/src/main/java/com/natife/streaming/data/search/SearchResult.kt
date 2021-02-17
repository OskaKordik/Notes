package com.natife.streaming.data.search

import com.natife.streaming.ui.favorites.FavoritesAdapter

data class SearchResult (
        val id: Int,
        val name: String,
        val type: Type,
        val image: String,
        val placeholder: String,
        val sport: Int,
        val gender: Int
        ): FavoritesAdapter.Favorite {
    enum class Type{
        PLAYER,
        TEAM,
        TOURNAMENT,
        NON
    }
}