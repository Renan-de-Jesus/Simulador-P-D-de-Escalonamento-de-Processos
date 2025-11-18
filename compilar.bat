@echo off
echo === Compilador do Simulador de Escalonamento ===
echo.

REM Criar diretório bin se não existir
if not exist "bin" (
    echo Criando diretório bin...
    mkdir bin
)

REM Criar estrutura src se não existir
if not exist "src" (
    echo Criando estrutura de diretórios src...
    mkdir src\modelo
    mkdir src\escalonadores
    mkdir src\rede
    mkdir src\ui
)

REM Compilar todos os arquivos Java
echo Compilando arquivos Java...
javac -d bin -sourcepath src src\modelo\*.java src\escalonadores\*.java src\rede\*.java src\ui\*.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo [OK] Compilação concluída com sucesso!
    echo.
    echo Para executar:
    echo   Servidor: executar_servidor.bat
    echo   Cliente:  executar_cliente.bat
    echo.
) else (
    echo.
    echo [ERRO] Erro na compilação!
    pause
    exit /b 1
)

pause