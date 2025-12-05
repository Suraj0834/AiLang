import { LANGUAGE_NAMES, GEMINI_API_URL, DEFAULTS } from '../utils/constants';

/**
 * Translation Service - Handles communication with Google Gemini AI API
 */
export class TranslationService {
  private apiKey: string;
  private timeout: number;
  private retryCount: number;
  private batchSize: number;
  private debugMode: boolean;

  constructor(
    apiKey: string,
    options?: {
      timeout?: number;
      retryCount?: number;
      batchSize?: number;
      debugMode?: boolean;
    }
  ) {
    this.apiKey = apiKey;
    this.timeout = options?.timeout ?? DEFAULTS.TIMEOUT;
    this.retryCount = options?.retryCount ?? DEFAULTS.RETRY_COUNT;
    this.batchSize = options?.batchSize ?? DEFAULTS.BATCH_SIZE;
    this.debugMode = options?.debugMode ?? false;
  }

  /**
   * Translate a single string
   */
  async translate(text: string, targetLanguage: string): Promise<string | null> {
    const result = await this.translateBatch({ single: text }, targetLanguage);
    return result.single ?? null;
  }

  /**
   * Translate multiple strings in batches
   */
  async translateBatch(
    strings: Record<string, string>,
    targetLanguage: string
  ): Promise<Record<string, string>> {
    const result: Record<string, string> = {};
    const entries = Object.entries(strings);

    // Split into batches
    for (let i = 0; i < entries.length; i += this.batchSize) {
      const batch = entries.slice(i, i + this.batchSize);
      const batchObj = Object.fromEntries(batch);

      const translated = await this.translateBatchInternal(batchObj, targetLanguage);
      Object.assign(result, translated);

      // Small delay between batches
      if (i + this.batchSize < entries.length) {
        await this.delay(100);
      }
    }

    return result;
  }

  private async translateBatchInternal(
    strings: Record<string, string>,
    targetLanguage: string
  ): Promise<Record<string, string>> {
    let lastError: Error | null = null;
    let retryDelay = DEFAULTS.RETRY_DELAY;

    for (let attempt = 0; attempt < this.retryCount; attempt++) {
      try {
        return await this.makeTranslationRequest(strings, targetLanguage);
      } catch (error) {
        lastError = error as Error;
        this.log(`Translation attempt ${attempt + 1} failed: ${lastError.message}`);

        // Longer delay for rate limit errors
        const delay =
          lastError.message.includes('429') || lastError.message.includes('rate')
            ? retryDelay * 2
            : retryDelay;

        await this.delay(delay);
        retryDelay = Math.min(retryDelay * 2, DEFAULTS.MAX_RETRY_DELAY);
      }
    }

    throw lastError ?? new Error('Translation failed');
  }

  private async makeTranslationRequest(
    strings: Record<string, string>,
    targetLanguage: string
  ): Promise<Record<string, string>> {
    const languageName = LANGUAGE_NAMES[targetLanguage] ?? targetLanguage;
    const stringsJson = JSON.stringify(strings);

    const prompt = `You are a professional translator. Translate the following JSON object values from English to ${languageName}.

IMPORTANT RULES:
1. Only translate the VALUES, keep the KEYS exactly the same
2. Maintain any placeholders like {name}, {count}, etc.
3. Return ONLY valid JSON, no explanations
4. Keep the same JSON structure
5. Translate naturally, considering context
6. If a value contains "|" for pluralization, translate both parts

JSON to translate:
${stringsJson}

Return only the translated JSON object:`;

    const requestBody = {
      contents: [
        {
          parts: [{ text: prompt }],
        },
      ],
      generationConfig: {
        temperature: 0.3,
        topP: 0.95,
        maxOutputTokens: 8192,
      },
    };

    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), this.timeout);

    try {
      const response = await fetch(`${GEMINI_API_URL}?key=${this.apiKey}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestBody),
        signal: controller.signal,
      });

      clearTimeout(timeoutId);

      if (response.status === 429) {
        throw new Error('Rate limit exceeded (429)');
      }

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`API error: ${response.status} - ${errorText}`);
      }

      const data = await response.json();
      return this.parseResponse(data);
    } catch (error) {
      clearTimeout(timeoutId);
      throw error;
    }
  }

  private parseResponse(data: any): Record<string, string> {
    try {
      const candidates = data.candidates;
      const firstCandidate = candidates?.[0];
      const content = firstCandidate?.content;
      const parts = content?.parts;
      const firstPart = parts?.[0];
      let text = firstPart?.text;

      if (!text) {
        throw new Error('No text in response');
      }

      // Clean up the response
      text = text.trim();
      if (text.startsWith('```json')) {
        text = text.slice(7);
      }
      if (text.startsWith('```')) {
        text = text.slice(3);
      }
      if (text.endsWith('```')) {
        text = text.slice(0, -3);
      }
      text = text.trim();

      return JSON.parse(text);
    } catch (error) {
      throw new Error(`Failed to parse response: ${(error as Error).message}`);
    }
  }

  private delay(ms: number): Promise<void> {
    return new Promise((resolve) => setTimeout(resolve, ms));
  }

  private log(message: string): void {
    if (this.debugMode) {
      console.log(`[AiLang] ${message}`);
    }
  }
}
