import React, { useState } from 'react';
import {
  View,
  Text,
  TouchableOpacity,
  ScrollView,
  StyleSheet,
  I18nManager,
} from 'react-native';
import { useAiLang } from '@ailang/react-native';

/**
 * Sample Screen demonstrating AiLang usage
 */
export const MainApp = () => {
  const {
    t,
    setLanguage,
    currentLanguage,
    isLoading,
    getSupportedLanguages,
    isRTL,
    clearCache,
  } = useAiLang();

  const [itemCount, setItemCount] = useState(1);
  const userName = 'Suraj';

  // Handle language change
  const handleLanguageChange = async (languageCode: string) => {
    await setLanguage(languageCode);
    
    // Handle RTL layout change
    const shouldBeRTL = isRTL();
    if (I18nManager.isRTL !== shouldBeRTL) {
      I18nManager.forceRTL(shouldBeRTL);
      // Note: App restart may be needed for RTL changes
    }
  };

  return (
    <ScrollView style={styles.container}>
      <Text style={styles.title}>{t('app_name')}</Text>
      
      {/* Loading indicator */}
      {isLoading && (
        <Text style={styles.loading}>{t('loading')}</Text>
      )}
      
      {/* Simple translations */}
      <View style={styles.section}>
        <Text style={styles.sectionTitle}>Simple Translations</Text>
        <Text style={styles.text}>{t('welcome')}</Text>
        <Text style={styles.text}>{t('home')}</Text>
        <Text style={styles.text}>{t('profile')}</Text>
        <Text style={styles.text}>{t('settings')}</Text>
      </View>
      
      {/* Parameterized translation */}
      <View style={styles.section}>
        <Text style={styles.sectionTitle}>With Parameters</Text>
        <Text style={styles.text}>
          {t('welcome_user', { name: userName })}
        </Text>
      </View>
      
      {/* Pluralization */}
      <View style={styles.section}>
        <Text style={styles.sectionTitle}>Pluralization</Text>
        <Text style={styles.text}>
          {t('items_count', { count: itemCount })}
        </Text>
        <View style={styles.buttonRow}>
          <TouchableOpacity
            style={styles.smallButton}
            onPress={() => setItemCount(Math.max(0, itemCount - 1))}
          >
            <Text style={styles.buttonText}>-</Text>
          </TouchableOpacity>
          <Text style={styles.countText}>{itemCount}</Text>
          <TouchableOpacity
            style={styles.smallButton}
            onPress={() => setItemCount(itemCount + 1)}
          >
            <Text style={styles.buttonText}>+</Text>
          </TouchableOpacity>
        </View>
      </View>
      
      {/* Language selector */}
      <View style={styles.section}>
        <Text style={styles.sectionTitle}>{t('select_language')}</Text>
        <Text style={styles.currentLang}>
          Current: {currentLanguage}
        </Text>
        <ScrollView horizontal showsHorizontalScrollIndicator={false}>
          {getSupportedLanguages().slice(0, 10).map((lang) => (
            <TouchableOpacity
              key={lang.code}
              style={[
                styles.langButton,
                currentLanguage === lang.code && styles.langButtonActive,
              ]}
              onPress={() => handleLanguageChange(lang.code)}
            >
              <Text
                style={[
                  styles.langButtonText,
                  currentLanguage === lang.code && styles.langButtonTextActive,
                ]}
              >
                {lang.nativeName}
              </Text>
            </TouchableOpacity>
          ))}
        </ScrollView>
      </View>
      
      {/* Action buttons */}
      <View style={styles.section}>
        <Text style={styles.sectionTitle}>Actions</Text>
        <TouchableOpacity style={styles.button}>
          <Text style={styles.buttonText}>{t('login')}</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.button}>
          <Text style={styles.buttonText}>{t('register')}</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={[styles.button, styles.dangerButton]}
          onPress={clearCache}
        >
          <Text style={styles.buttonText}>{t('clear')} Cache</Text>
        </TouchableOpacity>
      </View>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
    padding: 16,
  },
  title: {
    fontSize: 28,
    fontWeight: 'bold',
    textAlign: 'center',
    marginVertical: 20,
    color: '#333',
  },
  loading: {
    textAlign: 'center',
    color: '#666',
    marginBottom: 10,
  },
  section: {
    backgroundColor: '#fff',
    borderRadius: 12,
    padding: 16,
    marginBottom: 16,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: '600',
    marginBottom: 12,
    color: '#333',
  },
  text: {
    fontSize: 16,
    color: '#666',
    marginBottom: 8,
  },
  currentLang: {
    fontSize: 14,
    color: '#888',
    marginBottom: 12,
  },
  buttonRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    marginTop: 10,
  },
  smallButton: {
    backgroundColor: '#007AFF',
    width: 40,
    height: 40,
    borderRadius: 20,
    justifyContent: 'center',
    alignItems: 'center',
  },
  countText: {
    fontSize: 24,
    fontWeight: 'bold',
    marginHorizontal: 20,
    color: '#333',
  },
  langButton: {
    paddingHorizontal: 16,
    paddingVertical: 8,
    borderRadius: 20,
    backgroundColor: '#e0e0e0',
    marginRight: 8,
  },
  langButtonActive: {
    backgroundColor: '#007AFF',
  },
  langButtonText: {
    color: '#333',
    fontSize: 14,
  },
  langButtonTextActive: {
    color: '#fff',
  },
  button: {
    backgroundColor: '#007AFF',
    paddingVertical: 14,
    borderRadius: 10,
    marginBottom: 10,
  },
  dangerButton: {
    backgroundColor: '#FF3B30',
  },
  buttonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '600',
    textAlign: 'center',
  },
});
