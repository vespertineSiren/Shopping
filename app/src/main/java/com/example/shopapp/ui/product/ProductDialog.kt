package com.example.shopapp.ui.product

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.shopapp.databinding.DialogProductBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDialog : DialogFragment() {

    private val productDialogViewModel: ProductDialogViewModel by viewModels()
    private var _binding: DialogProductBinding? = null

    val args: ProductDialogArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogProductBinding.inflate(inflater, container, false)

        productDialogViewModel.apply {
            searchProductById(args.id)
        }

        setupListeners()
        setupObservers()

        return binding.root
    }

    private fun setupListeners() {

        binding.detailNameEditText.doAfterTextChanged {
            productDialogViewModel
                .handleProductFieldInputs(it.toString(), ProductDialogViewModel.NAME)
        }

        binding.detailPriceEditText.doAfterTextChanged {
            productDialogViewModel
                .handleProductFieldInputs(it.toString(), ProductDialogViewModel.SHIP)
        }

        binding.detailDescEditText.doAfterTextChanged {
            productDialogViewModel
                .handleProductFieldInputs(it.toString(), ProductDialogViewModel.DESC)
        }

        binding.detailSaveButton.setOnClickListener {
            productDialogViewModel.handleSaveButtonClick()
        }

        binding.detailCancelButton.setOnClickListener { dismiss() }
    }

    private fun setupObservers() {

        productDialogViewModel.apply {

            getFoundProduct().observe(
                viewLifecycleOwner,
                {
                    if (it != null) {
                        binding.apply {
                            detailNameEditText.setText(it.productName)
                            detailBrandEditText.setText(it.brand)
                            detailPriceEditText.setText(it.shippingPrice.toString())
                            detailDescEditText.setText(it.description)
                            detailSaveButton.text = "Update"
                        }
                    }
                }
            )

            getStylesList().observe(
                viewLifecycleOwner,
                { primaryList ->
                    val newAdapter = ArrayAdapter(
                        requireContext(),
                        R.layout.simple_spinner_item,
                        primaryList
                    ).apply {
                        setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                    }

                    binding.detailSpinnerStyle.adapter = newAdapter
                    binding.detailSpinnerStyle.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                position: Int,
                                p3: Long
                            ) {
                                productDialogViewModel.handleProductFieldInputs(
                                    primaryList[position],
                                    ProductDialogViewModel.STYLE
                                )
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                // Do Nothing
                            }
                        }
                }
            )

            getFoundStyleIndex().observe(
                viewLifecycleOwner,
                {
                    binding.detailSpinnerStyle.setSelection(it, true)
                }
            )
        }
    }
}
