package mr.shtein.buddyandroidclient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

class ProfileFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.login_fragment_start_button).setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }

        view.findViewById<Button>(R.id.registration_fragment_start_button).setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_userRegistrationFragment)
        }
    }
}