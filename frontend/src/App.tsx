import { useMemo, useState } from 'react'
import type { FormEvent } from 'react'
import { fetchMediaMetadata, uploadMedia } from './api/media'
import type { MediaMetadataResponse } from './types/media'
import './App.css'

function App() {
  const [selectedFile, setSelectedFile] = useState<File | null>(null)
  const [lookupId, setLookupId] = useState('')
  const [activeMedia, setActiveMedia] = useState<MediaMetadataResponse | null>(null)
  const [isUploading, setIsUploading] = useState(false)
  const [isLoadingMetadata, setIsLoadingMetadata] = useState(false)
  const [errorMessage, setErrorMessage] = useState('')
  const [copied, setCopied] = useState(false)

  const prettyCreatedAt = useMemo(() => {
    if (!activeMedia?.createdAt) {
      return '-'
    }

    return new Date(activeMedia.createdAt).toLocaleString('de-DE')
  }, [activeMedia])

  const onUpload = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    setErrorMessage('')
    setCopied(false)

    if (!selectedFile) {
      setErrorMessage('Bitte zuerst eine Datei auswählen.')
      return
    }

    setIsUploading(true)
    try {
      const metadata = await uploadMedia(selectedFile)
      setActiveMedia(metadata)
      setLookupId(metadata.id)
    } catch (error) {
      setErrorMessage(error instanceof Error ? error.message : 'Upload fehlgeschlagen.')
    } finally {
      setIsUploading(false)
    }
  }

  const onLookup = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    setErrorMessage('')
    setCopied(false)

    if (!lookupId.trim()) {
      setErrorMessage('Bitte eine Media-ID eingeben.')
      return
    }

    setIsLoadingMetadata(true)
    try {
      const metadata = await fetchMediaMetadata(lookupId.trim())
      setActiveMedia(metadata)
    } catch (error) {
      setErrorMessage(error instanceof Error ? error.message : 'Metadaten konnten nicht geladen werden.')
    } finally {
      setIsLoadingMetadata(false)
    }
  }

  const copyEmbed = async () => {
    if (!activeMedia?.embed) {
      return
    }

    try {
      await navigator.clipboard.writeText(activeMedia.embed)
      setCopied(true)
      window.setTimeout(() => setCopied(false), 1800)
    } catch {
      setErrorMessage('Embed-Code konnte nicht in die Zwischenablage kopiert werden.')
    }
  }

  const isImage = activeMedia?.contentType?.startsWith('image/')

  return (
    <main className="app">
      <header>
        <h1>Media Handler Demo</h1>
        <p>Upload testen, Metadaten prüfen und Embed-Link direkt kopieren.</p>
      </header>

      <section className="card">
        <h2>Datei hochladen</h2>
        <form className="form" onSubmit={onUpload}>
          <input
            type="file"
            onChange={(event) => setSelectedFile(event.target.files?.[0] ?? null)}
            aria-label="Datei auswählen"
          />
          <button type="submit" disabled={isUploading}>
            {isUploading ? 'Lade hoch...' : 'Upload starten'}
          </button>
        </form>
      </section>

      <section className="card">
        <h2>Metadaten per ID abrufen</h2>
        <form className="form" onSubmit={onLookup}>
          <input
            type="text"
            value={lookupId}
            onChange={(event) => setLookupId(event.target.value)}
            placeholder="z. B. 79ce9c1f-..."
            aria-label="Media ID"
          />
          <button type="submit" disabled={isLoadingMetadata}>
            {isLoadingMetadata ? 'Lade...' : 'Abrufen'}
          </button>
        </form>
      </section>

      {errorMessage && <p className="error">{errorMessage}</p>}

      {activeMedia && (
        <section className="card result">
          <h2>Aktive Media</h2>
          <dl>
            <div><dt>ID</dt><dd>{activeMedia.id}</dd></div>
            <div><dt>Dateiname</dt><dd>{activeMedia.originalFilename}</dd></div>
            <div><dt>Content-Type</dt><dd>{activeMedia.contentType}</dd></div>
            <div><dt>Größe</dt><dd>{activeMedia.sizeBytes} Bytes</dd></div>
            <div><dt>Erstellt</dt><dd>{prettyCreatedAt}</dd></div>
            <div>
              <dt>Content</dt>
              <dd>
                <a href={activeMedia.contentUrl} target="_blank" rel="noreferrer">
                  {activeMedia.contentUrl}
                </a>
              </dd>
            </div>
          </dl>

          <div className="embedBox">
            <label htmlFor="embedText">Embed-Code</label>
            <textarea id="embedText" readOnly value={activeMedia.embed} />
            <button type="button" onClick={copyEmbed}>Embed kopieren</button>
            {copied && <span className="copied">Kopiert.</span>}
          </div>

          {isImage && (
            <div className="preview">
              <h3>Bildvorschau</h3>
              <img src={activeMedia.contentUrl} alt={activeMedia.originalFilename} />
            </div>
          )}
        </section>
      )}
    </main>
  )
}

export default App
