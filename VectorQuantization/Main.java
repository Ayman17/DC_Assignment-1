import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        boolean useGUI = true;

        if (useGUI) {
            GUI gui = new GUI();

        } else {
            String fileName = "TextToCompress";
            String extension = ".txt";
            
            StandardHuffman huffmanCompression = new StandardHuffman();
            
            String fileContent = huffmanCompression.readFile(fileName + extension);
            ArrayList<Byte> fileCompressed = huffmanCompression.compress(fileContent);
            huffmanCompression.saveBytesFile(fileCompressed, fileName + "-compressed.bin");
            
            ArrayList<Byte> commpressedContent= huffmanCompression.readFileBinary(fileName + "-compressed.bin");
            String fileDecompresed = huffmanCompression.decompress(commpressedContent);
            huffmanCompression.saveFile(fileDecompresed, fileName + "-decompressed" + extension);
        }
    }
}