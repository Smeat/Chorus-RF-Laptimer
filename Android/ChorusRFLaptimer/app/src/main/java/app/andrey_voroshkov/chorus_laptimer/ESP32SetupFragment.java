package app.andrey_voroshkov.chorus_laptimer;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

public class ESP32SetupFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private View mRootView;
    private Context mContext;

    public ESP32SetupFragment() {

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ESP32SetupFragment newInstance(int sectionNumber) {
        ESP32SetupFragment fragment = new ESP32SetupFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.chorus32_setup, container, false);
        mRootView = rootView;
        mContext = getContext();


        Button btn_dec_rx = (Button) rootView.findViewById(R.id.btnDecRxNum);
        Button btn_inc_rx = (Button) rootView.findViewById(R.id.btnIncRxNum);

        btn_dec_rx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num_rx = AppState.getInstance().getRxNum();
                if(num_rx >= 2) {
                    AppState.getInstance().sendBtCommand("ER*M" + String.format("%01X", num_rx - 1));
                }
            }
        });

        btn_inc_rx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num_rx = AppState.getInstance().getRxNum();
                if(num_rx <= 16) {
                    AppState.getInstance().sendBtCommand("ER*M" + String.format("%01X", num_rx + 1));
                }
            }
        });

        SpinnerUserEvent spinner_adc_mode = (SpinnerUserEvent) rootView.findViewById(R.id.esp32_vbat_mode);
        String[] adc_items = new String[]{"OFF", "ADC on GPIO 32", "ADC on GPIO 30", "INA219"};
        ArrayAdapter<String> adc_mode_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, adc_items);
        spinner_adc_mode.setAdapter(adc_mode_adapter);

        spinner_adc_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AppState.getInstance().sendBtCommand("ER*v" + String.format("%01X", position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btn_dec_adc = (Button) rootView.findViewById(R.id.btnDecAdcVal);
        Button btn_inc_adc= (Button) rootView.findViewById(R.id.btnIncAdcVal);

        btn_dec_adc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adc_val = AppState.getInstance().getAdcVal();
                if(adc_val >= 1) {
                    AppState.getInstance().sendBtCommand("ER*V" + String.format("%04X", adc_val - 1));
                }
            }
        });

        btn_inc_adc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adc_val = AppState.getInstance().getAdcVal();
                    AppState.getInstance().sendBtCommand("ER*V" + String.format("%04X", adc_val + 1));
            }
        });

        SpinnerUserEvent spinner_wifi_proto = (SpinnerUserEvent) rootView.findViewById(R.id.esp32_wifi_proto);
        String[] wifi_items = new String[]{"b", "bgn"};
        ArrayAdapter<String> wifi_proto_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, wifi_items);
        spinner_wifi_proto.setAdapter(wifi_proto_adapter);

        Button btn_dec_wifi = (Button) rootView.findViewById(R.id.btnDecWifiChannel);
        Button btn_inc_wifi = (Button) rootView.findViewById(R.id.btnIncWifiChannel);

        btn_dec_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int channel = AppState.getInstance().getWifiChannell();
                if(channel > 1){
                    AppState.getInstance().sendBtCommand("ER*W" + String.format("%01X", channel - 1));
                }
            }
        });

        btn_inc_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int channel = AppState.getInstance().getWifiChannell();
                if(channel < 13){
                    AppState.getInstance().sendBtCommand("ER*W" + String.format("%01X", channel + 1));
                }
            }
        });


        Button btn_dec_filter = (Button) rootView.findViewById(R.id.btnDecWifiChannel);
        Button btn_inc_filter = (Button) rootView.findViewById(R.id.btnIncWifiChannel);

        btn_dec_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int val = AppState.getInstance().getFilterCutoff();
                if(val > 5) {
                    AppState.getInstance().sendBtCommand("ER*F" + String.format("%04X", val - 5));
                }
            }
        });

        btn_inc_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int val = AppState.getInstance().getFilterCutoff();
                AppState.getInstance().sendBtCommand("ER*F" + String.format("%04X", val + 5));
            }
        });


        if(isAdded()) {
            updateADCMode();
            updateADCVal();
            updateNumRX();
            updateWifiChannel();
            updateWifiProto();
            updateFilterCutoff();
        }

        AppState.getInstance().addListener(new IDataListener() {
            @Override
            public void onDataChange(DataAction dataItemName) {
                if (!isAdded()) return;

                switch (dataItemName) {
                    case EXTENDED_ADC_MODE:
                        updateADCMode();
                        break;
                    case EXTENDED_ADC_VAL:
                        updateADCVal();
                        break;
                    case EXTENDED_WIFI_CHANNEL:
                        updateWifiChannel();
                        break;
                    case EXTENDED_WIFI_PROTO:
                        updateWifiProto();
                        break;
                    case EXTENDED_RX_NUM:
                        updateNumRX();
                        break;
                    case EXTENDED_FILTER_CUTOFF:
                        updateFilterCutoff();
                        break;
                }
            }
        });


        this.mRootView = rootView;
        return rootView;
    }


    private void updateADCMode() {
        int mode = AppState.getInstance().getAdcMode();
        SpinnerUserEvent spinner_adc_mode = (SpinnerUserEvent) mRootView.findViewById(R.id.esp32_vbat_mode);
        spinner_adc_mode.setSelectionNoEvent(mode);
    }

    private void updateADCVal() {
        int val = AppState.getInstance().getAdcVal();
        TextView txt = (TextView) mRootView.findViewById(R.id.esp32_vbat_val);
        txt.setText(String.valueOf(val));
    }

    private void updateWifiProto() {
        int proto = AppState.getInstance().getWifiProtocol();
        SpinnerUserEvent spinner = (SpinnerUserEvent) mRootView.findViewById(R.id.esp32_wifi_proto);
        spinner.setSelectionNoEvent(proto);
    }

    private void updateWifiChannel() {
        int val = AppState.getInstance().getWifiChannell();
        TextView txt = (TextView) mRootView.findViewById(R.id.esp32_wifi_channel);
        txt.setText(String.valueOf(val));
    }

    private void updateNumRX() {
        int val = AppState.getInstance().getRxNum();
        TextView txt = (TextView) mRootView.findViewById(R.id.esp32_rx_num);
        txt.setText(String.valueOf(val));
    }

    private void updateFilterCutoff() {
        int val = AppState.getInstance().getFilterCutoff();
        TextView txt = (TextView) mRootView.findViewById(R.id.esp32_filter);
        txt.setText(String.valueOf(val));
    }
}
