package com.find1x.waiMai;

import com.adsmogo.adapters.AdsMogoCustomEventPlatformEnum;
import com.adsmogo.adview.AdsMogoLayout;
import com.adsmogo.controller.listener.AdsMogoListener;
import com.adsmogo.util.AdsMogoUtil;
import com.find1x.waiMai.db.DatabaseHelper;

import com.find1x.waiMai.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddTelActivity extends Activity implements OnClickListener {
	private Button button1 = null;
	private TextView editText1 = null;
	private TextView editText2 = null;
	private String namenew;
	public String telnew;
	public String[] a = new String[2];
	LinearLayout layout = null;

	// AdsMogoLayout adsMogoLayout;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add);
		editText1 = (TextView) findViewById(R.id.editText1);
		editText2 = (TextView) findViewById(R.id.editText2);
		button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(this);

		// 芒果广告
		// adsMogoLayout = ((AdsMogoLayout)
		// this.findViewById(R.id.adsMogoView));
		// adsMogoLayout.setAdsMogoListener(this);
		// adsMogoLayout.downloadIsShowDialog = true;
		// adsMogoLayout.downloadIsShowDialog=true;
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (editText1.getText().toString().equals("")
				|| editText2.getText().toString().equals("")) {
			new AlertDialog.Builder(this).setTitle("错误")
					.setMessage("您未输入名称或者电话,请重新输入")
					.setPositiveButton("我知道了", null)
					.setIcon(android.R.drawable.ic_dialog_alert).show();
		} else {
			namenew = editText1.getText().toString();
			telnew = editText2.getText().toString();
			new AlertDialog.Builder(this)
					.setTitle("确认输入")
					.setMessage(
							"商家:" + namenew + "\n" + "电话:" + telnew + "\n确认新增？")
					.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									DatabaseHelper dbHelper = new DatabaseHelper(
											AddTelActivity.this, "waimai_db");
									SQLiteDatabase db = dbHelper
											.getWritableDatabase();
									ContentValues values = new ContentValues();

									values.put("name", namenew);
									values.put("tel", telnew);
									db.insert("user", null, values);
									Intent intent = new Intent(
											AddTelActivity.this,
											WaiMaiActivity.class);
									startActivity(intent);
								}

							}).setNegativeButton("取消", null).show();
		}
	}
}
