class LZ77 {

    /*
     * 1. Comress text file and print it in terminal and save it in TextToCompress-compressed.txt file:
     * (V 0.0)
     *  - First, we need to creat CompressionLz77 class (attributes: input, output | operations: compress)
     *      - main send text to compress method
     *      - the method compress text and return it to main
     *      - main print the compressed text in terminal 
     * (V 1.0)
     *  - read from TextToCompress.txt
     *      - main ask user to enter file want to compress name
     *      - main send file name to Lz77 object
     *      - Lz77 compress file and save it in another file it name is (nameOfOriginalFile-compressed.txt)
     * 2. Decompress text file and print it in terminal and append it in TextToCompress.txt file
     * (V 0.0)
     *  - First, we need to creat DeCompressionLz77 class (attributes: input, output | operations: deCompress)
            * - main send Compressed stream to decompress method 
            *      - the method decompress the stream and return it to main
            *      - main print the compressed text in terminal 
       (V 1.0)
    *  - read from TextToCompress-compressed.txt
    *      - main ask user to enter file want to decompress name
    *      - main send file name to deLz77 object
    *      - deLz77 decompress file and append it in another file it name is (nameOfOriginalFile(exept -compressed).txt)
    * (V 1.1)
        - add windowSize and lookaheadSize to CompressionLz77 class
     */
    public static void main(String[] args) {
        String Text = "Hello World!";
        // int windowSize = 5;
        // int lookaheadSize = 3;
        // CompressionLz77 lz77 = new CompressionLz77(Text);
        // String compressedText = lz77.compress();
        // System.out.println(compressedText);
    }
}