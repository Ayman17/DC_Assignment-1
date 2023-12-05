import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JTextField;

// TODO: edit gui to standard huffman
public class GUI {
    JFrame frame = new JFrame("LZW");
    String inputPath ;

    public GUI() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 400);
        
        JPanel panel = new JPanel(new GridLayout(0, 2, 20, 20));

        JButton b1 = new JButton("Choose the file");

        JLabel inputLabel = new JLabel("Input File : ");

        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir"))); 
                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    java.io.File selectedFile = fileChooser.getSelectedFile();
                    inputPath = selectedFile.getAbsolutePath();
                    inputLabel.setText("Input File : " + new File(inputPath).getName());
                }
            }
        });

        JLabel vectorDimensionLabel = new JLabel("Enter Vector Dimension");
        JTextField vectorDimensionInput = new JTextField(10);

        JLabel numberOfVectorsLabel= new JLabel("Enter Number Of Vectors");
        JTextField numberOfVectorsInput = new JTextField(10);

        JButton compress = new JButton("Compress");
    
        compress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            VectorQuantization v = new VectorQuantization();
            BufferedImage image = v.readGrayImage(inputPath);


            java.util.List<Byte> result = v.compress(image, Integer.parseInt(vectorDimensionInput.getText()), Integer.parseInt(numberOfVectorsInput.getText()));
            v.saveCompressedImage(result, "compressed.bin");       
            }
        });

        JButton decompress = new JButton("Decompress");

        decompress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VectorQuantization v = new VectorQuantization();

                java.util.List<Byte> compressedImage = v.readBinaryFile("compressed.bin");
                BufferedImage decompressedImage = v.decompress(compressedImage);
                v.saveGrayImage(decompressedImage, "decomressed.jpg");
            }
        });

        panel.add(b1, constraints);
        panel.add(inputLabel, constraints);

        panel.add(vectorDimensionLabel, constraints); 
        panel.add(vectorDimensionInput, constraints); 

        panel.add(numberOfVectorsLabel, constraints); 
        panel.add(numberOfVectorsInput, constraints); 

        panel.add(compress);
        panel.add(decompress);

        frame.add(panel);

        frame.setVisible(true);
    }
}