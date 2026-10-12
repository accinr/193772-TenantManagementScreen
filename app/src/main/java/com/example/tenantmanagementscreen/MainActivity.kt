package com.example.tenantmanagementscreen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.tenantmanagementscreen.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding

    // Lost on rotation unless we save it in onSaveInstanceState
    private var lastTenant: Tenant? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TENANT_LIFECYCLE", "MainActivity: onCreate")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RESTORE STATE: savedInstanceState is null the first time, filled after rotation
        if (savedInstanceState != null) {
            val name = savedInstanceState.getString("tenant_name")
            val phone = savedInstanceState.getString("tenant_phone")
            val rent = savedInstanceState.getString("tenant_rent")
            if (name != null && phone != null && rent != null) {
                val tenant = Tenant(name, phone, rent)
                lastTenant = tenant        // so CALL and SHARE work again
                binding.tenant = tenant    // so the summary appears again
            }
        } else {
            // Only show the welcome Toast on a fresh open, not after rotation
            val email = intent.getStringExtra("EMAIL")
            if (email != null) {
                Toast.makeText(this, "Logged in as $email", Toast.LENGTH_SHORT).show()
            }
        }

        binding.saveButton.setOnClickListener {
            val name = binding.tenantNameEditText.text.toString().trim()
            val phone = binding.phoneEditText.text.toString().trim()
            val rent = binding.rentEditText.text.toString().trim()

            var valid = true
            if (name.isEmpty()) { binding.tenantNameEditText.error = "Required"; valid = false }
            if (phone.isEmpty()) { binding.phoneEditText.error = "Required"; valid = false }
            if (rent.isEmpty()) { binding.rentEditText.error = "Required"; valid = false }
            if (!valid) return@setOnClickListener

            val tenant = Tenant(name, phone, rent)
            binding.tenant = tenant
            lastTenant = tenant
        }

        binding.callButton.setOnClickListener {
            val tenant = lastTenant
            if (tenant == null) {
                Toast.makeText(this, "Save a tenant first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${tenant.phone}")))
        }

        binding.shareButton.setOnClickListener {
            val tenant = lastTenant
            if (tenant == null) {
                Toast.makeText(this, "Save a tenant first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, tenant.summary())
            startActivity(Intent.createChooser(intent, "Share tenant"))
        }
    }

    // SAVE STATE: Android calls this just before destroying the Activity (e.g. rotation)
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("TENANT_LIFECYCLE", "MainActivity: onSaveInstanceState")

        val tenant = lastTenant
        if (tenant != null) {
            outState.putString("tenant_name", tenant.name)
            outState.putString("tenant_phone", tenant.phone)
            outState.putString("tenant_rent", tenant.rent)
        }
    }

    // LIFECYCLE METHODS: we only log, so we can watch the order in Logcat
    override fun onStart() { super.onStart(); Log.d("TENANT_LIFECYCLE", "MainActivity: onStart") }
    override fun onResume() { super.onResume(); Log.d("TENANT_LIFECYCLE", "MainActivity: onResume") }
    override fun onPause() { super.onPause(); Log.d("TENANT_LIFECYCLE", "MainActivity: onPause") }
    override fun onStop() { super.onStop(); Log.d("TENANT_LIFECYCLE", "MainActivity: onStop") }
    override fun onRestart() { super.onRestart(); Log.d("TENANT_LIFECYCLE", "MainActivity: onRestart") }
    override fun onDestroy() { super.onDestroy(); Log.d("TENANT_LIFECYCLE", "MainActivity: onDestroy") }
}