package com.ongroa.tilepuzzle;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		size = getIntent().getIntExtra("SIZE", 3);
		setContentView(R.layout.activity_tile);
		drawView = new DrawView(this, size);
		drawView.setBackgroundColor(Color.WHITE);
		setContentView(drawView);
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
		int oldal = 100;

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
			builder.setMessage("Kiraktad " + t.getNofMoves() + " lépésből!");
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
			Display display = getWindowManager().getDefaultDisplay();
			@SuppressWarnings("deprecation")
			int width = display.getWidth();
			oldal = width / size;
			t.shuffle();
			t.resetNofMoves();
		}

	}

}
