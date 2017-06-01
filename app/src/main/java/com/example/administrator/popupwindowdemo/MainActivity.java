package com.example.administrator.popupwindowdemo;

import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private PopupWindow mPopupWindow;
    private Toolbar mToolbar;
    //是否支持沉浸式状态栏
    private final static boolean IS_IMMERSIVE_STATUS_BAR;

    static {
        IS_IMMERSIVE_STATUS_BAR = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //4.4以上支持沉浸式
        if (IS_IMMERSIVE_STATUS_BAR) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            if (IS_IMMERSIVE_STATUS_BAR) {
                int statusBarHeight = StatusBarHeightUtil.getStatusBarHeight(getBaseContext());
                mToolbar.setPadding(0, statusBarHeight, 0, 0);
            }
            setSupportActionBar(mToolbar);
            mToolbar.setNavigationIcon(R.drawable.ic_menu);
        }
        final TextView helloText = (TextView) findViewById(R.id.main_text);
        helloText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helloText.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
    }

    private void showPopupWindow(View viewLayout) {
        View popView = View.inflate(this, R.layout.activity_popu_layout, null);
        /*WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        if(mPopupWindow == null){
            mPopupWindow = new PopupWindow(popView, display.getWidth(), display.getHeight(), true);
        }
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = 0.5f;
        getWindow().setAttributes(layoutParams);
        mPopupWindow.showAsDropDown(viewLayout);
        popView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                closePopupWindow();
                return false;
            }
        });*/
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(this);
            mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            //点击外部区域关闭窗口
            mPopupWindow.setOutsideTouchable(true);
            //获得焦点，true时点击事件不会向上传递由Activity处理
            mPopupWindow.setFocusable(true);
        }
        mPopupWindow.setContentView(popView);
        mPopupWindow.showAtLocation(viewLayout, Gravity.RIGHT | Gravity.TOP, 0
                , IS_IMMERSIVE_STATUS_BAR ? viewLayout.getHeight() : viewLayout.getHeight() + StatusBarHeightUtil.getStatusBarHeight(this));
        mPopupWindow.update();
        changeBackground(0.6f);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                changeBackground(1f);
            }
        });
    }

    private void changeBackground(Float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }

    private void closePopupWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.alpha = 1f;
            getWindow().setAttributes(layoutParams);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (android.R.id.home == id) {
            clickLeftButton();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clickLeftButton() {
        if (mToolbar != null) showPopupWindow(mToolbar);
    }
}
