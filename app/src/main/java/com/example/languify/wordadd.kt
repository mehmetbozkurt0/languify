package com.example.languify

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.languify.databinding.FragmentWordadd2Binding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate

class wordadd : Fragment() {

    private var _binding: FragmentWordadd2Binding? = null
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private val binding get() = _binding!!
    private var secilengorsel: Uri?=null
    private var secilenmap: Bitmap?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLauncher()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentWordadd2Binding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button6.setOnClickListener {
            kaydet(it)
        }
        binding.imageView2.setOnClickListener {
            gorsel(it)
        }
        binding.imageView14.setOnClickListener {
            addtomenu(it)
        }
        binding.imageView15.setOnClickListener {
            addtoayarlar(it)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun kaydet(view: View) {
        val EngWordName = binding.editTextText2.text.toString()
        val TurWordName = binding.editTextText3.text.toString()
        val WordSamples=binding.editTextText4.text.toString()

        if (EngWordName.isEmpty() || TurWordName.isEmpty()) {
            Toast.makeText(requireContext(), "Lütfen Boş Bırakmayınız", Toast.LENGTH_LONG).show()
            return
        }

        val imagePath = secilengorsel?.let { saveImageToInternalStorage(it) }
        val sharedPref = requireActivity().getSharedPreferences("user_prefs", 0)
        val userId = sharedPref.getInt("userId", -1)
        val word = Word(
            EngWordName = EngWordName,
            TurWordName = TurWordName,
            imagepath = imagePath,
            id = userId,
            WordSamples = WordSamples
        )

        lifecycleScope.launch {
            val db = UserDatabase.getDatabase(requireContext())
            // Word tablosuna ekle, ID’yi al
            val insertedId = db.WordDao().insert(word).toInt()
            // WordProgress kaydı oluştur
            val today = LocalDate.now().toString()
            val progress = WordProgress(
                word_id = insertedId,
                user_id = userId,
                repetition_stage = 0,
                next_due_date = today,
                last_answered_date = today,
                is_learned = 0
            )
            db.WordProgressDao().insertProgress(progress)
            // ViewModel güncelle & geri dön
            val viewModel = ViewModelProvider(requireActivity())[WordViewModel::class.java]
            viewModel.refreshWords(requireContext())
            findNavController().popBackStack()
        }
    }
    fun gorsel(view: View){
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
                if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.READ_MEDIA_IMAGES)!= PackageManager.PERMISSION_GRANTED){
//İZİN VERİLMEMİS
                    if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                            Manifest.permission.READ_MEDIA_IMAGES)){
                        //snackbar gostermek gerekıyor
                        Snackbar.make(view,"Galeriye gorsel secmek ıcın gırmek gerekıyor", Snackbar.LENGTH_INDEFINITE).setAction("izin ver",
                            View.OnClickListener {
                                //İZİN İSTEYECEGİZ
                                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                            }).show()
                    }
                    else{
                        //izin isteyecegiz
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    }
                }
                else{
                    //İzin verilmis galeriye gidebilirim
                    val intenttogallery= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intenttogallery)
                }
            }
            else{
                if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
//İZİN VERİLMEMİS
                    if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE)){
                        //snackbar gostermek gerekıyor
                        Snackbar.make(view,"Galeriye gorsel secmek ıcın gırmek gerekıyor", Snackbar.LENGTH_INDEFINITE).setAction("izin ver",
                            View.OnClickListener {
                                //İZİN İSTEYECEGİZ
                                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                            }).show()
                    }
                    else{
                        //izin isteyecegiz
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                }
                else{
                    //İzin verilmis galeriye gidebilirim
                    val intenttogallery= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intenttogallery)
                }
            }


        }
    private fun registerLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    val intentfromresult = result.data
                    if (intentfromresult != null) {
                        secilengorsel = intentfromresult.data
                        try {
                            if (Build.VERSION.SDK_INT >= 28) {
                                val source = ImageDecoder.createSource(requireActivity().contentResolver, secilengorsel!!)
                                secilenmap = ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
                                    val targetSize = 1024
                                    val scale = targetSize.toFloat() / maxOf(info.size.width, info.size.height).toFloat()
                                    if (scale < 1.0f) {
                                        decoder.setTargetSize(
                                            (info.size.width * scale).toInt(),
                                            (info.size.height * scale).toInt()
                                        )
                                    }
                                }
                                binding.imageView2.setImageBitmap(secilenmap)
                            } else {
                                secilenmap = MediaStore.Images.Media.getBitmap(
                                    requireActivity().contentResolver,
                                    secilengorsel
                                )
                                binding.imageView2.setImageBitmap(secilenmap)
                            }
                        } catch (e: Exception) {
                            println(e.localizedMessage)
                        }


                    }
                }
            }


        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    //izin verildi
                    val intenttogallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intenttogallery)
                } else {
                    //izin verilmedi
                    Toast.makeText(requireContext(), "izin verilmedi", Toast.LENGTH_LONG).show()
                }
            }

    }

    fun saveImageToInternalStorage(uri: Uri): String? {
        return try {
            val contentResolver = requireContext().contentResolver
            val mimeType = contentResolver.getType(uri)

            val extension = when (mimeType) {
                "image/png" -> ".png"
                "image/jpeg", "image/jpg" -> ".jpg"
                else -> ".jpg" // default fallback
            }

            // 1️⃣ Bitmap'i oku
            val source = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.createSource(contentResolver, uri)
            } else {
                null
            }

            val originalBitmap = if (source != null) {
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(contentResolver, uri)
            }

            // 2️⃣ Bitmap'i küçült (örnek: max 1024 px)
            val targetSize = 1024
            val scale = targetSize.toFloat() / maxOf(originalBitmap.width, originalBitmap.height).toFloat()
            val scaledBitmap = if (scale < 1.0f) {
                Bitmap.createScaledBitmap(
                    originalBitmap,
                    (originalBitmap.width * scale).toInt(),
                    (originalBitmap.height * scale).toInt(),
                    true
                )
            } else {
                originalBitmap
            }

            // 3️⃣ Dosya kaydet
            val fileName = "img_${System.currentTimeMillis()}$extension"
            val file = File(requireContext().filesDir, fileName)
            val outputStream = FileOutputStream(file)

            if (extension == ".png") {
                scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            } else {
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
            }

            outputStream.flush()
            outputStream.close()

            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun addtoayarlar(view: View){
        val action = wordaddDirections.actionWordaddToAyarlar()
        Navigation.findNavController(view).navigate(action)
    }

    fun addtomenu(view: View){
        val action = wordaddDirections.actionWordaddToMenu()
        Navigation.findNavController(view).navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}