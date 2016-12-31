package com.jason.trip.account;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

/**
 * Version V1.0 <Trip客户端>
 * ClassName:AddAccountActivity
 * Description:
 * Created by Jason on 16/12/19.
 */

public class AddAccountActivity extends AppCompatActivity {

    public static final String EXTRA_ON_ADDED_INTENT = AddAccountActivity.class.getName()
            + ".on_added_intent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            AccountUtils.addAccount(this, new AccountManagerCallback<Bundle>() {
                @Override
                public void run(AccountManagerFuture<Bundle> future) {
                    finish();
                    try {
                        Bundle result = future.getResult();
                        if (result.containsKey(AccountManager.KEY_ACCOUNT_NAME)
                                && result.containsKey(AccountManager.KEY_ACCOUNT_TYPE)) {
                            // NOTE:
                            // Active account should have been set in
                            // AuthenticatorActivity.onAuthResult() since the mode should be
                            // AUTH_MODE_NEW.
                            Intent onAddedIntent = getIntent()
                                    .getParcelableExtra(EXTRA_ON_ADDED_INTENT);
                            startActivity(onAddedIntent);
                        }
                    } catch (AuthenticatorException | IOException | OperationCanceledException e) {
                        e.printStackTrace();
                    }
                }
            }, null);
        }
    }
}