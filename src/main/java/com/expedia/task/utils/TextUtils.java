package com.expedia.task.utils;

/**
 *
 * @author Ahmad Khaled Naser
 * info: This class responsible for validation parameters contains three validations:
 *      1. if this string is empty or is null.
 *      2. if this text is number.
 *      3. if this number is large than zero(positive)
 * 
 */
public class TextUtils {

    
    public static boolean isStringEmptyOrNull(String text) {
        if (text == null || text.trim().length() == 0) {
            return true;
        }
        return false;

    }
    
}
