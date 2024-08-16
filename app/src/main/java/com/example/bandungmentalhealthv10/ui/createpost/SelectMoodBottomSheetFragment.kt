package com.example.bandungmentalhealthv10.ui.createpost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bandungmentalhealthv10.R
import com.example.bandungmentalhealthv10.data.model.ImageListData
import com.example.bandungmentalhealthv10.databinding.DialogBottomSelectMoodBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectMoodBottomSheetFragment(private val selectedMoodListener: SelectedMoodListener) :
    BottomSheetDialogFragment() {

    private var _binding: DialogBottomSelectMoodBinding? = null
    private val binding get() = _binding!!
    lateinit var moodList: ArrayList<ImageListData>
    lateinit var moodListAdapter: MoodListAdapter
    private var selectedMood: ImageListData? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogBottomSelectMoodBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvMoodList.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvMoodList.setHasFixedSize(true)
        binding.rvMoodList.isNestedScrollingEnabled = false

        moodList = ArrayList()
        addMoodListData()

        moodListAdapter =
            MoodListAdapter(
                moodList,
                onItemClicked = { item ->
                    selectedMood = item
                }
            )

        binding.rvMoodList.adapter = moodListAdapter

        binding.btNext.setOnClickListener {
            if (selectedMood != null) selectedMoodListener.selectedMood(selectedMood!!)
            dismiss()
        }

    }

    private fun addMoodListData() {
        moodList.add(ImageListData(R.drawable.emoticon_happy, "Senang"))
        moodList.add(ImageListData(R.drawable.emoticon_relaxed, "Santai"))
        moodList.add(ImageListData(R.drawable.emoticon_sad, "Sedih"))
        moodList.add(ImageListData(R.drawable.emoticon_afraid, "Takut"))
        moodList.add(ImageListData(R.drawable.emoticon_angry, "Marah"))
        moodList.add(ImageListData(R.drawable.emoticon_bored, "Bosan"))
        moodList.add(ImageListData(R.drawable.emoticon_ashamed, "Malu"))
        moodList.add(ImageListData(R.drawable.emoticon_frustrated, "Frustrasi"))
        moodList.add(ImageListData(R.drawable.emoticon_sick, "Sakit"))
        moodList.add(ImageListData(R.drawable.emoticon_depressed, "Depresi"))
        moodList.add(ImageListData(R.drawable.emoticon_anxious, "Cemas"))
        moodList.add(ImageListData(R.drawable.emoticon_exhausted, "Lelah"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface SelectedMoodListener {
        fun selectedMood(selectedMood: ImageListData)
    }

}