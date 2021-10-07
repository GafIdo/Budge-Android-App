package ido.apps.finance.budge.util;

import java.util.concurrent.atomic.AtomicLong;

public class Ided {

    protected static long createUniqueId() {
        return System.currentTimeMillis()/1000;
    }

}

