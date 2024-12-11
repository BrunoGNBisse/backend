package view;

import controller.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

public class TelaDeHistoricoView extends JFrame {
    public static JList<String> lstHistorico;
    public static JButton btnEnviarHistorico;

    public static GridBagLayout gbLayout;
    public static GridBagConstraints gbConstraints;

    public TelaDeHistoricoView() {
        super("Tela de Histórico");

        gbLayout = new GridBagLayout();
        setLayout(gbLayout);

        gbConstraints = new GridBagConstraints();

        lstHistorico = new JList<String>(TelaDeHistoricoController.preencherHistorico());
        lstHistorico.setVisibleRowCount(5);
        lstHistorico.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        InterfaceView.addComponent(lstHistorico, 0, 0, 1, 1, gbLayout, gbConstraints, this);

        btnEnviarHistorico = new JButton("Enviar Histórico");
        btnEnviarHistorico.setEnabled(false);
        InterfaceView.addComponent(btnEnviarHistorico, 1, 0, 1, 1, gbLayout, gbConstraints, this);

        lstHistorico.addListSelectionListener(
            new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent event) {
                    btnEnviarHistorico.setEnabled(true);
                }
            }
        );

        btnEnviarHistorico.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    TelaDePesquisaView.txtPesquisa.setText(lstHistorico.getSelectedValue());
                    TelaDePesquisaView.appTelaDePesquisaView.setVisible(true);
                    dispose();
                }
            }
        );

        InterfaceView.definirIcone(this);
        setSize(200, 200);
        InterfaceView.setFrameCenterPosition(this);
        setVisible(true);
    }

    public static TelaDeHistoricoView appTelaDeHistoricoView;
    public static void main(String[] args) {
        // InterfaceView.idLoginAtual = "16";
        if (InterfaceView.idLoginAtual.equals("")) {
            TelaDeLoginView.appTelaDeLoginView = new TelaDeLoginView();
            TelaDeLoginView.appTelaDeLoginView.setDefaultCloseOperation(EXIT_ON_CLOSE);
        } else {
            appTelaDeHistoricoView = new TelaDeHistoricoView();
            appTelaDeHistoricoView.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            appTelaDeHistoricoView.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        TelaDePesquisaView.appTelaDePesquisaView.setVisible(true);
                        appTelaDeHistoricoView.dispose();
                    }
                }
            );
        }
    }
}
