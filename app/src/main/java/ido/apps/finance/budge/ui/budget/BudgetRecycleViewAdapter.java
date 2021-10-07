package ido.apps.finance.budge.ui.budget;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import ido.apps.finance.budge.R;
import ido.apps.finance.budge.data.Transaction;
import ido.apps.finance.budge.ui.editActivities.TransactionActivity;

public class BudgetRecycleViewAdapter extends RecyclerView.Adapter<BudgetRecycleViewAdapter.TransactionViewHolder> {
    private List<Transaction> data;

    public BudgetRecycleViewAdapter() {
        data = Collections.emptyList();

    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_transaction_view, parent, false);
        return new TransactionViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        holder.bindItem(data.get(position));
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public void setTransactions(List<Transaction> transactions) {
        data = transactions;
        notifyDataSetChanged();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {

        TextView subjectTextView, sizeTextView, dateTextView;

        public TransactionViewHolder(View view) {
            super(view);
            this.subjectTextView = view.findViewById(R.id.transactionSubjectView);
            this.sizeTextView = view.findViewById(R.id.transactionSizeView);
            this.dateTextView = view.findViewById(R.id.transactionDateView);

        }

        public void bindItem(Transaction transaction) {
            subjectTextView.setText(transaction.getPayee());
            sizeTextView.setText(String.valueOf(transaction.getPayment()));
            dateTextView.setText(transaction.getDate().toString());

            itemView.setOnClickListener(v -> {

                Intent intent = new Intent(v.getContext(), TransactionActivity.class);
                intent.putExtra(TransactionActivity.TRANSACTION_ID_KEY, transaction.getId());
                v.getContext().startActivity(intent);

            });
        }

        public void setTransactions() {

        }
    }
}
