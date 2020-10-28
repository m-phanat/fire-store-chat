package com.arukas.network.notification


data class NotificationContent(
    var headings: Heading = Heading(),
    var contents: Content = Content(),
    var include_player_ids: List<String> = listOf()
)

data class Content(
    var en: String = "",
)

data class Heading(
    var en: String = "you have a new messages",
)