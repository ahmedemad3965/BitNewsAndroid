package com.bitnews.bitnews.utils;


import android.util.Base64;

public final class PaginationCursorGenerator {
    public static String getPositionCursor(int position) {
        return encodeStringToBase64("p=" + position);
    }

    public static String getPositionReverseCursor(int position, boolean reverse) {
        return encodeStringToBase64("p=" + position + "&r=" + (reverse ? 1 : 0));
    }

    public static String getPositionReverseOffsetCursor(int position, boolean reverse, int offset) {
        return encodeStringToBase64("p=" + position + "&r=" + (reverse ? 1 : 0) + "&o=" + offset);
    }

    private static String encodeStringToBase64(String string) {
        return Base64.encodeToString(string.getBytes(), Base64.DEFAULT);
    }
}