public class LZW {
    
    public static void main(String[] args) {
        boolean useGUI = true;

        if (useGUI) {
            GUI gui = new GUI();

        } else {
            String fileName = "TextToCompress";
            String extension = ".txt";
            
            CompressionLzw lzw = new CompressionLzw();
            
            String fileContent = lzw.readFile(fileName + extension);
            String fileCompressed = lzw.compress(fileContent);
            lzw.saveFile(fileCompressed, fileName + "-compressed" + extension);
            
            fileContent = lzw.readFile(fileName + "-compressed" + extension);
            String fileDecompresed = lzw.decompress(fileContent);
            lzw.saveFile(fileDecompresed, fileName + "-decompressed" + extension);
        }
    }
}