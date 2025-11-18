package modelo.escalonadores;

import modelo.Processo;
import java.util.List;

/**
 * Classe que armazena os resultados de uma simulação de escalonamento
 * Contém os processos executados, logs e métricas calculadas
 */
public class ResultadoSimulacao {
    private List<Processo> processos;
    private List<String> logExecucao;
    private double tempoMedioEspera;
    private double tempoMedioRetorno;
    private double tempoMedioResposta;
    private long tempoRealExecucao;
    
    /**
     * Construtor que calcula automaticamente as métricas
     * @param processos Lista de processos executados
     * @param log Log detalhado da execução
     * @param tempoReal Tempo real de execução em ms
     */
    public ResultadoSimulacao(List<Processo> processos, List<String> log, long tempoReal) {
        this.processos = processos;
        this.logExecucao = log;
        this.tempoRealExecucao = tempoReal;
        calcularMedias();
    }

    private void calcularMedias() {
        if (processos == null || processos.isEmpty()) {
            tempoMedioEspera = 0;
            tempoMedioRetorno = 0;
            tempoMedioResposta = 0;
            return;
        }
        
        int somaEspera = 0;
        int somaRetorno = 0;
        int somaResposta = 0;
        
        for (Processo p : processos) {
            somaEspera += p.getTempoEspera();
            somaRetorno += p.getTempoRetorno();
            somaResposta += p.getTempoResposta();
        }
        
        int n = processos.size();
        tempoMedioEspera = (double) somaEspera / n;
        tempoMedioRetorno = (double) somaRetorno / n;
        tempoMedioResposta = (double) somaResposta / n;
    }
    
    public List<Processo> getProcessos() {
        return processos;
    }
    
    public List<String> getLogExecucao() {
        return logExecucao;
    }
    
    public double getTempoMedioEspera() {
        return tempoMedioEspera;
    }
    
    public double getTempoMedioRetorno() {
        return tempoMedioRetorno;
    }
    
    public double getTempoMedioResposta() {
        return tempoMedioResposta;
    }
    
    public long getTempoRealExecucao() {
        return tempoRealExecucao;
    }
    
    public String getResumo() {
        return String.format(
            "╔════════════════════════════════════════════════════╗\n" +
            "║           RESUMO DA SIMULAÇÃO                      ║\n" +
            "╠════════════════════════════════════════════════════╣\n" +
            "║ Processos executados: %-28d ║\n" +
            "║ Tempo Médio de Espera: %-23.2f ║\n" +
            "║ Tempo Médio de Retorno: %-22.2f ║\n" +
            "║ Tempo Médio de Resposta: %-21.2f ║\n" +
            "║ Tempo Real de Execução: %-19d ms ║\n" +
            "╚════════════════════════════════════════════════════╝",
            processos.size(),
            tempoMedioEspera,
            tempoMedioRetorno,
            tempoMedioResposta,
            tempoRealExecucao
        );
    }
    
    public String getResumoSimples() {
        return String.format(
            "Processos executados: %d\n" +
            "Tempo Médio de Espera: %.2f\n" +
            "Tempo Médio de Retorno: %.2f\n" +
            "Tempo Médio de Resposta: %.2f\n" +
            "Tempo Real de Execução: %d ms",
            processos.size(),
            tempoMedioEspera,
            tempoMedioRetorno,
            tempoMedioResposta,
            tempoRealExecucao
        );
    }
    
    public void exibirLog() {
        System.out.println("\n=== LOG DE EXECUÇÃO ===");
        for (String linha : logExecucao) {
            System.out.println(linha);
        }
    }
    
    public void exibirMetricasProcessos() {
        System.out.println("\n=== MÉTRICAS POR PROCESSO ===");
        System.out.printf("%-10s | %8s | %8s | %8s | %8s | %8s\n",
            "Processo", "Chegada", "Duração", "Espera", "Retorno", "Resposta");
        System.out.println("-".repeat(70));
        
        for (Processo p : processos) {
            System.out.printf("%-10s | %8d | %8d | %8d | %8d | %8d\n",
                p.getId(),
                p.getTempoChegada(),
                p.getDuracaoCPU(),
                p.getTempoEspera(),
                p.getTempoRetorno(),
                p.getTempoResposta()
            );
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== RESULTADO DA SIMULAÇÃO ===\n");
        sb.append(getResumoSimples());
        sb.append("\n\n=== PROCESSOS ===\n");
        for (Processo p : processos) {
            sb.append(p.toStringCompleto()).append("\n");
        }
        return sb.toString();
    }
    
    public int compararTempoEspera(ResultadoSimulacao outro) {
        return Double.compare(this.tempoMedioEspera, outro.tempoMedioEspera);
    }

    public int compararTempoRetorno(ResultadoSimulacao outro) {
        return Double.compare(this.tempoMedioRetorno, outro.tempoMedioRetorno);
    }

    public int compararTempoResposta(ResultadoSimulacao outro) {
        return Double.compare(this.tempoMedioResposta, outro.tempoMedioResposta);
    }
}