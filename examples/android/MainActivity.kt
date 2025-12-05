package com.ailang.sample

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ailang.core.AiLang
import com.ailang.sample.databinding.ActivityMainBinding

/**
 * Sample Activity demonstrating AiLang usage
 */
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupLanguageSpinner()
        updateUI()
        
        // Add language change listener
        AiLang.addLanguageChangeListener { language ->
            updateUI()
            Toast.makeText(
                this,
                "Language changed to: ${AiLang.getSupportedLanguages().find { it.code == language }?.name}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    
    private fun setupLanguageSpinner() {
        val languages = AiLang.getSupportedLanguages()
        val languageNames = languages.map { "${it.nativeName} (${it.code})" }
        
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languageNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        
        binding.spinnerLanguage.adapter = adapter
        
        // Set current language
        val currentIndex = languages.indexOfFirst { it.code == AiLang.getCurrentLanguage() }
        if (currentIndex >= 0) {
            binding.spinnerLanguage.setSelection(currentIndex)
        }
        
        binding.spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLanguage = languages[position].code
                if (selectedLanguage != AiLang.getCurrentLanguage()) {
                    AiLang.setLanguage(selectedLanguage)
                }
            }
            
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
    
    private fun updateUI() {
        // Simple translations
        binding.tvWelcome.text = AiLang.t("welcome")
        binding.btnLogin.text = AiLang.t("login")
        binding.btnLogout.text = AiLang.t("logout")
        binding.tvHome.text = AiLang.t("home")
        binding.tvProfile.text = AiLang.t("profile")
        binding.tvSettings.text = AiLang.t("settings")
        
        // Translation with parameters
        binding.tvWelcomeUser.text = AiLang.t("welcome_user", mapOf("name" to "Suraj"))
        
        // Translation with count (pluralization)
        binding.tvItemCount1.text = AiLang.t("items_count", 1)
        binding.tvItemCount5.text = AiLang.t("items_count", 5)
        
        // Check RTL
        val layoutDirection = if (AiLang.isRTL()) {
            View.LAYOUT_DIRECTION_RTL
        } else {
            View.LAYOUT_DIRECTION_LTR
        }
        binding.root.layoutDirection = layoutDirection
        
        // Update toolbar title
        supportActionBar?.title = AiLang.t("app_name")
    }
}
