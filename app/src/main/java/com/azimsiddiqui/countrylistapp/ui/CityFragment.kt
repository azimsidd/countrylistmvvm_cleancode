package com.azimsiddiqui.countrylistapp.ui

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azimsiddiqui.countrylistapp.R
import com.azimsiddiqui.countrylistapp.databinding.ActivityMainBinding
import com.azimsiddiqui.countrylistapp.util.ApiResult
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

const val COUNTRY_NAME: String = "name"

@AndroidEntryPoint
class CityFragment : Fragment(), CountryItemClickListener {

    private var countryName: String? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var countryRecyclerAdapter: CountryRecyclerAdapter

    private val viewModel: CountryViewModel by activityViewModels()

    companion object {
        fun newInstance(countryName: String) =
            CityFragment().apply {
                arguments = bundleOf(COUNTRY_NAME to countryName)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            countryName = it.getString(COUNTRY_NAME)
            viewModel.getCityList(country = countryName ?: "")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.title = getString(R.string.city_list)
        initRecyclerview()
        observeLiveEvent()

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s.toString().let { query ->
                    if (query.isNotEmpty()) {
                        viewModel.filterCity(query)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.filterCity(s.toString())
            }
        })
    }

    private fun observeLiveEvent() {
        viewModel.cityListLiveData.observe(viewLifecycleOwner) { cityResponse ->
            when (cityResponse) {
                is ApiResult.Loading -> {
                    showProgressBar(true)
                }
                is ApiResult.Success -> {
                    showProgressBar(true)
                    cityResponse.data?.let {
                        showProgressBar(false)
                        countryRecyclerAdapter.setData(it)
                    }
                }
                is ApiResult.Error -> {
                    showProgressBar(false)
                    cityResponse.message?.let {
                        Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun initRecyclerview() {
        binding.mainScreen.rvCountry.apply {
            layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
            countryRecyclerAdapter = CountryRecyclerAdapter(this@CityFragment)
            binding.mainScreen.rvCountry.adapter = countryRecyclerAdapter
            hasFixedSize()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun showProgressBar(isVisible: Boolean) {
        binding.mainScreen.pbLoading.visibility = if (isVisible) View.VISIBLE else View.GONE
        //binding.mainScreen.tvNoResult.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun onClick(country: String) {
        TODO("Not yet implemented")
    }

    override fun openBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog)
        bottomSheetDialog.show()
        val btn1 = bottomSheetDialog.findViewById<TextView>(R.id.btn1)
        val btn2 = bottomSheetDialog.findViewById<TextView>(R.id.btn2)
        val btn3 = bottomSheetDialog.findViewById<TextView>(R.id.btn3)

        selectButton(btn1 as TextView)
        btn1.setOnClickListener {
            selectButton(it as TextView)
            btn2?.apply { unSelectButton(this) }
            btn3?.apply { unSelectButton(this) }
        }
        btn2?.setOnClickListener {
            selectButton(it as TextView)
            btn1.apply { unSelectButton(this) }
            btn3?.apply { unSelectButton(this) }
        }
        btn3?.setOnClickListener {
            selectButton(it as TextView)
            btn2?.apply { unSelectButton(this) }
            btn1.apply { unSelectButton(this) }
        }
        bottomSheetDialog.dismissWithAnimation = true
    }

    private fun unSelectButton(view: TextView) {
        view.apply {
            setBackgroundResource(R.drawable.unselected_btn_bg)
            getCompoundDrawables().get(0)?.setTint(Color.BLACK)
            setTextColor(resources.getColor(R.color.teal_700))
        }
    }

    private fun selectButton(view: TextView) {
        view.apply {
            setBackgroundResource(R.drawable.selected_btn_bg)
            getCompoundDrawables().get(0)?.setTint(Color.WHITE)
            setTextColor(Color.WHITE)
        }
    }
}