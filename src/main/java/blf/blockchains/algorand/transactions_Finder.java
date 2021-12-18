package blf.blockchains.algorand;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import com.algorand.algosdk.v2.client.common.IndexerClient;
import com.algorand.algosdk.v2.client.model.Enums.AddressRole;
import com.algorand.algosdk.v2.client.model.Enums.TxType;
import com.algorand.algosdk.crypto.Address;
import java.util.concurrent.TimeUnit;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

import org.json.*;

import jnr.ffi.StructLayout.id_t;

public class transactions_Finder {
    public static void finding(
        String address,
        String token,
        int block_start,
        int block_end,
        String address_set,
        String output_path,
        List<String> duplicates,
        List<String> duplicates_value,
        List<String> savings,
        String val1,
        int lung
    ) throws InterruptedException, IOException, NoSuchAlgorithmException {
        String data_check = "";
        try {
            connection_To_Indexer ex = new connection_To_Indexer();
            IndexerClient indexerClientInstance = (IndexerClient) ex.connectToNetwork(address, token);
            Address account = new Address(address_set);
            Path fileToDeletePath = Paths.get(output_path + "/output" + val1 + ".json");
            Files.deleteIfExists(fileToDeletePath);
            fileToDeletePath = Paths.get(output_path + "/output.csv");
            Files.deleteIfExists(fileToDeletePath);
            FileWriter file = new FileWriter(output_path + "/output" + val1 + ".json", true);
            data_check = data_check + "[" + "\n";
            while (block_start < block_end + 1) {
                try {
                    String response = indexerClientInstance.searchForTransactions()
                        .address(account)
                        .round(Long.valueOf(block_start))

                        // ValueToChange0
                        // ValueToChange1
                        // ValueToChange2
                        // ValueToChange3
                        // ValueToChange4
                        // ValueToChange5
                        // ValueToChange6
                        // ValueToChange7
                        // ValueToChange8

                        .execute()
                        .toString();
                    JSONObject jsonObj = new JSONObject(response.toString());
                    if (jsonObj.has("next-token")) {
                        System.out.println("INFO: Transaction found in block: " + block_start);
                        for (int i = 0; i < jsonObj.getJSONArray("transactions").length(); i++) {
                            JSONObject dato = new JSONObject();
                            for (int e = 0; e < duplicates.size(); e++) {
                                String key = "";
                                if (duplicates_value.get(e).equals("amount")) {

                                    int key1 = jsonObj.getJSONArray("transactions")
                                        .getJSONObject(i)
                                        // .getJSONObject("asset-transfer-transaction")
                                        .getJSONObject("payment-transaction")
                                        .getInt("amount");
                                    key = String.valueOf(key1);

                                } else if (duplicates_value.get(e).equals("receiver")) {

                                    key = jsonObj.getJSONArray("transactions")
                                        .getJSONObject(i)
                                        .getJSONObject("payment-transaction")
                                        .getString("receiver");
                                } else if (duplicates_value.get(e).equals("round-time")) {
                                    int key1 = jsonObj.getJSONArray("transactions").getJSONObject(i).getInt("round-time");
                                    java.util.Date time = new java.util.Date((long) key1 * 1000);
                                    String pattern = "yyyy-MM-dd HH:mm:ss.SSS";
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                                    String date = simpleDateFormat.format(time);
                                    key = String.valueOf(date);

                                } else if (duplicates_value.get(e).charAt(duplicates_value.get(e).length() - 1) == '"'
                                    && duplicates_value.get(e).charAt(0) == '"') {
                                        key = duplicates_value.get(e).substring(1, duplicates_value.get(e).length() - 1);
                                    } else {
                                        throw new java.lang.RuntimeException("There is a problem with your CSV output types");
                                    }

                                dato.put(duplicates.get(e), key);
                            }
                            data_check = data_check + dato.toString() + "," + "\n";
                        }
                    }
                    System.out.println("INFO: Processing of block " + block_start + " finished");
                    block_start += 1;
                } catch (JSONException e) {} catch (RuntimeException e) {
                    System.out.println("ERROR: There is a problem with your CSV output types");
                    // reset_Finder.resetting(savings, val1);
                    System.exit(1);

                } catch (Exception e) {
                    continue;
                }
            }
            if (lung == Integer.valueOf(val1) + 1) {
                data_check = data_check.substring(0, data_check.length() - 2) + "\n" + "]";
            } else {
                data_check = data_check.substring(0, data_check.length() - 2) + "\n" + ",";
            }

            file.append(data_check);
            file.close();
            if (lung == Integer.valueOf(val1) + 1) {
                json_To_Csv.createCsv(output_path, val1);
                if (data_check.length() < 10) {
                    System.out.println("INFO: No transactions found with these filters!");
                    fileToDeletePath = Paths.get(output_path + "/output.json");
                    Files.deleteIfExists(fileToDeletePath);
                } else {
                    System.out.println("INFO: Json and Csv export ended.");

                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
