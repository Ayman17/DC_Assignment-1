import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeSet;

import javax.print.DocFlavor.INPUT_STREAM;

import java.util.Iterator;
import java.util.Map;

public class StandardHuffman {
    char CODE_SEPARATOR = '-';
    char CODE_END = '~';

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

    public void saveBytesFile(String content, String fileName) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            byte[] bytes = content.getBytes();
            fos.write(bytes);

            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveFile(String content, String fileName) {
        try (FileWriter f = new FileWriter(fileName)) {
            f.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeFirst(TreeSet<Node> s) {
        if (s.size() == 0) {
            return;
        }

        Iterator<Node> i = s.iterator();
        i.next();
        i.remove();
    }

    private void combine(TreeSet<Node> s) {
        while (s.size() > 2) {
            Node n1 = s.first();
            removeFirst(s);
            Node n2 = s.first();
            removeFirst(s);

            Node combined = new Node();
            combined.freq = n1.freq + n2.freq;
            combined.right = (n1.freq > n2.freq) ? n1 : n2;
            combined.left = (n1.freq <= n2.freq) ? n1 : n2;
            combined.isLeaf = false;
            s.add(combined);
        }
    }

    private void divide(Node n, String[] codes, String code) {
        if (n == null) {
            return;
        }
        if (n.isLeaf) {
            codes[n.ch] = code;
        }

        divide(n.left, codes, code + "0");
        divide(n.right, codes, code + "1");
    }

    public String compress(String input) {
        int numberOfCodes = 0;
        String output = "";
        int inputLen = input.length();
        TreeSet<Node> sortedNodes = new TreeSet<>();
        float[] freq = new float[256];
        String[] codes = new String[256];

        for (int i = 0; i < inputLen; i++) {
            // freq[input.charAt(i)] += (float)1/inputLen;
            freq[input.charAt(i)] += 1;
        }

        for (int i = 0; i < 256; i++) {
            if (freq[i] != 0) {
                numberOfCodes++;
                Node current = new Node();
                current.freq = freq[i];
                current.ch = (char) i;
                sortedNodes.add(current);
            }
        }

        combine(sortedNodes);

        divide(sortedNodes.first(), codes, "0");
        divide(sortedNodes.last(), codes, "1");

        // printNodes(sortedNodes.first(), "");
        // printNodes(sortedNodes.last(), "");

        // for (int i = 0; i < 256; i++) {
        //     if (codes[i] != null) {
        //         System.out.println((char) i + " : " + codes[i]);
        //     }
        // }


        String numberOfCodesBin = Integer.toBinaryString(numberOfCodes);

        output += numberOfCodesBin + CODE_END;

        for (int i = 0; i < 256; i++) {
            if (codes[i] != null) {
                String ch = Integer.toBinaryString(i);

                
                output += ch;
                output += CODE_SEPARATOR;
                output += codes[i];
                output += CODE_END;
            }
        }

        for (int i = 0; i < inputLen; i++) {
            output += codes[input.charAt(i)];
        }


        return output;
    }

    private void printNodes(Node n, String depth) {
        if (n == null) {
            return;
        }
        System.out.print(depth);
        if (n.isLeaf) {
            System.out.print(n.ch + " : ");
        }

        System.out.println(n.freq);

        printNodes(n.left, depth + "   ");

        printNodes(n.right, depth + "   ");
    }

    public String decompress(String input) {
        String output = "";
        Map<String, Character> codes = new HashMap<>();

        int numberOfCodes = Integer.parseInt(input.substring(0, input.indexOf(CODE_END)), 2);
        input = input.substring(input.indexOf(CODE_END) + 1);

        for (int i = 0; i < numberOfCodes; i++) {
            char ch = (char) Integer.parseInt(input.substring(0, input.indexOf(CODE_SEPARATOR)), 2);
            String code = input.substring(input.indexOf(CODE_SEPARATOR) + 1, input.indexOf(CODE_END));
            input = input.substring(input.indexOf(CODE_END) + 1);
            codes.put(code, ch);
        }



        int i = 1;
        while (input.length() > 0) {
            if (codes.containsKey(input.substring(0, i))) {
                output += codes.get(input.substring(0, i));
                input = input.substring(i);
                i = 0;
            }

            i++;
        }

        return output;
    }
}
