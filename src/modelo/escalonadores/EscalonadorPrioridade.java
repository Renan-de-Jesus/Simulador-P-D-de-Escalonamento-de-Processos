package modelo.escalonadores;

import modelo.Processo;
import java.util.*;

public class EscalonadorPrioridade implements Escalonador {
    private int velocidadeSimulacao = 100;
    
    public EscalonadorPrioridade(int velocidade) {
        this.velocidadeSimulacao = velocidade;
    }
    
    @Override
    public String getNome() {
        return "Prioridade Preemptiva (menor número = maior prioridade)";
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
        
        log.add("=== Iniciando Prioridade Preemptiva ===");
        
        processosClone.sort(Comparator.comparingInt(Processo::getTempoChegada));
        
        while (!processosClone.isEmpty() || !filaEspera.isEmpty()) {
            Iterator<Processo> it = processosClone.iterator();
            while (it.hasNext()) {
                Processo p = it.next();
                if (p.getTempoChegada() <= tempoAtual) {
                    filaEspera.add(p);
                    it.remove();
                    log.add(String.format("Tempo %d: Processo %s chegou (prioridade: %d)", 
                        tempoAtual, p.getId(), p.getPrioridade()));
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
            
            filaEspera.sort(Comparator.comparingInt(Processo::getPrioridade));
            Processo atual = filaEspera.get(0);
            
            int tempoExec = 1;
            
            log.add(String.format("Tempo %d: Executando processo %s (prioridade: %d, restante: %d)", 
                tempoAtual, atual.getId(), atual.getPrioridade(), atual.getTempoRestante()));
            
            atual.executar(tempoExec, tempoAtual);
            
            try {
                Thread.sleep(tempoExec * velocidadeSimulacao);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            tempoAtual += tempoExec;
            
            if (atual.isCompleto()) {
                filaEspera.remove(0);
                executados.add(atual);
                log.add(String.format("Tempo %d: Processo %s finalizado", 
                    tempoAtual, atual.getId()));
            }
        }
        
        log.add("=== Prioridade Preemptiva Finalizado ===");
        
        long fim = System.currentTimeMillis();
        return new ResultadoSimulacao(executados, log, fim - inicio);
    }
}