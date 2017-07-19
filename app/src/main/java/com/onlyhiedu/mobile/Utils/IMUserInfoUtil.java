package com.onlyhiedu.mobile.Utils;

import android.text.TextUtils;

import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.util.HanziToPinyin;
import com.onlyhiedu.mobile.Model.bean.IMUserInfo;

import java.util.ArrayList;

/**
 * Created by pengpeng on 2017/7/19.
 */

public class IMUserInfoUtil {
    public static void setUserInitialLetter(IMUserInfo user) {
        final String DefaultLetter = "#";
        String letter = DefaultLetter;

        final class GetInitialLetter {
            String getLetter(String name) {
                if (TextUtils.isEmpty(name)) {
                    return DefaultLetter;
                }
                char char0 = name.toLowerCase().charAt(0);
                if (Character.isDigit(char0)) {
                    return DefaultLetter;
                }
                ArrayList<HanziToPinyin.Token> l = HanziToPinyin.getInstance().get(name.substring(0, 1));
                if (l != null && l.size() > 0 && l.get(0).target.length() > 0)
                {
                    HanziToPinyin.Token token = l.get(0);
                    String letter = token.target.substring(0, 1).toUpperCase();
                    char c = letter.charAt(0);
                    if (c < 'A' || c > 'Z') {
                        return DefaultLetter;
                    }
                    return letter;
                }
                return DefaultLetter;
            }
        }

        if ( !TextUtils.isEmpty(user.userName) ) {
            letter = new GetInitialLetter().getLetter(user.userName);
            user.setInitialLetter(letter);
            return;
        }
        if (letter.equals(DefaultLetter) && !TextUtils.isEmpty(user.userName)) {
            letter = new GetInitialLetter().getLetter(user.userName);
        }
        user.setInitialLetter(letter);
    }
}
