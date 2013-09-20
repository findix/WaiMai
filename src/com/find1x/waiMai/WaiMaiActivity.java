package com.find1x.waiMai;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.adsmogo.adapters.AdsMogoAdapter;
import com.adsmogo.adapters.AdsMogoCustomEventPlatformEnum;
import com.adsmogo.adview.AdsMogoLayout;
import com.adsmogo.controller.listener.AdsMogoListener;
import com.adsmogo.util.AdsMogoUtil;
import com.find1x.waiMai.db.DatabaseHelper;

import Findix.waiMai.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;

public class WaiMaiActivity extends Activity implements OnClickListener,
		AdsMogoListener {
	// 定义控件变量
	private Button button1 = null;
	private Button button2 = null;
	private TextView textView1 = null;
	// private static String[] name = new String[] { "小小美食屋", "叽叽吖吖",
	// "豪大大","食为天", "妙味骨头饭", "小四川", "好奇汉堡", "西北狼烧烤" };
	// private static String[] tel = new String[] { "13564668239",
	// "13062639362","15021109715", "13774390890", "13621886517",
	// "13661516698","15000663069", "13564952523" };
	// private static String[] a301 = new String[] { "凤翔", "王琰", "秦铮", "董嘉维" };
	String choose = new String();
	String lastChoose = new String();
	String[] chaxun = new String[] { "name", "tel" };
	private static Boolean isAdded = false;

	AdsMogoLayout adsMogoLayoutCode;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// 使用控件
		textView1 = (TextView) findViewById(R.id.textView1);
		button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(this);
		button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		init();
		adsMogoLayoutCode.downloadIsShowDialog = true;
	}

	@Override
	// 定义点击事件
	public void onClick(View v) {
		// 用点击返回的id确定点击的按钮，用switch分支

		switch (v.getId()) {
		case R.id.button1: {
			File dir = new File("data/data/com.find1x.waiMai/");
			File file = new File(dir, "name.ini");
			if (!file.exists()) {
				new AlertDialog.Builder(this).setTitle("Tip")
						.setMessage("请先按菜单键设置姓名")
						.setIcon(R.drawable.ic_launcher)
						.setPositiveButton("好的~", null).show();
			}
			try {
				FileReader FR = new FileReader(file);
				BufferedReader BR = new BufferedReader(FR);
				String name = BR.readLine();
				String[] nameArray;
				if (name == "") {
					new AlertDialog.Builder(this).setTitle("Tip")
							.setMessage("请先按菜单键设置姓名")
							.setIcon(R.drawable.ic_launcher)
							.setPositiveButton("好的~", null).show();
				}
				nameArray = name.split(" ");
				int value = (int) (Math.random() * 100 % nameArray.length);
				choose = nameArray[value];
				if (choose.equals(lastChoose)) {
					value = (int) (Math.random() * 100 % nameArray.length);
					choose = nameArray[value];
				}
				lastChoose = choose;
				textView1.setText(choose);
				new AlertDialog.Builder(this).setTitle("喂人民服务的是：")
						.setMessage(choose).setIcon(R.drawable.ic_launcher)
						.setPositiveButton("- -认命吧", null).show();
				BR.close();
				FR.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			break;
		case R.id.button2: {
			// 读取数据库
			DatabaseHelper dbHelper = new DatabaseHelper(WaiMaiActivity.this,
					"waimai_db");
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			Cursor cursor = db.rawQuery("select * from user", null);
			int num = cursor.getCount();
			String[] name1 = new String[num];
			final String[] tel1 = new String[num];
			System.out.println("cursor,getCount" + cursor.getCount());
			int i = 0;
			while (cursor.moveToNext()) {
				name1[i] = cursor.getString(cursor.getColumnIndex("name"));
				tel1[i] = cursor.getString(cursor.getColumnIndex("tel"));
				i++;
			}
			System.out.println("i=" + i);
			db.close();

			new AlertDialog.Builder(this).setTitle("您要哪家的外卖？")
					.setItems(name1, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Uri uri = Uri.parse("tel:" + tel1[which]);
							Intent dialIntent = new Intent(Intent.ACTION_DIAL,
									uri);

							startActivity(dialIntent);
						}
					}).show();

		}
		default: {
		}
		}
	}

	// 以下用于显示菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about: {
			// 弹出关于对话框
			new AlertDialog.Builder(this)
					.setTitle("关于")
					.setMessage(
					// 直接用R.string.*得到的是一个int变量（地址），所以必须用下面显示的两种方法得到string才行
							"谁去拿外卖？\n\n"
									+ "Version:"
									+ getResources()
											.getString(R.string.version)
									+ "\n"
									+ getResources().getString(
											R.string.Copyright)
									+ "\n"
									+ WaiMaiActivity.this
											.getString(R.string.http))
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton("确定", null).show();
			break;
		}
		case R.id.add: {
			// intent到add activity
			Intent browserIntent = new Intent(this, AddTelActivity.class);
			startActivity(browserIntent);
			break;
		}
		case R.id.setName: {
			final EditText et = new EditText(this);
			new AlertDialog.Builder(this)
					.setTitle("设置姓名")
					.setMessage("请输入所有人的名字，以空格分开。\n如：张三 李四 王五\n")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(et)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// 数据获取
									File dir = new File(
											"data/data/com.find1x.waiMai/");
									File file = new File(dir, "name.ini");
									if (!file.exists()) {
										try {
											Log.i("创建文件", file.createNewFile()
													+ "");
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
									file.delete();
									try {
										file.createNewFile();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									try {
										FileWriter FOS = new FileWriter(file);
										BufferedWriter BW = new BufferedWriter(
												FOS);
										BW.append(et.getText().toString());
										BW.close();
										FOS.close();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}
							}).setNegativeButton("取消", null).show();

			break;
		}
		case R.id.editTel: {
			// 读取数据库
			DatabaseHelper dbHelper = new DatabaseHelper(WaiMaiActivity.this,
					"waimai_db");
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			Cursor cursor = db.rawQuery("select * from user", null);
			int num = cursor.getCount();
			final String[] name1 = new String[num];
			final String[] tel1 = new String[num];
			System.out.println("cursor,getCount" + cursor.getCount());
			int i = 0;
			while (cursor.moveToNext()) {
				name1[i] = cursor.getString(cursor.getColumnIndex("name"));
				tel1[i] = cursor.getString(cursor.getColumnIndex("tel"));
				i++;
			}
			System.out.println("i=" + i);
			db.close();

			final boolean[] defaultSelectedStatus = new boolean[num];
			for (i = 0; i < num; i++)
				defaultSelectedStatus[i] = false;
			new AlertDialog.Builder(this)
					.setTitle("删除外卖电话")
					// 设置对话框标题
					.setMultiChoiceItems(name1, defaultSelectedStatus,
							new OnMultiChoiceClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which, boolean isChecked) {
									// 来回重复选择取消，得相应去改变item对应的bool值，点击确定时，根据这个bool[],得到选择的内容
									defaultSelectedStatus[which] = isChecked;
								}
							}) // 设置对话框[肯定]按钮
					.setPositiveButton("删除",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									int delete = 0;
									for (int i = 0; i < defaultSelectedStatus.length; i++) {
										if (defaultSelectedStatus[i]) {
											DatabaseHelper dbHelper = new DatabaseHelper(
													WaiMaiActivity.this,
													"waimai_db");
											SQLiteDatabase db = dbHelper
													.getWritableDatabase();
											String[] whereArgs = { name1[i] };
											db.delete("user", "name=?",
													whereArgs);
											db.close();
											delete++;
										}
									}
									Toast.makeText(WaiMaiActivity.this, "删除成功",
											Toast.LENGTH_LONG).show();
									Log.i("", delete + " "
											+ defaultSelectedStatus.length + "");
									if (delete == defaultSelectedStatus.length) {
										isAdded = false;
									}
								}
							}).setNegativeButton("取消", null)// 设置对话框[否定]按钮
					.show();
			break;
		}
		// case R.id.addTel: {
		// if (isAdded == false) {
		// DatabaseHelper dbHelper = new DatabaseHelper(
		// WaiMaiActivity.this, "waimai_db");
		// SQLiteDatabase db = dbHelper.getWritableDatabase();
		// ContentValues values = new ContentValues();
		// System.out.println("name数组长度为" + name.length);
		// for (int i = 0; i < name.length; i++) {
		// values = new ContentValues();
		// values.put("name", name[i]);
		// values.put("tel", tel[i]);
		// db.insert("user", null, values);
		// System.out.println("插入名称:" + name[i]);
		// System.out.println("插入电话:" + tel[i]);
		// }
		// isAdded = true;
		// db.close();
		// } else {
		// new AlertDialog.Builder(this).setTitle("基情提示")
		// .setMessage("已经插入，请勿重复插入。")
		// .setIcon(android.R.drawable.ic_dialog_info)
		// .setPositiveButton("确定", null).show();
		// }
		// }
		}
		return true;
	}

	private void init() {

		// 注意：因单一平台SDK有互相冲突现象，所以demo中的jar文件不全，详细请查看libs.zip

		/*------------------------------------------------------------*/
		// 初始化AdsMogoLayout 初始化分为以下几种方式
		// 构造方法，设置广告类型，如全屏广告，banner广告
		// public AdsMogoLayout(final Activity context, final String keyAdMogo,
		// final int ad_type) {
		// }

		// 默认的构造方法，默认开启快速模式，banner广告
		// public AdsMogoLayout(final Activity context, final String keyAdMogo)
		// {
		// }

		// 构造方法，设置快速模式
		// public AdsMogoLayout(final Activity context, final String keyAdMogo,
		// boolean expressMode) {
		// }

		// 构造方法，设置广告类型和快速模式
		// public AdsMogoLayout(final Activity context, final String keyAdMogo,
		// final int ad_type, final boolean expressMode) {
		// }
		/*------------------------------------------------------------*/

		// 构造方法，设置快速模式
		adsMogoLayoutCode = new AdsMogoLayout(this,
				"e738bd5f299243a187f941685e98624a", false);

		// 设置监听回调 其中包括 请求 展示 请求失败等事件的回调
		adsMogoLayoutCode.setAdsMogoListener(this);

		/*------------------------------------------------------------*/
		// 通过Code方式添加广告条 本例的结构如下(仅供参考)
		// -RelativeLayout/(FILL_PARENT,FILL_PARENT)
		// |
		// +RelativeLayout/(FILL_PARENT,WRAP_CONTENT)
		// |
		// +AdsMogoLayout(FILL_PARENT,WRAP_CONTENT)
		// |
		// \
		// |
		// \
		/*------------------------------------------------------------*/
		RelativeLayout parentLayput = new RelativeLayout(this);
		RelativeLayout.LayoutParams parentLayputParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
				RelativeLayout.TRUE);
		parentLayput.addView(adsMogoLayoutCode, layoutParams);

		this.addContentView(parentLayput, parentLayputParams);
	}

	/**
	 * 当用户点击广告*(Mogo服务根据次记录点击数，次点击是过滤过的点击，一条广告一次展示只能对应一次点击)
	 */
	@Override
	public void onClickAd(String arg0) {
		Log.d(AdsMogoUtil.ADMOGO, "-=onClickAd=-");

	}

	// 当用户点击了广告关闭按钮时回调(关闭广告按钮功能可以在Mogo的App管理中设置)
	// return false 则广告关闭 return true 广告将不会关闭

	@Override
	public boolean onCloseAd() {
		Log.d(AdsMogoUtil.ADMOGO, "-=onCloseAd=-");
		AlertDialog dialog = new AlertDialog.Builder(this).create();

		dialog.setMessage("是否关闭广告？");

		dialog.setButton("是", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// return true;

				dialog.dismiss();

				if (adsMogoLayoutCode != null) {
					// 关闭当前广告
					adsMogoLayoutCode.setADEnable(false);
				}

			}
		});

		dialog.setButton2("否", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				dialog.dismiss();
			}
		});

		dialog.show();

		return true;
	}

	/**
	 * 当用户关闭了下载类型广告的详细界面时回调(广告物料类型为下载广告并且是弹出简介下载的才会有此Dialog)
	 */
	@Override
	public void onCloseMogoDialog() {
		Log.d(AdsMogoUtil.ADMOGO, "-=onCloseMogoDialog=-");
	}

	/**
	 * 所有广告平台请求失败时回调
	 */
	@Override
	public void onFailedReceiveAd() {
		Log.d(AdsMogoUtil.ADMOGO, "-=onFailedReceiveAd=-");

	}

	/**
	 * 当用户点击广告*(真实点击 Mogo不会根据此回调时记录点击数，次点击是无过滤过的点击)
	 */
	@Override
	public void onRealClickAd() {
		Log.d(AdsMogoUtil.ADMOGO, "-=onRealClickAd=-");

	}

	/**
	 * 请求广告成功时回调 arg0为单一平台的广告视图 arg1为请求平台名称
	 */

	@Override
	public void onReceiveAd(ViewGroup arg0, String arg1) {
		Log.d(AdsMogoUtil.ADMOGO, "-=onReceiveAd=-");

	}

	/**
	 * 开始请求广告时回调 arg0为请求平台名称
	 */
	@Override
	public void onRequestAd(String arg0) {
		Log.d(AdsMogoUtil.ADMOGO, "-=onRequestAd=-");

	}

	@Override
	protected void onDestroy() {
		// 清除 adsMogoLayout 实例 所产生用于多线程缓冲机制的线程池
		// 此方法请不要轻易调用，如果调用时间不当，会造成无法统计计数
		if (adsMogoLayoutCode != null) {
			adsMogoLayoutCode.clearThread();
		}
		super.onDestroy();
	}

	// 自定义平台功能：关联自定义Adapter
	// 如不需要自定义平台功能， 返回 null
	// AdsMogoCustomEventPlatform_1对应平台一
	// AdsMogoCustomEventPlatform_2对应平台二，如果开发者修改平台名称的话，需备注一下以免弄混
	// 如不需要自定义平台功能， 返回 null

	@Override
	public Class<? extends AdsMogoAdapter> getCustomEvemtPlatformAdapterClass(
			AdsMogoCustomEventPlatformEnum enumIndex) {
		Class<? extends AdsMogoAdapter> clazz = null;
		if (AdsMogoCustomEventPlatformEnum.AdsMogoCustomEventPlatform_1
				.equals(enumIndex)) {
			// clazz = DianDongAdapter.class;
		}
		return clazz;
	}
}