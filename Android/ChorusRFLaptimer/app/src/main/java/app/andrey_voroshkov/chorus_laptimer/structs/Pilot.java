package app.andrey_voroshkov.chorus_laptimer.structs;

import android.util.Log;

import app.andrey_voroshkov.chorus_laptimer.AppState;
import app.andrey_voroshkov.chorus_laptimer.DeviceState;

public class Pilot {
    private String m_name;
    private int m_channel;
    private int m_band;
    private int m_threshold;
    private boolean m_enabled;

    public void applySettings(int id) {
        AppState.getInstance().deviceStates.get(id).pilotName = m_name;

        // If we try to send everything separately packets get lost on UDP. Maybe the small buf problem?
        String packet = "R" + id + "C" + String.format("%X", m_channel) + '\n';
        packet += "R" + id + "B" + String.format("%X", m_band) + '\n';
        packet += "R" + id + "T" + String.format("%04X", m_threshold) + '\n';
        packet += "R" + id + "A" + (m_enabled ? 1 : 0);

        AppState.getInstance().sendBtCommand(packet);
        AppState.getInstance().updatePilotNamesInEdits();
    }

    public void collectSettings(int id) {
        DeviceState state = AppState.getInstance().deviceStates.get(id);
        m_name = state.pilotName;
        m_channel = state.channel;
        m_band = state.band;
        m_threshold = state.threshold;
        m_enabled = state.isEnabled;
    }
}