package com.lkj.weatherforecast.weather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lkj.weatherforecast.R;
import com.lkj.weatherforecast.entity.Weather;
import com.lkj.weatherforecast.lab.ImageResourceLab;
import com.lkj.weatherforecast.lab.WeatherLab;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static android.widget.CompoundButton.OnClickListener;

/**
 * 采用fragment管理UI，可绕开Android系统activity使用规则的限制，一个页面2~3个fragment即可
 * fragment是一种控制器对象
 * fragment本身没有在屏幕上显示视图的能力，只有将fragment视图放置在activity的视图中，fragment视图才能显示在屏幕上
 *
 * 添加fragment的方式有
 * 1.在activity布局中添加fragment
 * 2.在activity代码中添加fragment
 */
public class WeatherFragment extends Fragment {

    private static final String ARG_WEATHER_ID = "weather_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;
    public static final int REQUEST_PHOTO = 2;

    private Weather mWeather;
    private File mPhotoFile;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckbox;
    private Button mSuspectButton;
    private Button mReportButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private Callbacks mCallbacks;

    // 天气组件
    private TextView mDayOfWeek; // 星期
    private TextView mMonthAndDay; // 日期
    private TextView mMaxTemperatureTextView; // 最高温度
    private TextView mMinTemperatureTextView; // 最低温度
    private ImageView mWeatherImageView; // 天气图片
    private TextView mTypeTextView; // 天气类型
    private TextView mInfo; // 天气信息


    public interface Callbacks {
        void onWeatherUpdated(Weather weather);
    }

    /**
     * 实例化对应的fragment，并添加参数
     * @param crimeId
     * @return
     */
    public static WeatherFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_WEATHER_ID, crimeId);

        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 与activity绑定时实例化mCallbacks
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    /**
     * 创建时实例化mWeather和mPhotoFile
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID weatherId = (UUID) getArguments().getSerializable(ARG_WEATHER_ID);
        mWeather = WeatherLab.get(getActivity()).getWeather(weatherId);
        String info = mWeather.toString();
//        mPhotoFile = WeatherLab.get(getActivity()).getPhotoFile(mWeather);
        Log.d("TAG", "test");
    }

    /**
     * 暂停的时候更新视图
     */
    @Override
    public void onPause() {
        super.onPause();

//        WeatherLab.get(getActivity())
//                .updateWeather(mWeather);
    }

    /**
     * 与activity解绑时释放mCallbacks对象
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    /**
     * 创建视图
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 绑定fragment_crime视图
        View v = inflater.inflate(R.layout.fragment_weather, container, false);

        // 实例化天气组件
        mDayOfWeek = v.findViewById(R.id.weather_day_of_week);
        mMonthAndDay = v.findViewById(R.id.weather_date);
        mMaxTemperatureTextView = v.findViewById(R.id.weather_maxt);
        mMinTemperatureTextView = v.findViewById(R.id.weather_mint);
        mWeatherImageView = v.findViewById(R.id.weather_image);
        mTypeTextView = v.findViewById(R.id.weather_type);
        mInfo = v.findViewById(R.id.weather_info);

        SimpleDateFormat simpleDateFormatDayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        SimpleDateFormat simpleDateFormatMonthAndDay = new SimpleDateFormat("MMM d", Locale.ENGLISH);
        String dayOfWeek = simpleDateFormatDayOfWeek.format(mWeather.getDate());
        String monthAndDay = simpleDateFormatMonthAndDay.format(mWeather.getDate());

        mDayOfWeek.setText(dayOfWeek);
        mMonthAndDay.setText(monthAndDay);

        String info = mWeather.toString();
        mMaxTemperatureTextView.setText(mWeather.getMaxT());
        mMinTemperatureTextView.setText(mWeather.getMinT());
        // todo set image
        mWeatherImageView.setImageResource(mWeather.getImage());
        mTypeTextView.setText(mWeather.getType());
        mInfo.setText(mWeather.getInfo());

        // 实例化组件，给组件添加动作监听
        mTitleField = v.findViewById(R.id.list_item_weather_date);
//        mTitleField.setText(mWeather.getType());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mWeather.setType(s.toString());
//                updateWeather();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 更新日期
        mDateButton = v.findViewById(R.id.list_item_weather_type);
//        updateDate();

        // 给日期按钮添加监听，点击后弹出日期对话框
//        mDateButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager manager = getFragmentManager();
//                WeatherDatePickerFragment dialog = WeatherDatePickerFragment.newInstance(mWeather.getDate());
//                dialog.setTargetFragment(WeatherFragment.this, REQUEST_DATE);
//
//                dialog.show(manager, DIALOG_DATE);
//            }
//        });

//        mSolvedCheckbox = v.findViewById(R.id.crime_solved);
//        mSolvedCheckbox.setChecked(mWeather.isSolved());
//        mSolvedCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//                mWeather.setSolved(isChecked);
//                updateCrime();
//            }
//        });

        mReportButton = v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
//                i.putExtra(Intent.EXTRA_TEXT, getWeatherReport());
                i.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.crime_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton = v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

//        if (mWeather.getSuspect() != null) {
//            mSuspectButton.setText(mWeather.getSuspect());
//        }

        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact,
                PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }

        mPhotoButton = v.findViewById(R.id.crime_camera);

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.lkj.weatherforecast.fileprovider", mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

//        mPhotoView = v.findViewById(R.id.crime_photo);
//        updatePhotoView();

        return v;
    }

    /**
     * 从上一个Activity进入的时候获取对请求码分析
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(WeatherDatePickerFragment.EXTRA_DATE);
//            mWeather.setDate(date);
//            updateWeather();
//            updateDate();
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            Cursor c = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null, null);
            try {
                if (c.getCount() == 0) {
                    return;
                }

                c.moveToFirst();
                String suspect = c.getString(0);
//                mWeather.setSuspect(suspect);
//                updateWeather();
                mSuspectButton.setText(suspect);
            } finally {
                c.close();
            }
        } else if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.lkj.weatherforecast.fileprovider",
                    mPhotoFile);
            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            updateWeather();
//            updatePhotoView();
        }
    }

    /**
     * 更新天气对象
     */
//    private void updateWeather() {
//        WeatherLab.get(getActivity()).updateWeather(mWeather);
//        mCallbacks.onWeatherUpdated(mWeather);
//    }

    /**
     * 更新日期
     */
//    private void updateDate() {
//        mDateButton.setText(mWeather.getDate().toString());
//    }

    /**
     * 获取Report
     * @return
     */
//    private String getWeatherReport() {
//        String solvedString = null;
////        if (mWeather.isSolved()) {
////            solvedString = getString(R.string.crime_report_solved);
////        } else {
////            solvedString = getString(R.string.crime_report_unsolved);
////        }
//
//        String dateFormat = "EEE, MMM, dd";
////        String dateString = DateFormat.format(dateFormat, mWeather.getDate()).toString();
//
//        String suspect = mWeather.getType();
////        if (suspect == null) {
////            suspect = getString(R.string.crime_report_no_suspect);
////        } else {
////            suspect = getString(R.string.crime_report_suspect, suspect);
////        }
//
////        String report = getString(R.string.crime_report,
////                mWeather.getType(), dateString, solvedString, suspect);
//
//        return report;
//    }

    /**
     * 更新照片
     */
//    private void updatePhotoView() {
//        if (mPhotoFile == null || !mPhotoFile.exists()) {
//            mPhotoView.setImageDrawable(null);
//        } else {
//            Bitmap bitmap = PictureUtils.getScaledBitmap(
//                    mPhotoFile.getPath(), getActivity());
//            mPhotoView.setImageBitmap(bitmap);
//        }
//    }
}
