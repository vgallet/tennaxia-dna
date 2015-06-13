import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;

public class DNAModifier {

    private static final String DNA = "ATGC";
    private static Integer position;
    private static boolean insert;

    public static void main(String[] args) throws IOException {
        File inputFile = new File(args[0]);

        File output = new File("output.txt");
        output.createNewFile();

        FileReader reader = new FileReader(inputFile);


        FileOutputStream outputStream = new FileOutputStream(output);
        BufferedReader bufferedReader = new BufferedReader(reader);

        String line;
        boolean reverse = false, count = false;
        int nbFragment = 0;
        String fragment = "";
        StringBuffer buffer = new StringBuffer();
        while ((line = bufferedReader.readLine()) != null) {
            if (line.equals("reverse")) {
                reverse = true;
                continue;
            }
            if (line.startsWith("count")) {
                fragment = line.substring(line.indexOf(" ") + 1);
                count = true;
                continue;
            }

            if (line.startsWith("insert")) {
                fragment = line.substring(line.indexOf(" ") + 1, line.lastIndexOf(" "));
                position = Integer.valueOf(line.substring(line.lastIndexOf(" ") + 1));
                insert = true;
                continue;
            }
            for(char buf : line.toCharArray()) {
                if(DNA.indexOf(buf) > -1){
                    buffer.append(buf);
                }
            }
        }
        if (reverse) {
            outputStream.write(buffer.reverse().toString().getBytes());
        } else if(count) {
            outputStream.write(String.valueOf(countFragment(buffer.toString(), nbFragment, fragment)).getBytes());
        } else if (insert) {
            String dna = buffer.toString();
            outputStream.write(
                (dna.substring(0, position) + fragment + dna.substring(position + 1)).getBytes()
            );
        } else {
            outputStream.write(buffer.toString().getBytes());
        }

        outputStream.close();
        reader.close();


    }

    private static int countFragment(String dnaStr, int nbFragment, String fragment) {
        if(!dnaStr.contains(fragment))
            return nbFragment;
        return countFragment(dnaStr.replaceFirst(fragment, ""), nbFragment + 1, fragment);
    }
}
