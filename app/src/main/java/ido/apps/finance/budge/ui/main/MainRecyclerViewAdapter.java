package ido.apps.finance.budge.ui.main;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ido.apps.finance.budge.R;
import ido.apps.finance.budge.data.Budget;
import ido.apps.finance.budge.data.Transaction;
import ido.apps.finance.budge.ui.budget.BudgetActivity;
import ido.apps.finance.budge.ui.editActivities.TransactionActivity;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.BudgetViewHolder> {
    private List<Budget> data;

    public MainRecyclerViewAdapter() {
        data = Collections.emptyList();
    }

    public void setRecyclerBudgetsList(List<Budget> budgets) {
        data = budgets;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_budget_view, parent, false);
        return new BudgetViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(BudgetViewHolder holder, int position) {
        holder.bindItem(data.get(position));
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public static class BudgetViewHolder extends RecyclerView.ViewHolder {
        TextView subjectTextView, budgetLeftTextView, budgetSizeTextView;
        ImageView imageView;
        ProgressBar progressBar;
        Button addButton;

        public BudgetViewHolder(View view) {
            super(view);
            this.subjectTextView = view.findViewById(R.id.subjectTextView);
            this.budgetLeftTextView = view.findViewById(R.id.budgetLeftTextView);
            this.budgetSizeTextView = view.findViewById(R.id.budgetSizeTextView);
            this.imageView = view.findViewById(R.id.imageView);
            this.progressBar = view.findViewById(R.id.progressBar);
            this.addButton = view.findViewById(R.id.add_button);
        }

        public void bindItem(Budget budget) {
            subjectTextView.setText(budget.getSubject());
            budgetLeftTextView.setText(String.valueOf(budget.getLeftBudget()));
            budgetSizeTextView.setText(String.valueOf(budget.getBudgetSize()));
           // imageView.setImageResource(budget.getImage());
            progressBar.setMax((int)(budget.getBudgetSize()));
            progressBar.setProgress((int) budget.getLeftBudget());

            //When clicking the root view -> open the activity_budget of the clicked budget
            itemView.setOnClickListener(v -> {

                Intent intent = new Intent(v.getContext(), BudgetActivity.class);
                intent.putExtra(BudgetActivity.BUDGET_ID_KEY, budget.getId());
                v.getContext().startActivity(intent);

            });
            //When clicking the the add_button -> open the activity_transaction of the clicked budget
            //in order to add another transaction
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), TransactionActivity.class);
                    intent.putExtra(BudgetActivity.BUDGET_ID_KEY, budget.getId());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
