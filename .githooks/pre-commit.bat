@echo off
REM Pre-commit hook for Windows: block commit if Maven tests fail.
REM Override 1: git commit --no-verify
REM Override 2: set GIT_ALLOW_COMMIT=1 (env var)

IF "%GIT_ALLOW_COMMIT%"=="1" (
  echo [pre-commit] Override detected (GIT_ALLOW_COMMIT=1). Skipping checks.
  EXIT /B 0
)

where mvn >NUL 2>&1
IF ERRORLEVEL 1 (
  echo [pre-commit] Maven (mvn) not found on PATH. Aborting commit.
  EXIT /B 1
)

echo [pre-commit] Running Maven tests...
REM Quiet-ish run, fail on test errors
mvn -q -DskipTests=false test
IF ERRORLEVEL 1 (
  echo [pre-commit] Maven tests failed. Fix issues or commit with --no-verify to override.
  EXIT /B 1
)

echo [pre-commit] Checks passed. Proceeding with commit.
EXIT /B 0

