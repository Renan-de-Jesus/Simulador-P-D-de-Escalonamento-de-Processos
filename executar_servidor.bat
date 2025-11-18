@echo off
echo === Iniciando Servidor de Simulação ===
echo.

if not exist "bin" (
    echo Erro: Diretório bin não encontrado!
    echo Execute compilar.bat primeiro
    pause
    exit /b 1
)

java -cp bin ui.InterfaceServidor
pause