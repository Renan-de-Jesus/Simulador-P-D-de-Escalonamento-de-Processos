@echo off
echo === Iniciando Cliente Gerador de Processos ===
echo.

if not exist "bin" (
    echo Erro: Diretório bin não encontrado!
    echo Execute compilar.bat primeiro
    pause
    exit /b 1
)

java -cp bin rede.ClienteGerador
pause