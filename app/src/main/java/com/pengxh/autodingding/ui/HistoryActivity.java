package com.pengxh.autodingding.ui;

import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.aihook.alertview.library.AlertView;
import com.aihook.alertview.library.OnItemClickListener;
import com.gyf.immersionbar.ImmersionBar;
import com.pengxh.app.multilib.base.BaseNormalActivity;
import com.pengxh.app.multilib.utils.DensityUtil;
import com.pengxh.autodingding.R;
import com.pengxh.autodingding.adapter.HistoryAdapter;
import com.pengxh.autodingding.bean.HistoryBean;
import com.pengxh.autodingding.utils.SQLiteUtil;
import com.pengxh.autodingding.utils.Utils;
import com.pengxh.autodingding.widgets.EasyPopupWindow;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HistoryActivity extends BaseNormalActivity
        implements View.OnClickListener, OnItemClickListener {

    @BindView(R.id.titleLayout)
    RelativeLayout titleLayout;
    @BindView(R.id.emptyLayout)
    RelativeLayout emptyLayout;
    @BindView(R.id.historyList)
    ListView historyList;
    private SQLiteUtil sqLiteUtil;
    private List<HistoryBean> historyBeans;
    private HistoryAdapter historyAdapter;

    private static final List<String> items = Arrays.asList("删除记录", "导出记录");
    private AlertView alertView;

    @Override
    public void initView() {
        setContentView(R.layout.activity_history);
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(R.color.colorAppThemeLight).init();
    }

    @Override
    public void initData() {
        sqLiteUtil = SQLiteUtil.getInstance();
        historyBeans = sqLiteUtil.loadHistory();
    }

    @Override
    public void initEvent() {
        if (historyBeans.size() == 0) {
            emptyLayout.setVisibility(View.VISIBLE);
        } else {
            emptyLayout.setVisibility(View.GONE);

            historyAdapter = new HistoryAdapter(this, historyBeans);
            historyList.setAdapter(historyAdapter);
        }
    }

    @OnClick({R.id.settingsView})
    @Override
    public void onClick(View view) {
        EasyPopupWindow easyPopupWindow = new EasyPopupWindow(this, items);
        easyPopupWindow.setPopupWindowClickListener(position -> {
            if (position == 0) {
                //添加导出功能
                if (historyBeans.size() == 0) {
                    new AlertView("温馨提示", "空空如也，无法删除", null, new String[]{"确定"}, null, this, AlertView.Style.Alert,
                            null).setCancelable(false).show();
                } else {
                    sqLiteUtil.deleteAll();
                    historyBeans.clear();
                    historyAdapter.notifyDataSetChanged();
                    emptyLayout.setVisibility(View.VISIBLE);
                }
            } else if (position == 1) {
                alertView = new AlertView("温馨提示", "导出到" + Utils.readEmailAddress(), "取消", new String[]{"确定"}, null, this, AlertView.Style.Alert,
                        this).setCancelable(false);
                alertView.show();
            }
        });
        easyPopupWindow.showAsDropDown(titleLayout, titleLayout.getWidth(), DensityUtil.dp2px(this, 1));
    }

    @Override
    public void onItemClick(Object o, int position) {
        switch (position) {
            case -1:
                alertView.dismiss();
                break;
            case 0:
                //导出Excel

                break;
        }
    }
}
