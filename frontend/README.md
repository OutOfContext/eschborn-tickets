# Frontend Ticket-Demo

Diese React-App zeigt jetzt einen kleinen App-Flow:

- Login via `POST /api/auth/login`
- Session-Check via `GET /api/auth/me`
- Absprung-Menue nach erfolgreichem Login
- Media-Demo als eigener Zielpunkt im Menue

In der Media-Demo selbst stehen weiterhin diese Endpunkte im Fokus:

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

1. Mit einem Testuser einloggen (z. B. `admin` / `admin123`).
2. Im Absprung-Menue die "MediaHandler-Demo" oeffnen.
3. Im Bereich "Datei hochladen" eine Datei auswaehlen und hochladen.
4. Danach werden Metadaten und Content-Link angezeigt.
5. Mit der erzeugten ID im Bereich "Metadaten per ID abrufen" den Abruf erneut testen.
6. Embed-Code kopieren und in Ticket-/Markdown-Text einfuegen.

## Build-Check

```bash
npm run build
```
