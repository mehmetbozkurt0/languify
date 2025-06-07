package com.example.languify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class kvkk : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_kvkk, container, false)

        val textView = view.findViewById<TextView>(R.id.textView8)
        textView.text = "Languify uygulaması olarak, 6698 sayılı Kişisel Verilerin Korunması Kanunu (“KVKK”) kapsamında kişisel verilerinizi korumaya önem veriyoruz.\n" +
                "\n" +
                "Uygulamamızda; ad, yaş, kullanıcı adı ve öğrenme verileriniz gibi bilgiler, yalnızca dil öğrenme sürecinizi geliştirmek ve uygulama hizmetlerini sunmak amacıyla işlenmektedir.\n" +
                "\n" +
                "Kişisel verileriniz, hiçbir şekilde üçüncü kişilerle paylaşılmaz ve yasal zorunluluklar dışında aktarılmaz. 18 yaş altı kullanıcıların verileri, ebeveyn onayı alınarak işlenmektedir.\n" +
                "\n" +
                "Daha fazla bilgi için bize kvkk@languify.com.tr adresinden ulaşabilirsiniz."

        return view
    }
}

