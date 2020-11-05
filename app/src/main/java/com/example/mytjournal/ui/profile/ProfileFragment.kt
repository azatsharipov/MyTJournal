package com.example.mytjournal.ui.profile

import android.Manifest
import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.AccountManagerCallback
import android.accounts.AccountManagerFuture
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.bumptech.glide.Glide
import com.example.mytjournal.R
import com.example.mytjournal.data.model.User


class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    val PERMISSION_REQUEST_CODE = 200
    private lateinit var viewModel: ProfileViewModel
    private lateinit var btExit: Button
    private lateinit var btAuth: Button
    private lateinit var tvName: TextView
    private lateinit var ivAva: ImageView
    private lateinit var scannerView: CodeScannerView
    private lateinit var scanner: CodeScanner
    private var isLogin: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_fragment, container, false)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        btExit = view.findViewById(R.id.bt_profile_exit)
        btAuth = view.findViewById(R.id.bt_profile_auth)
        tvName = view.findViewById(R.id.tv_profile_name)
        ivAva = view.findViewById(R.id.iv_profile_ava)
        scannerView = view.findViewById(R.id.scanner_profile)

        scanner = CodeScanner(activity as Context, scannerView)
        scanner.decodeCallback = DecodeCallback {
            viewModel.auth(it.text)
        }

        btAuth.setOnClickListener {
            if (checkPermission()) {
                startScanning()
                scanner.startPreview()
            } else {
                requestPermission()
            }
        }

        btExit.setOnClickListener {
            deleteUser()
            openLogin()
        }

        viewModel.user.observe(this as LifecycleOwner, Observer {
            showUser(it)
        })

        checkIsLogin()
        if (isLogin) {
            btAuth.visibility = View.GONE
        } else {
            btExit.visibility = View.GONE
            tvName.visibility = View.GONE
            ivAva.visibility = View.GONE
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        scanner.startPreview()
    }

    override fun onPause() {
        scanner.releaseResources()
        super.onPause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                startScanning()
                scanner.startPreview()
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(activity as Context, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        showMessageOKCancel("Вам нужно разрешить использование камеры",
                            DialogInterface.OnClickListener { dialog, which ->
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermission()
                                }
                            })
                    }
                }
            }
        }
    }



    fun showMessageOKCancel(
        message: String,
        okListener: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(activity)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Отмена", null)
            .create()
            .show()
    }

    fun showUser(user: User?) {
        if (user != null) {
            openUser()
            saveUser(user)
            tvName.setText(user.name)
            Glide.with(this).load(user.ava).into(ivAva)
        } else {
            openLogin()
            Toast.makeText(activity, "Не удалось авторизоваться", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveUser(user: User?) {
        val sPref = activity?.getPreferences(MODE_PRIVATE)
        val editor = sPref?.edit()
        editor?.putString("name", user?.name)
        editor?.putString("ava", user?.ava)
        editor?.apply()
    }

    fun deleteUser() {
        val sPref = activity?.getPreferences(MODE_PRIVATE)
        val editor = sPref?.edit()
        editor?.remove("name")
        editor?.remove("ava")
        editor?.apply()
    }

    fun checkPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(activity as Context, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            return false
        }
        return true
    }

    fun requestPermission() {
        ActivityCompat.requestPermissions(activity as Activity,
            arrayOf(Manifest.permission.CAMERA),
            PERMISSION_REQUEST_CODE)
    }

    fun startScanning() {
        scannerView.visibility = View.VISIBLE
        btAuth.visibility = View.GONE
        tvName.visibility = View.GONE
        ivAva.visibility = View.GONE
        btExit.visibility = View.GONE
    }

    fun openUser() {
        scannerView.visibility = View.GONE
        btAuth.visibility = View.GONE
        tvName.visibility = View.VISIBLE
        ivAva.visibility = View.VISIBLE
        btExit.visibility = View.VISIBLE
    }

    fun openLogin() {
        scannerView.visibility = View.GONE
        btAuth.visibility = View.VISIBLE
        tvName.visibility = View.GONE
        ivAva.visibility = View.GONE
        btExit.visibility = View.GONE
    }

    fun checkIsLogin() {
        val sPref = activity?.getPreferences(MODE_PRIVATE);
        val name = sPref?.getString("name", null)
        val ava = sPref?.getString("ava", "") ?: ""
        if (name != null) {
            isLogin = true
            viewModel.user.postValue(User(name, ava))
        } else
            isLogin = false
    }

}