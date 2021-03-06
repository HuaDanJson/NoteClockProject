package cool.camerax.noteclockproject.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.blankj.utilcode.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cool.camerax.noteclockproject.R;
import cool.camerax.noteclockproject.base.BaseActivity;
import cool.camerax.noteclockproject.bean.DataSaveEvent;
import cool.camerax.noteclockproject.bean.NoteBean;
import cool.camerax.noteclockproject.utils.DBNoteUtils;
import cool.camerax.noteclockproject.utils.TimeUtils;
import cool.camerax.noteclockproject.utils.ToastHelper;
import cool.camerax.noteclockproject.view.NoteEditText;

public class NoteInfoActivity extends BaseActivity {

    @BindView(R.id.tvAddActivityTitle)
    TextView tvAddActivityTitle;
    @BindView(R.id.edtNotXiangQingTitle)
    EditText edtNotXiangQingTitle;
    @BindView(R.id.edtNotXiangQingTime)
    EditText edtNotXiangQingTime;
    @BindView(R.id.edtNotXiangQingCount)
    NoteEditText edtNotXiangQingCount;
    @BindView(R.id.btnNotXiangQingActivityUpdata)
    Button btnNotXiangQingActivityUpdata;
    @BindView(R.id.btnNotXiangQingActivityDelete)
    Button btnNotXiangQingActivityDelete;
    @BindView(R.id.btnNotXiangQingActivityCacel)
    Button btnNotXiangQingActivityCacel;
    @BindView(R.id.rlNotXiangQingActivity)
    RelativeLayout rlNotXiangQingActivity;

    @BindView(R.id.button_add_clock)
    Button mAddClockView;
    @BindView(R.id.button_small_text)
    TextView mSmallViewt;
    @BindView(R.id.button_middle_text)
    TextView mMiddleViewt;
    @BindView(R.id.button_big_text)
    TextView mBigView;

    private NoteBean mDiaryBean;
    private int mSelectGradle = -1;
    private long mClockTime = -1;
    private String mShowClockTime;
    private Calendar calendar = Calendar.getInstance(Locale.CHINA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_info);
        ButterKnife.bind(this);
        initData();
    }

    public void initData() {
        mDiaryBean = DBNoteUtils.getInstance().queryOneData(getIntent().getLongExtra("NotID", 0));
        edtNotXiangQingTitle.setText(mDiaryBean.getTitle());
        SimpleDateFormat sdr1 = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        String CreatedTime1 = sdr1.format(new Date(mDiaryBean.getCreatTimeAsId()));
        edtNotXiangQingTime.setText(CreatedTime1);
        edtNotXiangQingCount.setText(mDiaryBean.getValue());
        mSelectGradle = mDiaryBean.getGrade();
        mClockTime = mDiaryBean.getClockTime();
        mShowClockTime = mDiaryBean.getShowClockTime();
        switch (mSelectGradle) {
            case 1:
                mSmallViewt.setSelected(true);
                mMiddleViewt.setSelected(false);
                mBigView.setSelected(false);
                break;
            case 2:
                mSmallViewt.setSelected(false);
                mMiddleViewt.setSelected(true);
                mBigView.setSelected(false);
                break;
            case 3:
                mSmallViewt.setSelected(false);
                mMiddleViewt.setSelected(false);
                mBigView.setSelected(true);
                break;
            default:
                break;
        }
    }

    //设置点击事件
    @OnClick({R.id.btnNotXiangQingActivityUpdata, R.id.btnNotXiangQingActivityDelete, R.id.btnNotXiangQingActivityCacel})
    public void onClick(View view) {
        switch (view.getId()) {
            //处理点击更新点击事件
            case R.id.btnNotXiangQingActivityUpdata:
                saveNote();
                break;
            //处理点击删除点击事件
            case R.id.btnNotXiangQingActivityDelete:
                DBNoteUtils.getInstance().deleteOneData(mDiaryBean);
                EventBus.getDefault().post(new DataSaveEvent());
                NoteInfoActivity.this.finish();
                break;
            case R.id.btnNotXiangQingActivityCacel:
                NoteInfoActivity.this.finish();
                break;
            default:
                break;
        }
    }


    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.button_small_text:
                if (mSmallViewt.isSelected()) {
                    mSmallViewt.setSelected(false);
                    mSelectGradle = -1;
                } else {
                    mSmallViewt.setSelected(true);
                    mMiddleViewt.setSelected(false);
                    mBigView.setSelected(false);
                    mSelectGradle = 1;
                }
                break;
            case R.id.button_middle_text:
                if (mMiddleViewt.isSelected()) {
                    mMiddleViewt.setSelected(false);
                    mSelectGradle = -1;
                } else {
                    mMiddleViewt.setSelected(true);
                    mSmallViewt.setSelected(false);
                    mBigView.setSelected(false);
                    mSelectGradle = 2;
                }
                break;

            case R.id.button_big_text:
                if (mBigView.isSelected()) {
                    mBigView.setSelected(false);
                    mSelectGradle = -1;
                } else {
                    mBigView.setSelected(true);
                    mMiddleViewt.setSelected(false);
                    mSmallViewt.setSelected(false);
                    mSelectGradle = 3;
                }
                break;
            case R.id.button_add_clock:
                showDatePickerDialog(NoteInfoActivity.this, 3, calendar);
                break;
            default:
                break;
        }
    }


    //保存备忘录
    public void saveNote() {
        //取得输入的内容
        String title = edtNotXiangQingTitle.getText().toString().trim();
        String content = edtNotXiangQingCount.getText().toString().trim();
        //内容和标题都不能为空
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            ToastHelper.showShortMessage("名称和内容都不能为空");
        } else if (mSelectGradle < 0) {
            ToastHelper.showShortMessage("请选择等级后再保存");
        } else if (mClockTime < 0 || TextUtils.isEmpty(mShowClockTime)) {
            ToastHelper.showShortMessage("请选择提醒时间");
        } else {
            mDiaryBean.setTitle(title);
            mDiaryBean.setValue(content);
            mDiaryBean.setCreatTimeAsId(System.currentTimeMillis());
            mDiaryBean.setGrade(mSelectGradle);
            mDiaryBean.setClockTime(mClockTime);
            mDiaryBean.setShowClockTime(mShowClockTime);
            DBNoteUtils.getInstance().updateData(mDiaryBean);
            ToastHelper.showShortMessage("更新数据成功");
            EventBus.getDefault().post(new DataSaveEvent());
            NoteInfoActivity.this.finish();
        }
    }

    public void showDatePickerDialog(Activity activity, int themeResId, final Calendar calendar) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
            // 绑定监听器(How the parent is notified that the date is set.)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                showTimePickerDialog(NoteInfoActivity.this, 3, calendar, year, monthOfYear + 1, dayOfMonth);
            }
        }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void showTimePickerDialog(Activity activity, int themeResId, Calendar calendar, final int Year, final int Month, final int Day) {
        // Calendar c = Calendar.getInstance();
        // 创建一个TimePickerDialog实例，并把它显示出来
        // 解释一哈，Activity是context的子类
        new TimePickerDialog(activity, themeResId,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        String mMonth = "" + Month;
                        String mDay = "" + Day;
                        String mMinute = String.valueOf(minute);
                        String mhourOfDay = String.valueOf(hourOfDay);
                        if (Month / 10 == 0) {
                            mMonth = "0" + Month;
                        }
                        if (Day / 10 == 0) {
                            mDay = "0" + Day;
                        }
                        if (minute / 10 == 0) {
                            mMinute = "0" + minute;
                        }
                        if (hourOfDay / 12 == 0) {
                            mhourOfDay = "0" + hourOfDay;
                        }
                        mShowClockTime = Year + "-" + mMonth + "-" + mDay + " " + mhourOfDay + ":" + mMinute + ":00";
                        mClockTime = TimeUtils.dateToStamp(mShowClockTime);
                        LogUtils.d(" AddNoteActivity  selectTime : " + mShowClockTime + "  mClockTime : " + mClockTime);
                    }
                }
                // 设置初始时间
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                // true表示采用24小时制
                , true).show();
    }
}
