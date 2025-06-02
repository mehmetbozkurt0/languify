package com.example.proje

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class iletisim : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_iletisim, container, false)

        val textView = view.findViewById<TextView>(R.id.textView9)
        val textView1 = view.findViewById<TextView>(R.id.textView10)
        textView.text = "Mail Adresimiz : ik@dillerim.com"
        textView1.text = "Adres: Acarlar Mah. Şehit, Acarlar, Ali Karakuzu Sk. No:10, 45400 Turgutlu/Manisa"
            return view
    }
}