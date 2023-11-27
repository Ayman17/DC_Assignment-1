import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

// TODO: edit gui to standard huffman
public class GUI {
    JFrame frame = new JFrame("LZW");
    String inputPath, outputPath;

    public GUI() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 400);
        
        JPanel panel = new JPanel(new GridLayout(0, 2, 20, 20));

        JButton b1 = new JButton("Choose the file to compress");

        JLabel inputLabel = new JLabel("Input File : ");
        JLabel outputLabel = new JLabel("Output File : ");

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


        JButton b2 = new JButton("Choose where to save the file");

        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir"))); 
                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    java.io.File selectedFile = fileChooser.getSelectedFile();
                    outputPath = selectedFile.getAbsolutePath();

                    outputLabel.setText("Output File : " + new File(outputPath).getName()); 
                }
            }
        });


        JButton compress = new JButton("Compress");
    
        compress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StandardHuffman h = new StandardHuffman();

                String fileContent = h.readFile(inputPath);
                String fileCompressed = h.compress(fileContent);
                h.saveFile(fileCompressed, outputPath);
            }
        });

        JButton decompress = new JButton("Decompress");

        decompress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StandardHuffman h = new StandardHuffman();

                String fileContent = h.readFile(inputPath);
                String fileCompressed = h.decompress(fileContent);
                h.saveFile(fileCompressed, outputPath);
            }
        });

        panel.add(b1, constraints);
        panel.add(inputLabel, constraints);

        panel.add(b2);
        panel.add(outputLabel);

        panel.add(compress);
        panel.add(decompress);

        frame.add(panel);

        frame.setVisible(true);
    }
}