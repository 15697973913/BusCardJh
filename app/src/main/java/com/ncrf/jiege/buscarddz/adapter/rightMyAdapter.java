package com.ncrf.jiege.buscarddz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.ncrf.jiege.buscarddz.R;
import com.ncrf.jiege.buscarddz.tools.AlignTextView;
import com.ncrf.jiege.buscarddz.util.SiteMsg_Util;

import java.util.ArrayList;
import java.util.List;

public class rightMyAdapter extends BaseAdapter {
	private List<SiteMsg_Util> list = new ArrayList<SiteMsg_Util>();
	private LayoutInflater inflater;
	private Context context;
	private int index;
	private AlignTextView zdname, zdname1;
	private ImageView img;
	private FrameLayout layout;
	private ImageView dqimg;
	private int isdz;

	public rightMyAdapter(List<SiteMsg_Util> list, Context context, int index, int isdz) {
		this.list = list;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.index = index;
		this.isdz = isdz;
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
		view = inflater.inflate(R.layout.right_list_item, null);
		zdname = (AlignTextView) view.findViewById(R.id.right_name);
		zdname1 = (AlignTextView) view.findViewById(R.id.right_name1);
		img = (ImageView) view.findViewById(R.id.right_stype_img);
		layout = (FrameLayout) view.findViewById(R.id.right_szdbuju);
		LayoutParams lp;
		lp = (LayoutParams) layout.getLayoutParams();
		lp.height = 350 / list.size();
		layout.setLayoutParams(lp);
		zdname.setAlingText(list.get(arg0).getStationName());
		if (list.get(arg0).getStationName() != null) {
			switch (list.get(arg0).getStationName().length()) {
				case 2:
					zdname.setLineSpacing(0, 1);
					zdname.setTextSize(25);
					break;
				case 3:
					zdname.setLineSpacing(0, 1);
					zdname.setTextSize(25);
					break;
				case 4:
					zdname.setLineSpacing(0, 1);
					zdname.setTextSize(25);
					break;
				case 5:
					zdname.setLineSpacing(0, 1);
					zdname.setTextSize(24);
					break;
				case 6:
					zdname.setLineSpacing(0, 1);
					zdname.setTextSize(24);
					break;
				case 7:
					zdname.setLineSpacing(0, 1);
					zdname.setTextSize(24);
					break;
				case 8:
					zdname.setLineSpacing(0, 1);
					zdname.setTextSize(24);
					zdname.setPadding(0, 0, 0, 22);
					break;
				case 9:
					zdname.setAlingText(list.get(arg0).getStationName().substring(0, 5));
					zdname1.setAlingText(list.get(arg0).getStationName().substring(5, 9));
					zdname1.setVisibility(View.VISIBLE);
					zdname.setTextSize(25);
					zdname1.setTextSize(24);
					break;
				case 10:
					zdname.setAlingText(list.get(arg0).getStationName().substring(0, 5));
					zdname1.setAlingText(list.get(arg0).getStationName().substring(5, 10));
					zdname1.setVisibility(View.VISIBLE);
					zdname.setTextSize(24);
					zdname1.setTextSize(24);
					break;
				case 11:
					zdname.setAlingText(list.get(arg0).getStationName().substring(0, 6));
					zdname1.setAlingText(list.get(arg0).getStationName().substring(6, 11));
					zdname1.setVisibility(View.VISIBLE);
					zdname.setTextSize(24);
					zdname1.setTextSize(24);
					break;
				case 12:
					zdname.setAlingText(list.get(arg0).getStationName().substring(0, 6));
					zdname1.setAlingText(list.get(arg0).getStationName().substring(6, 12));
					zdname1.setVisibility(View.VISIBLE);
					zdname.setTextSize(24);
					zdname1.setTextSize(24);
					break;
				case 13:
					zdname.setAlingText(list.get(arg0).getStationName().substring(0, 7));
					zdname1.setAlingText(list.get(arg0).getStationName().substring(7, 13));
					zdname1.setVisibility(View.VISIBLE);
					zdname.setTextSize(24);
					zdname1.setTextSize(24);
					break;
				case 14:
					zdname.setAlingText(list.get(arg0).getStationName().substring(0, 7));
					zdname1.setAlingText(list.get(arg0).getStationName().substring(7, 14));
					zdname1.setVisibility(View.VISIBLE);
					zdname.setTextSize(24);
					zdname1.setTextSize(24);
					break;
				case 15:
					zdname.setAlingText(list.get(arg0).getStationName().substring(0, 8));
					zdname1.setAlingText(list.get(arg0).getStationName().substring(8, 15));
					zdname1.setVisibility(View.VISIBLE);
					zdname.setTextSize(24);
					zdname1.setTextSize(24);
					break;
				case 16:
					zdname.setAlingText(list.get(arg0).getStationName().substring(0, 8));
					zdname1.setAlingText(list.get(arg0).getStationName().substring(8, 16));
					zdname1.setVisibility(View.VISIBLE);
					zdname.setTextSize(24);
					zdname1.setTextSize(24);
					break;
				default:
					break;
			}
		}
		TextPaint tp = zdname.getPaint();
		tp.setFakeBoldText(true);
		TextPaint tp1 = zdname1.getPaint();
		tp1.setFakeBoldText(true);
		if (arg0 < index) {
			img.setImageResource(R.mipmap.heise_yuan);
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
			img.setImageResource(R.mipmap.bs_yuan);
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
