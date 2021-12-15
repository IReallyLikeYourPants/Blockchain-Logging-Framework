package blf.blockchains.algorand;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class check_Manifest {
    public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException, IOException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException

    //public static void startAlgo(String bcqlFile, Boolean type) throws InterruptedException, NoSuchAlgorithmException, IOException 
    {
        File mani = new File("./target/myManifest.bcql");
        Scanner mani2 = new Scanner(mani);
        boolean blockchain_set = false;
        String output_path = "";
        String address = "";
        String token = "";
        int block_start = -1;
        int block_end = -1;
        String address_set = "";
        String options_set = "";
        boolean output_type1 = false;
        boolean output_type2 = false;
        String csv_options = "";
        String single_transaction = "";
        List<String> duplicates = new ArrayList<String>();
        List<String> duplicates_value = new ArrayList<String>();
        List<String> saving_address = new ArrayList<String>();
        List<String> saving_options = new ArrayList<String>();
        List<String> saving_output = new ArrayList<String>();
        List<String> saving_singles = new ArrayList<String>();

        while (mani2.hasNextLine()) {
            String data = mani2.nextLine();
            if (data.contains("SET")
                && data.contains("BLOCKCHAIN")
                && (data.indexOf("//") > data.indexOf("SET") || data.indexOf("//") == -1)
                && (data.contains("ALGORAND") || data.contains("algorand") || data.contains("Algorand"))) {
                blockchain_set = true;
            }
            if (data.contains("SET")
                && data.contains("OUTPUT")
                && data.contains("FOLDER")
                && (data.indexOf("//") > data.indexOf("SET") || data.indexOf("//") == -1)) {
                String[] result1 = data.split("\"");
                if (result1.length < 2) {
                    throw new java.lang.RuntimeException("Error in Output Folder. You need to write it in the same line!");
                }
                output_path = result1[1];
            }
            if (data.contains("SET") && data.contains("CONNECTION")) {
                String[] result = data.split("\"");
                if ((data.indexOf("//") < data.indexOf(result[1]))) {
                    continue;
                }
                if (result.length != 5) {
                    throw new java.lang.RuntimeException("You need to write the address and the token in the same line!");
                }
                address = result[1];
                token = result[3];
            }
            if (data.contains("BLOCKS") && (data.indexOf("//") > data.indexOf("BLOCKS") || data.indexOf("//") == -1)) {
                int number = 0;
                while (data.charAt(number) != '(') {
                    number += 1;
                }
                int beginning = number + 1;
                while (data.charAt(number) != ')') {
                    number += 1;
                }
                if (beginning != number)
                {
                block_start = Integer.parseInt(data.substring(beginning, number).replaceAll("\\s", ""));
                }
                else
                {
                    block_start = -100;
                }
                while (data.charAt(number) != '(') {
                    number += 1;
                }
                int ini = number + 1;
                while (data.charAt(number) != ')') {
                    number += 1;
                }
                if (ini != number)
                {
                block_end = Integer.parseInt(data.substring(ini, number).replaceAll("\\s", ""));
                }
                else
                {
                    block_end = -100;
                }
            }
            if (data.contains("LOG")
                && data.contains("ENTRIES")
                && (data.indexOf("//") > data.indexOf("LOG") || data.indexOf("//") == -1)) {
                address_set = "";
                options_set = "";
                int number1 = 0;
                while (data.charAt(number1) != '(') {
                    number1 += 1;
                }
                int iniziamo1 = number1 + 1;
                while (data.charAt(number1) != ')') {
                    number1 += 1;
                }
                address_set = data.substring(iniziamo1, number1).replaceAll("\\s", "");
                saving_address.add(address_set);
                while (data.charAt(number1) != '(') {
                    number1 += 1;
                }
                int beginning1 = number1 + 1;
                while (data.charAt(number1) != ')') {
                    number1 += 1;
                }
                options_set = data.substring(beginning1, number1);
                saving_options.add(options_set);
            }
            if (data.contains("EMIT")
                && data.contains("CSV")
                && data.contains("ROW")
                && (data.indexOf("//") > data.indexOf("EMIT") || data.indexOf("//") == -1)) {
                output_type1 = true;
                csv_options="";
                int next_csv = 0;
                while (data.charAt(next_csv) != '(') {
                    next_csv += 1;
                }
                int start_csv = next_csv;
                while (data.charAt(next_csv) != ')') {
                    next_csv += 1;
                }
                csv_options = data.substring(start_csv + 1, next_csv);
                saving_output.add(csv_options);
            }

            if (data.contains("TRANSACTION")
            && data.contains("FILTERS")
            && (data.indexOf("//") > data.indexOf("TRANSACTION") || data.indexOf("//") == -1)) {
                int next_single = 0;
                while (data.charAt(next_single) != '(') {
                    next_single += 1;
                }
                int start_single = next_single;
                while (data.charAt(next_single) != ')') {
                    next_single += 1;
                }
                single_transaction = data.substring(start_single + 1, next_single);
                saving_singles.add(single_transaction);
            }

            if (data.contains("EMIT")
                && data.contains("XES")
                && data.contains("EVENT")
                && (data.indexOf("//") > data.indexOf("EMIT") || data.indexOf("//") == -1)) {
                output_type2 = true;
                csv_options="";
                System.out.println(
                    "INFO: XES output is not yet implemented. I am going to produce a CSV file. You can transform it to XES with for example Disco."
                );
                int next_csv = 0;
                while (data.charAt(next_csv) != '(') {
                    next_csv += 1;
                }
                int start_csv = next_csv;
                while (data.charAt(next_csv) != ')') {
                    next_csv += 1;
                }
                csv_options = data.substring(start_csv + 1, next_csv);
                saving_output.add(csv_options);
            }
        }
        if (!csv_options.contains("CaseID ") || !csv_options.contains("Activity ")) {
            throw new java.lang.RuntimeException("Your CSV file need at least CaseID and Activity!");
        }
        if (blockchain_set == true
            && ((output_type1 && !output_type2) || (!output_type1 && output_type2))
            && output_path != ""
            && address != ""
            && token != ""
            && block_start != -1
            && block_end != -1
            && address_set != ""
            && csv_options != ""
            && saving_address.size() == saving_options.size() &&  saving_options.size() == saving_output.size() && saving_singles.size() == saving_output.size()
            ) {
            System.out.println("INFO: The validation did not find errors.");
            if (true == true) {
            //if (type == true) {
                int lung = saving_output.size();
                int times = lung - 2;
                if (lung > 2)
                {
                    
                    for (int i =0 ; i < times; i++)
                    {
                    File da = new File("./src/main/java/blf/blockchains/algorand/transactions_Finder1.java");
                    File a = new File("./src/main/java/blf/blockchains/algorand/transactions_Finder" + Integer.valueOf(i+2) + ".java");
                    Files.copy(da.toPath() , a.toPath());
                    File fileToBeModified = new File("src/main/java/blf/blockchains/algorand/transactions_Finder" +Integer.valueOf(i+2)+ ".java");
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
                newContent = oldContent.replaceAll("public class transactions_Finder1", "public class transactions_Finder"+ Integer.valueOf(i+2));
                writer = new FileWriter(fileToBeModified);
                writer.write(newContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                reader.close();
                writer.close();
            } catch (IOException e) {
            }
                    }
                }
                for (int v = 0 ; v < saving_output.size() ; v ++ )
            {
                duplicates.clear();
                duplicates_value.clear();
                
                String[] result = saving_output.get(v).split(",");
                for (int i = 0; i < result.length; i++) {
                    int val = result[i].trim().indexOf(' ');
                    String word = result[i].trim().substring(0, val).trim();
                    String word1 = result[i].trim().substring(val).trim();
                    if (duplicates.contains(word)) {
                        throw new java.lang.RuntimeException("There are duplicates in your CSV file output!");
                    } else {
                        duplicates.add(word);
                        duplicates_value.add(word1);
                    }
                }

                if (saving_options.get(v) != "") {
                    List<String> savings = apply_Filters.implement_filters(saving_options.get(v), String.valueOf(v), saving_address.get(v), block_start, block_end);
                    Class<?> clazz = Class.forName("blf.blockchains.algorand.transactions_Finder" + String.valueOf(v));
                    Method method = clazz.getMethod("finding", String.class, String.class,
                    String.class, String.class, List.class,
                    List.class, List.class, String.class, int.class, List.class);
                    method.invoke(null, address, token, saving_address.get(v),
                    output_path, duplicates, duplicates_value, savings, String.valueOf(v), lung, saving_singles);
/*                      transactions_Finder.finding(
                        address,
                        token,
                        block_start,
                        block_end,
                        saving_address.get(v),
                        output_path,
                        duplicates,
                        duplicates_value,
                        savings
                    );  */
                    reset_Finder.resetting(savings, String.valueOf(v), block_start, block_end);
                }
                else
                {
                    throw new java.lang.RuntimeException("There is a problem with your manifest file!");
                }
                
            
            }
            for (int i =0 ; i < times; i++)
                    {
            Path fileToDeletePath = Paths.get("./src/main/java/blf/blockchains/algorand/transactions_Finder" + Integer.valueOf(i+2) + ".java");
            Files.deleteIfExists(fileToDeletePath);
                    }
        }
        } else {
            throw new java.lang.RuntimeException("There is a problem with your manifest file!");
        }
        mani2.close();
    }
}
