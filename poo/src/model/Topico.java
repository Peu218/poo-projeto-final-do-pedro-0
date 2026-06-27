package model;


public class Topico extends Conteudo {

    
    private static final String SEPARADOR_CSV = ";";

    private String materia;
    private int tempoEstudoMinutos;
    private boolean concluido;

    
     * @param nome               nome do tópico (ex: "Herança")
     * @param materia            matéria à qual o tópico pertence (ex: "Java")
     * @param descricao          descrição livre sobre o tópico
     * @param tempoEstudoMinutos tempo de estudo dedicado, em minutos
     * @param concluido          true se o tópico já foi estudado/concluído
    
    public Topico(String nome, String materia, String descricao,
                   int tempoEstudoMinutos, boolean concluido) {
       
        super(nome, descricao);
        this.materia = materia;
        this.tempoEstudoMinutos = tempoEstudoMinutos;
        this.concluido = concluido;
    }

    
    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public int getTempoEstudoMinutos() {
        return tempoEstudoMinutos;
    }

    public void setTempoEstudoMinutos(int tempoEstudoMinutos) {
        this.tempoEstudoMinutos = tempoEstudoMinutos;
    }

    public boolean isConcluido() {
        return concluido;
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }

   
    public String getStatusTexto() {
        return concluido ? "Concluído" : "Pendente";
    }

    
    @Override
    public String getResumo() {
        return nome + " [" + materia + "] - " + getStatusTexto()
                + " (" + tempoEstudoMinutos + " min)";
    }

    
     * @return linha formatada em CSV
     
    public String toCSV() {
        return sanitize(materia) + SEPARADOR_CSV
                + sanitize(nome) + SEPARADOR_CSV
                + sanitize(descricao) + SEPARADOR_CSV
                + tempoEstudoMinutos + SEPARADOR_CSV
                + concluido;
    }

    
     * gerado por {@link #toCSV()}.
     
     * @param linha linha lida do arquivo CSV
     * @return objeto Topico correspondente, ou null se a linha for inválida
     
    public static Topico fromCSV(String linha) {
        if (linha == null || linha.trim().isEmpty()) {
            return null;
        }

       
        String[] campos = linha.split(SEPARADOR_CSV, -1);

        if (campos.length < 5) {
           
            return null;
        }

        String materia = campos[0];
        String nome = campos[1];
        String descricao = campos[2];
        int tempo;
        try {
            tempo = Integer.parseInt(campos[3].trim());
        } catch (NumberFormatException e) {
            tempo = 0;
        }
        boolean concluido = Boolean.parseBoolean(campos[4].trim());

        return new Topico(nome, materia, descricao, tempo, concluido);
    }

    
    private static String sanitize(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace(SEPARADOR_CSV, ",")
                .replace("\n", " ")
                .replace("\r", " ")
                .trim();
    }

    @Override
    public String toString() {
        return getResumo();
    }
}
