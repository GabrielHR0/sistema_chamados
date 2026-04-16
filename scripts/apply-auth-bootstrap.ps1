param(
    [string]$ContainerName = "chamados-db",
    [string]$DbUser = "chamados_dev",
    [string]$DbName = "chamados"
)

$ErrorActionPreference = "Stop"

$scriptPath = Join-Path $PSScriptRoot "..\src\main\resources\db\seed\auth_bootstrap.sql"
$resolvedScriptPath = (Resolve-Path $scriptPath).Path

if (-not (Test-Path $resolvedScriptPath)) {
    throw "Arquivo SQL nao encontrado: $scriptPath"
}

Get-Content -Raw $resolvedScriptPath |
    docker exec -i $ContainerName psql -v ON_ERROR_STOP=1 -U $DbUser -d $DbName
