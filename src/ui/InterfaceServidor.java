package ui;

import modelo.Processo;
import modelo.escalonadores.*;
import rede.ServidorSimulacao;
import java.util.*;

public class InterfaceServidor {
    private ServidorSimulacao servidor;
    private Scanner scanner;
    private int velocidadeSimulacao = 100;
    private int quantum = 2;
    
    public InterfaceServidor() {
        this.servidor = new ServidorSimulacao();
        this.scanner = new Scanner(System.in);
    }
    
    public void iniciar() {
        servidor.iniciar();
        
        System.out.println("\n=== Simulador de Escalonamento de Processos ===");
        
        boolean executando = true;
        while (executando) {
            exibirMenu();
            int opcao = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcao) {
                case 1:
                    listarProcessos();
                    break;
                case 2:
                    configurarParametros();
                    break;
                case 3:
                    executarFCFS();
                    break;
                case 4:
                    executarSJF();
                    break;
                case 5:
                    executarRoundRobin();
                    break;
                case 6:
                    executarPrioridade();
                    break;
                case 7:
                    compararAlgoritmos();
                    break;
                case 8:
                    limparProcessos();
                    break;
                case 0:
                    executando = false;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
        
        servidor.parar();
        scanner.close();
        System.out.println("Servidor encerrado.");
    }
    
    private void exibirMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Processos recebidos: " + servidor.getQuantidadeProcessos());
        System.out.println("=".repeat(50));
        System.out.println("1 - Listar processos recebidos");
        System.out.println("2 - Configurar parâmetros (velocidade, quantum)");
        System.out.println("3 - Executar FCFS");
        System.out.println("4 - Executar SJF");
        System.out.println("5 - Executar Round Robin");
        System.out.println("6 - Executar Prioridade Preemptiva");
        System.out.println("7 - Comparar todos os algoritmos");
        System.out.println("8 - Limpar processos");
        System.out.println("0 - Sair");
        System.out.print("\nEscolha: ");
    }
    
    private void listarProcessos() {
        List<Processo> processos = servidor.getProcessosRecebidos();
        
        if (processos.isEmpty()) {
            System.out.println("\nNenhum processo recebido ainda.");
            return;
        }
        
        System.out.println("\n=== Processos Recebidos ===");
        for (Processo p : processos) {
            System.out.println(p);
        }
    }
    
    private void configurarParametros() {
        System.out.println("\n=== Configuração de Parâmetros ===");
        System.out.print("Velocidade de simulação (ms por unidade): [" + 
                        velocidadeSimulacao + "] ");
        String velStr = scanner.nextLine();
        if (!velStr.isEmpty()) {
            velocidadeSimulacao = Integer.parseInt(velStr);
        }
        
        System.out.print("Quantum para Round Robin: [" + quantum + "] ");
        String quantumStr = scanner.nextLine();
        if (!quantumStr.isEmpty()) {
            quantum = Integer.parseInt(quantumStr);
        }
        
        System.out.println("\nParâmetros atualizados!");
        System.out.println("Velocidade: " + velocidadeSimulacao + " ms");
        System.out.println("Quantum: " + quantum);
    }
    
    private void executarFCFS() {
        List<Processo> processos = servidor.getProcessosRecebidos();
        if (processos.isEmpty()) {
            System.out.println("\nNenhum processo para escalonar!");
            return;
        }
        
        System.out.println("\n=== Executando FCFS ===");
        Escalonador escalonador = new EscalonadorFCFS(velocidadeSimulacao);
        ResultadoSimulacao resultado = escalonador.executar(processos);
        exibirResultado(resultado);
    }
    
    private void executarSJF() {
        List<Processo> processos = servidor.getProcessosRecebidos();
        if (processos.isEmpty()) {
            System.out.println("\nNenhum processo para escalonar!");
            return;
        }
        
        System.out.println("\n=== Executando SJF ===");
        Escalonador escalonador = new EscalonadorSJF(velocidadeSimulacao);
        ResultadoSimulacao resultado = escalonador.executar(processos);
        exibirResultado(resultado);
    }
    
    private void executarRoundRobin() {
        List<Processo> processos = servidor.getProcessosRecebidos();
        if (processos.isEmpty()) {
            System.out.println("\nNenhum processo para escalonar!");
            return;
        }
        
        System.out.println("\n=== Executando Round Robin ===");
        Escalonador escalonador = new EscalonadorRoundRobin(quantum, velocidadeSimulacao);
        ResultadoSimulacao resultado = escalonador.executar(processos);
        exibirResultado(resultado);
    }
    
    private void executarPrioridade() {
        List<Processo> processos = servidor.getProcessosRecebidos();
        if (processos.isEmpty()) {
            System.out.println("\nNenhum processo para escalonar!");
            return;
        }
        
        System.out.println("\n=== Executando Prioridade Preemptiva ===");
        Escalonador escalonador = new EscalonadorPrioridade(velocidadeSimulacao);
        ResultadoSimulacao resultado = escalonador.executar(processos);
        exibirResultado(resultado);
    }
    
    private void compararAlgoritmos() {
        List<Processo> processos = servidor.getProcessosRecebidos();
        if (processos.isEmpty()) {
            System.out.println("\nNenhum processo para escalonar!");
            return;
        }
        
        System.out.println("\n=== Comparando Algoritmos ===");
        System.out.println("Executando todos os algoritmos...\n");
        
        Escalonador[] escalonadores = {
            new EscalonadorFCFS(velocidadeSimulacao),
            new EscalonadorSJF(velocidadeSimulacao),
            new EscalonadorRoundRobin(quantum, velocidadeSimulacao),
            new EscalonadorPrioridade(velocidadeSimulacao)
        };
        
        List<ResultadoSimulacao> resultados = new ArrayList<>();
        
        for (Escalonador esc : escalonadores) {
            System.out.println("Executando: " + esc.getNome());
            ResultadoSimulacao resultado = esc.executar(processos);
            resultados.add(resultado);
        }
        
        exibirComparacao(escalonadores, resultados);
    }
    
    private void exibirResultado(ResultadoSimulacao resultado) {
        System.out.println("\n--- Log de Execução ---");
        for (String log : resultado.getLogExecucao()) {
            System.out.println(log);
        }
        
        System.out.println("\n--- Métricas por Processo ---");
        for (Processo p : resultado.getProcessos()) {
            System.out.println(p.toStringCompleto());
        }
        
        System.out.println("\n--- Resumo ---");
        System.out.println(resultado.getResumo());
    }
    
    private void exibirComparacao(Escalonador[] escalonadores, 
                                  List<ResultadoSimulacao> resultados) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("COMPARAÇÃO DE ALGORITMOS");
        System.out.println("=".repeat(80));
        
        System.out.printf("%-30s | %15s | %15s | %15s | %10s\n",
            "Algoritmo", "Tempo Médio", "Tempo Médio", "Tempo Médio", "Tempo Real");
        System.out.printf("%-30s | %15s | %15s | %15s | %10s\n",
            "", "Espera", "Retorno", "Resposta", "(ms)");
        System.out.println("-".repeat(80));
        
        for (int i = 0; i < escalonadores.length; i++) {
            ResultadoSimulacao r = resultados.get(i);
            System.out.printf("%-30s | %15.2f | %15.2f | %15.2f | %10d\n",
                escalonadores[i].getNome(),
                r.getTempoMedioEspera(),
                r.getTempoMedioRetorno(),
                r.getTempoMedioResposta(),
                r.getTempoRealExecucao()
            );
        }
        System.out.println("=".repeat(80));
    }
    
    private void limparProcessos() {
        servidor.limparProcessos();
        System.out.println("\nProcessos limpos!");
    }
    
    public static void main(String[] args) {
        InterfaceServidor ui = new InterfaceServidor();
        ui.iniciar();
    }
}