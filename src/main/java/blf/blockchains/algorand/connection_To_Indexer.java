package blf.blockchains.algorand;

import com.algorand.algosdk.v2.client.common.IndexerClient;
import com.algorand.algosdk.v2.client.common.Client;

public class connection_To_Indexer {
    public Client connectToNetwork(String address, String pass) {
        final String IDX_API_ADDR = address;
        final int IDX_PORT = 443;
        final String IDX_API_TOKEN_KEY = "X-API-Key";
        final String IDX_API_TOKEN = pass;
        IndexerClient client = new IndexerClient(IDX_API_ADDR, IDX_PORT, IDX_API_TOKEN, IDX_API_TOKEN_KEY);
        return client;
    }
}
