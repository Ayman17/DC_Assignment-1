public class LZW {

    public static void main(String[] args) {
        String fileName = "TextToCompress";
        String extension = ".txt";

        CompressionLzw lzw = new CompressionLzw();
        lzw.compress(fileName + extension);
        lzw.decompress(fileName + "-compressed" + extension);
    }
}