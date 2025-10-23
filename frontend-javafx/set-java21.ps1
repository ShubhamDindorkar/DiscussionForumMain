# Set Java 21 as default Java version
# Run this script as Administrator

Write-Host "Setting Java 21 as default..." -ForegroundColor Green

# Set JAVA_HOME
$javaHome = "C:\Program Files\Java\jdk-21"
[System.Environment]::SetEnvironmentVariable('JAVA_HOME', $javaHome, 'Machine')
Write-Host "JAVA_HOME set to: $javaHome" -ForegroundColor Cyan

# Update PATH - Move Java 21 to the front
$currentPath = [System.Environment]::GetEnvironmentVariable('Path', 'Machine')

# Remove any existing Java paths
$pathEntries = $currentPath -split ';' | Where-Object { $_ -notmatch 'Java\\jdk' }

# Add Java 21 at the beginning
$newPath = "$javaHome\bin;" + ($pathEntries -join ';')
[System.Environment]::SetEnvironmentVariable('Path', $newPath, 'Machine')

Write-Host "PATH updated - Java 21 is now prioritized" -ForegroundColor Cyan
Write-Host ""
Write-Host "Please restart your terminal/IDE for changes to take effect!" -ForegroundColor Yellow
Write-Host ""
Write-Host "After restarting, verify with: java -version" -ForegroundColor Yellow

pause
