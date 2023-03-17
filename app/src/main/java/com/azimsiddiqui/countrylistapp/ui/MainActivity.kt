package com.azimsiddiqui.countrylistapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azimsiddiqui.countrylistapp.R
import com.azimsiddiqui.countrylistapp.databinding.ActivityMainBinding
import com.azimsiddiqui.countrylistapp.util.ApiResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), CountryItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: CountryViewModel by viewModels()
    private lateinit var countryRecyclerAdapter: CountryRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = getString(R.string.country_list)
        viewModel.getCountryList()
        supportActionBar?.hide()

        initRecyclerView()
        //observeFLow()
        observeLiveEvent()

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s.toString().let { query ->
                    if (query.isNotEmpty()) {
                        viewModel.filterCountry(query)
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {
                viewModel.filterCountry(s.toString())
            }
        })
    }

    private fun observeLiveEvent() {
        viewModel.countryListLiveData.observe(this) { countryResponse ->
            when (countryResponse) {
                is ApiResult.Loading -> {
                    showProgressBar(true)
                    noData(false)
                }
                is ApiResult.Success -> {
                    countryResponse.data?.let {
                        if(it.isEmpty()) noData(true)
                        showProgressBar(false)
                        countryRecyclerAdapter.setData(it)
                    } ?: noData(true)
                }
                is ApiResult.Error -> {
                    showProgressBar(false)
                    countryResponse.message?.let {
                        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun noData(isVisible: Boolean) {
        binding.mainScreen.tvNoResult.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun initRecyclerView() {
        binding.mainScreen.rvCountry.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            countryRecyclerAdapter = CountryRecyclerAdapter(this@MainActivity)
            binding.mainScreen.rvCountry.adapter = countryRecyclerAdapter
            hasFixedSize()
        }
    }

    private fun showProgressBar(isVisible: Boolean) {
        binding.mainScreen.pbLoading.visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.mainScreen.tvNoResult.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun onClick(country: String) {
        val cityFragment = CityFragment.newInstance(country)
        supportFragmentManager.beginTransaction()
            .replace(R.id.parent_container, cityFragment, "cityFragment").addToBackStack(null)
            .commit()
    }

    override fun openBottomSheet() {
        TODO("Not yet implemented")
    }
}
