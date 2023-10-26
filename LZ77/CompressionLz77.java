class CompressionLz77 {
    private String input;
    private String output;

    public CompressionLz77(String input) {
        this.input = input;
        this.output = "";
    }
    // Hello World!
    public String compress() {
        String buffer = "";
        for (int i = 0, j = 0; i < input.length(); i++) {
            while (j <= buffer.length()) {
                if (input.charAt(i) == buffer.charAt(j)) {
                    buffer += input.charAt(i);
                    j++;
                    break;
                }
                else {
                    j++;
                }
            }
            buffer += input.charAt(i);
            output += "<0,0," + input.charAt(i) + ">\n";
        }
        return output;
    }
}