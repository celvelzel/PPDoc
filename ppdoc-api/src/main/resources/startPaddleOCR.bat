@echo off
D:
cd <LOCAL_PATH_REDACTED>
activate paddle_env
cd <LOCAL_PATH_REDACTED>
hub serving start -c deploy\hubserving\ocr_system\config.json
pause
