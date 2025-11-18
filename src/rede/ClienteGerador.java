package rede;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClienteGerador {
    private static final String HOST = "localhost";
    private static final int PORTA = 5000;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Cliente Gerador de Processos ===\n");
        
        while (true) {
            System.out.println("\n1 - Enviar processos manualmente");
            System.out.println("2 - Enviar processos de arquivo");
            System.out.println("3 - Gerar processos aleatórios");
            System.out.println("0 - Sair");
            System.out.print("\nEscolha: ");
            
            int opcao = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcao) {
                case 1:
                    enviarProcessosManual(scanner);
                    break;
                case 2:
                    enviarProcessosArquivo(scanner);
                    break;
                case 3:
                    gerarProcessosAleatorios(scanner);
                    break;
                case 0:
                    System.out.println("Encerrando cliente...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
    
    private static void enviarProcessosManual(Scanner scanner) {
        List<String> processos = new ArrayList<>();
        
        System.out.println("\nDigite os processos no formato:");
        System.out.println("ID;tempoChegada;duracao;prioridade");
        System.out.println("Exemplo: P1;0;5;1");
        System.out.println("Digite 'FIM' quando terminar\n");
        
        while (true) {
            System.out.print("Processo: ");
            String linha = scanner.nextLine();
            
            if (linha.equalsIgnoreCase("FIM")) {
                break;
            }
            
            processos.add(linha);
        }
        
        if (!processos.isEmpty()) {
            enviarParaServidor(processos);
        } else {
            System.out.println("Nenhum processo para enviar.");
        }
    }
    
    private static void enviarProcessosArquivo(Scanner scanner) {
        System.out.print("\nNome do arquivo: ");
        String nomeArquivo = scanner.nextLine();
        
        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
            List<String> processos = new ArrayList<>();
            String linha;
            
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (!linha.isEmpty() && !linha.startsWith("#")) {
                    processos.add(linha);
                }
            }
            
            System.out.println("Lidos " + processos.size() + " processos do arquivo.");
            enviarParaServidor(processos);
            
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo: " + e.getMessage());
        }
    }
    
    private static void gerarProcessosAleatorios(Scanner scanner) {
        System.out.print("\nQuantos processos gerar? ");
        int quantidade = scanner.nextInt();
        scanner.nextLine();
        
        List<String> processos = new ArrayList<>();
        Random random = new Random();
        
        for (int i = 1; i <= quantidade; i++) {
            String id = "P" + i;
            int tempoChegada = random.nextInt(10);
            int duracao = random.nextInt(10) + 1;
            int prioridade = random.nextInt(5) + 1;
            
            String processo = String.format("%s;%d;%d;%d", 
                id, tempoChegada, duracao, prioridade);
            processos.add(processo);
            System.out.println("Gerado: " + processo);
        }
        
        System.out.println("\nEnviar esses processos? (S/N): ");
        String resposta = scanner.nextLine();
        
        if (resposta.equalsIgnoreCase("S")) {
            enviarParaServidor(processos);
        }
    }
    
    private static void enviarParaServidor(List<String> processos) {
        try (
            Socket socket = new Socket(HOST, PORTA);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("\nConectado ao servidor " + HOST + ":" + PORTA);
            
            int enviados = 0;
            for (String processo : processos) {
                out.println(processo);
                String resposta = in.readLine();
                
                if (resposta.equals("OK")) {
                    enviados++;
                    System.out.println("Enviado: " + processo);
                } else {
                    System.err.println("Erro ao enviar: " + processo + 
                                     " - Resposta: " + resposta);
                }
            }
            
            out.println("FIM");
            System.out.println("\n" + enviados + " processos enviados com sucesso!");
            
        } catch (IOException e) {
            System.err.println("Erro na conexão: " + e.getMessage());
            System.err.println("Verifique se o servidor está rodando.");
        }
    }
}