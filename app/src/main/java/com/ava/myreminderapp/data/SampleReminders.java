package com.ava.myreminderapp.data;


import com.ava.myreminderapp.model.ReminderModel;

import java.util.ArrayList;
import java.util.List;

public class SampleReminders {

    public static List<ReminderModel> getSampleReminderList() {
        List<ReminderModel> list = new ArrayList<>(10);

        list.add(new ReminderModel("One"));
        list.add(new ReminderModel("Two"));
        list.add(new ReminderModel("Three"));
        list.add(new ReminderModel("Four"));
        list.add(new ReminderModel("Five"));

        list.add(new ReminderModel("Six"));
        list.add(new ReminderModel("Seven"));
        list.add(new ReminderModel("Eight"));
        list.add(new ReminderModel("Nine"));
        list.add(new ReminderModel("Ten"));

        return list;
    }
}
