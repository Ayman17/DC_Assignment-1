public class LZW {

    public static void main(String[] args) {
        String fileName = "TextToCompress";
        String extension = ".txt";

        CompressionLzw lz77 = new CompressionLzw();
        lz77.compress(fileName + extension);
        lz77.decompress(fileName + "-compressed" + extension);
    }
}