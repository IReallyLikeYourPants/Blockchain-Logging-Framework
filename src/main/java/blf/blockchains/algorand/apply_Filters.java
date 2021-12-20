package blf.blockchains.algorand;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

// Apply filters for the Indexer
public class apply_Filters {
    public static List<String> implementFilters(String options_set, String val1, String adressino, int block_start, int block_end)
        throws InterruptedException {
        String[] result = options_set.split(",");
        List<String> saving = new ArrayList<String>();
        saving.add(result[0]);
        // If no filters applied
        if (result.length == 1) {
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
                if (adressino.length() < 15) {
                    oldContent = oldContent.replaceAll("// ValueToChange0", ".applicationId(Long.valueOf(" + adressino + "))");
                    saving.add(".applicationId(Long.valueOf(" + adressino + "))");
                } else {
                    oldContent = oldContent.replaceAll("// ValueToChange0", ".address(new Address(" + adressino + "))");
                    saving.add(".address(new Address(" + adressino + "))");
                }
                if (block_start != -100) {
                    oldContent = oldContent.replaceAll("// ValueToChangeMIN", ".minRound(Long.valueOf(" + block_start + "))");
                }
                if (block_end != -100) {
                    oldContent = oldContent.replaceAll("// ValueToChangeMAX", ".maxRound(Long.valueOf(" + block_end + "))");
                }
                writer = new FileWriter(fileToBeModified);
                writer.write(oldContent);

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                reader.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // If filters applied
        else {
            for (int i = 1; i < result.length; i++) {
                if (result[i].trim().indexOf(' ') == -1) {
                    throw new java.lang.RuntimeException("There is an error with a transaction filter!");
                }
                int val = result[i].trim().indexOf(' ');
                String word = result[i].trim().substring(0, val);
                String word1 = result[i].trim().substring(val);
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
                    if (i == 1) {
                        if (adressino.length() < 15) {
                            oldContent = oldContent.replaceAll("// ValueToChange0", ".applicationId(Long.valueOf(" + adressino + "))");
                            saving.add(".applicationId(Long.valueOf(" + adressino + "))");
                        } else {
                            oldContent = oldContent.replaceAll("// ValueToChange0", ".address(new Address(" + adressino + "))");
                            saving.add(".address(new Address(" + adressino + "))");
                        }
                        if (block_start != -100) {
                            oldContent = oldContent.replaceAll("// ValueToChangeMIN", ".minRound(Long.valueOf(" + block_start + "))");
                        }
                        if (block_end != -100) {
                            oldContent = oldContent.replaceAll("// ValueToChangeMAX", ".maxRound(Long.valueOf(" + block_end + "))");
                        }
                    }
                    if (word.equals("asset_id")) {
                        newContent = oldContent.replaceAll("// ValueToChange" + i, ".assetId(Long.valueOf(" + word1.trim() + "))");
                        saving.add(".assetId(Long.valueOf(" + word1.trim() + "))");
                    } else if (word.equals("address_role")) {
                        newContent = oldContent.replaceAll(
                            "// ValueToChange" + i,
                            ".addressRole(AddressRole." + word1.trim().toUpperCase() + ")"
                        );
                        saving.add(".addressRole(AddressRole." + word1.trim().toUpperCase() + ")");
                    } else if (word.equals("txn_type")) {
                        newContent = oldContent.replaceAll("// ValueToChange" + i, ".txType(TxType." + word1.trim().toUpperCase() + ")");
                        saving.add(".txType(TxType." + word1.trim().toUpperCase() + ")");
                    } else if (word.equals("min_amount")) {
                        newContent = oldContent.replaceAll(
                            "// ValueToChange" + i,
                            ".currencyGreaterThan(Long.valueOf(" + word1.trim() + "))"
                        );
                        saving.add(".currencyGreaterThan(Long.valueOf(" + word1.trim() + "))");
                    } else {
                        reader.close();
                        throw new java.lang.RuntimeException("There is an error with a transaction filter!");
                    }
                    writer = new FileWriter(fileToBeModified);
                    writer.write(newContent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    reader.close();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        TimeUnit.SECONDS.sleep(2);
        return saving;
    }
}
