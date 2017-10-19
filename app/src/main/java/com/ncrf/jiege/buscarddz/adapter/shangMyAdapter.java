package com.ncrf.jiege.buscarddz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.ncrf.jiege.buscarddz.MainActivity;
import com.ncrf.jiege.buscarddz.R;
import com.ncrf.jiege.buscarddz.util.SiteMsg_Util;

import java.util.ArrayList;
import java.util.List;


public class shangMyAdapter extends BaseAdapter {
	private List<SiteMsg_Util> list = new ArrayList<SiteMsg_Util>();
	private LayoutInflater inflater;
	private Context context;
	private int index;
	private TextView zdname, zdname1;
	private ImageView img;
	private FrameLayout layout;
	private ImageView dqimg;
	private int isdz;
	private boolean isdayu45;

	public shangMyAdapter(List<SiteMsg_Util> list, Context context, int index,
						  int isdz, boolean isdayu45) {
		this.list = list;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.index = index;
		this.isdz = isdz;
		this.isdayu45 = isdayu45;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		view = inflater.inflate(R.layout.shanghuangse_item, null);
		zdname = (TextView) view.findViewById(R.id.shhs_name);
		zdname1 = (TextView) view.findViewById(R.id.shhs_name1);
		img = (ImageView) view.findViewById(R.id.stype_img);
		layout = (FrameLayout) view.findViewById(R.id.szdbuju);
		LayoutParams lp;
		lp = (LayoutParams) layout.getLayoutParams();
		if (isdayu45) {
			lp.width = 1350 / list.size();
		} else {
			lp.width = 1880 / list.size();
		}
		layout.setLayoutParams(lp);
		zdname.setText(list.get(arg0).getStationName());
		if (MainActivity.isdayu30) {
			Log.v("Adapter", "isdayu30:" +MainActivity.isdayu30);
			LayoutParams lp1;
			lp1 = (LayoutParams) zdname.getLayoutParams();
			lp1.width = 30;
			zdname.setLayoutParams(lp1);
			zdname1.setLayoutParams(lp1);
			if (list.get(arg0).getStationName() != null) {
				switch (list.get(arg0).getStationName().length()) {
					case 2:
						zdname.setLineSpacing(0, 5.3f);
						zdname.setTextSize(30);
						break;
					case 3:
						zdname.setLineSpacing(0, 2.84f);
						zdname.setTextSize(30);
						break;
					case 4:
						zdname.setLineSpacing(0, 1.9f);
						zdname.setTextSize(30);
						break;
					case 5:
						zdname.setLineSpacing(0, 1.45f);
						zdname.setTextSize(30);
						break;
					case 6:
						zdname.setLineSpacing(0, 1.15f);
						zdname.setTextSize(30);
						break;
					case 7:
						zdname.setLineSpacing(0, 0.98f);
						zdname.setTextSize(30);
						break;
					case 8:
						zdname.setLineSpacing(0, 0.83f);
						zdname.setTextSize(30);
						break;
					case 9:
						zdname.setText(list.get(arg0).getStationName()
								.substring(0, 5));
						zdname1.setText(list.get(arg0).getStationName()
								.substring(5, 9));
						zdname1.setVisibility(View.VISIBLE);
						zdname.setLineSpacing(0, 1.45f);
						zdname.setTextSize(30);
						zdname1.setLineSpacing(0, 1.9f);
						zdname1.setTextSize(30);
						break;
					case 10:
						zdname.setText(list.get(arg0).getStationName()
								.substring(0, 5));
						zdname1.setText(list.get(arg0).getStationName()
								.substring(5, 10));
						zdname1.setVisibility(View.VISIBLE);
						zdname.setLineSpacing(0, 1.45f);
						zdname.setTextSize(30);
						zdname1.setLineSpacing(0, 1.45f);
						zdname1.setTextSize(30);
						break;
					case 11:
						zdname.setText(list.get(arg0).getStationName()
								.substring(0, 6));
						zdname1.setText(list.get(arg0).getStationName()
								.substring(6, 11));
						zdname1.setVisibility(View.VISIBLE);
						zdname.setLineSpacing(0, 1.15f);
						zdname.setTextSize(30);
						zdname1.setLineSpacing(0, 1.45f);
						zdname1.setTextSize(30);
						break;
					case 12:
						zdname.setText(list.get(arg0).getStationName()
								.substring(0, 6));
						zdname1.setText(list.get(arg0).getStationName()
								.substring(6, 12));
						zdname1.setVisibility(View.VISIBLE);
						zdname.setLineSpacing(0, 1.15f);
						zdname.setTextSize(30);
						zdname1.setLineSpacing(0, 1.15f);
						zdname1.setTextSize(30);
						break;
					case 13:
						zdname.setText(list.get(arg0).getStationName()
								.substring(0, 7));
						zdname1.setText(list.get(arg0).getStationName()
								.substring(7, 13));
						zdname1.setVisibility(View.VISIBLE);
						zdname.setLineSpacing(0, 0.98f);
						zdname.setTextSize(30);
						zdname1.setLineSpacing(0, 1.15f);
						zdname1.setTextSize(30);
						break;
					case 14:
						zdname.setText(list.get(arg0).getStationName()
								.substring(0, 7));
						zdname1.setText(list.get(arg0).getStationName()
								.substring(7, 14));
						zdname1.setVisibility(View.VISIBLE);
						zdname.setLineSpacing(0, 0.98f);
						zdname.setTextSize(30);
						zdname1.setLineSpacing(0, 0.98f);
						zdname1.setTextSize(30);
						break;
					case 15:
						zdname.setText(list.get(arg0).getStationName()
								.substring(0, 8));
						zdname1.setText(list.get(arg0).getStationName()
								.substring(8, 15));
						zdname1.setVisibility(View.VISIBLE);
						zdname.setLineSpacing(0, 0.83f);
						zdname.setTextSize(30);
						zdname1.setLineSpacing(0, 0.98f);
						zdname1.setTextSize(30);
						break;
					case 16:
						zdname.setText(list.get(arg0).getStationName()
								.substring(0, 8));
						zdname1.setText(list.get(arg0).getStationName()
								.substring(8, 16));
						zdname1.setVisibility(View.VISIBLE);
						zdname.setLineSpacing(0, 0.83f);
						zdname.setTextSize(30);
						zdname1.setLineSpacing(0, 0.83f);
						zdname1.setTextSize(30);
						break;
					default:
						break;
				}
			}
		} else {
			if (list.get(arg0).getStationName() != null) {
				switch (list.get(arg0).getStationName().length()) {
					case 2:
						zdname.setLineSpacing(0, 6f);
						zdname.setTextSize(25);
						break;
					case 3:
						zdname.setLineSpacing(0, 3.2f);
						zdname.setTextSize(25);
						break;
					case 4:
						zdname.setLineSpacing(0, 2.18f);
						zdname.setTextSize(25);
						break;
					case 5:
						zdname.setLineSpacing(0, 1.74f);
						zdname.setTextSize(24);
						break;
					case 6:
						zdname.setLineSpacing(0, 1.4f);
						zdname.setTextSize(24);
						break;
					case 7:
						zdname.setLineSpacing(0, 1.162f);
						zdname.setTextSize(24);
						break;
					case 8:
						zdname.setLineSpacing(0, 0.983f);
						zdname.setTextSize(24);
						break;
					case 9:
						zdname.setText(list.get(arg0).getStationName()
								.substring(0, 5));
						zdname1.setText(list.get(arg0).getStationName()
								.substring(5, 9));
						zdname1.setVisibility(View.VISIBLE);
						zdname.setLineSpacing(0, 1.7f);
						zdname.setTextSize(25);
						zdname1.setLineSpacing(0, 2.3f);
						zdname1.setTextSize(24);
						break;
					case 10:
						zdname.setText(list.get(arg0).getStationName()
								.substring(0, 5));
						zdname1.setText(list.get(arg0).getStationName()
								.substring(5, 10));
						zdname1.setVisibility(View.VISIBLE);
						zdname.setLineSpacing(0, 1.74f);
						zdname.setTextSize(24);
						zdname1.setLineSpacing(0, 1.74f);
						zdname1.setTextSize(24);
						break;
					case 11:
						zdname.setText(list.get(arg0).getStationName()
								.substring(0, 6));
						zdname1.setText(list.get(arg0).getStationName()
								.substring(6, 11));
						zdname1.setVisibility(View.VISIBLE);
						zdname.setLineSpacing(0, 1.42f);
						zdname.setTextSize(24);
						zdname1.setLineSpacing(0, 1.74f);
						zdname1.setTextSize(24);
						break;
					case 12:
						zdname.setText(list.get(arg0).getStationName()
								.substring(0, 6));
						zdname1.setText(list.get(arg0).getStationName()
								.substring(6, 12));
						zdname1.setVisibility(View.VISIBLE);
						zdname.setLineSpacing(0, 1.4f);
						zdname.setTextSize(24);
						zdname1.setLineSpacing(0, 1.4f);
						zdname1.setTextSize(24);
						break;
					case 13:
						zdname.setText(list.get(arg0).getStationName()
								.substring(0, 7));
						zdname1.setText(list.get(arg0).getStationName()
								.substring(7, 13));
						zdname1.setVisibility(View.VISIBLE);
						zdname.setLineSpacing(0, 1.17f);
						zdname.setTextSize(24);
						zdname1.setLineSpacing(0, 1.38f);
						zdname1.setTextSize(24);
						break;
					case 14:
						zdname.setText(list.get(arg0).getStationName()
								.substring(0, 7));
						zdname1.setText(list.get(arg0).getStationName()
								.substring(7, 14));
						zdname1.setVisibility(View.VISIBLE);
						zdname.setLineSpacing(0, 1.17f);
						zdname.setTextSize(24);
						zdname1.setLineSpacing(0, 1.17f);
						zdname1.setTextSize(24);
						break;
					case 15:
						zdname.setText(list.get(arg0).getStationName()
								.substring(0, 8));
						zdname1.setText(list.get(arg0).getStationName()
								.substring(8, 15));
						zdname1.setVisibility(View.VISIBLE);
						zdname.setLineSpacing(0, 1.01f);
						zdname.setTextSize(24);
						zdname1.setLineSpacing(0, 1.17f);
						zdname1.setTextSize(24);
						break;
					case 16:
						zdname.setText(list.get(arg0).getStationName()
								.substring(0, 8));
						zdname1.setText(list.get(arg0).getStationName()
								.substring(8, 16));
						zdname1.setVisibility(View.VISIBLE);
						zdname.setLineSpacing(0, 0.984f);
						zdname.setTextSize(24);
						zdname1.setLineSpacing(0, 0.984f);
						zdname1.setTextSize(24);
						break;
					default:
						break;
				}
			}
		}
		TextPaint tp = zdname.getPaint();
		tp.setFakeBoldText(true);
		TextPaint tp1 = zdname1.getPaint();
		tp1.setFakeBoldText(true);
		if (arg0 < index) {
			img.setImageResource(R.mipmap.bs_yuan);
			zdname.setTextColor(Color.parseColor("#ffffff"));
			zdname1.setTextColor(Color.parseColor("#ffffff"));
		} else if (arg0 == index) {
			if (isdz == 1) {
				img.setImageResource(R.mipmap.hs_yuan);
				zdname.setTextColor(Color.parseColor("#ffffff"));
				zdname1.setTextColor(Color.parseColor("#ffffff"));
			} else {
				dqimg = img;
				handler.sendEmptyMessage(0x4141);
				zdname.setTextColor(Color.parseColor("#ffffff"));
				zdname1.setTextColor(Color.parseColor("#ffffff"));
			}
		} else {
			img.setImageResource(R.mipmap.heise_yuan);
			zdname.setTextColor(Color.parseColor("#ffffff"));
			zdname1.setTextColor(Color.parseColor("#ffffff"));
		}
		return view;
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 0x4141:
					dqimg.setImageResource(R.mipmap.hs_yuan);
					handler.sendEmptyMessageDelayed(0x3131, 200);
					break;
				case 0x3131:
					dqimg.setImageResource(R.mipmap.heise_yuan);
					handler.sendEmptyMessageDelayed(0x4141, 200);
					break;
				default:
					break;
			}
		};
	};
}
