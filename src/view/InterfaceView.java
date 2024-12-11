package view;
import controller.*;

import java.awt.*;
import java.awt.event.*;

import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class InterfaceView extends JFrame {
    public static final int IMG_WIDTH = 100;
    public static final int IMG_HEIGHT = 100;

    public static String idLoginAtual = "";

    public static final String localViewImgFolder = System.getProperty("user.dir") 
        + "\\" 
        + "src" 
        + "\\" 
        + "view" 
        + "\\" 
        + "img";

    public static final String localViewFolder = System.getProperty("user.dir") 
        + "\\" 
        + "src" 
        + "\\" 
        + "view";

    public static void addComponent(Component component, int row, int column, int width, int height, GridBagLayout gbLayout, GridBagConstraints gbConstraints, JFrame frame) {
        if (height > 1 && width > 1) {
            gbConstraints.fill = GridBagConstraints.BOTH;
        } else if (height > 1) {
            gbConstraints.fill = GridBagConstraints.VERTICAL;
        } else {
            gbConstraints.fill = GridBagConstraints.HORIZONTAL;
        }
        gbConstraints.gridy = row;
        gbConstraints.gridx = column;
        gbConstraints.gridwidth = width;
        gbConstraints.gridheight = height;
        gbLayout.setConstraints(component, gbConstraints);
        frame.add(component);
    }

    public static void verificarLarguraEAltura(JFrame frame, JLabel label) { // checkFrameWidthHeight()
        frame.getRootPane().addComponentListener(
            new ComponentAdapter() {
                public void componentResized(ComponentEvent e) {
                    int larguraTela = frame.getWidth();
                    int alturaTela = frame.getHeight();
                    notificarUsuario(String.format("Largura: %s, Altura: %s", larguraTela, alturaTela), label);
                }
            }
        );
    }

    public static void notificarUsuario(String str, JLabel label) {
        label.setText(setHtmlFormat(str));
    }

    public static String setHtmlFormat(String str) {
        return "<html><body>" + str + "</body></html>";
    }

    public static void removerImagensInuteis() {
        InterfaceController.verificarApagarImagensInuteis();
    }

    public static void definirIcone(JFrame frame) {
        try {
            InputStream imageInputStream = frame.getClass().getResourceAsStream("senac-logo.png");
            BufferedImage bufferedImage = ImageIO.read(imageInputStream);
            frame.setIconImage(bufferedImage);
        } catch (Exception e) {
            System.err.println("Erro: " + e);
        }
    }

    public static void setFrameCenterPosition(JFrame frame) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(
            (dim.width / 2) - (frame.getSize().width / 2), 
            (dim.height / 2) - (frame.getSize().height / 2)
        );
    }
}
