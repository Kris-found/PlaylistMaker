package com.practicum.playlistmaker.sharing.domain

import com.practicum.playlistmaker.sharing.domain.model.EmailData

class SharingInteractorImpl(private val externalNavigator: ExternalNavigator) : SharingInteractor {
    override fun shareApp(linkApp: String, title: String) {
        externalNavigator.shareLinkApp(linkApp, title)
    }

    override fun openSupport(emailData: EmailData) {
        externalNavigator.openEmailSupport(emailData)
    }

    override fun orenTerms(linkTerms: String) {
        externalNavigator.openTermsLink(linkTerms)
    }
}