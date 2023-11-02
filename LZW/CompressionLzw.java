import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class CompressionLzw {
    private final String TAG_SEPARATOR = ",";
    private Map<Integer, String> patternMap = new HashMap<>();

    public CompressionLzw() {

    }

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

    private String getCompressedStream(int index) {
        String stream = index + TAG_SEPARATOR;
        return stream;
    }

    public void compress(String fileName) {
        String input = readFile(fileName);
        String output = "";

        for (int i = 0; i < input.length(); i++) {
            output = Integer.toString((int) output.charAt(i));
        }
        
        String outputFileName = fileName.substring(0, fileName.indexOf(".txt")) + "-compressed.txt";
        saveFile(output, outputFileName);
        return;
    }

    public void decompress(String fileName) {
        String input = readFile(fileName);
        String output = "";

        if (fileName.contains("-compressed")) {
            fileName = fileName.substring(0, fileName.indexOf("-compressed"));
        }
        String outputFileName = fileName + "-decompressed.txt";
        saveFile(output, outputFileName);
        return;
    }
}
