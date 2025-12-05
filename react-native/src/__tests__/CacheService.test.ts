import { CacheService } from '../services/CacheService';
import AsyncStorage from '@react-native-async-storage/async-storage';

// Mock AsyncStorage
jest.mock('@react-native-async-storage/async-storage', () => ({
  getItem: jest.fn(),
  setItem: jest.fn(),
  removeItem: jest.fn(),
}));

describe('CacheService', () => {
  let cacheService: CacheService;

  beforeEach(() => {
    jest.clearAllMocks();
    (AsyncStorage.getItem as jest.Mock).mockResolvedValue(null);
    (AsyncStorage.setItem as jest.Mock).mockResolvedValue(undefined);
    (AsyncStorage.removeItem as jest.Mock).mockResolvedValue(undefined);

    cacheService = new CacheService({
      cacheDuration: 24 * 60 * 60 * 1000, // 24 hours
      maxCacheSize: 10,
      debugMode: false,
    });
  });

  // ==================== get() Tests ====================

  describe('get()', () => {
    it('should return null for non-existent key', () => {
      const result = cacheService.get('nonexistent', 'es');
      expect(result).toBeNull();
    });

    it('should return null for non-existent language', async () => {
      await cacheService.put('hello', 'Hola', 'es');
      const result = cacheService.get('hello', 'fr');
      expect(result).toBeNull();
    });

    it('should return cached value', async () => {
      await cacheService.put('greeting', 'Namaste', 'hi');
      const result = cacheService.get('greeting', 'hi');
      expect(result).toBe('Namaste');
    });
  });

  // ==================== put() Tests ====================

  describe('put()', () => {
    it('should store value in cache', async () => {
      await cacheService.put('hello', 'Hola', 'es');
      const result = cacheService.get('hello', 'es');
      expect(result).toBe('Hola');
    });

    it('should store multiple values', async () => {
      await cacheService.put('hello', 'Hola', 'es');
      await cacheService.put('goodbye', 'Adiós', 'es');
      await cacheService.put('hello', 'Bonjour', 'fr');

      expect(cacheService.get('hello', 'es')).toBe('Hola');
      expect(cacheService.get('goodbye', 'es')).toBe('Adiós');
      expect(cacheService.get('hello', 'fr')).toBe('Bonjour');
    });

    it('should overwrite existing value', async () => {
      await cacheService.put('hello', 'Hola', 'es');
      await cacheService.put('hello', 'Hola Updated', 'es');
      expect(cacheService.get('hello', 'es')).toBe('Hola Updated');
    });

    it('should save to AsyncStorage', async () => {
      await cacheService.put('key', 'value', 'es');
      expect(AsyncStorage.setItem).toHaveBeenCalled();
    });
  });

  // ==================== putBatch() Tests ====================

  describe('putBatch()', () => {
    it('should store multiple translations at once', async () => {
      await cacheService.putBatch(
        {
          hello: 'Hola',
          goodbye: 'Adiós',
          thanks: 'Gracias',
        },
        'es'
      );

      expect(cacheService.get('hello', 'es')).toBe('Hola');
      expect(cacheService.get('goodbye', 'es')).toBe('Adiós');
      expect(cacheService.get('thanks', 'es')).toBe('Gracias');
    });

    it('should only save to disk once', async () => {
      await cacheService.putBatch({ a: '1', b: '2', c: '3' }, 'es');
      expect(AsyncStorage.setItem).toHaveBeenCalledTimes(1);
    });
  });

  // ==================== remove() Tests ====================

  describe('remove()', () => {
    it('should delete cached entry', async () => {
      await cacheService.put('hello', 'Hola', 'es');
      await cacheService.remove('hello', 'es');
      expect(cacheService.get('hello', 'es')).toBeNull();
    });

    it('should not affect other languages', async () => {
      await cacheService.put('hello', 'Hola', 'es');
      await cacheService.put('hello', 'Bonjour', 'fr');
      await cacheService.remove('hello', 'es');

      expect(cacheService.get('hello', 'es')).toBeNull();
      expect(cacheService.get('hello', 'fr')).toBe('Bonjour');
    });

    it('should not throw for non-existent key', async () => {
      await expect(cacheService.remove('nonexistent', 'es')).resolves.not.toThrow();
    });
  });

  // ==================== clearAll() Tests ====================

  describe('clearAll()', () => {
    it('should remove all entries', async () => {
      await cacheService.put('key1', 'value1', 'es');
      await cacheService.put('key2', 'value2', 'es');
      await cacheService.put('key3', 'value3', 'fr');

      await cacheService.clearAll();

      expect(cacheService.get('key1', 'es')).toBeNull();
      expect(cacheService.get('key2', 'es')).toBeNull();
      expect(cacheService.get('key3', 'fr')).toBeNull();
    });

    it('should remove from AsyncStorage', async () => {
      await cacheService.clearAll();
      expect(AsyncStorage.removeItem).toHaveBeenCalled();
    });
  });

  // ==================== clearLanguage() Tests ====================

  describe('clearLanguage()', () => {
    it('should remove only specified language entries', async () => {
      await cacheService.put('hello', 'Hola', 'es');
      await cacheService.put('goodbye', 'Adiós', 'es');
      await cacheService.put('hello', 'Bonjour', 'fr');

      await cacheService.clearLanguage('es');

      expect(cacheService.get('hello', 'es')).toBeNull();
      expect(cacheService.get('goodbye', 'es')).toBeNull();
      expect(cacheService.get('hello', 'fr')).toBe('Bonjour');
    });
  });

  // ==================== getStats() Tests ====================

  describe('getStats()', () => {
    it('should return zero for empty cache', () => {
      const stats = cacheService.getStats();
      expect(stats.totalEntries).toBe(0);
      expect(stats.activeEntries).toBe(0);
      expect(stats.expiredEntries).toBe(0);
    });

    it('should return correct count after adding entries', async () => {
      await cacheService.put('key1', 'value1', 'es');
      await cacheService.put('key2', 'value2', 'es');
      await cacheService.put('key3', 'value3', 'fr');

      const stats = cacheService.getStats();
      expect(stats.totalEntries).toBe(3);
      expect(stats.activeEntries).toBe(3);
      expect(stats.expiredEntries).toBe(0);
    });
  });

  // ==================== getAllForLanguage() Tests ====================

  describe('getAllForLanguage()', () => {
    it('should return all translations for a language', async () => {
      await cacheService.put('hello', 'Hola', 'es');
      await cacheService.put('goodbye', 'Adiós', 'es');
      await cacheService.put('hello', 'Bonjour', 'fr');

      const esTranslations = cacheService.getAllForLanguage('es');

      expect(Object.keys(esTranslations)).toHaveLength(2);
      expect(esTranslations['hello']).toBe('Hola');
      expect(esTranslations['goodbye']).toBe('Adiós');
    });

    it('should return empty object for non-existent language', () => {
      const result = cacheService.getAllForLanguage('xyz');
      expect(result).toEqual({});
    });
  });

  // ==================== Edge Cases ====================

  describe('edge cases', () => {
    it('should handle empty key', async () => {
      await cacheService.put('', 'empty key value', 'es');
      expect(cacheService.get('', 'es')).toBe('empty key value');
    });

    it('should handle empty value', async () => {
      await cacheService.put('key', '', 'es');
      expect(cacheService.get('key', 'es')).toBe('');
    });

    it('should handle unicode content', async () => {
      await cacheService.put('greeting', 'नमस्ते', 'hi');
      await cacheService.put('japanese', 'こんにちは', 'ja');

      expect(cacheService.get('greeting', 'hi')).toBe('नमस्ते');
      expect(cacheService.get('japanese', 'ja')).toBe('こんにちは');
    });

    it('should handle very long values', async () => {
      const longValue = 'A'.repeat(10000);
      await cacheService.put('long', longValue, 'es');
      expect(cacheService.get('long', 'es')).toBe(longValue);
    });

    it('should handle special characters in key', async () => {
      await cacheService.put('key.with.dots', 'value1', 'es');
      await cacheService.put('key-with-dashes', 'value2', 'es');

      expect(cacheService.get('key.with.dots', 'es')).toBe('value1');
      expect(cacheService.get('key-with-dashes', 'es')).toBe('value2');
    });

    it('should separate same key with different languages', async () => {
      await cacheService.put('hello', 'Hola', 'es');
      await cacheService.put('hello', 'Bonjour', 'fr');
      await cacheService.put('hello', 'Ciao', 'it');

      expect(cacheService.get('hello', 'es')).toBe('Hola');
      expect(cacheService.get('hello', 'fr')).toBe('Bonjour');
      expect(cacheService.get('hello', 'it')).toBe('Ciao');
    });
  });

  // ==================== init() Tests ====================

  describe('init()', () => {
    it('should load cache from AsyncStorage', async () => {
      const mockCache = {
        'hello_es': { value: 'Hola', timestamp: Date.now() },
        'goodbye_es': { value: 'Adiós', timestamp: Date.now() },
      };
      (AsyncStorage.getItem as jest.Mock).mockResolvedValue(JSON.stringify(mockCache));

      await cacheService.init();

      expect(cacheService.get('hello', 'es')).toBe('Hola');
      expect(cacheService.get('goodbye', 'es')).toBe('Adiós');
    });

    it('should handle empty cache', async () => {
      (AsyncStorage.getItem as jest.Mock).mockResolvedValue(null);
      await expect(cacheService.init()).resolves.not.toThrow();
    });

    it('should handle corrupted cache data', async () => {
      (AsyncStorage.getItem as jest.Mock).mockResolvedValue('invalid json');
      await expect(cacheService.init()).resolves.not.toThrow();
    });
  });
});
