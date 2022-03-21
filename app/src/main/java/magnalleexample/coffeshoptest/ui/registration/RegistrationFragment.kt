package magnalleexample.coffeshoptest.ui.registration

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import magnalleexample.coffeshoptest.R
import magnalleexample.coffeshoptest.app
import magnalleexample.coffeshoptest.databinding.LoginFragmentBinding
import magnalleexample.coffeshoptest.databinding.RegistrationFragmentBinding
import magnalleexample.coffeshoptest.ui.login.LoginFragmentDirections
import magnalleexample.coffeshoptest.ui.login.LoginViewModel

class RegistrationFragment : Fragment() {

    companion object {
        fun newInstance() = RegistrationFragment()
    }

    private lateinit var viewModel: RegistrationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : RegistrationFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.registration_fragment, container, false
        )
        val application = requireNotNull(this.activity).application
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Регистрация"
        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)

        binding.lifecycleOwner = this

        binding.registrationButton.setOnClickListener {
            viewModel.register(
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString(),
                binding.passwordRepeatEditText.text.toString())
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
                    RegistrationFragmentDirections.actionRegistrationFragmentToCoffeeShopsListFragment())
            }
        })

        return binding.root
    }

}