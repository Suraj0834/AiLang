package com.ailang.sample

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ailang.core.AiLang
import com.ailang.core.model.Language
import com.ailang.core.model.TranslationResult
import com.ailang.sample.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

/**
 * Main Activity demonstrating AiLang SDK usage.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var selectedLanguage: Language = Language.SPANISH

    // Available languages for translation
    private val languages = listOf(
        Language.SPANISH,
        Language.FRENCH,
        Language.GERMAN,
        Language.ITALIAN,
        Language.PORTUGUESE,
        Language.RUSSIAN,
        Language.JAPANESE,
        Language.KOREAN,
        Language.CHINESE,
        Language.ARABIC,
        Language.HINDI
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupLanguageSpinner()
        setupButtons()
        setupSampleTexts()
    }

    private fun setupLanguageSpinner() {
        val languageNames = languages.map { it.displayName }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languageNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        
        binding.spinnerLanguage.adapter = adapter
        binding.spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedLanguage = languages[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedLanguage = Language.SPANISH
            }
        }
    }

    private fun setupButtons() {
        binding.btnTranslate.setOnClickListener {
            translateText()
        }

        binding.btnTranslateBatch.setOnClickListener {
            translateBatch()
        }

        binding.btnClearCache.setOnClickListener {
            clearCache()
        }
    }

    private fun setupSampleTexts() {
        // Set sample text for quick testing
        binding.etInput.setText("Hello, how are you today?")
    }

    private fun translateText() {
        val inputText = binding.etInput.text.toString().trim()
        
        if (inputText.isEmpty()) {
            showError("Please enter text to translate")
            return
        }

        showLoading(true)
        
        lifecycleScope.launch {
            try {
                val result = AiLang.translate(
                    text = inputText,
                    targetLanguage = selectedLanguage.code
                )
                
                handleResult(result)
            } catch (e: Exception) {
                showError("Translation failed: ${e.message}")
            } finally {
                showLoading(false)
            }
        }
    }

    private fun translateBatch() {
        val inputText = binding.etInput.text.toString().trim()
        
        if (inputText.isEmpty()) {
            showError("Please enter text to translate")
            return
        }

        // Split input by new lines for batch translation
        val texts = inputText.split("\n").filter { it.isNotBlank() }
        
        if (texts.size < 2) {
            showError("Enter multiple lines for batch translation")
            return
        }

        showLoading(true)
        
        lifecycleScope.launch {
            try {
                val results = AiLang.translateBatch(
                    texts = texts,
                    targetLanguage = selectedLanguage.code
                )
                
                val translatedTexts = results.mapNotNull { result ->
                    when (result) {
                        is TranslationResult.Success -> result.translatedText
                        is TranslationResult.Error -> "[Error: ${result.message}]"
                    }
                }
                
                binding.tvOutput.text = translatedTexts.joinToString("\n\n")
                binding.tvStatus.text = "Batch translated ${results.size} texts"
                
            } catch (e: Exception) {
                showError("Batch translation failed: ${e.message}")
            } finally {
                showLoading(false)
            }
        }
    }

    private fun handleResult(result: TranslationResult) {
        when (result) {
            is TranslationResult.Success -> {
                binding.tvOutput.text = result.translatedText
                binding.tvStatus.text = buildString {
                    append("✓ Translated to ${selectedLanguage.displayName}")
                    if (result.fromCache) {
                        append(" (cached)")
                    }
                }
            }
            is TranslationResult.Error -> {
                showError(result.message)
            }
        }
    }

    private fun clearCache() {
        lifecycleScope.launch {
            AiLang.clearCache()
            binding.tvStatus.text = "Cache cleared"
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnTranslate.isEnabled = !show
        binding.btnTranslateBatch.isEnabled = !show
    }

    private fun showError(message: String) {
        binding.tvOutput.text = ""
        binding.tvStatus.text = "✗ Error: $message"
    }
}
