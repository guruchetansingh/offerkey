package com.example.payu.offerkey;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    //Mandatory feilds for hash generation

    private  String key = "gtKFFx"; //for testing,you need to cahnge key with your live key
    private  String transaction_Id;
    private  String amount = "10.00 ";
    private  String product_info = "guru123";
    private  String f_Name = "guru";
    private  String email = "guru@guru.com";
    private  String s_Url = "https://payu.herokuapp.com/success";
    private  String f_Url = "https://payu.herokuapp.com/failure";
    private  String user_credentials = key + ":" + "guru@guru.com";
    private  String udf1 = "";
    private  String udf2 = "";
    private  String udf3 = "";
    private  String udf4 = "";
    private  String udf5 = "";
    //madatory
    // both offer keys will expire on 31 Dec 2015
  private String offer_key ="guru_offer@7260";//for all cards catagory for testing
//    private String offer_key ="guru_offer_visa@7262";//for only visa cards catagory for testing

    private String cardBin = " ";


    //for offer key
    private String catagory="CC";
    private String bankCode="CC";
    private String phone = "1234567890";
    //mandatory
    private String cardNumber="5123456789012346";//card catagory (visa/master...etc) will be get by card bin(first 4 or 6 digits)
    private String nameOnCard="Guru";
    private String command="check_offer_status";


    private String check_offer_status_hash;





    private ProgressBar spinner;
    private TextView textViewPayu;
    private String postData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner=(ProgressBar)findViewById(R.id.progressBar);
        textViewPayu=(TextView)findViewById(R.id.pay_text_view);
//       spinner.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onResume() {
        super.onResume();

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


    public void checkOfferKey(View v)
    {
        generateHashFromServer();
    }

    public void generateHashFromServer() {

        // lets create the post params

        StringBuffer postParamsBuffer = new StringBuffer();
        postParamsBuffer.append(concatParams("key",key));
        postParamsBuffer.append(concatParams("amount",amount));
        postParamsBuffer.append(concatParams("txnid", transaction_Id=System.currentTimeMillis()+""));
        postParamsBuffer.append(concatParams("email",email));
        postParamsBuffer.append(concatParams("productinfo",product_info));
        postParamsBuffer.append(concatParams("firstname",f_Name));
        postParamsBuffer.append(concatParams("udf1",udf1));
        postParamsBuffer.append(concatParams("udf2",udf2));
        postParamsBuffer.append(concatParams("udf3",udf3));
        postParamsBuffer.append(concatParams("udf4",udf4));
        postParamsBuffer.append(concatParams("udf5",udf5));
        postParamsBuffer.append(concatParams("user_credentials",user_credentials));

        // for offer_key(optional)
        postParamsBuffer.append(concatParams("offer_key", offer_key));
        // for check_isDomestic(oprional)
        postParamsBuffer.append(concatParams("card_bin",cardBin));

        String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ? postParamsBuffer.substring(0, postParamsBuffer.length() - 1).toString() : postParamsBuffer.toString();
        // make api call
        GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
        getHashesFromServerTask.execute(postParams);

    }

    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }



    /**
     *
     * This Asyntask will hit API to get all hashes from server.
     *
     * */
    class GetHashesFromServerTask extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            spinner.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... postParams) {

            try {


                URL url = new URL("https://payu.herokuapp.com/get_hash");//replace this url with your server url for hahs generation

                // get the payuConfig first
                String postParam = postParams[0];

                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                JSONObject response = new JSONObject(responseStringBuffer.toString());
                System.out.println("response = " +response.toString());
                return  response.getString("check_offer_status_hash");


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String reponse) {
            System.out.println("offer_key_hash" +reponse);
            check_offer_status_hash=reponse;
            spinner.setVisibility(View.GONE);

            postData="&key="+key//mandatory
                    +"&var1="+offer_key//madatory
                    +"&var2="+amount//can be empty
                    +"&var3="+catagory//can be empty
                    +"&var4="+bankCode//can be empty
                    +"&var5="+cardNumber//mandatory
                    +"&var6="+nameOnCard//can be empty
                    +"&var7="+phone//can be empty
                    +"&var8="+email//can be empty
                    +"&command="+command
                    +"&hash="+check_offer_status_hash;
            new  GetOfferTask().execute(postData);

        }

    }



    class GetOfferTask extends AsyncTask<String, String, String> {

        private String apiurl="https://mobiletest.payu.in/merchant/postservice?form=2"; //for testing
//        private String apiurl="https://info.payu.in/merchant/postservice.php?form=2"; //fro production

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            spinner.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... postParams) {

            try {


                URL url = new URL(apiurl);

                // get the payuConfig first
                String postParam = postParams[0];

                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                JSONObject response = new JSONObject(responseStringBuffer.toString());
                return  response.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String reponse) {
            System.out.println("reponse" + reponse);
            spinner.setVisibility(View.GONE);
            textViewPayu.setText(reponse);

        }

    }

}

