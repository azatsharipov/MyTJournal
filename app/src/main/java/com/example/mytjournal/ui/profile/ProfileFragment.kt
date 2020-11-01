package com.example.mytjournal.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.mytjournal.R

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private lateinit var btExit: Button
    private lateinit var btLogin: Button
    private lateinit var tvName: TextView
    private lateinit var ivAva: ImageView
    private var isLoggedIn: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_fragment, container, false)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        btExit = view.findViewById(R.id.bt_profile_exit)
        btLogin = view.findViewById(R.id.bt_profile_login)
        tvName = view.findViewById(R.id.tv_profile_name)
        ivAva = view.findViewById(R.id.iv_profile_ava)

        checkIsLoggedIn()
        if (isLoggedIn) {
            btLogin.visibility = View.GONE
        } else {
            btExit.visibility = View.GONE
            tvName.visibility = View.GONE
            ivAva.visibility = View.GONE
        }

        return view
    }

    fun checkIsLoggedIn() {
        isLoggedIn = false
    }

}