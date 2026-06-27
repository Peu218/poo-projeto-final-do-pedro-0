package model;


public abstract class Conteudo {

    protected String nome;
    protected String descricao;

   
     * @param nome      nome do conteúdo (não pode ser vazio)
     * @param descricao descrição/detalhamento do conteúdo
    
    public Conteudo(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    
    public abstract String getResumo();
}

