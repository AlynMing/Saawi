package codepath.demos.helloworlddemo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText editText;
    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editText = findViewById(R.id.editText);
        btnSave = findViewById(R.id.btnSave);

        // More descriptive title
        getSupportActionBar().setTitle("Edit Task");

        editText.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));
        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Create Intent to contain the results
                Intent intent = new Intent();
                //Pass edit
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, editText.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
                // Set the result of the intent
                setResult(RESULT_OK, intent);
                // Finish the activity/close the screen back to main
                finish();
            }
        });
    }
}