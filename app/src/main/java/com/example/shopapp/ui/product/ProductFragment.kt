package com.example.shopapp.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.shopapp.databinding.FragmentProductBinding
import com.example.shopapp.ui.adapters.ProductListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductFragment : Fragment() {

    private val productViewModel: ProductViewModel by viewModels()
    private var _binding: FragmentProductBinding? = null

    private lateinit var productAdapter: ProductListAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)

        productViewModel.searchInitialProducts()

        setupListeners()
        setupAdapters()
        setupObservers()

        return binding.root
    }

    private fun setupListeners() {

        binding.buttonProductDetails.setOnClickListener {
            val action = ProductFragmentDirections.displayProductDialog()
            requireView().findNavController().navigate(action)
        }

        binding.previousPageButton.setOnClickListener {
            productViewModel.handlePreviousButtonClick()
        }

        binding.nextPageButton.setOnClickListener {
            productViewModel.handleNextButtonClick()
        }
    }

    private fun setupAdapters() {

        productAdapter = ProductListAdapter() {
            val action = ProductFragmentDirections.displayProductDialog()
            action.id = it.id
            requireView().findNavController().navigate(action)
        }

        binding.statsRecyclerProduct.adapter = productAdapter
    }

    private fun setupObservers() {

        productViewModel.apply {

            getDisableAllButtonsBool().observe(
                viewLifecycleOwner,
                {
                    binding.groupButtons.isVisible = it
                }
            )

            getProductList().observe(
                viewLifecycleOwner,
                {
                    productAdapter.setProducts(it)
                }
            )

            getPreviousButtonBool().observe(
                viewLifecycleOwner,
                {
                    binding.previousPageButton.isEnabled = !it
                }
            )

            getNextButtonBool().observe(
                viewLifecycleOwner,
                {
                    binding.nextPageButton.isEnabled = !it
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
