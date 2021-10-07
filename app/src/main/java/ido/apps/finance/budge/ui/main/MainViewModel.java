package ido.apps.finance.budge.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ido.apps.finance.budge.data.Budget;
import ido.apps.finance.budge.data.RepositoryImpl;

public class MainViewModel extends AndroidViewModel {
    private RepositoryImpl repository;

    public MainViewModel(@NonNull Application application) {
        super(application);

        repository = RepositoryImpl.getInstance(application);
    }

    LiveData<List<Budget>> getBudgets(){
       return repository.getBudgets();
    }



}
