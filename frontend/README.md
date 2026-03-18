# Frontend Media Demo

Diese React-App demonstriert das Media-Feature aus dem Backend:

- Datei per `POST /api/media` hochladen
- Metadaten per `GET /api/media/{id}` abrufen
- Content-Link `GET /api/media/{id}/content` öffnen
- Embed-Code anzeigen und kopieren

## Voraussetzungen

- Backend läuft lokal auf `http://localhost:8080`
- Node.js 20+ (oder kompatibel)

## Starten

```bash
npm install
npm run dev
```

Die Vite-Dev-Config proxyt `/api` automatisch auf `http://localhost:8080`.

## Demo-Ablauf

1. Im Bereich "Datei hochladen" eine Datei auswählen und hochladen.
2. Danach werden Metadaten und Content-Link angezeigt.
3. Mit der erzeugten ID im Bereich "Metadaten per ID abrufen" den Abruf erneut testen.
4. Embed-Code kopieren und in Ticket-/Markdown-Text einfügen.

## Build-Check

```bash
npm run build
```
