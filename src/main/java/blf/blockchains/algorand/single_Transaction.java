package blf.blockchains.algorand;

import org.json.JSONObject;

public class single_Transaction {
    public static boolean controlla(JSONObject singolo, String saving_singles) {
        if (saving_singles.equals("")) {
            return true;
        }
        String[] result = saving_singles.split(",");
        for (int i = 0; i < result.length; i++) {
            int val = result[i].trim().indexOf(' ');
            String word = result[i].trim().substring(0, val).trim();
            String word1 = result[i].trim().substring(val).trim();
            String key = singolo.getString(word);
            // int key = singolo.getInt(word);
            // if (key != Integer.valueOf(word1))
            if (!key.equals(word1)) {
                return false;
            }
        }
        return true;
    }

}
