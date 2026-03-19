param(
  [switch]$NoCache
)

$ErrorActionPreference = 'Stop'

$repoRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $repoRoot

Write-Host "[1/5] Stoppe alte Container..."
docker compose down --remove-orphans

Write-Host "[2/5] Ziehe aktuelle Registry-Images..."
docker compose pull --ignore-pull-failures

Write-Host "[3/5] Baue lokale Services (backend/frontend) mit aktuellen Base-Images..."
$buildArgs = @('compose', 'build', '--pull')
if ($NoCache) {
  $buildArgs += '--no-cache'
}
$buildArgs += @('backend', 'frontend')
docker @buildArgs

Write-Host "[4/5] Starte Stack frisch..."
docker compose up -d --force-recreate --remove-orphans

Write-Host "[5/5] Status"
docker compose ps

