package com.fadi.notetakingapp.database;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {NoteEntity.class} , version = 1, exportSchema = false)
@TypeConverters(DateConverter.class )
public abstract class AppDatabase extends RoomDatabase {


    public static final String DATABASE_NAME = "AppDatabase.db";
        // volatile means that is can only be referenced from main memory
    private static volatile AppDatabase instance;
    // when we synchronize in java we need some object to synchronize  on
    // it can be anything any object
    private static final Object LOCK = new Object();
    // it is abstract because this method will not be called directly, but the Room will
    // generate a code and that is the code which will be called
    // you need as many abstract method as many entities we have and as many dao
    public abstract NoteDao noteDao();



    public static AppDatabase getInstance(Context context) {
        if(context != null){
            synchronized (LOCK){
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,DATABASE_NAME).build();


                }
            }
        }

        return instance;
    }



}
