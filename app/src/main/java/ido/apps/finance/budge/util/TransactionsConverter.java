package ido.apps.finance.budge.util;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import ido.apps.finance.budge.data.Transaction;

public class TransactionsConverter {

    static Gson gson = new Gson();

    @TypeConverter
    public static List<Transaction> stringToTransactionList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<Transaction>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<Transaction> someObjects) {
        return gson.toJson(someObjects);
    }
}