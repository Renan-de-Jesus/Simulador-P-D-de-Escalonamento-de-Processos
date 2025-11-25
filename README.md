# Simulador Paralelo e Distribuído de Escalonamento de Processos

## 📋 Descrição

Sistema distribuído para simulação de algoritmos de escalonamento de processos com arquitetura cliente-servidor, implementado em Java com threads e comunicação em rede.

## 🏗️ Arquitetura

O projeto está organizado nos seguintes pacotes:

```
src/
├── modelo/
│   └── Processo.java
├── escalonadores/
│   ├── Escalonador.java
│   ├── ResultadoSimulacao.java
│   ├── EscalonadorFCFS.java
│   ├── EscalonadorSJF.java
│   ├── EscalonadorRoundRobin.java
│   └── EscalonadorPrioridade.java
├── rede/
│   ├── ServidorSimulacao.java
│   └── ClienteGerador.java
└── ui/
    └── InterfaceServidor.java
```

### Componentes Principais

- **Modelo de Processo**: Classe que representa um processo com seus atributos e métricas
- **Escalonadores**: Implementação dos algoritmos FCFS, SJF, Round Robin e Prioridade Preemptiva
- **Servidor**: Recebe processos pela rede e executa as simulações
- **Cliente**: Envia processos ao servidor
- **Interface**: Menu interativo para controlar o servidor

## 🚀 Como Compilar

### Pré-requisitos

- Java JDK 8 ou superior
- Compilador `javac` disponível no PATH

### Estrutura de Diretórios

Antes de compilar, organize os arquivos nesta estrutura:

```
SimuladorEscalonamento/
├── src/
│   ├── modelo/
│   │   └── Processo.java
│   ├── escalonadores/
│   │   ├── Escalonador.java
│   │   ├── ResultadoSimulacao.java
│   │   ├── EscalonadorFCFS.java
│   │   ├── EscalonadorSJF.java
│   │   ├── EscalonadorRoundRobin.java
│   │   └── EscalonadorPrioridade.java
│   ├── rede/
│   │   ├── ServidorSimulacao.java
│   │   └── ClienteGerador.java
│   └── ui/
│       └── InterfaceServidor.java
└── bin/
```

### Criar Estrutura de Diretórios

```bash
mkdir -p src/modelo src/escalonadores src/rede src/ui bin
```

### Compilação

#### Opção 1: Compilação Simples (Todos os arquivos de uma vez)

```bash
javac -d bin -encoding UTF-8 -sourcepath src src/**/*.java
```

#### Opção 2: Compilação Por Ordem de Dependências

```bash
# 1. Compilar modelo
javac -d bin -encoding UTF-8 src/modelo/Processo.java

# 2. Compilar interfaces e classes base
javac -d bin -encoding UTF-8 -cp bin src/escalonadores/Escalonador.java
javac -d bin -encoding UTF-8 -cp bin src/escalonadores/ResultadoSimulacao.java

# 3. Compilar escalonadores
javac -d bin -encoding UTF-8 -cp bin src/escalonadores/EscalonadorFCFS.java
javac -d bin -encoding UTF-8 -cp bin src/escalonadores/EscalonadorSJF.java
javac -d bin -encoding UTF-8 -cp bin src/escalonadores/EscalonadorRoundRobin.java
javac -d bin -encoding UTF-8 -cp bin src/escalonadores/EscalonadorPrioridade.java

# 4. Compilar rede
javac -d bin -encoding UTF-8 -cp bin src/rede/ServidorSimulacao.java
javac -d bin -encoding UTF-8 -cp bin src/rede/ClienteGerador.java

# 5. Compilar interface
javac -d bin -encoding UTF-8 -cp bin src/ui/InterfaceServidor.java
```

#### Opção 3: Usando Lista de Arquivos

```bash
# Criar lista de arquivos
find src -name "*.java" > sources.txt

# Compilar
javac -d bin -encoding UTF-8 @sources.txt

# Limpar
rm sources.txt
```

### Verificação da Compilação

Após compilar, verifique se os arquivos `.class` foram criados:

```bash
ls -R bin/
```

Deve mostrar estrutura similar a:
```
bin/
├── modelo/
│   └── Processo.class
├── escalonadores/
│   ├── Escalonador.class
│   ├── ResultadoSimulacao.class
│   └── ...
├── rede/
│   └── ...
└── ui/
    └── InterfaceServidor.class
```

## ▶️ Como Executar

### 1. Iniciar o Servidor

```bash
java -cp bin ui.InterfaceServidor
```

O servidor iniciará na porta **5000** e exibirá o menu principal.

### 2. Executar o Cliente (em outro terminal)

```bash
java -cp bin rede.ClienteGerador
```

## 📝 Formato dos Processos

Os processos devem ser enviados no formato:

```
ID;tempoChegada;duracao;prioridade
```

**Exemplo:**
```
P1;0;5;1
P2;1;3;2
P3;2;8;1
P4;3;6;3
```

Onde:
- **ID**: Identificador único do processo
- **tempoChegada**: Momento em que o processo chega (unidades de tempo)
- **duracao**: Tempo de CPU necessário (burst time)
- **prioridade**: Número de prioridade (menor = maior prioridade)

## 🎮 Uso do Sistema

### Menu do Servidor

1. **Listar processos recebidos**: Mostra todos os processos que o servidor recebeu
2. **Configurar parâmetros**: Ajusta velocidade de simulação e quantum do RR
3. **Executar FCFS**: Simula com First Come, First Served
4. **Executar SJF**: Simula com Shortest Job First (não preemptivo)
5. **Executar Round Robin**: Simula com Round Robin
6. **Executar Prioridade**: Simula com Prioridade Preemptiva
7. **Comparar algoritmos**: Executa todos e compara resultados
8. **Limpar processos**: Remove todos os processos da memória

### Menu do Cliente

1. **Enviar processos manualmente**: Digite processos um por um
2. **Enviar processos de arquivo**: Carrega de um arquivo .txt
3. **Gerar processos aleatórios**: Cria N processos automaticamente

## 📊 Métricas Calculadas

Para cada processo:
- **Tempo de Espera**: Tempo aguardando na fila
- **Tempo de Retorno**: Tempo total no sistema
- **Tempo de Resposta**: Tempo até primeira execução

Médias gerais:
- Tempo médio de espera
- Tempo médio de retorno
- Tempo médio de resposta
- Tempo real de execução da simulação

## 🔧 Algoritmos Implementados

### 1. FCFS (First Come, First Served)
- Não preemptivo
- Executa processos na ordem de chegada
- Simples mas pode causar efeito comboio

### 2. SJF (Shortest Job First)
- Não preemptivo
- Escolhe processo com menor burst
- Minimiza tempo médio de espera

### 3. Round Robin
- Preemptivo
- Cada processo recebe quantum de tempo
- Quantum configurável pelo usuário

### 4. Prioridade Preemptiva
- Preemptivo
- Menor número = maior prioridade
- Pode causar starvation

## 🧵 Uso de Threads

- **Thread principal**: Gerencia a interface do servidor
- **Thread de aceitação**: Aceita conexões de clientes (ServerSocket.accept())
- **Thread pool**: Pool de threads para atender múltiplos clientes simultaneamente
- **Thread de CPU**: Cada simulação usa Thread.sleep() para simular tempo de execução

## 🌐 Comunicação em Rede

- **Protocolo**: TCP/IP via Sockets
- **Porta**: 5000
- **Formato**: Texto simples (linha por processo)
- **Confirmação**: Servidor responde "OK" para cada processo recebido
- **Fim de transmissão**: Cliente envia "FIM"

## 📁 Exemplo de Arquivo de Processos

Crie um arquivo `processos.txt`:

```
# Exemplo de processos para simulação
P1;0;5;1
P2;1;3;2
P3;2;8;1
P4;3;6;3
P5;4;2;2
```

Use a opção 2 do cliente para carregar este arquivo.

## ⚙️ Configurações

### Velocidade de Simulação
- Padrão: 100 ms por unidade de tempo
- Ajustável pelo menu do servidor
- Valores menores = simulação mais rápida

### Quantum (Round Robin)
- Padrão: 2 unidades de tempo
- Ajustável pelo menu do servidor

## 🎯 Exemplo de Uso Completo

**Terminal 1 - Servidor:**
```bash
$ java -cp bin ui.InterfaceServidor
# Servidor iniciado, aguardando...
# Escolher opção 7 (comparar todos)
```

**Terminal 2 - Cliente:**
```bash
$ java -cp bin rede.ClienteGerador
# Escolher opção 3 (gerar aleatórios)
# Gerar 5 processos
# Confirmar envio
```

**Terminal 1 - Servidor:**
```
# Ver mensagens de processos recebidos
# Visualizar resultados comparativos
```

## 🐛 Solução de Problemas

### "Connection refused"
- Verifique se o servidor está rodando
- Confirme que a porta 5000 está disponível
- Certifique-se de estar usando localhost

### Processos não aparecem
- Verifique o formato: `ID;tempo;duracao;prioridade`
- Confirme que enviou "FIM" ao terminar
- Veja se há mensagens de erro no servidor

### Simulação muito lenta
- Reduza a velocidade de simulação no menu do servidor
- Use valores menores (ex: 50 ou 10 ms)

### Erros de compilação
- Verifique se todos os arquivos estão nos diretórios corretos
- Confirme que o package está correto em cada arquivo
- Certifique-se que o JDK está instalado (não apenas JRE)
- Use a opção de compilação por ordem de dependências

## 📚 Dependências

- Java 8 ou superior
- Nenhuma biblioteca externa necessária

## 🎓 Estrutura para Entrega Acadêmica

O projeto deve conter:

1. **Código-fonte completo** organizado em pacotes
2. **Arquivo README** (este documento)
3. **Relatório** (3-5 páginas) contendo:
   - Descrição da arquitetura
   - Explicação das threads usadas
   - Explicação da comunicação em rede
   - Tabelas comparando os algoritmos
   - Dificuldades encontradas e soluções adotadas
4. **Exemplos de uso** (arquivo de processos de teste)

## 📊 Requisitos Implementados

✅ **Requisitos Obrigatórios:**
- Implementar FCFS, SJF, Round Robin e Prioridade Preemptiva
- Utilizar threads na simulação
- Implementar comunicação cliente-servidor via rede
- Permitir escolha dinâmica do algoritmo pela interface
- Medir e exibir métricas de tempo para cada processo e médias
- Interface clara para controlar a simulação

🌟 **Desafios Opcionais:**
- Servidor capaz de receber clientes concorrentes
- Thread pool para gerenciar múltiplos clientes
- Comparação automática entre todos os algoritmos
- Configuração dinâmica de parâmetros

## 👥 Desenvolvimento

Projeto desenvolvido como trabalho acadêmico de Sistemas Operacionais.

### Tecnologias Utilizadas
- Linguagem: Java
- Paradigma: Orientado a Objetos
- Conceitos: Threads, Sockets, Algoritmos de Escalonamento

## 📄 Licença

Projeto educacional - livre para uso acadêmico.

---

**Desenvolvido para aprendizado de:**
- Sistemas Operacionais
- Programação Paralela e Distribuída
- Algoritmos de Escalonamento
- Comunicação em Rede

**Versão:** 1.0  
**Data:** Novembro 2025