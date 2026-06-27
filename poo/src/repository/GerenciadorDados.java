package repository;

import model.Topico;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class GerenciadorDados {

    
    private static final String DIRETORIO_DADOS = "dados";
    private static final String NOME_ARQUIVO = "topicos.csv";
    private static final String CAMINHO_ARQUIVO =
            DIRETORIO_DADOS + File.separator + NOME_ARQUIVO;

    
    private static final String CABECALHO =
            "materia;nome;descricao;tempoEstudoMinutos;concluido";

    
     * @param topicos lista de tópicos a ser persistida
     * @throws IOException caso ocorra um erro de escrita em disco
   
    public void salvarTopicos(List<Topico> topicos) throws IOException {
        // Garante que o diretório "dados" exista antes de tentar escrever.
        Files.createDirectories(Paths.get(DIRETORIO_DADOS));

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(CAMINHO_ARQUIVO, false))) { // false = sobrescreve
            writer.write(CABECALHO);
            writer.newLine();

            for (Topico topico : topicos) {
                writer.write(topico.toCSV());
                writer.newLine();
            }
        }
    }

    
     * @return lista de tópicos carregados do arquivo
     * @throws IOException caso ocorra um erro de leitura em disco
    
    public List<Topico> carregarTopicos() throws IOException {
        List<Topico> topicos = new ArrayList<>();

        Path caminho = Paths.get(CAMINHO_ARQUIVO);
        if (!Files.exists(caminho)) {
           
            return topicos;
        }

        try (BufferedReader reader = new BufferedReader(
                new FileReader(CAMINHO_ARQUIVO))) {
            String linha = reader.readLine(); 
            while ((linha = reader.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    Topico topico = Topico.fromCSV(linha);
                    if (topico != null) {
                        topicos.add(topico);
                    }
                }
            }
        }

        return topicos;
    }
}
