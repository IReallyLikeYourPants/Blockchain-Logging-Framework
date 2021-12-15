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

public class transactions_Finder1 {
    public static void finding(
        String address,
        String token,
        String address_set,
        String output_path,
        List<String> duplicates,
        List<String> duplicates_value,
        List<String> savings,
        String val1,
        int lung,
        List<String> saving_singles
    ) throws InterruptedException, IOException, NoSuchAlgorithmException {
        String data_check = "";
        try {
            connection_To_Indexer ex = new connection_To_Indexer();
            IndexerClient indexerClientInstance = (IndexerClient) ex.connectToNetwork(address, token);
            Path fileToDeletePath = Paths.get(output_path + "/output.json");
            FileWriter file = new FileWriter(output_path + "/output.json", true);
            data_check = data_check + "\n";

            String nexttoken = "";
            Integer numtx = 1; 
            Long limit = Long.valueOf(900);
            String valore_finale = "";
            try 
        {
        while (numtx > 0) 
        {
            String next_page = nexttoken;
            String response = indexerClientInstance.searchForTransactions()
            .limit(limit).next(next_page)
            
            
            // ValueToChange0
            // ValueToChangeMIN
            // ValueToChangeMAX
            // ValueToChange1
            // ValueToChange2
            // ValueToChange3
            // ValueToChange4
            // ValueToChange5
            // ValueToChange6
            // ValueToChange7
            // ValueToChange8
            
            
            .execute().toString();
            JSONObject jsonObj = new JSONObject(response.toString());
            JSONArray jsonArray = (JSONArray) jsonObj.get("transactions");
            numtx = jsonArray.length();
            if (numtx > 0) 
            {
                nexttoken = jsonObj.get("next-token").toString();
                JSONObject jsonObjAll = new JSONObject(response.toString());
                String Str = new String(jsonObjAll.toString(2));
/*                 valore_finale = valore_finale + Str;
                System.out.println(valore_finale.length()); */
            
            
        

        //JSONObject jsonObj1 = new JSONObject(valore_finale.toString());
        if (jsonObjAll.has("next-token")) {
            for (int i = 0; i < jsonObjAll.getJSONArray("transactions").length(); i++) {
                if (single_Transaction.controlla(jsonObjAll.getJSONArray("transactions").getJSONObject(i).getJSONObject(savings.get(0)), saving_singles.get(Integer.valueOf(val1))) == false)
                {
                    continue;
                }
                JSONObject dato = new JSONObject();
                for (int e = 0; e < duplicates.size(); e++) {
                    String key = "";
                    if (duplicates_value.get(e).equals("amount")) {
                        
                        int key1 = jsonObjAll.getJSONArray("transactions")
                            .getJSONObject(i)
                            .getJSONObject(savings.get(0))
                            .getInt("amount");
                        key = String.valueOf(key1);
                        
                    } else if (duplicates_value.get(e).equals("receiver")) {
                        
                        key = jsonObjAll.getJSONArray("transactions")
                            .getJSONObject(i)
                            .getJSONObject(savings.get(0))
                            .getString("receiver");
                    }
                    else if (duplicates_value.get(e).equals("round-time")) {
                        int key1 = jsonObjAll.getJSONArray("transactions")
                            .getJSONObject(i)
                            .getInt("round-time");
                        java.util.Date time = new java.util.Date((long)key1*1000);
                        String pattern = "yyyy-MM-dd HH:mm:ss.SSS";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                        String date = simpleDateFormat.format(time);
                        key = String.valueOf(date);
                        
                    }
                    else if (duplicates_value.get(e).equals("sender")) {
                         key = jsonObjAll.getJSONArray("transactions")
                            .getJSONObject(i)
                            .getString("sender");
                    }

                    else if (duplicates_value.get(e).charAt(duplicates_value.get(e).length() - 1) == '"'
                        && duplicates_value.get(e).charAt(0) == '"') 
                        {
                            key = duplicates_value.get(e).substring(1, duplicates_value.get(e).length() - 1);
                        } 
                    else 
                    {
                        throw new java.lang.RuntimeException("There is a problem with your CSV output types");
                    }
                    
                    dato.put(duplicates.get(e), key);
                    
                }
                data_check = data_check + dato.toString() + "," + "\n";
            }
        }
        }
        TimeUnit.SECONDS.sleep(2);
    }
    }
    catch (Exception e)
    {
        System.out.println(e);
    }

            if (lung == Integer.valueOf(val1)+1)
            {
                if (data_check.length() > 10)
                {
            data_check = data_check.substring(0, data_check.length() - 2) + "\n\n" + "]";
                }
                else 
                {
                    data_check = data_check + "]";
                }
            }
            else
            {
                if (data_check.length() > 10)
                {
            data_check = data_check.substring(0, data_check.length() - 2) + "," + "\n"; 
                }
            }

            if (data_check.length() > 10)
            {
            file.append(data_check);
            }
            file.close();
            if (lung == Integer.valueOf(val1)+1)
            {
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
