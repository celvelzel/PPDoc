@echo off
@REM Activate the PaddleOCR environment before running this script.
cd /d "%~dp0"
hub serving start -c deploy\hubserving\ocr_system\config.json
pause
