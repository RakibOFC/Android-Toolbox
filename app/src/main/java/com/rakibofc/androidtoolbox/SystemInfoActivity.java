package com.rakibofc.androidtoolbox;

import static com.rakibofc.androidtoolbox.MainActivity.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class SystemInfoActivity extends AppCompatActivity {

    public AlertDialog.Builder alertBuilder;
    public ListView listViewSystemInfo;
    public ActivityManager.MemoryInfo memoryInfo;
    public ActivityManager activityManager;
    public double availMem;
    public double totalMem;
    public final static double BYTE_TO_GIGABYTE = 1073741824;
    public String ramInfo;
    public TextView textViewPIP;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.system_info_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.pictureInPicture:
                goPictureInPictureMode();
                break;

            case R.id.theme:
                changeThemeMode();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void goPictureInPictureMode() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            enterPictureInPictureMode();
        } else {
            Toast.makeText(getApplicationContext(), "This device not support PiP mode. Use Android 8+ (API Level 26+)", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);

        if (isInPictureInPictureMode) {

            listViewSystemInfo.setVisibility(View.INVISIBLE);
            textViewPIP.setVisibility(View.VISIBLE);
            Objects.requireNonNull(getSupportActionBar()).hide();

            ramInfo();

        } else {

            Objects.requireNonNull(getSupportActionBar()).show();
            textViewPIP.setVisibility(View.INVISIBLE);
            listViewSystemInfo.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    public void ramInfo() {
        memoryInfo = new ActivityManager.MemoryInfo();
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);

        availMem = (double) memoryInfo.availMem / BYTE_TO_GIGABYTE;
        totalMem = (double) memoryInfo.totalMem / BYTE_TO_GIGABYTE;
        textViewPIP.setText(String.format("%.2f", (totalMem - availMem)) + "GB/" + (int) Math.ceil(totalMem) + "GB");

        // Check every second for RAM
        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {

                handler.postDelayed(this, 500);

                memoryInfo = new ActivityManager.MemoryInfo();
                activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                activityManager.getMemoryInfo(memoryInfo);

                availMem = (double) memoryInfo.availMem / BYTE_TO_GIGABYTE;
                textViewPIP.setText(String.format("%.2f", (totalMem - availMem)) + "GB/" + (int) Math.ceil(totalMem) + "GB");
            }
        };
        handler.post(runnable);
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_info);
        this.setTitle("System Info");

        textViewPIP = findViewById(R.id.textViewPIP);

        // Initialize SystemInfo List
        alertBuilder = new AlertDialog.Builder(this);
        listViewSystemInfo = findViewById(R.id.listViewSystemInfo);
        ArrayList<SystemInfo> systemInfoData = new ArrayList<>();

        // Android Version
        SystemInfo androidVersion = new SystemInfo("Android Version", Build.VERSION.RELEASE);
        systemInfoData.add(androidVersion);

        // API Level
        SystemInfo apiLevel = new SystemInfo("API Level", String.valueOf(Build.VERSION.SDK_INT));
        systemInfoData.add(apiLevel);

        // RAM used info
        memoryInfo = new ActivityManager.MemoryInfo();
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);
        availMem = (double) memoryInfo.availMem / BYTE_TO_GIGABYTE;
        totalMem = (double) memoryInfo.totalMem / BYTE_TO_GIGABYTE;
        ramInfo = String.format("%.2f", (totalMem - availMem)) + "GB used out of " + (int) Math.ceil(totalMem) + "GB";
        SystemInfo ram = new SystemInfo("RAM Used", ramInfo);
        systemInfoData.add(ram);

        // Storage Info
        double freeSpace = (double) Environment.getDataDirectory().getFreeSpace() / BYTE_TO_GIGABYTE;
        // (totalSpace - freeSpace) + "GB used out of " + totalSpace + "GB"
        SystemInfo storageUsed = new SystemInfo("Storage Info", "Free space " + String.format("%.2f", freeSpace) + "GB");
        systemInfoData.add(storageUsed);

        // Available Processor
        SystemInfo availableProcessor = new SystemInfo("Available Processor (Core)", String.valueOf(Runtime.getRuntime().availableProcessors()));
        systemInfoData.add(availableProcessor);

        // Board
        SystemInfo board = new SystemInfo("Board", Build.BOARD);
        systemInfoData.add(board);

        // Boot Loader
        SystemInfo bootLoader = new SystemInfo("Boot Loader", Build.BOOTLOADER);
        systemInfoData.add(bootLoader);

        // Brand
        SystemInfo brand = new SystemInfo("Brand", Build.BRAND);
        systemInfoData.add(brand);

        // Build ID
        SystemInfo buildId = new SystemInfo("Build ID", Build.ID);
        systemInfoData.add(buildId);

        // Build Time
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        Date date = new Date(Build.TIME);
        String buildDate = "" + simpleDateFormat.format(date);
        SystemInfo buildTime = new SystemInfo("Build Time", buildDate);
        systemInfoData.add(buildTime);

        // Build Type
        SystemInfo buildType = new SystemInfo("Build Type", Build.TYPE);
        systemInfoData.add(buildType);

        // CPU ABI
        SystemInfo cpuAbi = new SystemInfo("CPU_ABI", Build.CPU_ABI);
        systemInfoData.add(cpuAbi);

        // CPU ABI2
        SystemInfo cpuAbi2 = new SystemInfo("CPU_ABI2", Build.CPU_ABI2);
        systemInfoData.add(cpuAbi2);

        // Device Design
        SystemInfo deviceDesign = new SystemInfo("Device Design", Build.DEVICE);
        systemInfoData.add(deviceDesign);

        // Display
        SystemInfo display = new SystemInfo("Display", Build.DISPLAY);
        systemInfoData.add(display);

        // Fingerprint
        SystemInfo fingerprint = new SystemInfo("Fingerprint", Build.FINGERPRINT);
        systemInfoData.add(fingerprint);

        // Hardware
        SystemInfo hardware = new SystemInfo("Hardware", Build.HARDWARE);
        systemInfoData.add(hardware);

        // Host
        SystemInfo host = new SystemInfo("Host", Build.HOST);
        systemInfoData.add(host);

        // Manufacturer
        SystemInfo manufacture = new SystemInfo("Manufacture", Build.MANUFACTURER);
        systemInfoData.add(manufacture);

        // Model
        SystemInfo model = new SystemInfo("Model", Build.MODEL);
        systemInfoData.add(model);

        // Product Name
        SystemInfo productName = new SystemInfo("Product Name", Build.PRODUCT);
        systemInfoData.add(productName);

        // Serial Number
        SystemInfo serialNumber = new SystemInfo("Serial No.", Build.SERIAL);
        systemInfoData.add(serialNumber);

        // If Android Version is LOLLIPOP or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            // Supported 32 Bit ABIS
            SystemInfo support32Bit = new SystemInfo("Supported 32 Bit ABIS", Arrays.toString(Build.SUPPORTED_32_BIT_ABIS));
            systemInfoData.add(support32Bit);

            // Supported 64 Bit ABIS
            SystemInfo support64Bit = new SystemInfo("Supported 64 Bit ABIS", Arrays.toString(Build.SUPPORTED_64_BIT_ABIS));
            systemInfoData.add(support64Bit);

            // Supported ABIS
            SystemInfo supportBit = new SystemInfo("Supported ABIS", Arrays.toString(Build.SUPPORTED_ABIS));
            systemInfoData.add(supportBit);
        }

        // Create SystemInfoListAdapter for ListView
        SystemInfoListAdapter systemInfoListAdapter = new SystemInfoListAdapter(this, R.layout.activity_system_info_list, systemInfoData);
        listViewSystemInfo.setAdapter(systemInfoListAdapter);
    }

    private void changeThemeMode() {

        alertBuilder.setCancelable(false);
        alertBuilder.setTitle("Theme");
        alertBuilder.setSingleChoiceItems(themeMode, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (themeMode[which].equals("Dark")) {

                    from = 2;

                } else if (themeMode[which].equals("Light")) {

                    from = 1;
                }
            }
        });
        alertBuilder.setPositiveButton("Change", (dialog, which) -> {

            if (from == 2) {
                sharedPreferences.edit().putInt("checkedItem", 0).apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else if (from == 1) {
                sharedPreferences.edit().putInt("checkedItem", 1).apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        alertBuilder.setNegativeButton("Cancel", (dialog, which) -> {
            //if user select "No", just cancel this dialog and continue with app
            dialog.cancel();
        });

        AlertDialog alert = alertBuilder.create();
        alert.setOnShowListener(arg0 -> {
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(0xff138f87);
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(0xff138f87);
        });
        alert.show();
    }
}