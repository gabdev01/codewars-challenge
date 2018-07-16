package codewars.tuigroup.com.codewars.ui.widget;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import codewars.tuigroup.com.codewars.R;

public class ContentLoadingView extends FrameLayout implements View.OnClickListener {

    private LinearLayout messageLayout;
    private ProgressBar loadingProgressBar;
    private ImageView iconImageView;
    private TextView descriptionTextView;
    private Button retryButton;

    private OnRetryRequestedListener onRetryRequestedListener;

    public ContentLoadingView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ContentLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ContentLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.widget_content_loading, this);

        messageLayout = findViewById(R.id.linearlayout_contentloading_message);
        loadingProgressBar = findViewById(R.id.progressbar_contentloading);
        descriptionTextView = findViewById(R.id.textview_contentloading_description);
        iconImageView = findViewById(R.id.imageview_contentloading_icon);
        retryButton = findViewById(R.id.button_contentloading_retry);

        retryButton.setOnClickListener(this);
    }

    public void setTextColor(@ColorInt int color) {
        descriptionTextView.setTextColor(color);
    }

    public void showProgressBar() {
        loadingProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        loadingProgressBar.setVisibility(View.GONE);
    }

    public boolean isProgressBarShown() {
        return loadingProgressBar.getVisibility() == View.VISIBLE;
    }

    public void showMessage(int messageResId) {
        showMessage(-1, messageResId, false);
    }

    public void showMessage(int iconResId, int messageResId, boolean isRetryButtonShown) {
        retryButton.setVisibility(isRetryButtonShown ? View.VISIBLE : View.GONE);
        messageLayout.setVisibility(View.VISIBLE);
        if (iconResId != -1) {
            iconImageView.setVisibility(View.VISIBLE);
            iconImageView.setImageResource(iconResId);
        } else {
            iconImageView.setVisibility(View.GONE);
        }
        if (messageResId != -1) {
            descriptionTextView.setVisibility(View.VISIBLE);
            descriptionTextView.setText(messageResId);
        } else {
            descriptionTextView.setVisibility(View.GONE);
        }
    }

    public void hideMessage() {
        messageLayout.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        performRetryButtonClick();
    }

    public void setOnRetryRequestedListener(OnRetryRequestedListener listener) {
        onRetryRequestedListener = listener;
    }

    public void performRetryButtonClick() {
        if (onRetryRequestedListener != null) {
            onRetryRequestedListener.onRetryRequested();
        }
    }

    public interface OnRetryRequestedListener {
        void onRetryRequested();
    }
}
