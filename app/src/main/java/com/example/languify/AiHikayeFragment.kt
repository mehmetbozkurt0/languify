package com.example.languify

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.languify.databinding.FragmentAiHikayeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class AIHikayeFragment : Fragment() {

    private var _binding: FragmentAiHikayeBinding? = null
    private val binding get() = _binding!!

    private lateinit var kelimeListesi: List<String>
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAiHikayeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Öğrenilen kelimeleri yükle
        lifecycleScope.launch {
            loadLearnedWords()
        }

        // Hikaye Üret butonu
        binding.btnHikayeUret.setOnClickListener {
            val selectedWords = mutableListOf<String>()
            val checkedPositions = binding.listView.checkedItemPositions

            for (i in 0 until checkedPositions.size()) {
                val pos = checkedPositions.keyAt(i)
                if (checkedPositions.valueAt(i)) {
                    selectedWords.add(kelimeListesi[pos])
                }
            }

            if (selectedWords.size != 5) {
                Toast.makeText(requireContext(), "Lütfen 5 kelime seçin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                generateStory(selectedWords)
            }
        }
    }

    //Öğrenilen kelimeleri yükle
    private suspend fun loadLearnedWords() {
        withContext(Dispatchers.IO) {
            val db = UserDatabase.getDatabase(requireContext())
            val learnedWords = db.WordDao().getLearnedWordsForAI()

            kelimeListesi = learnedWords.map { it.EngWordName }
            withContext(Dispatchers.Main) {
                adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_multiple_choice, kelimeListesi)
                binding.listView.adapter = adapter
                binding.listView.choiceMode = android.widget.ListView.CHOICE_MODE_MULTIPLE
            }
        }
    }

    private suspend fun generateStory(selectedWords: List<String>) {
        //apikey'i dosya içinde yazmak hatalı ama hızlı olması için burada çalışıyor
        val apiKey = "AIzaSyDBr1FY-vmxTuPqTnYypXmKZzrfiGH08yY"
        val url = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-pro:generateContent?key=$apiKey"

        val prompt = "Bu 5 kelimeyi içeren kısa ve eğlenceli bir hikaye yaz. Sana verdiğim kelimelerin tüm harfleri hikaye içinde büyük olsun. Toplam 500 karakteri geçmesin: ${selectedWords.joinToString(", ")}"

        val requestBodyJson = JSONObject()
        val contentsArray = JSONArray()
        val partObject = JSONObject().put("text", prompt)
        val contentObject = JSONObject().put("parts", JSONArray().put(partObject))
        contentsArray.put(contentObject)

        requestBodyJson.put("contents", contentsArray)

        val mediaType = "application/json".toMediaTypeOrNull()
        val requestBody = requestBodyJson.toString().toRequestBody(mediaType)

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (response.isSuccessful && responseBody != null) {
                    val jsonResponse = JSONObject(responseBody)
                    val candidates = jsonResponse.getJSONArray("candidates")
                    val content = candidates.getJSONObject(0).getJSONObject("content")
                    val parts = content.getJSONArray("parts")
                    val story = parts.getJSONObject(0).getString("text")

                    withContext(Dispatchers.Main) {
                        binding.txtHikaye.text = story
                    }
                } else {
                    Log.e("GeminiAPI", "Error: ${response.code} - $responseBody")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Hikaye oluşturulamadı!", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("GeminiAPI", "Exception: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Hata: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
