import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;


public class TelaDeRemocao extends JFrame{
    public static JLabel lblId;
    public static JComboBox<String> cbxId;

    public static JLabel lblNome;
    public static JTextField txtNome;

    public static JLabel lblEmail;
    public static JTextField txtEmail;

    public static JButton btnCancelar;
    public static JButton btnRemover;

    public static JLabel lblNotificacoes;

    public static GridBagLayout gbLayout;
    public static GridBagConstraints gBagConstraints;

    public TelaDeRemocao(){
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
        txtNome.setEditable(false);
        addComponent(txtNome,1,1,1,1);

        lblEmail = new JLabel("Email: ");
        addComponent(lblEmail, 2,0,1,1);
        txtEmail = new JTextField(10);
        txtEmail.setEditable(false);
        addComponent(txtEmail,2,1,1,1);

        atualizarCampos(String.valueOf(cbxId.getSelectedItem()));

        btnRemover = new JButton("Remover");
        addComponent(btnRemover, 4,0,1,1);
        
        btnCancelar = new JButton("Cancelar");
        addComponent(btnCancelar, 4,1,1,1);

        lblNotificacoes = new JLabel("Notificações",SwingConstants.CENTER);
        addComponent(lblNotificacoes, 5,0,2,1);


        cbxId.addItemListener(
                new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent event) {
                        if (event.getStateChange() == ItemEvent.SELECTED){
                            atualizarCampos(String.valueOf(cbxId.getSelectedItem()));
                        }
                    }
                }
        );


        btnRemover.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        try {
                            Connection conexao = MySQLConnector.conectar();
                            String strSqlDeletarId = "delete from `db_senac`.`tbl_senac` where `id` = " + String.valueOf(cbxId.getSelectedItem()) + ";";
                            Statement stmSqlDeletarId = conexao.createStatement();
                            stmSqlDeletarId.addBatch(strSqlDeletarId);
                            stmSqlDeletarId.executeBatch();
                            notificaUsuario("O id " + String.valueOf(cbxId.getSelectedItem()) + " foi atualizado com sucesso.");
                        } catch (Exception e) {
                            notificaUsuario("Ops! Problema no servidor, tente novamente mais tarde.");
                            System.err.println("Erro: " + e);
                        }
                        try{
                            cbxId.setSelectedIndex(cbxId.getSelectedIndex() + 1);
                            cbxId.removeItemAt(cbxId.getSelectedIndex() -1);
                        } catch (Exception e) {
                            cbxId.setSelectedIndex(cbxId.getSelectedIndex() - 1);
                            cbxId.removeItemAt(cbxId.getSelectedIndex() + 1);
                        }
                        atualizarCampos(String.valueOf(cbxId.getSelectedItem()));
                    }
                }
        );

        btnCancelar.addActionListener(
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent event) {
                    if(JOptionPane.showConfirmDialog (null, "Deseja cancelar e sair da Tela de Remoção?") == 0) {
                    System.exit(1);
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
                txtEmail.setText(rstSqlAtualizarCampos.getString("email"));
            }else{
                notificaUsuario("Id não encontrado");
            }
        } catch (Exception e) {
            notificaUsuario("Ops! Erro no servidor. Tente novamente mais tarde.");
            System.err.println("Erro: " + e);
        }


    }

    public static void verificarAlturaELargura(){
        appTelaDeRemocao.getRootPane().addComponentListener(
            new ComponentAdapter() {
                public void componentResized(ComponentEvent e){
                    int larguraTela = appTelaDeRemocao.getWidth();
                    int alturaTela = appTelaDeRemocao.getHeight();
                    notificaUsuario(String.format("Largura %s, Altura: %s", larguraTela, alturaTela));
                }
            }
        );
    }


    public static TelaDeRemocao appTelaDeRemocao;

    public static void main (String[] args) {
        appTelaDeRemocao = new TelaDeRemocao();
        appTelaDeRemocao.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
