package com.practicum.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {
    override fun shareLinkApp(linkApp: String, title: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, linkApp)
        }
        val chooser = Intent.createChooser(shareIntent, title)
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }

    override fun openEmailSupport(emailData: EmailData) {
        val shareIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.email))
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            putExtra(Intent.EXTRA_TEXT, emailData.message)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(shareIntent)
    }

    override fun openTermsLink(linkTerms: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(linkTerms)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun sharePlaylist(message: String, title: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
        }
        val chooser = Intent.createChooser(intent, title)
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }
}