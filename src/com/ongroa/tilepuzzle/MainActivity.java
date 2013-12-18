package com.ongroa.tilepuzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void startTileActivity(View view) {
		int size = 3;
		int id = view.getId();
		if (id == R.id.button2) size = 2;
		if (id == R.id.button3) size = 3;
		if (id == R.id.button4) size = 4;
		if (id == R.id.button5) size = 5;
		if (id == R.id.button6) size = 6;
		Intent intent = new Intent(this, TileActivity.class);
		intent.putExtra("SIZE", size);
		startActivity(intent);
	}

}
