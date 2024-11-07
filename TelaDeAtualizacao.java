import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class TelaDeAtualizacao extends JFrame{
    public static JLabel lblId;
    public static JComboBox<String> cbxId;

    public static JLabel lblNome;
    public static JTextField txtNome;
    public static String txtNomeCarregado;
    
    public static JLabel lblEmail;
    public static JTextField txtEmail;
    public static String txtEmailCarregado;

    public static JLabel lblSenha;
    public static JPasswordField txtSenha;

    public static JButton btnAtualizar;
    public static JButton btnCancelar;

    public static JLabel lblNotificacoes;

    public static GridBagLayout gbLayout;
    public static GridBagConstraints gBagConstraints;

    public TelaDeAtualizacao(){
        super("Tela de Atualização");

        gbLayout = new GridBagLayout();
        setLayout(gbLayout);
        gBagConstraints = new GridBagConstraints();

        lblId = new JLabel("Id:", SwingConstants.RIGHT);
        addComponent(lblId,0,0,1,1);


        cbxId = new JComboBox<String>();
        popularCbxId();
        addComponent(cbxId, 0,1,1,1);

        lblNome = new JLabel("Nome: ");
        addComponent(lblNome, 1,0,1,1);
        txtNome = new JTextField(10);
        addComponent(txtNome,1,1,1,1);

        lblEmail = new JLabel("Email: ");
        addComponent(lblEmail, 2,0,1,1);
        txtEmail = new JTextField(10);
        addComponent(txtEmail,2,1,1,1);

        lblSenha = new JLabel("Senha: ");
        addComponent(lblSenha, 3,0,1,1);
        txtSenha = new JPasswordField(10);
        addComponent(txtSenha,3,1,1,1);

        btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setEnabled(false);
        addComponent(btnAtualizar, 4,0,1,1);
        btnCancelar = new JButton("Cancelar");
        addComponent(btnCancelar, 4,1,1,1);

        lblNotificacoes = new JLabel("Notificações",SwingConstants.CENTER);
        addComponent(lblNotificacoes, 5,0,2,1);


        cbxId.addItemListener(
                new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent event) {
                        // if (!txtNome.getText().trim().equals(txtNomeCarregado.trim()) &&
                        //         JOptionPane.showConfirmDialog(null, "Coé parsa, vai salvar não?") == JOptionPane.CANCEL_OPTION){
                        //     return;
                        // }
                        if (event.getStateChange() == ItemEvent.SELECTED){
                            atualizarCampos(String.valueOf(cbxId.getSelectedItem()));
                        }
                    }
                }
        );

//        cbxId.addActionListener(
//                new ActionListener() {
//                    @Override
//                    public void actionPerformed(ActionEvent event) {
//                        if (!txtNome.getText().trim().equals(txtNomeCarregado.trim()) &&
//                                JOptionPane.showConfirmDialog(null, "Coé parsa, vai salvar não?") == JOptionPane.CANCEL_OPTION) {
//                            return;
//                        }
//
//                    }
//                }
//        );


        btnAtualizar.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        try {
                            Connection conexao = MySQLConnector.conectar();
                            String atualizarSenha = "";
                            if (String.valueOf(txtSenha.getPassword()).trim().length() > 0) {
                                atualizarSenha = ", `senha` = '" + String.valueOf(txtSenha.getPassword()).trim() + "'";
                            }
                            String strSqlAtualizarId = "update `db_senac`.`tbl_senac` set `nome` = '" + txtNome.getText().trim() + "', `email` = '" + txtEmail.getText().trim() + "'" + atualizarSenha + " where `id` = " + String.valueOf(cbxId.getSelectedItem()) + ";";
                            Statement stmSqlAtualizarId = conexao.createStatement();
                            stmSqlAtualizarId.addBatch(strSqlAtualizarId);
                            stmSqlAtualizarId.executeBatch();
                            txtNomeCarregado = txtNome.getText().trim();
                            txtEmailCarregado = txtEmail.getText().trim();
                            btnAtualizar.setEnabled(false);
                            notificaUsuario("O id " + String.valueOf(cbxId.getSelectedItem()) + " foi atualizado com sucesso.");
                        } catch (Exception e) {
                            notificaUsuario("Ops! Problema no servidor, tente novamente mais tarde.");
                            System.err.println("Erro: " + e);
                        }
                    }
                }
        );





        txtNome.addKeyListener(
            new KeyAdapter() {
                @Override
                    public void keyReleased(KeyEvent event){
                    if (txtNomeCarregado.trim().equals(txtNome.getText().trim())){
                        btnAtualizar.setEnabled(false);
                    } else{
                        btnAtualizar.setEnabled(true);
                    }
                }
            }
        );

        txtEmail.addKeyListener(
                new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent event){
                        if (txtEmailCarregado.trim().equals(txtEmail.getText().trim())){
                            btnAtualizar.setEnabled(false);
                        } else {
                            btnAtualizar.setEnabled(true);
                        }
                    }
                }
        );

        txtSenha.addKeyListener(
                new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent event){
                        if (String.valueOf(txtSenha.getPassword()).trim().length() == 0){
                            btnAtualizar.setEnabled(false);
                        } else{
                            btnAtualizar.setEnabled(true);
                        }
                    }
                }
        );

        setSize(400,200);
        setVisible(true);

    }

    public void addComponent(Component component, int row, int column, int width, int height){
        if (height > 1 && width > 1){
            gBagConstraints.fill = GridBagConstraints.BOTH;
        } else if (height > 1){
            gBagConstraints.fill = GridBagConstraints.VERTICAL;
        } else{
            gBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        }
        gBagConstraints.gridx = column;
        gBagConstraints.gridy = row;
        gBagConstraints.gridwidth = width;
        gBagConstraints.gridheight = height;
        gbLayout.setConstraints(component, gBagConstraints);
        add(component);
    }

    public static void popularCbxId(){
        try {
            Connection conexao = MySQLConnector.conectar();
            String strSqlPopularCbxId = "select * from `db_senac`.`tbl_senac` order by `id` asc;";
            Statement stmSqlPopularCbxId = conexao.createStatement();
            ResultSet rstSqlPopularCbxId = stmSqlPopularCbxId.executeQuery(strSqlPopularCbxId);
            while (rstSqlPopularCbxId.next()){
                cbxId.addItem(rstSqlPopularCbxId.getString("Id"));
            }
            stmSqlPopularCbxId.close();
        } catch (Exception e) {
            lblNotificacoes.setText("Ops! ocorreu um problema no servidor e não será possível carregar os ids neste momento. Por Favor, retorne novamente mais tarde.");
            System.err.println("Erro: " + e);
        }
    }

    public static void  notificaUsuario (String str){
        lblNotificacoes.setText(setHtmlFotmat(str));
    }

    public static String setHtmlFotmat (String str){
        return "<html><body>" + str + "</body></html>";
    }

    public static void atualizarCampos(String strId){
        try{
            Connection conexao = MySQLConnector.conectar();
            String strSqlAtualizarCampos = "select * from `db_senac`.`tbl_senac` where id = " + strId + ";";
            Statement stmSqlAtualizarCampos = conexao.createStatement();
            ResultSet rstSqlAtualizarCampos = stmSqlAtualizarCampos.executeQuery(strSqlAtualizarCampos);
            if(rstSqlAtualizarCampos.next()){
                txtNome.setText(rstSqlAtualizarCampos.getString("nome"));
                txtNomeCarregado = txtNome.getText();
                txtEmail.setText(rstSqlAtualizarCampos.getString("email"));
                txtEmailCarregado = txtNome.getText();
            }else{
                notificaUsuario("Id não encontrado");
            }
        } catch (Exception e) {
            notificaUsuario("Ops! Erro no servidor. Tente novamente mais tarde.");
            System.err.println("Erro: " + e);
        }


    }


    public static void main (String[] args) {
        TelaDeAtualizacao appTelaDeAtualizacao = new TelaDeAtualizacao();
        }
    }

