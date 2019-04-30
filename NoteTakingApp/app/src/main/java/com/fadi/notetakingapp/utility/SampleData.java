package com.fadi.notetakingapp.utility;

import com.fadi.notetakingapp.model.Note;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
// dummy data that has been used during the development phase
public class SampleData {


    private static Date getDate(int diff) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.add(Calendar.MILLISECOND, diff);
        return cal.getTime();
    }

    public static List<Note> getNotes() {
        List<Note> notes = new ArrayList<>();
        notes.add(new Note(getDate(0), Constant.SampleData_DUMMY_DATE1));
        notes.add(new Note(getDate(-1), Constant.SampleData_DUMMY_DATE2));
        notes.add(new Note(getDate(-2), Constant.SampleData_DUMMY_DATE3));
        return notes;
    }
}