import type { MediaMetadataResponse } from '../types/media'

const MEDIA_ENDPOINT = '/api/media'

async function parseError(response: Response): Promise<string> {
  const fallback = `Request fehlgeschlagen (${response.status})`

  try {
    const text = await response.text()
    return text ? `${fallback}: ${text}` : fallback
  } catch {
    return fallback
  }
}

export async function uploadMedia(file: File): Promise<MediaMetadataResponse> {
  const formData = new FormData()
  formData.append('file', file)

  const response = await fetch(MEDIA_ENDPOINT, {
    method: 'POST',
    body: formData,
  })

  if (!response.ok) {
    throw new Error(await parseError(response))
  }

  return (await response.json()) as MediaMetadataResponse
}

export async function fetchMediaMetadata(id: string): Promise<MediaMetadataResponse> {
  const response = await fetch(`${MEDIA_ENDPOINT}/${encodeURIComponent(id)}`)

  if (!response.ok) {
    throw new Error(await parseError(response))
  }

  return (await response.json()) as MediaMetadataResponse
}
