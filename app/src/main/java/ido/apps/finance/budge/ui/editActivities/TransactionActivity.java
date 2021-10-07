package ido.apps.finance.budge.ui.editActivities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ido.apps.finance.budge.R;
import ido.apps.finance.budge.data.Budget;
import ido.apps.finance.budge.data.Transaction;
import ido.apps.finance.budge.databinding.ActivityTransactionBinding;
import ido.apps.finance.budge.util.UIUtils;

public class TransactionActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public final static String TRANSACTION_ID_KEY = "transaction id key";
    private static final String BUDGET_ID_KEY = "budget id key";
    private int transactionSign = -1;
    private long transactionId = Long.MIN_VALUE;
    private long budgetId;
    private LiveData<List<Budget>> budgetsListLiveData;
    private LiveData<Budget> budgetLiveData;
    private LiveData<Transaction> transactionLiveData;
    private PopupMenu menu;
    private Menu dynamicMenu;
    private TransactionViewModel viewModel;
    private ActivityTransactionBinding binding;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        menu = new PopupMenu(findViewById(R.id.budget_popup_menu_button).getContext(), findViewById(R.id.budget_popup_menu_button));
        dynamicMenu = menu.getMenu();

        viewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        // assigning ID of the toolbar to a variable
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_new_budget);
        // using toolbar as ActionBar
        setSupportActionBar(toolbar);
        //to extract the id's of the budget and the transaction in order to know if it is an edit or an add action:
        setViewModelIDs();
        getAndSetLiveData();
        //if it is an edit of an exist transaction:
        observeViewModel();
        setupMenuViews();
        setupClickListeners();
    }


    private void setViewModelIDs() {
        Intent intent = getIntent();
        if (intent.hasExtra(TRANSACTION_ID_KEY)) {
            transactionId = intent.getLongExtra(TRANSACTION_ID_KEY, Long.MIN_VALUE);
        }
        budgetId = intent.getLongExtra(BUDGET_ID_KEY, Long.MIN_VALUE);
        viewModel.setIDs(budgetId, transactionId);
    }

    private void getAndSetLiveData() {
        budgetsListLiveData = viewModel.getBudgetsList();
        budgetLiveData = viewModel.getBudget();
        transactionLiveData = viewModel.getTransaction();
    }

    private void observeViewModel() {
        transactionLiveData.observe(this, transaction -> {
            if (transaction != null) {
                updateTransactionData(transaction);
            }
        });

        budgetsListLiveData.observe(this, budgets -> {
            if (budgets != null) {
                setBudgetPopupMenuData();
            }
        });

    }

    private void updateTransactionData(Transaction transaction) {
        binding.editTextPayee.setText(transaction.getPayee());
        binding.dateEditText.setText(transaction.getDate().toString());
        binding.paymentEditText.setText(String.valueOf(transaction.getPayment()));
        binding.popupMenuIncomeExpense.setText(transaction.getExpanseSign() == 1 ? "Income" : "Expense");
        binding.dateEditText.setText(transaction.getDate().toString());
    }

    private void setupClickListeners() {
        binding.dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAndSetDate();
                UIUtils.hideSoftKeyboard(TransactionActivity.this);
            }
        });
        binding.textInputLayoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAndSetDate();
                UIUtils.hideSoftKeyboard(TransactionActivity.this);
            }
        });

        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(binding.getRoot(), "You are about to delete a transaction, ARE YOU SURE?", Snackbar.LENGTH_LONG);
                snackbar.setAction("I'm Sure", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.deleteTransaction();
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
        });

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String payee = binding.editTextPayee.getText().toString();
                double payment = transactionSign * (Float.parseFloat(binding.paymentEditText.getText().toString()));
                Date date = null;
                try {
                    date = new SimpleDateFormat("dd/MM/yyyy").parse(binding.dateEditText.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                viewModel.saveTransaction(payee, payment, date, transactionSign);
            }
        });
    }

    private void showAndSetDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void setupMenuViews() {

        binding.dateEditText.setShowSoftInputOnFocus(false);
        binding.popupMenuIncomeExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(TransactionActivity.this, v);
                popup.inflate(R.menu.expense_income_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    //changes the transaction sign (+/-) as chosen at the income/expense popup
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.expense: {
                                binding.popupMenuIncomeExpense.setText("Expense");
                            }
                            return true;
                            case R.id.income: {
                                binding.popupMenuIncomeExpense.setText("Income");
                                transactionSign = 1;
                            }
                            return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();
            }
        });

        binding.budgetPopupMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBudgetPopupMenuData();
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    //sets the budgetId to the ID of the chosen budget in order to add the transaction to the correct budget
                    public boolean onMenuItemClick(MenuItem item) {
                        budgetId = item.getItemId();
                        return true;
                    }
                });
                menu.show();
            }
        });
    }

    private void setBudgetPopupMenuData() {
        if (budgetsListLiveData.getValue() == null)
            budgetsListLiveData.getValue().forEach(s -> {
                dynamicMenu.add(Menu.NONE, (int) s.getId(), Menu.NONE, s.getSubject());
            });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "/" + month + "/" + year;
        binding.dateEditText.setText(date);
    }

}
