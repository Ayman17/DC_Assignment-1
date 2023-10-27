class CompressionLz77 {
    private String input;
    private String output;
    private final int BUFFER_SIZE = 10000; 

    public CompressionLz77(String input) {
        this.input = input;
        this.output = "";
    }
    // Hello World!

    private String getLongestMatch(String currentBuffer, String currentInput) {
        String result = "";
        for (int i = 0; i < currentInput.length(); i++) {        
            if (currentInput.charAt(i) != currentBuffer.charAt(i)) {
                return result;
            }
            result += currentInput.charAt(i);
        }

        return result;
    }

    private String getCompressedStream(String maxMatch, int position) {
        return (Integer.toString(position) + "," + Integer.toString(maxMatch.length()) + "," );
    }

    public String compress() {
        for (int i = 0; i < input.length(); ) {
            
            String maxMatch = "";
            char nextChar = input.charAt(i);
            int maxPosition = 0;

            // TODO: need to add max for loop iteration (BUFFER_SIZE) 
            for (int j = i - 1; j >= 0; j--) {
                String currentMatch = getLongestMatch(input.substring(i - (j + 1)), input.substring(i));
                if (currentMatch.length() > maxMatch.length()) {
                    maxMatch = currentMatch;
                    nextChar = input.charAt(maxMatch.length() + i);
                }
                maxPosition = i - j;
            }

            System.out.println(maxMatch + ", " + nextChar + ", " + maxPosition);

            if (maxMatch.length() > 0) {
                i += maxMatch.length();
            }  else {
                i += 1;
            }

            // String commpressedStream= getCompressedStream()


            //     String currntMatch = "";

            //     if (input.charAt(i) == buffer.charAt(j)) {
            //         // buffer += input.charAt(i);
            //         // output += "<p,l," + input
            //         i++;
            //         break;
            //     }
            //     else {
            //         j++;
            //     }
            // }
            // if (j >= buffer.length()) {
            //     buffer += input.charAt(i);
            //     this.output += "<0,0," + input.charAt(i) + ">\n";
            }
            // j = 0
        return output;
    }
}