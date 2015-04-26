package citris.stockup.scanner;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import citris.stockup.R;
import citris.stockup.scanner.zxing.ScannerActivity;
import citris.stockup.scanner.zxing.ScannerFragmentActivity;
import citris.stockup.scanner.zxing.SimpleScannerActivity;
import citris.stockup.scanner.zxing.SimpleScannerFragmentActivity;

/**
 * Created by Panic on 4/24/2015.
 */

public class CameraActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_camera);
    }

    public void launchSimpleActivity(View v) {
        Intent intent = new Intent(this, SimpleScannerActivity.class);
        startActivity(intent);
    }

    public void launchSimpleFragmentActivity(View v) {
        Intent intent = new Intent(this, SimpleScannerFragmentActivity.class);
        startActivity(intent);
    }

    public void launchActivity(View v) {
        Intent intent = new Intent(this, ScannerActivity.class);
        startActivity(intent);
    }

    public void launchFragmentActivity(View v) {
        Intent intent = new Intent(this, ScannerFragmentActivity.class);
        startActivity(intent);
    }
}