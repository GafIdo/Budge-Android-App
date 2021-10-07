package ido.apps.finance.budge.data;

import android.content.Context;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import ido.apps.finance.budge.util.TransactionsConverter;

@androidx.room.Database(entities = {Budget.class}, version = 1, exportSchema = false)
@TypeConverters(TransactionsConverter.class)
public abstract class Database extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "budgedb";
    private static Database sInstance;

    static Database getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null)
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            Database.class, Database.DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
            }
        }
        return sInstance;
    }
    public abstract BudgeDao dao();
}
