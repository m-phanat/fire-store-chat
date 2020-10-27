package com.arukas.base.notification


data class NotificationContent(
    var content: Content = Content(),
    var include_player_ids: List<String> = listOf()
)

data class Content(
    var en: String = "",
)