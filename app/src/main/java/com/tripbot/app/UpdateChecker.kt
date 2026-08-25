package com.tripbot.app

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
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
    // Владелец и название репозитория на GitHub
    private const val REPO_OWNER = "Deniskaaaz"
    private const val REPO_NAME = "TripBotApp"
    // Текущая версия приложения (должна совпадать с versionName в build.gradle)
    private const val CURRENT_VERSION = "1.0"

    fun checkForUpdate(context: Context) {
        // Запускаем сетевой запрос в фоновом потоке, чтобы не блокировать UI
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://api.github.com/repos/$REPO_OWNER/$REPO_NAME/releases/latest")
                    .build()
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) return@launch

                val json = JSONObject(response.body?.string() ?: return@launch)
                val tagName = json.getString("tag_name") // например "v25"
                val assets = json.getJSONArray("assets")
                if (assets.length() == 0) return@launch

                // Берём первый asset — обычно это APK
                val apkAsset = assets.getJSONObject(0)
                val downloadUrl = apkAsset.getString("browser_download_url")

                // Убираем "v" из тега и сравниваем версии
                val latestVersion = tagName.removePrefix("v")
                if (isNewer(latestVersion, CURRENT_VERSION)) {
                    // Переключаемся на главный поток для показа диалога
                    withContext(Dispatchers.Main) {
                        showUpdateDialog(context, downloadUrl)
                    }
                }
            } catch (e: Exception) {
                // Логировать или просто игнорировать (нет соединения, ошибка парсинга и т.п.)
            }
        }
    }

    // Простое сравнение версий вида "1.2.3"
    private fun isNewer(latest: String, current: String): Boolean {
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

    // Показываем диалог с предложением обновиться
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

    // Скачиваем APK и запускаем установку
    private fun downloadAndInstall(context: Context, url: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) return@launch

                // Сохраняем во временную папку приложения
                val apkFile = File(context.cacheDir, "update.apk")
                val inputStream = response.body?.byteStream() ?: return@launch
                val outputStream = FileOutputStream(apkFile)
                inputStream.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }

                // Получаем content:// URI через FileProvider
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    apkFile
                )

                // Создаём Intent для установки APK
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(uri, "application/vnd.android.package-archive")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } catch (e: Exception) {
                // Ошибка скачивания или установки
            }
        }
    }
}
