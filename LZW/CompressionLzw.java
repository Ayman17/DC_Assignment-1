import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class CompressionLzw {
    private final String TAG_SEPARATOR = ",";
    
    public String readFile(String fileName) {
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
    
    public void saveFile(String content, String fileName) {
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

    public String compress(String input) {
        String output = "";

        Integer counter = 128;

        Map<String, Integer> patternMap = new HashMap<>();

        String currentPattern = String.valueOf(input.charAt(0));

        for (int i = 0; i < input.length(); i++) {
            if (i < input.length() - 1) {
                currentPattern += input.charAt(i + 1);
            }
            
            if (!patternMap.containsKey(currentPattern)) {
                patternMap.put(currentPattern, counter);
                counter++;

                if (currentPattern.length() <= 2) {
                    output += Integer.toString((int) currentPattern.charAt(0)) + TAG_SEPARATOR;
                } else {
                    output += Integer.toString((int) patternMap.get(currentPattern.substring(0, currentPattern.length() - 1))) + TAG_SEPARATOR;
                }
                
                currentPattern = String.valueOf(currentPattern.charAt(currentPattern.length() - 1));
            }
        }

        if (currentPattern.length() > 1) {
            output += Integer.toString((int) patternMap.get(currentPattern.substring(0, currentPattern.length()))) + TAG_SEPARATOR;
        }
        return output;
    }

    public String decompress(String input) {
        String output = "";

        Map<Integer, String> patternMap = new HashMap<>();

        String[] tags = input.split(TAG_SEPARATOR); 
        int counter = 128;
        
        String prevPattern = String.valueOf((char) Integer.parseInt(tags[0]));
        output += prevPattern;

        for (int i = 1; i < tags.length; i++) {
            int tagNumber = Integer.parseInt(tags[i]);

            String current;

            if (tagNumber > 127 && !patternMap.containsKey(tagNumber)) {
                current = prevPattern + prevPattern.charAt(0);
            }
            else if  (tagNumber > 127) {
                current = patternMap.get(tagNumber);
            } else {
                current = String.valueOf((char) tagNumber);
            }

            patternMap.put(counter, prevPattern + current.charAt(0)) ;

            prevPattern = current;

            counter++;

            output += current;
        }

        return output;
    }
}
