package citris.stockup.grocerylist;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import citris.stockup.R;
import citris.stockup.groceries.GroceryList;

/**
 * Created by Panic on 4/25/2015.
 */

public class AddListActivity extends AddGroceryActivity {
    private TextView insertCreator;
    private EditText listName;
    private EditText insertShare;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);
        setViews();
        getActionBar().setTitle("Add List");
    }

    //TODO add edit list
    private void addList() {
        String name = listName.getText().toString();
        String shareUser = insertShare.getText().toString();

        if (!name.isEmpty()) {
            if (!shareUser.isEmpty()){
                GroceryList gl = new GroceryList(name, ParseUser.getCurrentUser().getUsername(), ParseUser.getCurrentUser().getUsername() + ", " + shareUser);
                getGroceryListApplication().addList(gl);
            } else {
                GroceryList gl = new GroceryList(name, ParseUser.getCurrentUser().getUsername(), ParseUser.getCurrentUser().getUsername());
                getGroceryListApplication().addList(gl);
            }
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "ERROR: Invalid list name.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setViews() {
        insertCreator = (TextView)findViewById(R.id.insert_list_creator);
        listName = (EditText)findViewById(R.id.insert_list_name);
        insertShare = (EditText)findViewById(R.id.insert_share);
        addButton = (Button)findViewById(R.id.add_button);

        insertCreator.setText(ParseUser.getCurrentUser().getUsername());

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addList();
            }
        });
    }
}