package com.example.shopapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.shopapp.databinding.FragmentAuthBinding
import com.example.shopapp.models.ValidUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private val authViewModel: AuthViewModel by viewModels()
    private var _binding: FragmentAuthBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        val root: View = binding.root

        authViewModel.searchSavedToken()
        setupListeners()
        setupObservers()

        return root
    }

    private fun setupListeners() {

        binding.editTextPassword.doAfterTextChanged { text ->
            authViewModel.handleEmailInput(text.toString())
        }

        binding.editTextPassword.doAfterTextChanged { text ->
            authViewModel.handlePasswordInput(text.toString())
        }

        binding.loginHortensia.setOnClickListener {
            authViewModel.handleLoginButtonClick(ValidUser.HORTENSIA)
        }
        binding.loginTamar.setOnClickListener {
            authViewModel.handleLoginButtonClick(ValidUser.TAMAR)
        }
        binding.loginTruman.setOnClickListener {
            authViewModel.handleLoginButtonClick(ValidUser.TRUMAN)
        }
        binding.loginMichelina.setOnClickListener {
            authViewModel.handleLoginButtonClick(ValidUser.MICHELINA)
        }

        binding.loginButton.setOnClickListener { authViewModel.handleLoginButtonClick() }
        binding.logoutButton.setOnClickListener { authViewModel.handleLogoutButtonClick() }
    }

    private fun setupObservers() {

        authViewModel.apply {

            getLoginButtonEnabled().observe(
                viewLifecycleOwner,
                {
                    binding.loginButton.isEnabled = it
                }
            )

            getLogoutButtonEnabled().observe(
                viewLifecycleOwner,
                {
                    binding.logoutButton.isEnabled = it
                }
            )

            getTokenRetrievedResult().observe(
                viewLifecycleOwner,
                {
                    binding.apply {
                        textLoggedIn.text = it
                        editTextPassword.text.clear()
                        editTextUserEmail.text.clear()
                    }
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
