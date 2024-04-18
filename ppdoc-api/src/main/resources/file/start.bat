@echo off
call <LOCAL_PATH_REDACTED>
pause
cd <LOCAL_PATH_REDACTED>
hub serving start -c deploy\hubserving\ocr_system\config.json
pause
