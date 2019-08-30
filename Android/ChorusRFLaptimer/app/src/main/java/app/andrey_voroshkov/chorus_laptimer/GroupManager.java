package app.andrey_voroshkov.chorus_laptimer;

import java.util.Vector;

import app.andrey_voroshkov.chorus_laptimer.structs.PilotGroup;

public class GroupManager {
    private Vector<PilotGroup> m_groups = new Vector<>();
    private PilotGroup m_current_group = null;

    private static GroupManager instance = new GroupManager();

    public static GroupManager getInstance() {
        return instance;
    }

    private GroupManager(){
        m_current_group = new PilotGroup();
        m_current_group.setName("Default");
        m_groups.add(m_current_group);

        // TODO: this is for debug!!
        m_groups.add(new PilotGroup());
    }

    public PilotGroup getCurrentGroup() {
        return m_current_group;
    }

    public void addGroup(PilotGroup group) {
        this.m_groups.add(group);
    }

    public void removeGroup(PilotGroup group) {
        if(group.equals(m_current_group)) {
            m_current_group = null;
        }
        m_groups.remove(group);
    }

    public PilotGroup getGroup(String name) {
        for(int i = 0; i < m_groups.size(); ++i) {
            if(name.equals(m_groups.get(i).getName())) {
                return m_groups.get(i);
            }
        }
        return null;
    }

    public void setCurrentGroup(PilotGroup g){
        if(g != null) {
            // Collect the current settings. Doing it this way to reduce the number of code changes needed
            m_current_group.collectSettings();
            m_current_group = g;
            m_current_group.applySettings();
        }
    }

    public void setCurrentGroup(String name) {
        setCurrentGroup(getGroup(name));
    }

};