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
            while (j < buffer.length()) {
                if (input.charAt(i) == buffer.charAt(j)) {
                    // buffer += input.charAt(i);
                    // output += "<p,l," + input
                    i++;
                    break;
                }
                else {
                    j++;
                }
            }
            if (j >= buffer.length()) {
                buffer += input.charAt(i);
                this.output += "<0,0," + input.charAt(i) + ">\n";
            }
            j = 0;
        }
        return output;
    }
}