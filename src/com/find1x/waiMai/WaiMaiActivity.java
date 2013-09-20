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
	// ����ؼ�����
	private Button button1 = null;
	private Button button2 = null;
	private TextView textView1 = null;
	// private static String[] name = new String[] { "СС��ʳ��", "ߴߴ߹߹",
	// "�����","ʳΪ��", "��ζ��ͷ��", "С�Ĵ�", "���溺��", "�������տ�" };
	// private static String[] tel = new String[] { "13564668239",
	// "13062639362","15021109715", "13774390890", "13621886517",
	// "13661516698","15000663069", "13564952523" };
	// private static String[] a301 = new String[] { "����", "����", "���", "����ά" };
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
		// ʹ�ÿؼ�
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
	// �������¼�
	public void onClick(View v) {
		// �õ�����ص�idȷ������İ�ť����switch��֧

		switch (v.getId()) {
		case R.id.button1: {
			File dir = new File("data/data/com.find1x.waiMai/");
			File file = new File(dir, "name.ini");
			if (!file.exists()) {
				new AlertDialog.Builder(this).setTitle("Tip")
						.setMessage("���Ȱ��˵�����������")
						.setIcon(R.drawable.ic_launcher)
						.setPositiveButton("�õ�~", null).show();
			}
			try {
				FileReader FR = new FileReader(file);
				BufferedReader BR = new BufferedReader(FR);
				String name = BR.readLine();
				String[] nameArray;
				if (name == "") {
					new AlertDialog.Builder(this).setTitle("Tip")
							.setMessage("���Ȱ��˵�����������")
							.setIcon(R.drawable.ic_launcher)
							.setPositiveButton("�õ�~", null).show();
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
				new AlertDialog.Builder(this).setTitle("ι���������ǣ�")
						.setMessage(choose).setIcon(R.drawable.ic_launcher)
						.setPositiveButton("- -������", null).show();
				BR.close();
				FR.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			break;
		case R.id.button2: {
			// ��ȡ���ݿ�
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

			new AlertDialog.Builder(this).setTitle("��Ҫ�ļҵ�������")
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

	// ����������ʾ�˵�
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
			// �������ڶԻ���
			new AlertDialog.Builder(this)
					.setTitle("����")
					.setMessage(
					// ֱ����R.string.*�õ�����һ��int��������ַ�������Ա�����������ʾ�����ַ����õ�string����
							"˭ȥ��������\n\n"
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
					.setPositiveButton("ȷ��", null).show();
			break;
		}
		case R.id.add: {
			// intent��add activity
			Intent browserIntent = new Intent(this, AddTelActivity.class);
			startActivity(browserIntent);
			break;
		}
		case R.id.setName: {
			final EditText et = new EditText(this);
			new AlertDialog.Builder(this)
					.setTitle("��������")
					.setMessage("�����������˵����֣��Կո�ֿ���\n�磺���� ���� ����\n")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(et)
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// ���ݻ�ȡ
									File dir = new File(
											"data/data/com.find1x.waiMai/");
									File file = new File(dir, "name.ini");
									if (!file.exists()) {
										try {
											Log.i("�����ļ�", file.createNewFile()
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
							}).setNegativeButton("ȡ��", null).show();

			break;
		}
		case R.id.editTel: {
			// ��ȡ���ݿ�
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
					.setTitle("ɾ�������绰")
					// ���öԻ������
					.setMultiChoiceItems(name1, defaultSelectedStatus,
							new OnMultiChoiceClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which, boolean isChecked) {
									// �����ظ�ѡ��ȡ��������Ӧȥ�ı�item��Ӧ��boolֵ�����ȷ��ʱ���������bool[],�õ�ѡ�������
									defaultSelectedStatus[which] = isChecked;
								}
							}) // ���öԻ���[�϶�]��ť
					.setPositiveButton("ɾ��",
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
									Toast.makeText(WaiMaiActivity.this, "ɾ���ɹ�",
											Toast.LENGTH_LONG).show();
									Log.i("", delete + " "
											+ defaultSelectedStatus.length + "");
									if (delete == defaultSelectedStatus.length) {
										isAdded = false;
									}
								}
							}).setNegativeButton("ȡ��", null)// ���öԻ���[��]��ť
					.show();
			break;
		}
		// case R.id.addTel: {
		// if (isAdded == false) {
		// DatabaseHelper dbHelper = new DatabaseHelper(
		// WaiMaiActivity.this, "waimai_db");
		// SQLiteDatabase db = dbHelper.getWritableDatabase();
		// ContentValues values = new ContentValues();
		// System.out.println("name���鳤��Ϊ" + name.length);
		// for (int i = 0; i < name.length; i++) {
		// values = new ContentValues();
		// values.put("name", name[i]);
		// values.put("tel", tel[i]);
		// db.insert("user", null, values);
		// System.out.println("��������:" + name[i]);
		// System.out.println("����绰:" + tel[i]);
		// }
		// isAdded = true;
		// db.close();
		// } else {
		// new AlertDialog.Builder(this).setTitle("������ʾ")
		// .setMessage("�Ѿ����룬�����ظ����롣")
		// .setIcon(android.R.drawable.ic_dialog_info)
		// .setPositiveButton("ȷ��", null).show();
		// }
		// }
		}
		return true;
	}

	private void init() {

		// ע�⣺��һƽ̨SDK�л����ͻ��������demo�е�jar�ļ���ȫ����ϸ��鿴libs.zip

		/*------------------------------------------------------------*/
		// ��ʼ��AdsMogoLayout ��ʼ����Ϊ���¼��ַ�ʽ
		// ���췽�������ù�����ͣ���ȫ����棬banner���
		// public AdsMogoLayout(final Activity context, final String keyAdMogo,
		// final int ad_type) {
		// }

		// Ĭ�ϵĹ��췽����Ĭ�Ͽ�������ģʽ��banner���
		// public AdsMogoLayout(final Activity context, final String keyAdMogo)
		// {
		// }

		// ���췽�������ÿ���ģʽ
		// public AdsMogoLayout(final Activity context, final String keyAdMogo,
		// boolean expressMode) {
		// }

		// ���췽�������ù�����ͺͿ���ģʽ
		// public AdsMogoLayout(final Activity context, final String keyAdMogo,
		// final int ad_type, final boolean expressMode) {
		// }
		/*------------------------------------------------------------*/

		// ���췽�������ÿ���ģʽ
		adsMogoLayoutCode = new AdsMogoLayout(this,
				"e738bd5f299243a187f941685e98624a", false);

		// ���ü����ص� ���а��� ���� չʾ ����ʧ�ܵ��¼��Ļص�
		adsMogoLayoutCode.setAdsMogoListener(this);

		/*------------------------------------------------------------*/
		// ͨ��Code��ʽ��ӹ���� �����Ľṹ����(�����ο�)
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
	 * ���û�������*(Mogo������ݴμ�¼��������ε���ǹ��˹��ĵ����һ�����һ��չʾֻ�ܶ�Ӧһ�ε��)
	 */
	@Override
	public void onClickAd(String arg0) {
		Log.d(AdsMogoUtil.ADMOGO, "-=onClickAd=-");

	}

	// ���û�����˹��رհ�ťʱ�ص�(�رչ�水ť���ܿ�����Mogo��App����������)
	// return false ����ر� return true ��潫����ر�

	@Override
	public boolean onCloseAd() {
		Log.d(AdsMogoUtil.ADMOGO, "-=onCloseAd=-");
		AlertDialog dialog = new AlertDialog.Builder(this).create();

		dialog.setMessage("�Ƿ�رչ�棿");

		dialog.setButton("��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// return true;

				dialog.dismiss();

				if (adsMogoLayoutCode != null) {
					// �رյ�ǰ���
					adsMogoLayoutCode.setADEnable(false);
				}

			}
		});

		dialog.setButton2("��", new DialogInterface.OnClickListener() {

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
	 * ���û��ر����������͹�����ϸ����ʱ�ص�(�����������Ϊ���ع�沢���ǵ���������صĲŻ��д�Dialog)
	 */
	@Override
	public void onCloseMogoDialog() {
		Log.d(AdsMogoUtil.ADMOGO, "-=onCloseMogoDialog=-");
	}

	/**
	 * ���й��ƽ̨����ʧ��ʱ�ص�
	 */
	@Override
	public void onFailedReceiveAd() {
		Log.d(AdsMogoUtil.ADMOGO, "-=onFailedReceiveAd=-");

	}

	/**
	 * ���û�������*(��ʵ��� Mogo������ݴ˻ص�ʱ��¼��������ε�����޹��˹��ĵ��)
	 */
	@Override
	public void onRealClickAd() {
		Log.d(AdsMogoUtil.ADMOGO, "-=onRealClickAd=-");

	}

	/**
	 * ������ɹ�ʱ�ص� arg0Ϊ��һƽ̨�Ĺ����ͼ arg1Ϊ����ƽ̨����
	 */

	@Override
	public void onReceiveAd(ViewGroup arg0, String arg1) {
		Log.d(AdsMogoUtil.ADMOGO, "-=onReceiveAd=-");

	}

	/**
	 * ��ʼ������ʱ�ص� arg0Ϊ����ƽ̨����
	 */
	@Override
	public void onRequestAd(String arg0) {
		Log.d(AdsMogoUtil.ADMOGO, "-=onRequestAd=-");

	}

	@Override
	protected void onDestroy() {
		// ��� adsMogoLayout ʵ�� ���������ڶ��̻߳�����Ƶ��̳߳�
		// �˷����벻Ҫ���׵��ã��������ʱ�䲻����������޷�ͳ�Ƽ���
		if (adsMogoLayoutCode != null) {
			adsMogoLayoutCode.clearThread();
		}
		super.onDestroy();
	}

	// �Զ���ƽ̨���ܣ������Զ���Adapter
	// �粻��Ҫ�Զ���ƽ̨���ܣ� ���� null
	// AdsMogoCustomEventPlatform_1��Ӧƽ̨һ
	// AdsMogoCustomEventPlatform_2��Ӧƽ̨��������������޸�ƽ̨���ƵĻ����豸עһ������Ū��
	// �粻��Ҫ�Զ���ƽ̨���ܣ� ���� null

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