package com.jason.trip.account;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jason.trip.R;
import com.jason.trip.base.framwork.SimpleDialogFragment;

/**
 * Version V1.0 <Trip客户端>
 * ClassName:SelectAccountActivity
 * Description:
 * Created by Jason on 16/12/19.
 */

public class SelectAccountActivity extends AppCompatActivity
        implements SimpleDialogFragment.SimpleDialogListenerProvider {

    public static final String EXTRA_ON_SELECTED_INTENT = SelectAccountActivity.class.getName()
            + ".on_selected_intent";

    private SimpleDialogFragment.SimpleDialogListener mDialogListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Account[] accounts = AccountUtils.getAccounts(this);
        int numAccounts = accounts.length;
        String[] accountNames = new String[numAccounts];
        for (int i = 0; i < numAccounts; ++i) {
            accountNames[i] = accounts[i].name;
        }

        mDialogListener = new SimpleDialogFragment.SimpleDialogListener() {
            @Override
            public void onSingleChoiceItemClicked(int requestCode, int index) {
                AccountUtils.setActiveAccount(accounts[index], SelectAccountActivity.this);
                // Calling finish() before startActivity() makes it work when the Intent is a
                // launcher one.
                finish();
                Intent onSelectedIntent = getIntent().getParcelableExtra(EXTRA_ON_SELECTED_INTENT);
                startActivity(onSelectedIntent);
            }
            @Override
            public void onNegativeButtonClicked(int requestCode) {
                onCancel(requestCode);
            }
            @Override
            public void onCancel(int requestCode) {
                finish();
            }
        };

        if (savedInstanceState == null) {
            SimpleDialogFragment.makeSingleChoice(R.string.auth_select_account, accountNames, -1,
                    this)
                    .show(getSupportFragmentManager());
        }
    }

    @Override
    public SimpleDialogFragment.SimpleDialogListener getDialogListener() {
        return mDialogListener;
    }
}

