param([Parameter(ValueFromRemainingArguments=$true)][string[]]$Arguments)
$ErrorActionPreference = 'Stop'
$python = if ($env:PYTHON) { $env:PYTHON } elseif (Get-Command python3 -ErrorAction SilentlyContinue) { 'python3' } else { 'python' }
& $python (Join-Path $PSScriptRoot 'build.py') 'test' @Arguments
exit $LASTEXITCODE
