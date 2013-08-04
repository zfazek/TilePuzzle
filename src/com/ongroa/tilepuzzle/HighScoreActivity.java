package com.ongroa.tilepuzzle;

import java.util.List;

import com.ongroa.tilepuzzle.R;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class HighScoreActivity extends Activity {
	int size = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		size = getIntent().getIntExtra("SIZE", 3);
		setContentView(R.layout.activity_highscore);
		printHighScores();
	}

	private void printHighScores() {
		GridView gridView = (GridView) findViewById(R.id.gridViewHighScore);
		HighScore highscore = new HighScore(getBaseContext());
		Log.d("Reading: ", "Reading all high scores...");
		List<Result> results = highscore.getAllResults(size);
		String[] array = new String[(results.size() + 1) * 3];
		int n = 1;
		int i = 0;
		array[i++] = "";
		array[i++] = "Time:";
		array[i++] = "Move:";
		if (results.size() > 0) {
			for (Result r : results) {
				String log = "Size: " + r.getSize() + " ,Time: " + 
						r.getTime() + " ,Move: " + r.getMove();
				Log.d("Name: ", log);
				array[i++] = "" + n++ + ".";
				//				array[i++] = "" + r.getSize();
				array[i++] = r.getTime();
				array[i++] = "" + r.getMove();
			}
		}
		ArrayAdapter<String> adapter = new MyAdapter<String>(this,
				android.R.layout.simple_list_item_1, array);
		gridView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.highscore_reset_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.highscore_reset:
			resetHighScore();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void resetHighScore() {
		HighScore highscore = new HighScore(getBaseContext());
		highscore.deleteCurrentSize(size);
		printHighScores();
	}

}
class MyAdapter<String> extends ArrayAdapter<String> {

	public MyAdapter(Context context, int resource, String[] objects) {
		super(context, resource, objects);
	}

	@Override  
	public View getView(int position, View view, ViewGroup viewGroup)
	{
		View v = super.getView(position, view, viewGroup);
		((TextView) v).setTypeface(null, Typeface.ITALIC);
		((TextView) v).setTextSize(15); 
		return v;
	}
}

