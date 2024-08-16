package com.example.bandungmentalhealthv10.ui.service

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.bandungmentalhealthv10.R
import com.example.bandungmentalhealthv10.databinding.FragmentServiceBinding
import com.example.bandungmentalhealthv10.ui.service.FiturCatatanKebaikan.CatatanKebaikanActivity
import com.example.bandungmentalhealthv10.ui.service.FiturKonsultasi.KonsultasiActivity
import com.example.bandungmentalhealthv10.ui.service.FiturMapLokasi.MapsActivity
import com.example.bandungmentalhealthv10.ui.service.FiturMeditasi.MeditasiActivity
import com.example.bandungmentalhealthv10.ui.service.FiturPanggilanDarurat.PanggilanDaruratActivity
import com.example.bandungmentalhealthv10.utils.FunPindah.pindahScene
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServiceFragment : Fragment(R.layout.fragment_service) {

    private lateinit var binding: FragmentServiceBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentServiceBinding.bind(view)

        binding.btMeditasi.setOnClickListener {
            pindahScene(requireContext(), MeditasiActivity::class.java)
        }

        binding.btCall.setOnClickListener {
            pindahScene(requireContext(), PanggilanDaruratActivity::class.java)
        }

        binding.btMaps.setOnClickListener {
            pindahScene(requireContext(), MapsActivity::class.java)
        }

        binding.btKonseling.setOnClickListener {
            pindahScene(requireContext(), KonsultasiActivity::class.java)
        }

        binding.btCatatan.setOnClickListener {
            pindahScene(requireContext(), CatatanKebaikanActivity::class.java)
        }
    }
}