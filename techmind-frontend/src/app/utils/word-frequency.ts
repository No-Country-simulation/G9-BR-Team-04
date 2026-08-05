export interface WordFrequency {
  word: string
  count: number
  size: number
}

const STOP_WORDS = new Set([
  'de', 'a', 'o', 'que', 'e', 'do', 'da', 'em', 'um', 'uma', 'para',
  'com', 'não', 'os', 'as', 'no', 'na', 'por', 'mais', 'como', 'ou',
  'ao', 'dos', 'se', 'foi', 'ser', 'é', 'seu', 'sua', 'quando'
])

export function extractWordFrequency(texts: string[], limit = 25): WordFrequency[] {
  const counts = new Map<string, number>()

  for (const text of texts) {
    const words = text
      .toLowerCase()
      .normalize('NFD').replace(/[\u0300-\u036f]/g, '')
      .replace(/[^\w\s]/g, '')
      .split(/\s+/)
      .filter(word => word.length > 2 && !STOP_WORDS.has(word))

    for (const word of words) {
      counts.set(word, (counts.get(word) || 0) + 1)
    }
  }

  const sorted = Array.from(counts.entries())
    .sort((a, b) => b[1] - a[1])
    .slice(0, limit)

  if (sorted.length === 0) return []

  const maxCount = sorted[0][1]
  const minCount = sorted[sorted.length - 1][1]

  return sorted.map(([word, count]) => ({
    word,
    count,
    size: normalizeSize(count, minCount, maxCount)
  }))
}

function normalizeSize(count: number, min: number, max: number): number {
  const MIN_SIZE = 0.85
  const MAX_SIZE = 2.25

  if (max === min) return (MIN_SIZE + MAX_SIZE) / 2

  const ratio = (count - min) / (max - min)
  return MIN_SIZE + ratio * (MAX_SIZE - MIN_SIZE)
}