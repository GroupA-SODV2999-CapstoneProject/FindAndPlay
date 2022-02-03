package com.hfad.findandplayA;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
    private String userName;
    private String passWord;

    public User(String newName, String newPass)
    {
        userName = newName;
        passWord = encryptString(newPass);
    }

    public String getName()
    {
        return userName;
    }

    public String getPass()
    {
        return passWord;
    }

    public boolean testPass(String inputString)
    {
        return encryptString(inputString).equals(passWord);
    }

    public String encryptString(String stringToEncrypt)
    {
        try {

            MessageDigest md = MessageDigest.getInstance("SHA-1");

            byte[] messageDigest = md.digest(stringToEncrypt.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            String hashText = no.toString(16);

            while (hashText.length() < 32)
            {
                hashText = "0" + hashText;
            }

            return hashText;

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
