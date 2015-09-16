package com.wootric.androidsdk.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wootric.androidsdk.R;

/**
 * Created by maciejwitowski on 9/15/15.
 */
public class FeedbackLayout extends LinearLayout {

    private Context mContext;

    private TextView mTvFeedbackHeader;
    private EditText mEtFeedback;
    private TextView mBtnDismiss;
    private TextView mBtnEditScore;
    private TextView mBtnSubmit;

    FeedbackLayoutListener mFeedbackLayoutListener;

    private int mColorDefault;
    private int mColorEtFocused;
    private int mColorBtnEnabled;

    public FeedbackLayout(Context context) {
        super(context);
        init(context);
    }

    public FeedbackLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FeedbackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void show(int score) {
        mTvFeedbackHeader.setText("You chose " + String.valueOf(score) + "/10");
        setVisibility(VISIBLE);
    }

    private void init(Context context) {
        mContext = context;
        initResources();

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.wootric_feedback_layout, this);

        mTvFeedbackHeader = (TextView) findViewById(R.id.wootric_tv_feedback_question);
        mEtFeedback = (EditText) findViewById(R.id.wootric_et_feedback);
        mBtnDismiss = (TextView) findViewById(R.id.wootric_btn_dismiss_feedback);
        mBtnSubmit = (TextView) findViewById(R.id.wootric_btn_submit_feedback);
        mBtnEditScore = (TextView) findViewById(R.id.wootric_btn_edit_score);

        Drawable etFeedbackBackground = mEtFeedback.getBackground();
        etFeedbackBackground.setColorFilter(mColorDefault, PorterDuff.Mode.SRC_ATOP);
        etFeedbackBackground.setAlpha(26);
        mEtFeedback.setOnFocusChangeListener(onEtFeedbackFocusChanged());
        mEtFeedback.addTextChangedListener(etFeedbackTextWatcher());

        mBtnDismiss.setOnClickListener(onDismissClick());
        mBtnEditScore.setOnClickListener(onEditScoreClick());
    }

    private TextWatcher etFeedbackTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean feedbackPresent = (s.length() > 0);
                mBtnSubmit.setEnabled(feedbackPresent);
                mBtnSubmit.setAlpha(feedbackPresent ? 1f : 0.26f);
                mBtnSubmit.setTextColor(feedbackPresent ? mColorBtnEnabled : mColorDefault);
            }
        };
    }

    private void initResources() {
        Resources res = mContext.getResources();

        mColorDefault = res.getColor(android.R.color.black);
        mColorEtFocused = res.getColor(R.color.wootric_brand_color);

        mColorBtnEnabled = res.getColor(R.color.wootric_dialog_header_background);
    }

    private OnFocusChangeListener onEtFeedbackFocusChanged() {
        return new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    Drawable etFeedbackBackground = mEtFeedback.getBackground();
                    etFeedbackBackground.setColorFilter(mColorEtFocused, PorterDuff.Mode.SRC_ATOP);
                    etFeedbackBackground.setAlpha(255);
                }
            }
        };
    }

    private OnClickListener onDismissClick() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFeedbackLayoutListener != null) {
                    mFeedbackLayoutListener.onFeedbackDismiss();
                }
            }
        };
    }

    private OnClickListener onEditScoreClick() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFeedbackLayoutListener != null) {
                    mFeedbackLayoutListener.onFeedbackEditScoreClick();
                }
            }
        };
    }

    public void setFeedbackLayoutListener(FeedbackLayoutListener feedbackLayoutListener) {
        mFeedbackLayoutListener = feedbackLayoutListener;
    }

    public void hide() {
        setVisibility(GONE);
    }

    public interface FeedbackLayoutListener {
        void onFeedbackDismiss();
        void onFeedbackSubmit(String text);
        void onFeedbackEditScoreClick();
    }
}
