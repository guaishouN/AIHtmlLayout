package com.gyso.mygeminiapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gyso.mygeminiapplication.databinding.ActivityMainWebViewBinding
import com.lkl.opengl.BaseActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class MainWebViewActivity : BaseActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private var checkUrl = "http://127.0.0.1:7749/check_update"
    private var baseUrl = "http://127.0.0.1:7749"
    private lateinit var binding: ActivityMainWebViewBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.mainWebviewLayout.settings.javaScriptEnabled = true
        binding.mainWebviewLayout.webViewClient = WebViewClient()
        val settings = binding.mainWebviewLayout.settings
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        binding.mainWebviewLayout.loadUrl(baseUrl)

        startPolling()
    }


    private fun startPolling() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                checkForUpdate()
                handler.postDelayed(this, 5000)
            }
        }, 5000)
    }

    private fun checkForUpdate() {
        val request = Request.Builder().url(checkUrl).build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let {
                    Log.d("checkForUpdate", it)
                    val update = JSONObject(it).optBoolean("update", false)
                    if (update) {
                        val newHtml = JSONObject(it).optString("filename", "")
                        runOnUiThread {
                            binding.mainWebviewLayout.clearCache(true)
                            binding.mainWebviewLayout.loadUrl("$baseUrl/$newHtml?ts=${System.currentTimeMillis()}")  // 刷新页面
                        }
                    }
                }
            }
        })
    }
}