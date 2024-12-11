package controller;
import view.*;
import model.*;

import static java.nio.file.StandardCopyOption.*;
import java.nio.file.*;

import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

// import javax.imageio.*;
// import java.awt.*;
// import java.awt.image.*;
import java.io.*;
// import java.nio.file.*;
import java.util.*;

public class TelaDeCadastroController extends TelaDeCadastroView {
    public static void cadastrarController() {
        TelaDeCadastroModel.cadastrarModel(txtNome.getText(), txtEmail.getText(), String.valueOf(txtSenha.getPassword()), nomeArquivoFoto);
    }

    public static void carregarFoto() {
        try {
            JFileChooser chooser = new JFileChooser();

            chooser.setDialogTitle("Selecione o arquivo que deseja copiar");
            chooser.setApproveButtonText("Copiar arquivo");
            int returnVal = chooser.showOpenDialog(null);
            String fileFullPath = "";
            String fileName = "";
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                fileFullPath = chooser.getSelectedFile().getAbsolutePath();
                fileName = "file-" + Math.random() + "-" + chooser.getSelectedFile().getName();
            } else {
                System.out.println("Usuário não selecionou o arquivo para copiar...");
            }

            Path pathOrigin = Paths.get(fileFullPath);
            String fullPathDestination = InterfaceView.localViewImgFolder + "\\" + fileName;
            Path pathDestination = Paths.get(fullPathDestination);
            if (fileFullPath.length() > 0) {
                Files.copy(pathOrigin, pathDestination, REPLACE_EXISTING);
                System.out.println("Arquivo " + chooser.getSelectedFile().getName() + " copiado/colado com sucesso.");
            } else {
                System.out.println("Ops! Não foi possível copiar o arquivo. Por favor, verifique e tente novamente mais tarde.");
            }

            nomeArquivoFoto = fileName;

            lblFoto.setIcon(new ImageIcon(new ImageIcon(InterfaceView.localViewImgFolder + "\\" + nomeArquivoFoto).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));

            try {
                redimensionarImagem(fileName);
            } catch (Exception e) {
                System.out.println("Erro: " + e);
            }
        } catch (Exception e) {
            System.out.println("Não foi possível copiar o arquivo.");
        }
    }

    public static void removerFoto() {
        lblFoto.setIcon(new ImageIcon(new ImageIcon(InterfaceView.localViewFolder + "\\imagem-padrao.jpg").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
        nomeArquivoFoto = "";
    }

    public static void redimensionarImagem(String img) throws IOException {

        String fullFileName = img;
        String fileName = getFileName(fullFileName);

        // String fileExtension = getFileExtension(fullFileName);
        String fullPathImageOrigin = InterfaceView.localViewImgFolder + "\\" + fullFileName;
        BufferedImage bfImg = ImageIO.read(new File(fullPathImageOrigin));

        String newFileExtension = "png";
        String newFullPathImageOrigin = InterfaceView.localViewImgFolder + "\\" + "novo-" + fileName + "." + newFileExtension;

        // BufferedImage jpgImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        File file = new File(newFullPathImageOrigin);
        ImageIO.write(bfImg, newFileExtension, file);

        Path source = Paths.get(fullPathImageOrigin);
        String newFileName = fileName + "-redimensionado.png";
        Path target = Paths.get(InterfaceView.localViewImgFolder + "\\" + newFileName);

        try (InputStream is = new FileInputStream(source.toFile())) {
            resize(is, target, InterfaceView.IMG_WIDTH, InterfaceView.IMG_HEIGHT, img);
        }

        nomeArquivoFoto = newFileName;
        Path newFullPathImage = Paths.get(newFullPathImageOrigin);
        Files.delete(newFullPathImage);
    }

    public static String getFileExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex >= 0) {
            return filename.substring(dotIndex + 1);
        }
        return "";
    }

    public static String getFileName(String filename) {
        if (filename == null) {
            return null;
        }
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex >= 0) {
            return filename.substring(0, dotIndex);
        }
        return "";
    }

    private static void resize(InputStream input, Path target,
    int width, int height, String img) throws IOException {

        // read an image to BufferedImage for processing
        BufferedImage originalImage = ImageIO.read(input);

        // create a new BufferedImage for drawing
        BufferedImage newResizedImage
        = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newResizedImage.createGraphics();

        //g.setBackground(Color.WHITE);
        //g.setPaint(Color.WHITE);

        // background transparent
        g.setComposite(AlphaComposite.Src);
        g.fillRect(0, 0, width, height);

        /* try addRenderingHints()
        // VALUE_RENDER_DEFAULT = good tradeoff of performance vs quality
        // VALUE_RENDER_SPEED   = prefer speed
        // VALUE_RENDER_QUALITY = prefer quality
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY);

        // controls how image pixels are filtered or resampled
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // antialiasing, on
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);*/

        Map<RenderingHints.Key,Object> hints = new HashMap<>();
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.addRenderingHints(hints);

        // puts the original image into the newResizedImage
        g.drawImage(originalImage, 0, 0, InterfaceView.IMG_WIDTH, InterfaceView.IMG_HEIGHT, null);
        g.dispose();

        // get file extension
        String s = target.getFileName().toString();
        String fileExtension = s.substring(s.lastIndexOf(".") + 1);

        // we want image in png format
        ImageIO.write(newResizedImage, fileExtension, target.toFile());
        // System.out.println(target.toFile().toString());
        // System.out.println(InterfaceView.localViewImgFolder + "\\" + target.toFile().toString());
    }
}
