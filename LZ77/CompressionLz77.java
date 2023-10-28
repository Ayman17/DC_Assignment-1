import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;

class CompressionLz77 {
    private final String TAG_SEPARATOR = ",";
    private final String TAG_END = "-";

    // Methods related to files 
    private String readFile(String fileName) {
        String Text = "";
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = "";
            
            while ((line = bufferedReader.readLine()) != null) {
                Text += line;
            }
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Text;
    }
    
    private void saveFile(String content, String fileName) {
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            
            fileWriter.write(content);
            
            bufferedWriter.close();
            fileWriter.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
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
            String stream = position + TAG_SEPARATOR + length + TAG_SEPARATOR + nextChar + TAG_END;
            return stream;
    }

    public String compress(String fileName) {
        String input = readFile(fileName); 
        String output = "";

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

        String outputFileName = fileName.substring(0, fileName.indexOf(".txt")) + "-compressed.txt";
        saveFile(output, outputFileName);
        return output;
    }

    private String decodeTag(String tag, String currentResult) {
        int relativePosition = Integer.parseInt(tag.substring(0, tag.indexOf(TAG_SEPARATOR)));
        tag = tag.substring(tag.indexOf(TAG_SEPARATOR) + 1);
        int length = Integer.parseInt(tag.substring(0, tag.indexOf(TAG_SEPARATOR)));
        tag = tag.substring(tag.indexOf(TAG_SEPARATOR) + 1);
        char nextChar = tag.charAt(tag.length() - 1);

        int position = currentResult.length() - relativePosition;

        for (int i = 0; i < length; i++) {
            currentResult += currentResult.charAt(i + position);
        }

        currentResult += nextChar;

        return currentResult;

    }

    // When decomporssing,  make sure your don't put TAG_END, and TAG_SEPARATOR in the text content
    public String decompress (String fileName) {
        String compressedString = readFile(fileName);

        String result = "";

        while (compressedString.length() > 0) {
            int endOfTagIndex = compressedString.indexOf(TAG_END);

            String currentTag = compressedString.substring(0, endOfTagIndex);

            result = decodeTag(currentTag, result);

            compressedString = compressedString.substring(endOfTagIndex + 1);
        }
        if (fileName.contains("-compressed")) {
            fileName = fileName.substring(0, fileName.indexOf("-compressed"));
        }
        String outputFileName = fileName + "-decompressed.txt";
        saveFile(result, outputFileName);

        return result;
    }
}