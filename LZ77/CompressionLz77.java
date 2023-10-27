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

    private String getCompressedStream(int position, int length, char nextChar) {
            String stream = position + "," + length + "," + nextChar + "-";
            // System.out.println(stream);
            return stream;
    }

    public String compress() {
        for (int i = 0; i < input.length(); i++) {
            
            String maxMatch = "";
            char nextChar = input.charAt(i);
            int position = 0;
            int length = 0;

            // TODO: need to add max for loop iteration (BUFFER_SIZE) 
            for (int j = i - 1; j >= 0; j--) {
                String currentMatch = getLongestMatch(input.substring(j), input.substring(i));
                if (currentMatch.length() > maxMatch.length()) {
                    maxMatch = currentMatch;
                    length = maxMatch.length();
                    position = i - j;
                    if (maxMatch.length() + i >= input.length()) {
                        nextChar = '\0';
                        break;
                    }
                    nextChar = input.charAt(maxMatch.length() + i);
                }
            }

            output += getCompressedStream(position, length, nextChar);

            i += length;
        }
        return output;
    }
}