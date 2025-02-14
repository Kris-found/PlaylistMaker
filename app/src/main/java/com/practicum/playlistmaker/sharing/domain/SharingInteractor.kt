package com.practicum.playlistmaker.sharing.domain

import com.practicum.playlistmaker.sharing.domain.model.EmailData

interface SharingInteractor {
    fun shareApp(linkApp: String, title: String)
    fun openSupport(emailData: EmailData)
    fun orenTerms(linkTerms: String)
}