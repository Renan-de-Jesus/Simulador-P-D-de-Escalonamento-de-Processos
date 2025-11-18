package modelo.escalonadores;

import modelo.Processo;
import java.util.List;

public interface Escalonador {
    /**
     * Executa o algoritmo de escalonamento
     * @param processos Lista de processos a escalonar
     * @return ResultadoSimulacao com métricas e log da execução
     */
    ResultadoSimulacao executar(List<Processo> processos);
    
    /**
     * Retorna o nome descritivo do algoritmo
     * @return Nome do algoritmo (ex: "FCFS", "Round Robin")
     */
    String getNome();
}