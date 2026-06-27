package view;

import model.Topico;
import repository.GerenciadorDados;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class MainFrame extends JFrame {

   
    private static final String STATUS_PENDENTE = "Pendente";
    private static final String STATUS_CONCLUIDO = "Concluído";

    private static final String[] COLUNAS = {
            "Matéria", "Tópico", "Descrição", "Tempo (min)", "Status"
    };

   
    private JTextField txtMateria;
    private JTextField txtTopico;
    private JTextField txtDescricao;
    private JTextField txtTempo;
    private JComboBox<String> comboStatus;

    
    private JTable tabela;
    private DefaultTableModel modeloTabela;

   
    private final GerenciadorDados gerenciadorDados;
    private List<Topico> listaTopicos;

   
    public MainFrame() {
        super("Sistema de Registro de Temas e Tópicos para Estudo de Programação");

        this.gerenciadorDados = new GerenciadorDados();
        this.listaTopicos = new ArrayList<>();

        inicializarComponentes();
        carregarDadosIniciais();
        configurarJanela();
    }

    

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));

        add(criarPainelFormulario(), BorderLayout.NORTH);
        add(criarPainelTabela(), BorderLayout.CENTER);
        add(criarPainelBotoes(), BorderLayout.SOUTH);
    }

    
    
    private JPanel criarPainelFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Cadastro de Tópico de Estudo"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMateria = new JTextField(15);
        txtTopico = new JTextField(15);
        txtDescricao = new JTextField(15);
        txtTempo = new JTextField(6);
        comboStatus = new JComboBox<>(new String[]{STATUS_PENDENTE, STATUS_CONCLUIDO});

       
        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new javax.swing.JLabel("Matéria:"), gbc);
        gbc.gridx = 1;
        painel.add(txtMateria, gbc);

        gbc.gridx = 2;
        painel.add(new javax.swing.JLabel("Tópico:"), gbc);
        gbc.gridx = 3;
        painel.add(txtTopico, gbc);

        
        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(new javax.swing.JLabel("Descrição:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        painel.add(txtDescricao, gbc);
        gbc.gridwidth = 1; // restaura o padrão

        
        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(new javax.swing.JLabel("Tempo de estudo (min):"), gbc);
        gbc.gridx = 1;
        painel.add(txtTempo, gbc);

        gbc.gridx = 2;
        painel.add(new javax.swing.JLabel("Status:"), gbc);
        gbc.gridx = 3;
        painel.add(comboStatus, gbc);

        return painel;
    }

    
    private JScrollPane criarPainelTabela() {
        modeloTabela = new DefaultTableModel(COLUNAS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
               
                return false;
            }
        };

        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setRowHeight(24);
        tabela.getTableHeader().setFont(tabela.getTableHeader().getFont().deriveFont(Font.BOLD));


        
        tabela.getSelectionModel().addListSelectionListener(evento -> {
            if (!evento.getValueIsAdjusting()) {
                preencherFormularioComSelecao();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Tópicos Cadastrados"));
        return scrollPane;
    }

    
    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout());

        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnListar = new JButton("Listar");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnRemover = new JButton("Remover");
        JButton btnLimpar = new JButton("Limpar Campos");

        btnAdicionar.addActionListener(this::acaoAdicionar);
        btnListar.addActionListener(this::acaoListar);
        btnAtualizar.addActionListener(this::acaoAtualizar);
        btnRemover.addActionListener(this::acaoRemover);
        btnLimpar.addActionListener(evento -> limparCampos());

        painel.add(btnAdicionar);
        painel.add(btnListar);
        painel.add(btnAtualizar);
        painel.add(btnRemover);
        painel.add(btnLimpar);

        return painel;
    }

    private void configurarJanela() {
        setSize(820, 560);
        setMinimumSize(new java.awt.Dimension(700, 450));
        setLocationRelativeTo(null); // centraliza na tela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    

    
    private void acaoAdicionar(ActionEvent evento) {
        if (!validarCampos()) {
            return;
        }

        try {
            Topico novoTopico = new Topico(
                    txtTopico.getText().trim(),
                    txtMateria.getText().trim(),
                    txtDescricao.getText().trim(),
                    Integer.parseInt(txtTempo.getText().trim()),
                    comboStatus.getSelectedItem().equals(STATUS_CONCLUIDO)
            );

            listaTopicos.add(novoTopico);
            gerenciadorDados.salvarTopicos(listaTopicos);
            atualizarTabela();
            limparCampos();

            JOptionPane.showMessageDialog(this,
                    "Tópico adicionado com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            exibirErro("Erro ao salvar os dados no arquivo: " + ex.getMessage());
        }
    }

    
    private void acaoListar(ActionEvent evento) {
        try {
            listaTopicos = gerenciadorDados.carregarTopicos();
            atualizarTabela();
        } catch (IOException ex) {
            exibirErro("Erro ao carregar os dados do arquivo: " + ex.getMessage());
        }
    }

    
    private void acaoAtualizar(ActionEvent evento) {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um tópico na tabela para atualizar.",
                    "Nenhum item selecionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validarCampos()) {
            return;
        }

        try {
            Topico topico = listaTopicos.get(linhaSelecionada);
            topico.setMateria(txtMateria.getText().trim());
            topico.setNome(txtTopico.getText().trim());
            topico.setDescricao(txtDescricao.getText().trim());
            topico.setTempoEstudoMinutos(Integer.parseInt(txtTempo.getText().trim()));
            topico.setConcluido(comboStatus.getSelectedItem().equals(STATUS_CONCLUIDO));

            gerenciadorDados.salvarTopicos(listaTopicos);
            atualizarTabela();
            limparCampos();

            JOptionPane.showMessageDialog(this,
                    "Tópico atualizado com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            exibirErro("Erro ao salvar os dados no arquivo: " + ex.getMessage());
        }
    }

    
    private void acaoRemover(ActionEvent evento) {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um tópico na tabela para remover.",
                    "Nenhum item selecionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja remover o tópico selecionado?",
                "Confirmar remoção", JOptionPane.YES_NO_OPTION);

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            listaTopicos.remove(linhaSelecionada);
            gerenciadorDados.salvarTopicos(listaTopicos);
            atualizarTabela();
            limparCampos();
        } catch (IOException ex) {
            exibirErro("Erro ao salvar os dados no arquivo: " + ex.getMessage());
        }
    }

   

    
    private boolean validarCampos() {
        StringBuilder erros = new StringBuilder();

        if (txtMateria.getText().trim().isEmpty()) {
            erros.append("- O campo \"Matéria\" não pode estar vazio.\n");
        }
        if (txtTopico.getText().trim().isEmpty()) {
            erros.append("- O campo \"Tópico\" não pode estar vazio.\n");
        }

        String tempoTexto = txtTempo.getText().trim();
        if (tempoTexto.isEmpty()) {
            erros.append("- O campo \"Tempo de estudo\" não pode estar vazio.\n");
        } else {
            try {
                int tempo = Integer.parseInt(tempoTexto);
                if (tempo < 0) {
                    erros.append("- O \"Tempo de estudo\" não pode ser negativo.\n");
                }
            } catch (NumberFormatException ex) {
                erros.append("- O \"Tempo de estudo\" deve ser um número inteiro válido.\n");
            }
        }

        if (erros.length() > 0) {
            JOptionPane.showMessageDialog(this, erros.toString(),
                    "Campos inválidos", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

   
    
    private void carregarDadosIniciais() {
        try {
            listaTopicos = gerenciadorDados.carregarTopicos();
            atualizarTabela();
        } catch (IOException ex) {
            listaTopicos = new ArrayList<>();
            exibirErro("Não foi possível carregar os dados salvos anteriormente.\n"
                    + "A aplicação será iniciada com a lista vazia.\nDetalhes: " + ex.getMessage());
        }
    }

   
    private void atualizarTabela() {
        modeloTabela.setRowCount(0); // limpa todas as linhas existentes

        for (Topico topico : listaTopicos) {
            modeloTabela.addRow(new Object[]{
                    topico.getMateria(),
                    topico.getNome(),
                    topico.getDescricao(),
                    topico.getTempoEstudoMinutos(),
                    topico.getStatusTexto()
            });
        }
    }

    
    private void preencherFormularioComSelecao() {
        int linha = tabela.getSelectedRow();
        if (linha < 0 || linha >= listaTopicos.size()) {
            return;
        }

        Topico topico = listaTopicos.get(linha);
        txtMateria.setText(topico.getMateria());
        txtTopico.setText(topico.getNome());
        txtDescricao.setText(topico.getDescricao());
        txtTempo.setText(String.valueOf(topico.getTempoEstudoMinutos()));
        comboStatus.setSelectedItem(topico.getStatusTexto());
    }

   
    private void limparCampos() {
        txtMateria.setText("");
        txtTopico.setText("");
        txtDescricao.setText("");
        txtTempo.setText("");
        comboStatus.setSelectedIndex(0);
        tabela.clearSelection();
    }

    private void exibirErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
