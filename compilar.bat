@echo off
echo === Compilador do Simulador de Escalonamento ===
echo.

REM Verificar se Java está instalado
javac -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ERRO] Java JDK nao encontrado!
    echo Instale o JDK e adicione ao PATH
    pause
    exit /b 1
)

REM Criar diretório bin se não existir
if not exist "bin" (
    echo Criando diretório bin...
    mkdir bin
)

REM Verificar se src existe
if not exist "src" (
    echo [ERRO] Diretório src nao encontrado!
    echo Crie a estrutura de diretorios primeiro.
    pause
    exit /b 1
)

REM Limpar bin antes de compilar
echo Limpando arquivos antigos...
del /Q bin\*.class 2>nul
del /Q bin\modelo\*.class 2>nul
del /Q bin\escalonadores\*.class 2>nul
del /Q bin\rede\*.class 2>nul
del /Q bin\ui\*.class 2>nul

REM Compilar na ordem de dependências
echo.
echo Compilando arquivos Java...
echo.

echo [1/5] Compilando modelo...
javac -d bin -encoding UTF-8 src\modelo\Processo.java
if %ERRORLEVEL% NEQ 0 goto erro

echo [2/5] Compilando escalonadores...
javac -d bin -encoding UTF-8 -cp bin src\escalonadores\Escalonador.java
if %ERRORLEVEL% NEQ 0 goto erro

javac -d bin -encoding UTF-8 -cp bin src\escalonadores\ResultadoSimulacao.java
if %ERRORLEVEL% NEQ 0 goto erro

javac -d bin -encoding UTF-8 -cp bin src\escalonadores\EscalonadorFCFS.java
if %ERRORLEVEL% NEQ 0 goto erro

javac -d bin -encoding UTF-8 -cp bin src\escalonadores\EscalonadorSJF.java
if %ERRORLEVEL% NEQ 0 goto erro

javac -d bin -encoding UTF-8 -cp bin src\escalonadores\EscalonadorRoundRobin.java
if %ERRORLEVEL% NEQ 0 goto erro

javac -d bin -encoding UTF-8 -cp bin src\escalonadores\EscalonadorPrioridade.java
if %ERRORLEVEL% NEQ 0 goto erro

echo [3/5] Compilando rede...
javac -d bin -encoding UTF-8 -cp bin src\rede\ServidorSimulacao.java
if %ERRORLEVEL% NEQ 0 goto erro

javac -d bin -encoding UTF-8 -cp bin src\rede\ClienteGerador.java
if %ERRORLEVEL% NEQ 0 goto erro

echo [4/5] Compilando interface...
javac -d bin -encoding UTF-8 -cp bin src\ui\InterfaceServidor.java
if %ERRORLEVEL% NEQ 0 goto erro

echo [5/5] Verificando compilacao...
if not exist "bin\ui\InterfaceServidor.class" goto erro
if not exist "bin\rede\ClienteGerador.class" goto erro

echo.
echo ========================================
echo [OK] Compilacao concluida com sucesso!
echo ========================================
echo.
echo Para executar:
echo   Servidor: executar_servidor.bat
echo   Cliente:  executar_cliente.bat
echo.
pause
exit /b 0

:erro
echo.
echo ========================================
echo [ERRO] Falha na compilacao!
echo ========================================
echo.
echo Verifique:
echo 1. Todos os arquivos .java estao nos diretorios corretos
echo 2. Nao ha erros de sintaxe no codigo
echo 3. A estrutura de pacotes esta correta
echo.
pause
exit /b 1