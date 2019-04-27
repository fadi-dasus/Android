package com.via.handin;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.via.handin.database.Database;
import com.via.handin.doa.NoteDao;
import com.via.handin.dummyData.Dummy;
import com.via.handin.model.Note;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    public static final String TAG = "Junit";
    private Database db;
    private NoteDao dao;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context,
                Database.class).build();
        dao = db.noteDao();
        Log.i(TAG, "createDb");
    }

    @After
    public void closeDb() {
        db.close();
        Log.i(TAG, "closeDb");
    }

    @Test
    public void insertAndGetNotes() {
        dao.insertAll(Dummy.getNotes());
        int count = dao.getCount();
        Log.i(TAG, "insertAndGetNotes: count=" + count);
        assertEquals(Dummy.getNotes().size(), count);
    }

    @Test
    public void compareStrings() {
        dao.insertAll(Dummy.getNotes());
        Note original = Dummy.getNotes().get(0);
        Note fromDb = dao.getNoteById(1);
        assertEquals(original.getText(), fromDb.getText());
        assertEquals(1, fromDb.getId());
    }
}