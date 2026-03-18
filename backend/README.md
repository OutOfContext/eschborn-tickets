# Backend Media API

Dieses Backend stellt einen einfachen Media-Handler bereit.

## Endpunkte

- `POST /api/media` (multipart/form-data, Feld: `file`)
- `GET /api/media/{id}` (Metadaten)
- `GET /api/media/{id}/content` (Dateiinhalt)

## Konfiguration

- `MEDIA_STORAGE_ROOT` (Default: `uploads`)
- `MAX_UPLOAD_FILE_SIZE` (Default: `20MB`)
- `MAX_UPLOAD_REQUEST_SIZE` (Default: `20MB`)

## Schnelltest

```bash
./mvnw test
./mvnw spring-boot:run
```

Beispiel-Upload:

```bash
curl -X POST http://localhost:8080/api/media \
  -F "file=@./example.png"
```

