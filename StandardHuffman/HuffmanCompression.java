
public class HuffmanCompression {

    public static void main(String[] args) {
        boolean useGUI = false;

        if (useGUI) {
            // GUI gui = new GUI();

        } else {
            String fileName = "TextToCompress";
            String extension = ".txt";
            
            StandardHuffman huffmanCompression = new StandardHuffman();
            
            String fileContent = huffmanCompression.readFile(fileName + extension);
            String fileCompressed = huffmanCompression.compress(fileContent);
            // huffmanCompression.saveFile(fileCompressed, fileName + "-compressed" + extension);
            
            // fileContent = huffmanCompression.readFile(fileName + "-compressed" + extension);
            // String fileDecompresed = huffmanCompression.decompress(fileContent);
            // huffmanCompression.saveFile(fileDecompresed, fileName + "-decompressed" + extension);
        }
    }
}