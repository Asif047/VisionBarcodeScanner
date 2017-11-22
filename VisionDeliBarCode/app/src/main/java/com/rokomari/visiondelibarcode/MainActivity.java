package com.rokomari.visiondelibarcode;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

public class MainActivity extends AppCompatActivity {

    private TextView barcodeResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(checkPermission())
        {
            Toast.makeText(MainActivity.this,"Permission is granted!",Toast.LENGTH_LONG).show();
        }
        else
        {

            buildDialog(MainActivity.this).show();

            // requestPermission();
        }

        barcodeResult= (TextView) findViewById(R.id.barcode_result);
    }

    private boolean checkPermission()
    {
        return (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED);
    }


    public void scanBarcode(View v)
    {
        Intent intent=new Intent(this,ScanBarcodeActivity.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==0)
        {
            if(resultCode== CommonStatusCodes.SUCCESS)
            {
                if(data!=null)
                {
                    Barcode barcode=data.getParcelableExtra("barcode");

                    barcodeResult.setText("Barcode value: "+barcode.displayValue);
                }
                else
                {
                    barcodeResult.setText("No barcode found");
                }
            }
        }

        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }


    }




    public android.app.AlertDialog.Builder buildDialog(Context c)
    {
        android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(c);
        builder.setTitle("Permission Required");
        builder.setMessage("You need to allow camera permission.Press ok to exit");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);

            }
        });

        return builder;
    }


}
