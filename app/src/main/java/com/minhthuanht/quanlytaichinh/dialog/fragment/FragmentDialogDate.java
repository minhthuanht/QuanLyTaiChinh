package com.minhthuanht.quanlytaichinh.dialog.fragment;

import android.app.DatePickerDialog;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.minhthuanht.quanlytaichinh.R;
import com.minhthuanht.quanlytaichinh.implementDAO.ISearchTransaction;
import com.minhthuanht.quanlytaichinh.implementDAO.SearchTransactionImpl;
import com.minhthuanht.quanlytaichinh.model.DateRange;
import com.minhthuanht.quanlytaichinh.model.MTDate;
import com.minhthuanht.quanlytaichinh.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


public class FragmentDialogDate extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public interface DialogFragmentCall {
        void replaceFragment(List<Transaction> transactionList);
    }

    private DialogFragmentCall mDialogFragmentCall;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    private MTDate mtDateStart;

    private MTDate mtDateEnd;

    private TextView mDateStart;

    private TextView mDateEnd;

    private Button mOKCLick;

    private final View.OnClickListener mDateStartClicked = view -> dateStartClicked();


    private final View.OnClickListener mDateEndClicked = view -> DateEndClicked();


    private final View.OnClickListener mOKCLicked = view -> btnOKClicked();


    public FragmentDialogDate() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FragmentDialogDate.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentDialogDate newInstance() {
        FragmentDialogDate fragment = new FragmentDialogDate();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_date, null);
        mDateStart = view.findViewById(R.id.txtDateStart);
        mDateEnd = view.findViewById(R.id.txtDateEnd);
        mOKCLick = view.findViewById(R.id.btnOK);

        initEvent();
        return view;
    }

    private void initEvent() {
        mDateStart.setOnClickListener(mDateStartClicked);
        mDateEnd.setOnClickListener(mDateEndClicked);
        mOKCLick.setOnClickListener(mOKCLicked);
    }

    private void DateEndClicked() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dialog = (datePicker, year, month, dayOfMonth) -> {

            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DATE, dayOfMonth);

            mDateEnd.setText(sdf.format(calendar.getTime()));
            mtDateEnd = new MTDate(calendar).setTimeToEndOfDay();
            Log.d("startend6", mtDateEnd.getMillis() + "");
        };
        DatePickerDialog mDatePickerDialog = new DatePickerDialog(
                Objects.requireNonNull(getActivity()),
                dialog,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE)
        );
        mDatePickerDialog.show();
    }

    private void dateStartClicked() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dialog = (datePicker, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DATE, dayOfMonth);

            mDateStart.setText(sdf.format(calendar.getTime()));
            mtDateStart = new MTDate(calendar).setTimeToBeginningOfDay();
            Log.d("startend5", mtDateStart.getMillis() + "");
        };
        DatePickerDialog mDatePickerDialog = new DatePickerDialog(
                Objects.requireNonNull(getActivity()),
                dialog,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE)
        );
        mDatePickerDialog.show();
    }

    private void btnOKClicked() {

        String dateStart = mDateStart.getText().toString().trim();
        String dateEnd = mDateEnd.getText().toString().trim();

        if (dateStart.equals("")) {
            mDateStart.setError(getResources().getString(R.string.select_date));
            mDateStart.requestFocus();
        } else if (dateEnd.equals("")) {
            mDateEnd.setError(getResources().getString(R.string.select_date));
            mDateEnd.requestFocus();
        } else if (!dateEnd.isEmpty() && !dateStart.isEmpty()) {

            DateRange dateRange = new DateRange(mtDateStart, mtDateEnd);
            Log.d("startend3", mtDateStart.getMillis() + "");
            Log.d("startend4", mtDateEnd.getMillis() + "");
            ISearchTransaction mISearchTransaction = new SearchTransactionImpl(getContext());

            List<Transaction> transactions = mISearchTransaction.searchTransactionByDate(dateRange);
            mDialogFragmentCall.replaceFragment(transactions);
            getDialog().dismiss();
        }

    }

    public void setDialogFragmentCall(DialogFragmentCall dialogFragmentCall) {
        if (dialogFragmentCall != null) {
            mDialogFragmentCall = dialogFragmentCall;
        }
    }
}
