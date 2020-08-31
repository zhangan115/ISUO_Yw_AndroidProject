package com.sito.customer.view.home.news.safe;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.bean.news.MessageContent;
import com.sito.customer.mode.bean.news.MessageListBean;
import com.sito.customer.view.BaseActivity;
import com.sito.customer.view.NotifyActivity;
import com.sito.customer.widget.PlaySoundLayout;
import com.sito.library.utils.DataUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 火警/救护
 */
public class NewsSafeActivity extends BaseActivity implements NewsSafeContract.View {
    NewsSafeContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_news_safe, "通知");
        new NewsSafePresenter(CustomerApp.getInstance().getRepositoryComponent().getRepository(), this);
        long messageId = getIntent().getLongExtra(ConstantStr.KEY_BUNDLE_LONG, -1);
        mPresenter.getMessageDetail(messageId);
    }

    @Override
    public void setPresenter(NewsSafeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showData(MessageListBean bean) {
        try {
            JSONObject jsonObject = new JSONObject(bean.getMessageItemList().get(0).getContentDetail());
            int mVoiceTime = jsonObject.getInt("soundTimescale");
            String mVoiceUser = jsonObject.getString("fromUserRealName");
            int mType = jsonObject.getInt("type");
            String mTitle = "";
            if (mType == 0) {
                mTitle = "火警通知";
            } else {
                mTitle = "救护通知";
            }
            String mTime = DataUtil.timeFormat(bean.getCreateTime(), "HH:mm");

            ImageView imgIcon = (ImageView) findViewById(R.id.id_news_icon);
            if (mType == 0) {
                imgIcon.setImageDrawable(findDrawById(R.drawable.message_fire_icon));
            } else {
                imgIcon.setImageDrawable(findDrawById(R.drawable.message_ambulance_icon));
            }
            TextView tvTitle = (TextView) findViewById(R.id.id_news_title);
            TextView tvTime = (TextView) findViewById(R.id.id_news_time);
            TextView tvPlace = (TextView) findViewById(R.id.id_news_place);
            PlaySoundLayout mPlaySoundLayout = (PlaySoundLayout) findViewById(R.id.fault_sound);
            TextView tvUser = (TextView) findViewById(R.id.id_news_user);
            tvTitle.setText(mTitle);
            tvTime.setText(mTime);
            tvUser.setText(String.format("上报人：%s", mVoiceUser));
            tvPlace.setText(String.format("地点：%s", jsonObject.get("place")));
            mPlaySoundLayout.setContent(jsonObject.getString("voiceUrl")
                    , mVoiceTime, "情况说明", jsonObject.getString("suggest"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void noData() {

    }
}
