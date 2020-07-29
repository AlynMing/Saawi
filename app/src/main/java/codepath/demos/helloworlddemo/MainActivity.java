package codepath.demos.helloworlddemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends Activity {

	public static final String KEY_ITEM_TEXT = "item_text";
	public static final String KEY_ITEM_POSITION = "item_position";

	List <String> tasks;

	Button btnAdd;
	EditText etItem;
	RecyclerView rvItems;
	TasksAdapter tasksAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnAdd = findViewById(R.id.btnAdd);
		etItem = findViewById(R.id.etItem);
		rvItems = findViewById(R.id.rvitems);

		loadTasks();
		//Implementing a working Recyclerview. Mostly working on the RecyclerView.Adapter
		//Viewholder: holds onto the text view and populate data from our model into the view.

		TasksAdapter.OnClickListener onClickListener = new TasksAdapter.OnClickListener() {
			@Override
			public void onItemClicked(int position) {
				// Create new activity using intent (used to open up anything from urls to screens)
				Intent i = new Intent(MainActivity.this, EditActivity.class);
				// edit data
				i.putExtra(KEY_ITEM_TEXT, tasks.get(position));
				i.putExtra(KEY_ITEM_POSITION, position);
				//Display editActivity
				startActivityForResult(i, 10);
			}
		};

		TasksAdapter.OnLongClickListener onLongClickListener = new TasksAdapter.OnLongClickListener() {
			@Override
			public void onItemLongClicked(int position) {
				// We have the exact the position user long pressed so delete the item
				tasks.remove(position);
				// Notify the adapter at which position we deleted the item
				tasksAdapter.notifyItemRemoved(position);
				Toast.makeText(getApplicationContext(), "Task go byebye", Toast.LENGTH_SHORT).show();
				saveTasks();
			}
		};
		tasksAdapter = new TasksAdapter(tasks, onLongClickListener, onClickListener);
		rvItems.setAdapter(tasksAdapter);
		rvItems.setLayoutManager(new LinearLayoutManager(this));

		btnAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String todoItem = etItem.getText().toString();
				// Add item to model
				tasks.add(todoItem);
				// Let adapter know that tasks been inserted at the end of the lsit
				tasksAdapter.notifyItemInserted(tasks.size() - 1);
				etItem.setText(""); // Clear the insert box
				Toast.makeText(getApplicationContext(), "Your tasks been added!", Toast.LENGTH_SHORT).show();
				saveTasks();
			}
		});
	}

	// Handle edit activity result
	@SuppressLint("ShowToast")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// check if requestcode mathces up
		if(resultCode == RESULT_OK && requestCode == 10){
			// Get updated text Value
			String itemText = data.getStringExtra(KEY_ITEM_TEXT);
			// Extract exact position of the edited item
			int position = data.getExtras().getInt(KEY_ITEM_POSITION);

			// update model at position with new itemtext
			tasks.set(position, itemText);
			//notify the adapter of a change
			tasksAdapter.notifyItemChanged(position);
			// persist the changes
			saveTasks();
			Toast.makeText(getApplicationContext(), "Tasks been updated!", Toast.LENGTH_SHORT).show();
		} else {
			Log.w("MainActivity", "What are you calling into onAcitivity Result");
		}
	}

	/*
	Below is the persistence layer that stores the tasks and updates them on device.

	getDataFile - generates a new file with the name tasks.txt
	 loadTasks - Load tasks by reading the txt file
	  - Writes tasks to tasks.txt
	 */

	private File getDataFile() {
		return new File(getFilesDir(), "tasks.txt");
	}

	private void loadTasks(){
		try {
			tasks = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
		} catch (IOException e) {
			Log.e("MainActivity", "Error reading items", e);
			tasks = new ArrayList<>();
		}
	}

	private void saveTasks(){
		try {
			FileUtils.writeLines(getDataFile(), tasks);
		} catch (IOException e) {
			Log.e("MainActivity", "Error saving items", e);
		}
	}

}
