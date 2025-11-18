package rede;

import modelo.Processo;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ServidorSimulacao {
    private static final int PORTA = 5000;
    private List<Processo> processosRecebidos;
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private volatile boolean executando;
    
    public ServidorSimulacao() {
        this.processosRecebidos = Collections.synchronizedList(new ArrayList<>());
        this.threadPool = Executors.newCachedThreadPool();
        this.executando = false;
    }
    
    public void iniciar() {
        try {
            serverSocket = new ServerSocket(PORTA);
            executando = true;
            System.out.println("Servidor iniciado na porta " + PORTA);
            System.out.println("Aguardando conexões...\n");
            
            Thread threadAceitarConexoes = new Thread(() -> {
                while (executando) {
                    try {
                        Socket clienteSocket = serverSocket.accept();
                        threadPool.execute(new ManipuladorCliente(clienteSocket));
                    } catch (IOException e) {
                        if (executando) {
                            System.err.println("Erro ao aceitar conexão: " + e.getMessage());
                        }
                    }
                }
            });
            
            threadAceitarConexoes.start();
            
        } catch (IOException e) {
            System.err.println("Erro ao iniciar servidor: " + e.getMessage());
        }
    }
    
    public void parar() {
        executando = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            threadPool.shutdown();
            threadPool.awaitTermination(5, TimeUnit.SECONDS);
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao parar servidor: " + e.getMessage());
        }
    }
    
    public List<Processo> getProcessosRecebidos() {
        synchronized (processosRecebidos) {
            return new ArrayList<>(processosRecebidos);
        }
    }
    
    public void limparProcessos() {
        synchronized (processosRecebidos) {
            processosRecebidos.clear();
        }
    }
    
    public int getQuantidadeProcessos() {
        return processosRecebidos.size();
    }
    
    private class ManipuladorCliente implements Runnable {
        private Socket socket;
        
        public ManipuladorCliente(Socket socket) {
            this.socket = socket;
        }
        
        @Override
        public void run() {
            try (
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                String enderecoCliente = socket.getInetAddress().getHostAddress();
                System.out.println("Cliente conectado: " + enderecoCliente);
                
                String linha;
                int contador = 0;
                
                while ((linha = in.readLine()) != null) {
                    if (linha.equals("FIM")) {
                        break;
                    }
                    
                    try {
                        Processo processo = Processo.fromString(linha);
                        processosRecebidos.add(processo);
                        contador++;
                        out.println("OK");
                        System.out.println("Processo recebido de " + enderecoCliente + 
                                         ": " + processo);
                    } catch (Exception e) {
                        out.println("ERRO: " + e.getMessage());
                        System.err.println("Erro ao processar dados: " + linha);
                    }
                }
                
                System.out.println("Cliente " + enderecoCliente + 
                                 " desconectado. Processos recebidos: " + contador);
                
            } catch (IOException e) {
                System.err.println("Erro na comunicação com cliente: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.err.println("Erro ao fechar socket do cliente: " + e.getMessage());
                }
            }
        }
    }
}