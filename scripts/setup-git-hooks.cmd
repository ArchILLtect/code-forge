@echo off
REM Configure local repo to use hooks in .githooks directory.
REM Run this once per clone.

cd /d "%~dp0.."

git config core.hooksPath .githooks
IF ERRORLEVEL 1 (
  echo [setup] Failed to set core.hooksPath. Ensure you are in a Git repo.
  EXIT /B 1
)

echo [setup] Git hooks configured to use .githooks

echo [setup] You can bypass hooks with: git commit --no-verify
EXIT /B 0

