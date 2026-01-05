package com.pdmadmin.pdmadmin.util;

public class UserContext {
    private static final ThreadLocal<Long> UID = new ThreadLocal<>();
    public static void set(Long id){ UID.set(id); }
    public static Long get(){ return UID.get(); }
    public static void clear(){ UID.remove(); }
}
