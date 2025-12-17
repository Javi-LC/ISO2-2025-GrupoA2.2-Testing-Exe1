<#
Create and optionally close GitHub issues for the Exe1 repository.

Usage examples:
  # From the repo folder, create issues and leave them open
  .\scripts\gh-issues-exe1.ps1

  # Create issues and close them immediately after creation
  .\scripts\gh-issues-exe1.ps1 -CloseImmediately

  # Run from another folder pointing to the repo path
  .\scripts\gh-issues-exe1.ps1 -RepoPath "C:\path\to\ISO2-2025-GrupoA2.2-Testing-Exe1"

Requirements:
  - `gh` CLI must be installed and authenticated (`gh auth login`).
  - Run this script in PowerShell 5+ or PowerShell Core.
#>

param(
    [string]$RepoPath = ".",
    [switch]$CloseImmediately
)

function Write-ErrAndExit($msg) {
    Write-Error $msg
    exit 1
}

if (-not (Get-Command gh -ErrorAction SilentlyContinue)) {
    Write-ErrAndExit "gh CLI not found in PATH. Install it (https://cli.github.com/) and authenticate (gh auth login)."
}

$issues = @(
    @{ title = 'Normalize packages and fix tests'; body = 'Normalize package names to lowercase, fix test package paths and ensure build passes.' },
    @{ title = 'Run site reports and fix Checkstyle'; body = 'Run `mvn clean site`, review `target/site/checkstyle.html` and fix reported Checkstyle issues.' },
    @{ title = 'Add docs/Maintenance-Exe1.md and update README'; body = 'Add maintenance report `docs/Maintenance-Exe1.md` and update README with links to site and reports.' },
    @{ title = 'Generate comparison artifacts and tag release'; body = 'Generate before/after site comparison artifacts and create release tag v0.0.1/v0.0.2 as appropriate.' }
)

Push-Location -LiteralPath $RepoPath
try {
    $created = @()
    foreach ($it in $issues) {
        Write-Host "Creating issue: $($it.title)"
        $out = gh issue create --title "${($it.title)}" --body "${($it.body)}" --json number,url 2>$null
        if ($LASTEXITCODE -ne 0 -or -not $out) {
            Write-Warning "Failed to create issue: $($it.title)"
            continue
        }
        $obj = $out | ConvertFrom-Json
        $num = $obj.number
        $url = $obj.url
        Write-Host "Created issue #$num -> $url"
        $created += @{ number = $num; url = $url; title = $it.title }
    }

    if ($CloseImmediately -and $created.Count -gt 0) {
        foreach ($c in $created) {
            Write-Host "Adding closing comment and closing issue #$($c.number)"
            gh issue comment $c.number --body "Maintenance performed; closing issue." 2>$null
            gh issue close $c.number 2>$null
            if ($LASTEXITCODE -eq 0) { Write-Host "Closed #$($c.number)" } else { Write-Warning "Could not close #$($c.number)" }
        }
    }

    if ($created.Count -gt 0) {
        Write-Host "Created issues:"
        foreach ($c in $created) { Write-Host "  #$($c.number) - $($c.title) -> $($c.url)" }
    } else {
        Write-Host "No issues were created."
    }
}
finally {
    Pop-Location
}
