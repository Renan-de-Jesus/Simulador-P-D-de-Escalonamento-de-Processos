package modelo.escalonadores;

import modelo.Processo;
import java.util.*;

public class EscalonadorFCFS implements Escalonador {
    private int velocidadeSimulacao = 100;
    
    public EscalonadorFCFS(int velocidade) {
        this.velocidadeSimulacao = velocidade;
    }
    
    @Override
    public String getNome() {
        return "FCFS (First Come, First Served)";
    }
    
    @Override
    public ResultadoSimulacao executar(List<Processo> processos) {
        long inicio = System.currentTimeMillis();
        
        List<Processo> processosClone = new ArrayList<>();
        for (Processo p : processos) {
            processosClone.add(p.clonar());
        }
        
        processosClone.sort(Comparator.comparingInt(Processo::getTempoChegada));
        
        List<String> log = new ArrayList<>();
        int tempoAtual = 0;
        
        log.add("=== Iniciando FCFS ===");
        
        for (Processo p : processosClone) {
            if (tempoAtual < p.getTempoChegada()) {
                log.add(String.format("Tempo %d-%d: CPU Ociosa", 
                    tempoAtual, p.getTempoChegada()));
                tempoAtual = p.getTempoChegada();
            }
            
            log.add(String.format("Tempo %d: Iniciando processo %s (duração: %d)", 
                tempoAtual, p.getId(), p.getDuracaoCPU()));
            
            p.executar(p.getDuracaoCPU(), tempoAtual);
            
            try {
                Thread.sleep(p.getDuracaoCPU() * velocidadeSimulacao);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            tempoAtual += p.getDuracaoCPU();
            
            log.add(String.format("Tempo %d: Finalizando processo %s", 
                tempoAtual, p.getId()));
        }
        
        log.add("=== FCFS Finalizado ===");
        
        long fim = System.currentTimeMillis();
        return new ResultadoSimulacao(processosClone, log, fim - inicio);
    }
}