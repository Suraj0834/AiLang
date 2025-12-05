import React, { useState, useCallback } from 'react';
import {
  SafeAreaView,
  StyleSheet,
  View,
  Text,
  TextInput,
  TouchableOpacity,
  ScrollView,
  ActivityIndicator,
  Alert,
} from 'react-native';
import { Picker } from '@react-native-picker/picker';
import { AiLang, Language, TranslationResult } from '@ailang/react-native';

// Initialize AiLang with your API key
// In production, use environment variables or secure storage
AiLang.initialize({
  apiKey: 'YOUR_GEMINI_API_KEY', // Replace with your actual API key
  defaultLanguage: 'en',
  enableCache: true,
  maxCacheSize: 500,
});

const LANGUAGES: { code: string; name: string }[] = [
  { code: 'es', name: 'Spanish' },
  { code: 'fr', name: 'French' },
  { code: 'de', name: 'German' },
  { code: 'it', name: 'Italian' },
  { code: 'pt', name: 'Portuguese' },
  { code: 'ru', name: 'Russian' },
  { code: 'ja', name: 'Japanese' },
  { code: 'ko', name: 'Korean' },
  { code: 'zh', name: 'Chinese' },
  { code: 'ar', name: 'Arabic' },
  { code: 'hi', name: 'Hindi' },
];

const App: React.FC = () => {
  const [inputText, setInputText] = useState('Hello, how are you today?');
  const [translatedText, setTranslatedText] = useState('');
  const [selectedLanguage, setSelectedLanguage] = useState('es');
  const [isLoading, setIsLoading] = useState(false);
  const [status, setStatus] = useState('');

  const handleTranslate = useCallback(async () => {
    if (!inputText.trim()) {
      Alert.alert('Error', 'Please enter text to translate');
      return;
    }

    setIsLoading(true);
    setStatus('Translating...');

    try {
      const result: TranslationResult = await AiLang.translate(
        inputText,
        selectedLanguage
      );

      if (result.success && result.translatedText) {
        setTranslatedText(result.translatedText);
        const langName = LANGUAGES.find(l => l.code === selectedLanguage)?.name;
        setStatus(
          `✓ Translated to ${langName}${result.fromCache ? ' (cached)' : ''}`
        );
      } else {
        setTranslatedText('');
        setStatus(`✗ Error: ${result.error || 'Unknown error'}`);
      }
    } catch (error) {
      setTranslatedText('');
      setStatus(`✗ Error: ${(error as Error).message}`);
    } finally {
      setIsLoading(false);
    }
  }, [inputText, selectedLanguage]);

  const handleBatchTranslate = useCallback(async () => {
    const texts = inputText.split('\n').filter(t => t.trim());
    
    if (texts.length < 2) {
      Alert.alert('Error', 'Enter multiple lines for batch translation');
      return;
    }

    setIsLoading(true);
    setStatus('Batch translating...');

    try {
      const results = await AiLang.translateBatch(texts, selectedLanguage);
      
      const translatedTexts = results.map((result, index) => {
        if (result.success && result.translatedText) {
          return result.translatedText;
        }
        return `[Error: ${result.error || 'Unknown error'}]`;
      });

      setTranslatedText(translatedTexts.join('\n\n'));
      setStatus(`✓ Batch translated ${results.length} texts`);
    } catch (error) {
      setTranslatedText('');
      setStatus(`✗ Error: ${(error as Error).message}`);
    } finally {
      setIsLoading(false);
    }
  }, [inputText, selectedLanguage]);

  const handleClearCache = useCallback(async () => {
    await AiLang.clearCache();
    setStatus('Cache cleared');
  }, []);

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.scrollContent}>
        {/* Header */}
        <View style={styles.header}>
          <Text style={styles.title}>AiLang Sample</Text>
          <Text style={styles.subtitle}>AI-Powered Translation Framework</Text>
        </View>

        {/* Language Selector */}
        <View style={styles.languageSection}>
          <Text style={styles.label}>Target Language:</Text>
          <View style={styles.pickerContainer}>
            <Picker
              selectedValue={selectedLanguage}
              onValueChange={setSelectedLanguage}
              style={styles.picker}
            >
              {LANGUAGES.map(lang => (
                <Picker.Item
                  key={lang.code}
                  label={lang.name}
                  value={lang.code}
                />
              ))}
            </Picker>
          </View>
        </View>

        {/* Input Section */}
        <View style={styles.inputSection}>
          <Text style={styles.label}>Enter text to translate:</Text>
          <TextInput
            style={styles.input}
            value={inputText}
            onChangeText={setInputText}
            placeholder="Enter text here..."
            multiline
            numberOfLines={4}
          />
        </View>

        {/* Action Buttons */}
        <View style={styles.buttonRow}>
          <TouchableOpacity
            style={[styles.button, styles.primaryButton]}
            onPress={handleTranslate}
            disabled={isLoading}
          >
            <Text style={styles.buttonText}>Translate</Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={[styles.button, styles.secondaryButton]}
            onPress={handleBatchTranslate}
            disabled={isLoading}
          >
            <Text style={[styles.buttonText, styles.secondaryButtonText]}>
              Batch
            </Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={[styles.button, styles.textButton]}
            onPress={handleClearCache}
          >
            <Text style={styles.textButtonText}>Clear Cache</Text>
          </TouchableOpacity>
        </View>

        {/* Loading Indicator */}
        {isLoading && (
          <ActivityIndicator size="large" color="#6200EE" style={styles.loader} />
        )}

        {/* Status */}
        {status ? <Text style={styles.status}>{status}</Text> : null}

        {/* Output Section */}
        <View style={styles.outputSection}>
          <Text style={styles.label}>Translation Result:</Text>
          <View style={styles.outputCard}>
            <ScrollView nestedScrollEnabled>
              <Text style={styles.outputText} selectable>
                {translatedText || 'Translated text will appear here...'}
              </Text>
            </ScrollView>
          </View>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  scrollContent: {
    padding: 16,
  },
  header: {
    alignItems: 'center',
    marginBottom: 24,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#6200EE',
  },
  subtitle: {
    fontSize: 14,
    color: '#666',
    marginTop: 4,
  },
  languageSection: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 16,
  },
  label: {
    fontSize: 16,
    fontWeight: '600',
    marginBottom: 8,
    color: '#333',
  },
  pickerContainer: {
    flex: 1,
    marginLeft: 16,
    borderWidth: 1,
    borderColor: '#ccc',
    borderRadius: 4,
    backgroundColor: '#fff',
  },
  picker: {
    height: 50,
  },
  inputSection: {
    marginBottom: 16,
  },
  input: {
    backgroundColor: '#fff',
    borderWidth: 1,
    borderColor: '#ccc',
    borderRadius: 8,
    padding: 12,
    fontSize: 16,
    minHeight: 100,
    textAlignVertical: 'top',
  },
  buttonRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 16,
  },
  button: {
    paddingVertical: 12,
    paddingHorizontal: 20,
    borderRadius: 8,
    alignItems: 'center',
    justifyContent: 'center',
  },
  primaryButton: {
    backgroundColor: '#6200EE',
    flex: 1,
    marginRight: 8,
  },
  secondaryButton: {
    backgroundColor: '#fff',
    borderWidth: 1,
    borderColor: '#6200EE',
    flex: 0.8,
    marginRight: 8,
  },
  textButton: {
    backgroundColor: 'transparent',
  },
  buttonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '600',
  },
  secondaryButtonText: {
    color: '#6200EE',
  },
  textButtonText: {
    color: '#6200EE',
    fontSize: 14,
  },
  loader: {
    marginVertical: 16,
  },
  status: {
    textAlign: 'center',
    fontSize: 12,
    color: '#666',
    marginBottom: 16,
  },
  outputSection: {
    flex: 1,
  },
  outputCard: {
    backgroundColor: '#fff',
    borderRadius: 8,
    padding: 16,
    minHeight: 150,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 2,
  },
  outputText: {
    fontSize: 16,
    color: '#333',
    lineHeight: 24,
  },
});

export default App;
