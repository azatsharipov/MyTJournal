package com.example.mytjournal.ui.profile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
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
import androidx.lifecycle.ViewModelProvider
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.example.mytjournal.R


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
    private var isLoggedIn: Boolean = false

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

            activity?.runOnUiThread {
                Toast.makeText(activity, it.text, Toast.LENGTH_LONG).show()
            }
        }

        btAuth.setOnClickListener {
            if (checkPermission()) {
                startScanning()
                scanner.startPreview()
            } else {
                requestPermission()
            }
        }

        checkIsLoggedIn()
        if (isLoggedIn) {
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

    private fun showMessageOKCancel(
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

    fun checkIsLoggedIn() {
        isLoggedIn = false
    }

}