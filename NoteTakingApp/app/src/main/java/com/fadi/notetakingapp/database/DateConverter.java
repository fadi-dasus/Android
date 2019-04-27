package com.fadi.notetakingapp.database;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;


// this class is for dealing with date since the SQLite does not
// know how to deal with date
public class DateConverter {


    @TypeConverter
    public static Date toDate(Long timestamp) {
        // if timestamp is null return null else create a new instance ....
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
