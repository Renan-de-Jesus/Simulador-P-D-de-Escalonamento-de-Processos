package modelo.escalonadores;

import modelo.Processo;
import java.util.*;

public class EscalonadorSJF implements Escalonador {
    private int velocidadeSimulacao = 100;
    
    public EscalonadorSJF(int velocidade) {
        this.velocidadeSimulacao = velocidade;
    }
    
    @Override
    public String getNome() {
        return "SJF (Shortest Job First) - Não Preemptivo";
    }
    
    @Override
    public ResultadoSimulacao executar(List<Processo> processos) {
        long inicio = System.currentTimeMillis();
        
        List<Processo> processosClone = new ArrayList<>();
        for (Processo p : processos) {
            processosClone.add(p.clonar());
        }
        
        List<String> log = new ArrayList<>();
        List<Processo> filaEspera = new ArrayList<>();
        List<Processo> executados = new ArrayList<>();
        int tempoAtual = 0;
        
        log.add("=== Iniciando SJF ===");
        
        processosClone.sort(Comparator.comparingInt(Processo::getTempoChegada));
        
        while (!processosClone.isEmpty() || !filaEspera.isEmpty()) {
            Iterator<Processo> it = processosClone.iterator();
            while (it.hasNext()) {
                Processo p = it.next();
                if (p.getTempoChegada() <= tempoAtual) {
                    filaEspera.add(p);
                    it.remove();
                    log.add(String.format("Tempo %d: Processo %s chegou (duração: %d)", 
                        tempoAtual, p.getId(), p.getDuracaoCPU()));
                } else {
                    break;
                }
            }
            
            if (filaEspera.isEmpty()) {
                if (!processosClone.isEmpty()) {
                    int proxChegada = processosClone.get(0).getTempoChegada();
                    log.add(String.format("Tempo %d-%d: CPU Ociosa", 
                        tempoAtual, proxChegada));
                    tempoAtual = proxChegada;
                }
                continue;
            }
            
            filaEspera.sort(Comparator.comparingInt(Processo::getDuracaoCPU));
            Processo atual = filaEspera.remove(0);
            
            log.add(String.format("Tempo %d: Executando processo %s (duração: %d)", 
                tempoAtual, atual.getId(), atual.getDuracaoCPU()));
            
            atual.executar(atual.getDuracaoCPU(), tempoAtual);
            
            try {
                Thread.sleep(atual.getDuracaoCPU() * velocidadeSimulacao);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            tempoAtual += atual.getDuracaoCPU();
            executados.add(atual);
            
            log.add(String.format("Tempo %d: Finalizando processo %s", 
                tempoAtual, atual.getId()));
        }
        
        log.add("=== SJF Finalizado ===");
        
        long fim = System.currentTimeMillis();
        return new ResultadoSimulacao(executados, log, fim - inicio);
    }
}