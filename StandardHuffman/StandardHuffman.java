import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;


import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class StandardHuffman {
    char CODE_SEPARATOR = '-';
    char CODE_END = '~';

    public ArrayList<Byte> readFileBinary(String fileName) {
        ArrayList <Byte> content = new ArrayList();
        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            int byteRead;
            while ((byteRead = fileInputStream.read()) != -1) {
                content.add((byte) byteRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

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

    private static ArrayList<Byte> convertBinaryToBytes(String binaryStream) {
        while (binaryStream.length()% 8 != 0) {
            binaryStream += "0";
        } 
        
        ArrayList<Byte> bytes = new ArrayList<Byte>();
        for (int i = 0; i < binaryStream.length(); i += 8) {
            String byteString = binaryStream.substring(i, i + 8);
            byte b = (byte) Integer.parseInt(byteString, 2);
            bytes.add(b);
        }
        
        return bytes;
    }

    public void saveBytesFile(ArrayList<Byte> content, String fileName) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            // Convert ArrayList<Byte> to byte array
            byte[] byteArray = new byte[content.size()];
            for (int i = 0; i < content.size(); i++) {
                byteArray[i] = content.get(i);
            }

            fileOutputStream.write(byteArray);

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

    private TreeSet<Node> getSortedNodes(int[] freq) {
        TreeSet<Node> result = new TreeSet<>();
        for (int i = 0; i < 256; i++) {
            if (freq[i] != 0) {
                Node current = new Node();
                current.freq = freq[i];
                current.ch = (char) i;
                result.add(current);
            }
        }
        
        return result;
    }

    public ArrayList<Byte> compress(String input) {
        int numberOfCodes = 0;
        String output = "";
        int inputLen = input.length();
        TreeSet<Node> sortedNodes;
        int[] freq = new int[256];
        String[] codes = new String[256];
        
        for (int i = 0; i < inputLen; i++) {
            freq[input.charAt(i)] += 1;
        }

        for (int i = 0; i < 256; i++) {
            if (freq[i] != 0) {
                numberOfCodes++;
            }
        }

        sortedNodes = getSortedNodes(freq);

        combine(sortedNodes);

        divide(sortedNodes.first(), codes, "0");
        divide(sortedNodes.last(), codes, "1");

        ArrayList<Byte> overHead = new  ArrayList(); 

        overHead.add((byte) numberOfCodes);

        overHead.add((byte)inputLen);

        for (int i = 0; i < 256; i++) {
            if (codes[i] != null) {
                overHead.add((byte) i );
                overHead.add((byte) freq[i]);
            }
        }

        for (int i = 0; i < inputLen; i++) {
            output += codes[input.charAt(i)];
        }

        overHead.addAll(convertBinaryToBytes(output));
        return overHead;

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

    public String decompress(List<Byte> input) {
        String output = "";
        String[] codesArr = new String[256];

        
        Map<String, Character> codes = new HashMap<>();
        int[] freq = new int[256];
        
        int numberOfCodes = input.get(0);
        int numberOfChars = input.get(1);

        input = input.subList(2, input.size());


        for (int i = 0; i < numberOfCodes * 2; i+=2) {
            freq[input.get(i)] = input.get(i+1);
        }


        List<Byte> codedChars = input.subList(numberOfCodes * 2, input.size());


        TreeSet<Node> sortedNodes = getSortedNodes(freq);

        combine(sortedNodes);

        divide(sortedNodes.first(), codesArr, "0");
        divide(sortedNodes.last(), codesArr, "1");

        for (int i = 0; i < 256; i++) {
            if (codesArr[i] != null) {
                codes.put(codesArr[i], (char) i);
            }
        }

        String soFar = "";

        String codedCharsStream = "";

        for (byte b : codedChars) {
            String current = Integer.toBinaryString((int) b & 0xFF);
            while (current.length() % 8 != 0) {
                current = "0" + current;
            }
            codedCharsStream += current;
        }
        
        for (int i = 0; i < codedCharsStream.length(); i++) {
            if (numberOfChars == 0) {
                break;
            }
            soFar += codedCharsStream.charAt(i);
            if (codes.containsKey(soFar)) {
                output += codes.get(soFar);
                soFar = "";
                numberOfChars --;
            }
        }
        return output;
    }
}
