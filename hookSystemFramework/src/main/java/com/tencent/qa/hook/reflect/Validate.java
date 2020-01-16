package com.tencent.qa.hook.reflect;

/**
 * Created by richarddu on 2018/6/22.
 */
class Validate {

    static void isTrue(final boolean expression, final String message, final Object... values) {
        if (expression == false) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }
}
