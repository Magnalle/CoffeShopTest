package magnalleexample.coffeshoptest.ui.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import magnalleexample.coffeshoptest.App
import magnalleexample.coffeshoptest.R
import magnalleexample.coffeshoptest.app
import magnalleexample.coffeshoptest.databinding.LoginFragmentBinding
import androidx.appcompat.app.AppCompatActivity




class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding : LoginFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.login_fragment, container, false
        )
        val application = requireNotNull(this.activity).application
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Вход"

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.loginButton.setOnClickListener {
            viewModel.login(binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())
        }

        binding.registrationButton.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToRegistrationFragment())
        }

        viewModel.displayError.observe(viewLifecycleOwner, { value ->
            value?.let { value ->
                Toast.makeText(application, value, Toast.LENGTH_LONG).show()
                viewModel.disableError()
            }
        })

        viewModel.navigateToCoffeeShopList.observe(viewLifecycleOwner, {
            if(it){
                viewModel.disableNavigation()
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToCoffeeShopsListFragment())
            }
        })
        return binding.root
    }


}