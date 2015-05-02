package citris.stockup.grocerylist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import citris.stockup.R;
import citris.stockup.adapters.ListListAdapter;
import citris.stockup.groceries.Grocery;
import citris.stockup.groceries.GroceryList;

/**
 * Created by Panic on 4/25/2015.
 */
public class EditListActivity extends AddGroceryActivity {
    private TextView insertCreator;
    private EditText insertListName;
    private EditText insertShare;
    private Button editButton;
    private Button removeButton;
    private Button shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_list);
        setViews();
        getActionBar().setTitle("Edit List");
    }

    //TODO add edit list
    private void editList(int pos) {
        Toast.makeText(getApplicationContext(), "This button doesn't do anything... yet!", Toast.LENGTH_SHORT).show();
    }

    private void setViews() {
        final GroceryList gl = getIntent().getParcelableExtra("tmpList");
        final int pos = getIntent().getIntExtra("position", 0);

        insertCreator = (TextView)findViewById(R.id.insert_list_creator);
        insertListName = (EditText)findViewById(R.id.insert_list_name);
        insertShare = (EditText)findViewById(R.id.insert_share);
        editButton = (Button)findViewById(R.id.edit_button);
        shareButton = (Button)findViewById(R.id.share_button);
        removeButton = (Button)findViewById(R.id.remove_button);

        insertListName.setText(gl.getName());
        insertCreator.setText(gl.getCreator());

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editList(pos);
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareList(pos);
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGroceryListApplication().removeList(pos);
                Toast.makeText(getApplicationContext(), "List removed.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void shareList(int pos) {
        String sharedUser = insertShare.getText().toString();
        Toast.makeText(getApplicationContext(), "List shared with " + sharedUser + ".", Toast.LENGTH_SHORT).show();
        getGroceryListApplication().shareList(pos, sharedUser);
    }
}