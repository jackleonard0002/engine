# PowerShell script to automate uploading a project to GitHub
# Usage: Run this script in your project root directory

# Prompt for GitHub repository URL
$repoUrl = Read-Host "Enter your GitHub repository URL (e.g., https://github.com/yourusername/your-repo.git)"

# Initialize git if not already initialized
if (!(Test-Path ".git")) {
    git init
}

# Add all files
git add .

# Commit changes
$commitMessage = Read-Host "Enter commit message (default: Initial commit)"
if ([string]::IsNullOrWhiteSpace($commitMessage)) {
    $commitMessage = "Initial commit"
}
git commit -m "$commitMessage"

# Set remote origin
if ((git remote) -notcontains "origin") {
    git remote add origin $repoUrl
} else {
    git remote set-url origin $repoUrl
}

# Set branch to main
try {
    git branch -M main
} catch {}

# Push to GitHub
Write-Host "Pushing to GitHub..."
git push -u origin main

Write-Host "Done!"
