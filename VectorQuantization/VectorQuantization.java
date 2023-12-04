import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VectorQuantization {
    char CODE_SEPARATOR = '-';
    char CODE_END = '~';

    public ArrayList<Byte> readFileBinary(String fileName) {
        ArrayList<Byte> content = new ArrayList<>();
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
        while (binaryStream.length() % 8 != 0) {
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


    public ArrayList<Byte> compress(String input) {
        return new ArrayList<>();
    }

    public String decompress(List<Byte> input) {
        return new String();
    }
}
