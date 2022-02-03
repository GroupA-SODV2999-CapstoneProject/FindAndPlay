package com.hfad.findandplayA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Accounts {

    private List<User> listAccounts = new ArrayList<>();

    public void createAccount(String userName, String passWord)
    {
        User newAccount = new User(userName,passWord);
        listAccounts.add(newAccount);
    }
    public boolean loginAccount(String userName, String passWord)
    {
        for(int i = 0; i < listAccounts.size(); i++)
        {
            if (listAccounts.get(i).getName().equals(userName))
            {
                if (listAccounts.get(i).testPass(passWord))
                {
                    return true;
                }
            }
        }
        return false;
    }
}
