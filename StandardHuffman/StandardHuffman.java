import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StandardHuffman {

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

    private <K, V extends Comparable<? super V>> ArrayList<Node> sortByValue(ArrayList<Node> list) {
        // Custom comparator to sort entries by values
        Comparator<Node> valueComparator = Comparator.comparing(Node::getSumFreq);

        // Sorting the list using the comparator
        list.sort(valueComparator);

        return list;
    }

    public String compress(String input) {
        String output = "";
        int inputLen = input.length();
        ArrayList<Node> arr = new ArrayList<Node>();
        float[] freq = new float[256];
        for (int i = 0; i < inputLen; i++) {
            freq[input.charAt(i)] += (float)1/inputLen;
        }
        for (int i = 0; i < 256; i++) {
            if (freq[i] != 0) {
                arr.add(new Node());
                arr.get(arr.size()-1).setSumFreq(freq[i]);
                arr.get(arr.size()-1).addLeaf(new Leaf(freq[i], (char)i));
            }
        }
        arr = sortByValue(arr);
        for (int i = 0; i < arr.size(); i++) {
            System.out.println(arr.get(i).getSumFreq() + " " + arr.get(i).getLeaves().get(0).getKey());
        }
        
        return output;
    }

    public String decompress(String input) {
        String output = "";
        return output;
    }
}
