package com.ongroa.tilepuzzle;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

public class TileActivity extends Activity {
	DrawView drawView;
	int size = 3;
	int oldal = 100;

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		size = getIntent().getIntExtra("SIZE", 3);
		setContentView(R.layout.activity_tile);
		drawView = new DrawView(this, size);
		drawView.setBackgroundColor(Color.WHITE);
		setContentView(drawView);
		Display display = getWindowManager().getDefaultDisplay();
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			Point point = new Point();
			display.getSize(point);
			oldal = Math.min(point.x, point.y - getStatusBarHeight()) / size;
		} else {
			oldal = Math.min(display.getHeight() - getStatusBarHeight(), 
					display.getWidth()) / size;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.highscore_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.highscore:
			showHighScore();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", 
				"dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	private void showHighScore() {
		Intent intent = new Intent(this, HighScoreActivity.class);
		intent.putExtra("SIZE", size);
		startActivity(intent);
	}

	public class DrawView extends View {
		Paint paint = new Paint();
		Context context;
		Table t;
		int size = 10;

		public DrawView(Context context, int size) {
			super(context);     
			this.context = context;
			this.size = size;
			initTable();
		}

		public void setSize(int size) { this.size = size; }

		@Override
		public void onDraw(Canvas canvas) {
			paint.setColor(Color.BLACK);
			paint.setStrokeWidth(0);
			paint.setStyle(Paint.Style.STROKE);
			paint.setTextSize(oldal / 2);
			String[][] table = t.getTable();
			for (int i = 0; i < size; i++)
				for (int j = 0; j < size; j++) {
					canvas.drawRect(j * oldal, i * oldal, 
							j * oldal + oldal, i * oldal + oldal, paint);
					if (table[i][j] == " ")
						canvas.drawText(table[i][j], oldal / 2 + j * oldal, 
								2 * oldal / 3 + i * oldal, paint);
					else
						if (Integer.parseInt(table[i][j]) > 9) 
							canvas.drawText(table[i][j], oldal / 4 + j * oldal, 
									2 * oldal / 3 + i * oldal, paint);
						else canvas.drawText(table[i][j], oldal / 3 + j * oldal, 
								2 * oldal / 3 + i * oldal, paint);
				}
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			int x = (int)event.getX() / oldal;
			int y = (int)event.getY() / oldal;
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				int size = t.getSize();
				int move = y * size + x;
				t.swap(move);
				invalidate();
				if (t.isDone()) {
					addResultToHighScore();
					showResultDialog();
				}
			}
			return false;
		}

		private void showResultDialog() {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			if (t.getNofMoves() == 1)
				builder.setMessage("You needed " + t.getNofMoves() + " move!");
			else
				builder.setMessage("You needed " + t.getNofMoves() + " moves!");
			builder.setTitle(R.string.dialog_title);
			builder.setPositiveButton(R.string.button_ok, 
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					Intent intent = new Intent(context, MainActivity.class);
					startActivity(intent);
				}
			});
			builder.setCancelable(false);
			AlertDialog dialog = builder.create();
			dialog.show();
		}

		private void addResultToHighScore() {
			String currentTime = getCurrentTime();
			HighScore highscore = new HighScore(context);
			highscore.addResult(size, currentTime, t.getNofMoves());
		}

		private String getCurrentTime() {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date time = new Date();
			return dateFormat.format(time);
		}

		private void initTable() {
			t = new Table();
			t.setSize(size);
			t.shuffle();
			t.resetNofMoves();
		}

	}

}
