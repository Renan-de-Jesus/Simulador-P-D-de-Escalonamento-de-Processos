package modelo.escalonadores;

import modelo.Processo;
import java.util.*;

public class EscalonadorRoundRobin implements Escalonador {
    private int quantum;
    private int velocidadeSimulacao = 100;
    
    public EscalonadorRoundRobin(int quantum, int velocidade) {
        this.quantum = quantum;
        this.velocidadeSimulacao = velocidade;
    }
    
    @Override
    public String getNome() {
        return "Round Robin (Quantum = " + quantum + ")";
    }
    
    @Override
    public ResultadoSimulacao executar(List<Processo> processos) {
        long inicio = System.currentTimeMillis();
        
        List<Processo> processosClone = new ArrayList<>();
        for (Processo p : processos) {
            processosClone.add(p.clonar());
        }
        
        List<String> log = new ArrayList<>();
        Queue<Processo> filaEspera = new LinkedList<>();
        List<Processo> executados = new ArrayList<>();
        int tempoAtual = 0;
        
        log.add("=== Iniciando Round Robin (Quantum=" + quantum + ") ===");
        
        processosClone.sort(Comparator.comparingInt(Processo::getTempoChegada));
        
        if (!processosClone.isEmpty() && processosClone.get(0).getTempoChegada() == 0) {
            filaEspera.add(processosClone.remove(0));
        }
        
        while (!filaEspera.isEmpty() || !processosClone.isEmpty()) {
            if (filaEspera.isEmpty()) {
                if (!processosClone.isEmpty()) {
                    Processo prox = processosClone.remove(0);
                    log.add(String.format("Tempo %d-%d: CPU Ociosa", 
                        tempoAtual, prox.getTempoChegada()));
                    tempoAtual = prox.getTempoChegada();
                    filaEspera.add(prox);
                }
                continue;
            }
            
            Processo atual = filaEspera.poll();
            int tempoExecucao = Math.min(quantum, atual.getTempoRestante());
            
            log.add(String.format("Tempo %d: Executando processo %s por %d unidades (restante: %d)", 
                tempoAtual, atual.getId(), tempoExecucao, atual.getTempoRestante()));
            
            atual.executar(tempoExecucao, tempoAtual);
            
            try {
                Thread.sleep(tempoExecucao * velocidadeSimulacao);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            tempoAtual += tempoExecucao;
            
            Iterator<Processo> it = processosClone.iterator();
            while (it.hasNext()) {
                Processo p = it.next();
                if (p.getTempoChegada() <= tempoAtual) {
                    filaEspera.add(p);
                    it.remove();
                    log.add(String.format("Tempo %d: Processo %s chegou", 
                        tempoAtual, p.getId()));
                } else {
                    break;
                }
            }
            
            if (atual.isCompleto()) {
                log.add(String.format("Tempo %d: Processo %s finalizado", 
                    tempoAtual, atual.getId()));
                executados.add(atual);
            } else {
                filaEspera.add(atual);
                log.add(String.format("Tempo %d: Processo %s retorna à fila", 
                    tempoAtual, atual.getId()));
            }
        }
        
        log.add("=== Round Robin Finalizado ===");
        
        long fim = System.currentTimeMillis();
        return new ResultadoSimulacao(executados, log, fim - inicio);
    }
}