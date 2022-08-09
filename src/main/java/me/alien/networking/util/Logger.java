package me.alien.networking.util;

import me.alien.networking.server.Client;

import java.util.Arrays;

public class Logger {
    public static void info(Class clazz, String message){
        System.out.println(clazz.getName()+": "+message);
    }

    public static void warn(Class clazz, String message){
        System.out.println("\\u001B[31m "+clazz.getName()+": "+message);
    }

    public static void exception(Class clazz, StackTraceElement[] stackTrace) {
        System.out.println("\\u001B[31m Exception in "+clazz.getName()+": "+ Arrays.toString(stackTrace));
    }
}
