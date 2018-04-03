/**
 * A single page application to control you Android wifi.
 * This application is designed to a command line control over Wifi thanks to Roboframework/Appium scripts.
 * <p>
 * Turn wifi ON/OFF
 * Set AccesPoint Credentials (SSID, password)
 * Remove acces point from internal config
 * Connect / disconnect to acces point
 */



package test.ivablagac.wificommander;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String networkSSID = "WifiTest";
        String networkPasskey = "testiva2018";

        EditText eText;

        eText = findViewById(R.id.editText_SSID);
        eText.append(networkSSID);
        eText = findViewById(R.id.editText_Password);
        eText.append(networkPasskey);

        trace("Hello\nReady to connect :)");
    }

    /**
     * trace functions return to a TextEdit with id=editText_Log
     * <p>
     * The content of this TextEdit is reset at each function call.
     *
     * @param  s  the text to trace
     */
    public void trace(String s){
        Log.d("trace", s);
        EditText eText;
        eText = findViewById(R.id.editText_log);
        eText.append(s);
    }

    // isWifiEnabled()
    // int addNetwork (WifiConfiguration config)
    // boolean enableNetwork (int netId,
    //                boolean attemptConnect)
    public void onClickBtn_WifiON(View v)
    {
        try {
            WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(WIFI_SERVICE);
            if (wifiManager.setWifiEnabled(true)) {
                trace("Wifi ON\n");
            /*
            List<WifiConfiguration> wifiConfigurations = wifiManager.getConfiguredNetworks();
            for (WifiConfiguration wifiConfiguration : wifiConfigurations) {
                trace (wifiConfiguration.SSID+"\n");

            }
            */
            } else {
                trace("Can't turn ON Wifi\n");
            }
        } catch (java.lang.SecurityException e){
            trace("Application not authorised to manage Wifi\n");
        }
    }



    public void onClickBtn_WifiOFF(View v)
    {
        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(WIFI_SERVICE);
        if (wifiManager.setWifiEnabled(false)){
            trace ("Wifi OFF\n");
        } else {
            trace ("Can't turn OFF Wifi\n");
        }
    }


    public void onClickBtn_AddAP(View v)
    {
        String networkSSID = "WifiTest";
        String networkPasskey = "testiva2018";


        EditText eText;
        eText = findViewById(R.id.editText_SSID);
        networkSSID = eText.getText().toString();
        eText = findViewById(R.id.editText_Password);
        networkPasskey = eText.getText().toString();



        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = "\"" + networkSSID + "\"";
        wifiConfiguration.preSharedKey = "\"" + networkPasskey + "\"";

        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(WIFI_SERVICE);
        int res =  wifiManager.addNetwork(wifiConfiguration);
        Log.d("onClickBtn_AddAP", "# addNetwork returned " + res);
        boolean changeHappen = wifiManager.saveConfiguration();
        if (res != -1 && changeHappen) {
            Log.d("onClickBtn_AddAP", "# Change happened: " + networkSSID);
        } else {
            Log.d("onClickBtn_AddAP", "# Change NOT happened");
        }

/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Integer foundNetworkID = findNetworkIdInExistingConfig(wifiManager, SSID);
            if (foundNetworkID != null) {
                Log.i("onClickBtn_AddAP", "Removing old configuration for network " + SSID);
                wifiManager.removeNetwork(foundNetworkID);
                wifiManager.saveConfiguration();
            }
            WifiConfiguration wifiConfiguration = createAPConfiguration(SSID, networkPasskey, securityMode);
            int res = wifiManager.addNetwork(wifiConfiguration);

        } else {

        }
*/
    }

    /**
     * If the given ssid name exists in the settings, then change its password
     * to the one given here, and save
     *
     * @param ssid
     */
    private WifiConfiguration findNetworkConfigurationInExistingConfig(WifiManager wifiManager, String ssid) {
        List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
        //Log.i("Start comparing","Size "+existingConfigs.size() );
        for (WifiConfiguration existingConfig : existingConfigs) {
            //Log.i("Compare with SSID", ssid + existingConfig.SSID);
            if (existingConfig.SSID.equals(ssid)) {
                //Log.i("Cmp success with SSID", ssid + existingConfig.SSID);
                return existingConfig;
            }
        }
        return null;
    }

    private int findNetworkIdInExistingConfig(WifiManager wifiManager, String ssid) {
        List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
        //Log.i("Start comparing","Size "+existingConfigs.size() );
        for (WifiConfiguration existingConfig : existingConfigs) {
            //Log.i("Compare with SSID", ssid + existingConfig.SSID);
            if (existingConfig.SSID.equals(ssid)) {
                //Log.i("Cmp success with SSID", ssid + existingConfig.SSID);
                return existingConfig.networkId;
            }
        }
        return 0;
    }


    public void onClickBtn_DelAP(View v)
    {
        String networkSSID = "WifiTest";
        EditText eText;
        eText = findViewById(R.id.editText_SSID);
        networkSSID = eText.getText().toString();
        networkSSID = "\""+networkSSID+"\"";


        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(WIFI_SERVICE);
        List<WifiConfiguration> wifiConfigurations = wifiManager.getConfiguredNetworks();
        if (wifiConfigurations==null) {
            trace("No configured network found\n");
            return;
        }

        for (WifiConfiguration wifiConfiguration : wifiConfigurations) {
            //trace(wifiConfiguration.SSID + "\n");
            if (wifiConfiguration.SSID.equals(networkSSID)){
                Log.d("onClickBtn_DelAP", "Found " + networkSSID +  " removing\n");
                wifiManager.removeNetwork(wifiConfiguration.networkId);
                boolean changeHappen = wifiManager.saveConfiguration();
                if (changeHappen) {
                    Log.d("onClickBtn_DelAP", "# Change happen: " + networkSSID);
                } else {
                    Log.d("onClickBtn_DelAP", "# Change NOT happen");
                }
            } else {
                //trace ("keep it : "+wifiConfiguration.SSID+" !="+networkSSID+"\n");
            }
        }

    }

    public void onClickBtn_ClearAllAP(View v)
    {
        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(WIFI_SERVICE);
        List<WifiConfiguration> wifiConfigurations = wifiManager.getConfiguredNetworks();
        if (wifiConfigurations==null) {
            trace("No configured network found\n");
            return;
        }

        for (WifiConfiguration wifiConfiguration : wifiConfigurations) {
            trace(wifiConfiguration.SSID + "\n");

            boolean ret = wifiManager.removeNetwork(wifiConfiguration.networkId);
            if (ret) {
                Log.d("onClickBtn_DelAP", "# removeNetwork: ok \n ");
            } else {
                Log.d("onClickBtn_DelAP", "# removeNetwork: ko maybe : Applications are not allowed to remove networks created by other applications.\n");
            }
            /*
            boolean changeHappen = wifiManager.saveConfiguration();
            if (changeHappen) {
                Log.d("onClickBtn_DelAP", "# Change happen: " + wifiConfiguration.networkId);
            } else {
                Log.d("onClickBtn_DelAP", "# Change NOT happen");
            }
            */
        }
    }

    public void onClickBtn_Connect(View v)
    {
        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(WIFI_SERVICE);
        wifiManager.reconnect();
    }

    public void onClickBtn_Disconnect(View v)
    {
        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(WIFI_SERVICE);
        wifiManager.disconnect();
    }
}
