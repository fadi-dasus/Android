package com.fadi.notetakingapp.database;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.fadi.notetakingapp.model.Note;


// the entities array represents a list of my tables( in this case one)
@Database(entities = {Note.class} , version = 1, exportSchema = false)
@TypeConverters(DateConverter.class )
public abstract class AppDatabase extends RoomDatabase {


    public static final String DATABASE_NAME = "AppDatabase.db";
        // volatile means that is can only be referenced from main memory
    private static volatile AppDatabase instance;
    // when we synchronize in java we need some object to synchronize on
    private static final Object obj = new Object();
    // it is abstract because this method will not be called directly, but the Room will
    // generate a code and that is the code which will be called
    // we need one abstract method for each entity we have, means one dao for each entity
    public abstract NoteDao noteDao();



    // getting database instance from room
    public static AppDatabase getInstance(Context context) {
        if(context != null){
            synchronized (obj){
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,DATABASE_NAME).build();


                }
            }
        }

        return instance;
    }



}
