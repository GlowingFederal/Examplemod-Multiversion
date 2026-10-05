param([Parameter(ValueFromRemainingArguments=$true)][string[]]$Arguments)
$ErrorActionPreference = 'Stop'
$python = if ($env:PYTHON) { $env:PYTHON } elseif (Get-Command python -ErrorAction SilentlyContinue) { 'python' } else { 'python3' }
& $python (Join-Path $PSScriptRoot 'build.py') 'all' @Arguments
exit $LASTEXITCODE
