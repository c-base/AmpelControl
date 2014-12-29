package org.c_base.ampelcontrol;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import org.c_base.ampelcontrol.TrafficLightState.Builder;


public class AmpelActivity extends ActionBarActivity {

    @InjectView(R.id.ampel_server)
    Spinner ampelServer;

    @InjectView(R.id.server_check_result)
    TextView serverCheckResult;

    @InjectView(R.id.button_red)
    ToggleButton redButton;

    @InjectView(R.id.button_yellow)
    ToggleButton yellowButton;

    @InjectView(R.id.button_green)
    ToggleButton greenButton;

    @InjectView(R.id.light_container)
    ViewGroup lightContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name_full);
        setContentView(R.layout.activity_ampel);
        ButterKnife.inject(this);

        setUpAmpelServerSpinner();
    }

    private void setUpAmpelServerSpinner() {
        List<TrafficLightHolder> trafficLightHolders = new ArrayList<>();
        for (TrafficLight light : TrafficLight.LIGHTS) {
            trafficLightHolders.add(new TrafficLightHolder(this, light));
        }
        ArrayAdapter<TrafficLightHolder> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                trafficLightHolders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ampelServer.setAdapter(adapter);
        ampelServer.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                queryAmpelServer();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void queryAmpelServer() {
        serverCheckResult.setVisibility(View.GONE);

        String host = getHostName();
        new QueryServerAsyncTask().execute(host);
    }

    @OnClick(R.id.button_server_check)
    public void serverCheckPressed() {
        queryAmpelServer();
    }

    private String getHostName() {
        return ((TrafficLightHolder) ampelServer.getSelectedItem()).getHost();
    }

    @OnClick(R.id.button_red)
    public void greenButtonPressed() {
        setAmpelColor();
    }

    @OnClick(R.id.button_yellow)
    public void yellowButtonPressed() {
        setAmpelColor();
    }

    @OnClick(R.id.button_green)
    public void redButtonPressed() {
        setAmpelColor();
    }

    public void setAmpelColor() {
        TrafficLightState state = new Builder()
                .red(redButton.isChecked())
                .yellow(yellowButton.isChecked())
                .green(greenButton.isChecked())
                .build();

        new SetAmpelColorAsyncTask().execute(state);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ampel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            showAboutDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAboutDialog() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment newFragment = AboutFragment.newInstance();
        newFragment.show(ft, "dialog");
    }

    private ToggleButton getButtonForColor(TrafficLightColor color) {
        switch (color) {
            case RED: {
                return redButton;
            }
            case YELLOW: {
                return yellowButton;
            }
            case GREEN: {
                return greenButton;
            }
            default: {
                throw new IllegalArgumentException("Unknown color: " + color.name());
            }
        }
    }

    private void enableButtons() {

        setButtonsEnabled(true);
    }

    private void disableButtons() {
        setButtonsEnabled(false);
    }

    private void setButtonsEnabled(boolean enabled) {
        redButton.setEnabled(enabled);
        yellowButton.setEnabled(enabled);
        greenButton.setEnabled(enabled);
    }

    private class QueryServerAsyncTask extends AsyncTask<String, Void, TrafficLightContainer> {

        @Override
        protected void onPreExecute() {
            disableButtons();
        }

        @Override
        protected TrafficLightContainer doInBackground(String... params) {
            try {
                TrafficLightController controller = TrafficLightController.newInstance(getHostName());
                return controller.getAmpelState();
            } catch (IOException e) {
                //TODO: display error message to user
                Log.e("AmpelControl", "Couldn't connect to server: " + e.getMessage(), e);
                cancel(false);
                return null;
            }
        }

        @Override
        protected void onPostExecute(TrafficLightContainer container) {
            enableButtons();

            TrafficLightConfig config = container.getConfig();
            List<TrafficLightColor> lightOrder = config.getLightOrder();
            for (TrafficLightColor color : TrafficLightColor.values()) {
                ToggleButton button = getButtonForColor(color);
                int index = lightOrder.indexOf(color);
                if (index != -1) {
                    lightContainer.removeView(button);
                    lightContainer.addView(button, index);
                }
                button.setVisibility(lightOrder.contains(color) ? View.VISIBLE : View.GONE);
            }

            TrafficLightState state = container.getState();
            redButton.setChecked(state.isRed());
            yellowButton.setChecked(state.isYellow());
            greenButton.setChecked(state.isGreen());
        }
    }

    private class SetAmpelColorAsyncTask extends AsyncTask<TrafficLightState, Void, Void> {
        @Override
        protected Void doInBackground(TrafficLightState... params) {
            TrafficLightState state = params[0];

            try {
                TrafficLightController controller = TrafficLightController.newInstance(getHostName());
                controller.setAmpelState(state);
            } catch (IOException e) {
                //TODO: display error message to user
                Log.e("AmpelControl", "Couldn't connect to server: " + e.getMessage(), e);
            }
            return null;
        }
    }
}
