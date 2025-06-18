package com.practicum.playlistmaker.sharing.domain

import com.practicum.playlistmaker.sharing.domain.model.EmailData

interface ExternalNavigator {
    fun shareLinkApp(linkApp: String, title: String)
    fun openEmailSupport(emailData: EmailData)
    fun openTermsLink(linkTerms: String)
    fun sharePlaylist(message: String, title: String)
}