# eschborn-tickets
Schulungsprojekt: Ticketsystem

## Immer den aktuellsten Stand starten

Im Projekt liegt ein Startskript, das den Stack immer frisch aufsetzt:

- zieht aktuelle Registry-Images
- baut `backend` und `frontend` mit aktuellen Base-Images
- recreated alle Container

```powershell
Set-Location "C:\Users\rhueh\Documents\Projects\eschborn-tickets"
.\start-latest.ps1
```

Optional mit komplett sauberem Build-Cache:

```powershell
Set-Location "C:\Users\rhueh\Documents\Projects\eschborn-tickets"
.\start-latest.ps1 -NoCache
```

