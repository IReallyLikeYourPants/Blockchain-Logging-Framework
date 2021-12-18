package blf.blockchains.algorand;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi.ecCVCDSA224;

public class reset_Finder {
    public static void resetting(List<String> savings, String val1, int block_start, int block_end) {
        for (int i = 1; i < savings.size(); i++) {
            File fileToBeModified = new File("src/main/java/blf/blockchains/algorand/transactions_Finder" + val1 + ".java");
            String oldContent = "";
            BufferedReader reader = null;
            FileWriter writer = null;
            try {
                reader = new BufferedReader(new FileReader(fileToBeModified));
                String line = reader.readLine();
                while (line != null) {
                    oldContent = oldContent + line + System.lineSeparator();
                    line = reader.readLine();
                }
                String newContent = "";
                newContent = oldContent.replaceAll(Pattern.quote(savings.get(i).trim()), "// ValueToChange" + (i - 1));
                newContent = newContent.replaceAll(Pattern.quote(".minRound(Long.valueOf(" + block_start + "))"), "// ValueToChangeMIN");
                newContent = newContent.replaceAll(Pattern.quote(".maxRound(Long.valueOf(" + block_end + "))"), "// ValueToChangeMAX");
                // newContent = oldContent.replaceAll("AXFER", "cane");
                writer = new FileWriter(fileToBeModified);
                writer.write(newContent);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    reader.close();
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {} catch (Exception e) {
                System.out.print("Errore!");
            }
        }
    }
}
