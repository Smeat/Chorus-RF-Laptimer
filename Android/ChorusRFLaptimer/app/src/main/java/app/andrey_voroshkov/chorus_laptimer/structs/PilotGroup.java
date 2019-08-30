package app.andrey_voroshkov.chorus_laptimer.structs;

import java.util.Vector;

import app.andrey_voroshkov.chorus_laptimer.AppState;

public class PilotGroup {
    private String m_name = "INVALID";
    private Vector<Pilot> m_pilots = new Vector<>();

    public PilotGroup() {
    }

    public void applySettings() {
        for(int i = 0; i < AppState.getInstance().deviceStates.size(); ++i) {
            if(i >= m_pilots.size()) {
                Pilot p = new Pilot();
                p.collectSettings(i);
                m_pilots.add(p);

            }
            m_pilots.get(i).applySettings(i);
        }
    }

    public void collectSettings() {
        for(int i = 0; i < m_pilots.size(); ++i) {
            if(i >= m_pilots.size()) {
                m_pilots.add(new Pilot());
            }
            m_pilots.get(i).collectSettings(i);
        }
    }

    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        m_name = name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public int getSize() {
        return m_pilots.size();
    }
};
