package citris.stockup.scanner.zxing;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;

import citris.stockup.R;

/**
 * Created by Panic on 4/24/2015.
 */

public class SimpleScannerFragmentActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_simple_scanner_fragment);
    }
}