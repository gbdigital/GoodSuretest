package g2tech.goodsure;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Grant on 10/05/2015.
 * This class contains two methods to achieve the same result - establishing a connection to a HTTP
 * server and processing a JSON formatted response from that server.  Note that the server contains
 * the PHP script to handle the interrogation of the database and provide the JSON response.
 *
 * The first method (requires no parameters) uses the deprecated (since API 22) HttpEntity and
 * httpResponse apache libraries.  The second method (requires a URL file location as a parameter)
 * uses the superceeding library.
 */
public class ApiConnector {

    public JSONArray GetTagDetails() {

        //URL for getting data from online data store
        String url = "http://www.goodsure.byethost4.com/test.php";

        //Get HttpResponse Object from URL
        //Get HttpEntity from HttpResponse object
        HttpEntity httpEntity = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            httpEntity = httpResponse.getEntity();
        } catch (ClientProtocolException e) {

            //Signals error in http protocol
            e.printStackTrace();

            //Log Errors here
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Convert HttpEntity into JSON Array
        JSONArray jsonArray = null;
        if (httpEntity != null) {
            try {
                String entityResponse = EntityUtils.toString(httpEntity);
                Log.e("Entity Reponse: ", entityResponse);
                jsonArray = new JSONArray(entityResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }
    public JSONArray GetTagDetails(String phpFileAddress){
        JSONArray jsonArray = null;
        try {
            URL url = new URL(phpFileAddress);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();

            InputStream is = conn.getInputStream();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            jsonArray = new JSONArray(responseStrBuilder.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }
}
