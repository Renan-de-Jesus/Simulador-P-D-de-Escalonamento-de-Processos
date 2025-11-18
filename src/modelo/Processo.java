package modelo;

public class Processo {
    private String id;
    private int tempoChegada;
    private int duracaoCPU;
    private int prioridade;
    private int tempoRestante;
    private int tempoEspera;
    private int tempoRetorno;
    private int tempoResposta;
    private int tempoInicioExecucao;
    private int tempoFimExecucao;
    private boolean primeiraExecucao;
    
    public Processo(String id, int tempoChegada, int duracaoCPU, int prioridade) {
        this.id = id;
        this.tempoChegada = tempoChegada;
        this.duracaoCPU = duracaoCPU;
        this.prioridade = prioridade;
        this.tempoRestante = duracaoCPU;
        this.primeiraExecucao = true;
        this.tempoInicioExecucao = -1;
        this.tempoFimExecucao = -1;
    }

    public static Processo fromString(String dados) {
        String[] partes = dados.split(";");
        return new Processo(
            partes[0].trim(),
            Integer.parseInt(partes[1].trim()),
            Integer.parseInt(partes[2].trim()),
            Integer.parseInt(partes[3].trim())
        );
    }
    
    public void executar(int tempo, int tempoAtual) {
        if (primeiraExecucao) {
            tempoInicioExecucao = tempoAtual;
            tempoResposta = tempoAtual - tempoChegada;
            primeiraExecucao = false;
        }
        tempoRestante -= tempo;
        if (tempoRestante <= 0) {
            tempoFimExecucao = tempoAtual + tempo + tempoRestante;
            tempoRetorno = tempoFimExecucao - tempoChegada;
            tempoEspera = tempoRetorno - duracaoCPU;
        }
    }
    
    public boolean isCompleto() {
        return tempoRestante <= 0;
    }
    
    public Processo clonar() {
        Processo clone = new Processo(id, tempoChegada, duracaoCPU, prioridade);
        return clone;
    }
    
    public String getId() { return id; }
    public int getTempoChegada() { return tempoChegada; }
    public int getDuracaoCPU() { return duracaoCPU; }
    public int getPrioridade() { return prioridade; }
    public int getTempoRestante() { return tempoRestante; }
    public int getTempoEspera() { return tempoEspera; }
    public int getTempoRetorno() { return tempoRetorno; }
    public int getTempoResposta() { return tempoResposta; }
    public int getTempoInicioExecucao() { return tempoInicioExecucao; }
    public int getTempoFimExecucao() { return tempoFimExecucao; }
    
    public void setTempoRestante(int tempo) { this.tempoRestante = tempo; }
    public void setTempoEspera(int tempo) { this.tempoEspera = tempo; }
    public void setTempoRetorno(int tempo) { this.tempoRetorno = tempo; }
    
    @Override
    public String toString() {
        return String.format("Processo[id=%s, chegada=%d, duracao=%d, prioridade=%d]", 
                           id, tempoChegada, duracaoCPU, prioridade);
    }
    
    public String toStringCompleto() {
        return String.format(
            "Processo %s: Chegada=%d, Duração=%d, Prioridade=%d, " +
            "Espera=%d, Retorno=%d, Resposta=%d",
            id, tempoChegada, duracaoCPU, prioridade,
            tempoEspera, tempoRetorno, tempoResposta
        );
    }
}