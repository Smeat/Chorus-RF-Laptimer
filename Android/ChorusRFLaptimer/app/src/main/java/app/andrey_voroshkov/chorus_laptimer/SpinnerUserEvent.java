package app.andrey_voroshkov.chorus_laptimer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;

public class SpinnerUserEvent extends android.support.v7.widget.AppCompatSpinner implements AdapterView.OnItemSelectedListener {

    private OnItemSelectedListener mListener;
    private boolean mUseraction = false; // start at false to catch init trigger

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(mListener != null && mUseraction) {
            mListener.onItemSelected(parent, view, position,id);
        }
        mUseraction = true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if(mListener != null) {
            mListener.onNothingSelected(parent);
        }
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        mListener = listener;
    }

    public void setSelectionNoEvent(int pos){
        if(pos != getSelectedItemPosition()) {
            mUseraction = false;
            super.setSelection(pos);
        }
    }

    public SpinnerUserEvent(Context context) {
        super(context);
        super.setOnItemSelectedListener(this);
    }

    public SpinnerUserEvent(Context context, int mode) {
        super(context, mode);
        super.setOnItemSelectedListener(this);
    }

    public SpinnerUserEvent(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOnItemSelectedListener(this);
    }

    public SpinnerUserEvent(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        super.setOnItemSelectedListener(this);
    }

    public SpinnerUserEvent(Context context, AttributeSet attrs, int defStyle, int mode) {
        super(context, attrs, defStyle, mode);
        super.setOnItemSelectedListener(this);
    }
}
