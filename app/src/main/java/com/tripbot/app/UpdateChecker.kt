package com.tripbot.app

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

object UpdateChecker {
    private const val REPO_OWNER = "Deniskaaaz"
    private const val REPO_NAME = "TripBotApp"
    private const val CURRENT_VERSION = "1.0"

    fun checkForUpdate(context: Context) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://api.github.com/repos/$REPO_OWNER/$REPO_NAME/releases/latest")
                    .build()
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) return@launch

                val json = JSONObject(response.body()?.string() ?: return@launch)
                val tagName = json.getString("tag_name") // например, "v26"
                val assets = json.getJSONArray("assets")
                if (assets.length() == 0) return@launch

                val apkAsset = assets.getJSONObject(0)
                val downloadUrl = apkAsset.getString("browser_download_url")

                val latestVersion = tagName.removePrefix("v")
                if (isNewer(latestVersion, CURRENT_VERSION)) {
                    withContext(Dispatchers.Main) {
                        showUpdateDialog(context, downloadUrl)
                    }
                }
            } catch (e: Exception) {
                Log.e("UpdateChecker", "Check update error", e)
            }
        }
    }

    private fun isNewer(latest: String, current: String): Boolean {
        if (!latest.contains(".") || !current.contains(".")) {
            return false
        }
        val latestParts = latest.split(".").map { it.toIntOrNull() ?: 0 }
        val currentParts = current.split(".").map { it.toIntOrNull() ?: 0 }
        for (i in 0 until maxOf(latestParts.size, currentParts.size)) {
            val l = latestParts.getOrElse(i) { 0 }
            val c = currentParts.getOrElse(i) { 0 }
            if (l > c) return true
            if (l < c) return false
        }
        return false
    }

    private fun showUpdateDialog(context: Context, downloadUrl: String) {
        AlertDialog.Builder(context)
            .setTitle("Доступно обновление")
            .setMessage("Найдена новая версия. Обновить?")
            .setPositiveButton("Обновить") { _, _ ->
                downloadAndInstall(context, downloadUrl)
            }
            .setNegativeButton("Позже", null)
            .show()
    }

    private fun downloadAndInstall(context: Context, url: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            "Ошибка скачивания: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    return@launch
                }

                val apkFile = File(context.cacheDir, "update.apk")
                val inputStream = response.body()?.byteStream() ?: return@launch
                val outputStream = FileOutputStream(apkFile)
                inputStream.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }

                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    apkFile
                )

                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(uri, "application/vnd.android.package-archive")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e("UpdateChecker", "Install error", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Не удалось установить: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
