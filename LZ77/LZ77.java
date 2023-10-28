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
        // String Text = "RARARASRARAS";
        // String Text = "ABAABAB";
        // String compressedString = "0,0,h-0,0,e-0,0,l-1,1,o-0,0, -0,0,t-7,1,i-0,0,s-5,1,i-3,2,a-10,2,e-6,1,t-5,1,f-18,1,r-19,3,e-4,1,c-0,0,m-9,1,p-10,1,e-17,1,s-24,2,n-0,0,d-17,5,d-3,1,c-18,1,m-19,6,f-0,0,u-20,1,c-19,1,i-14,1,n-27,1,l-5,1,t-0,0,y-14,1,I-67,4,n-0,0,k-68,2,t-0,0,'-27,2,v-32,1,r-18,2,c-24,1,e-8,3, -58,4,I-29,17,g-56,1,o-23,1,.-";
        // String compressedString = "0,0,A-0,0,B-2,1,A-3,2,B-5,3,B-1,10,A-";

        // int windowSize = 5;
        // int lookaheadSize = 3;

        CompressionLz77 lz77 = new CompressionLz77();
        lz77.compress("test.txt");
        lz77.decompress("test-compressed.txt");

        // System.out.println(compressedText);
    }
}