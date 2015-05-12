package g2tech.goodsure;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

  //public class MainActivity extends ActionBarActivity {
  public class MainActivity extends AppCompatActivity {

    public static final String TAG = "gbsDevMessage";
    private TextView fetchedvalue; //the label of the layout element for returning the JSON string data
    public static final String MIME_TEXT_PLAIN = "text/plain"; //required for handling nfc

    private TextView mTextView;
    private NfcAdapter mNfcAdapter;

    //Button btn = (Button) findViewById(R.id.scan_tag);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

      @Override
      protected void onNewIntent(Intent intent) {
          if (intent.getAction().equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {
              ((TextView)findViewById(R.id.rfid)).setText(
                      "NFC Tag\n" +
                              ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)));
          }
      }

      private String ByteArrayToHexString(byte [] inarray) {
          int i, j, in;
          String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
          String out = "";

          for (j = 0; j < inarray.length; ++j) {
              in = (int) inarray[j] & 0xff;
              i = (in >> 4) & 0x0f;
              out += hex[i];
              i = in & 0x0f;
              out += hex[i];
          }
          return out;
      }
      private Integer htod(String hexstr){
          Integer dec = Integer.parseInt(hexstr, 16);
          return dec;
      }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState");
    }

    public void scanItemOnClick(View v){
    //Do something when the button is clicked
        /*NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter!=null && nfcAdapter.isEnabled()){
            Toast.makeText(this, "NFC available",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"NFC not available",Toast.LENGTH_LONG).show();
        }
        handleIntent(getIntent()); */
        Intent intent = new Intent(v.getContext(),TagScan.class);
        startActivityForResult(intent,0);
  //      Button button = (Button) findViewById(R.id.scan_tag);
   //     button.setOnClickListener(new View.OnClickListener() {
  //          @Override
  //          public void onClick(View v) {
  //              Intent intent = new Intent(v.getContext(),TagScan.class);
  //              startActivityForResult(intent,0);
  //          }
  //      });
        //Button button = (Button) v;
        //((Button) v).setText("Scanning");
        //Intent intent = new Intent(v.getContext(),TagScan.class);
        //startActivityForResult(intent, 0);
    }


    private void handleIntent(Intent intent) {
     /* TODO: handle Intent */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void refbuttonOnClick(View v) {
          //Do something when the button is clicked
        this.fetchedvalue = (TextView) this.findViewById(R.id.fetchedvalue);
        new GetDetails().execute(new ApiConnector());
      }

    /* Decodes the JSONArray object and separates out the JSON string to the various database field
     * elements and their values
     */
    public void setTextToTextView(JSONArray jsonArray){
        String s = "";
        for(int i=0; i<jsonArray.length();i++){
            JSONObject json = null;
            try{
                json = jsonArray.getJSONObject(i);
                s = s+
                        "TagID: " +json.getInt("TagID")+"\n"+
                        "Refence Number: " +json.getInt("RefNum")+"\n\n";
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        this.fetchedvalue.setText(s);
    }

    //Retrieves the JSON response from the server using the Api connector class
    private class GetDetails extends AsyncTask<ApiConnector,Long,JSONArray>{
        //Executed on background thread
        @Override
        protected JSONArray doInBackground(ApiConnector... params) {
            return params[0].GetTagDetails();
        }
        //Executed in foreground thread
        @Override
        protected void onPostExecute(JSONArray jsonArray){

            setTextToTextView(jsonArray);
        }
    }

      //Required for NFC functionality
      public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
          final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
          intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

          final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

          IntentFilter[] filters = new IntentFilter[1];
          String[][] techList = new String[][]{};

          // Notice that this is the same filter as in our manifest.
          filters[0] = new IntentFilter();
          filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
          filters[0].addCategory(Intent.CATEGORY_DEFAULT);
          try {
              filters[0].addDataType(MIME_TEXT_PLAIN);
          } catch (IntentFilter.MalformedMimeTypeException e) {
              throw new RuntimeException("Check your mime type.");
          }

          adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
      }

      /**
       * @param activity The corresponding {@link BaseActivity} requesting to stop the foreground dispatch.
       * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
       */
      public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
          adapter.disableForegroundDispatch(activity);
      }
}
