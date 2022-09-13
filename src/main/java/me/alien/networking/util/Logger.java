package me.alien.networking.util;

import me.alien.networking.server.Client;

import java.util.Arrays;

/**
 * Logger are used to give a more specified information and the class that sent it
 * @author Zacharias Zellén
 */
public class Logger {
    /**
     * A method to print out which class that is doing what
     * @param clazz the class that is sending the message
     * @param message a message that is in the {@link String} format
     */
    public static void info(Class<?> clazz, String message){
        System.out.println(clazz.getName()+": "+message);
    }
    /**
     * A method to print out a warning of which class that is doing what
     * @param clazz the class that is sending the message
     * @param message a message that is in the {@link String} format
     */
    public static void warn(Class<?> clazz, String message){
        System.out.println("\\u001B[31m "+clazz.getName()+": "+message);
    }
    /**
     * A method to print out an exception stack trance and which class that is getting it
     * @param clazz the class that is sending the exception
     * @param stackTrace an array of {@link StackTraceElement} that happened
     */
    public static void exception(Class<?> clazz, StackTraceElement[] stackTrace) {
        System.out.println("\\u001B[31m Exception in "+clazz.getName()+": "+ Arrays.toString(stackTrace));
    }

    /**
     * A method to print out title that is doing something
     * @param title the title to be used for the message, recommendation is to use the same everywhere, example application name
     * @param message the message to be displayed
     */
    public static void info(String title, String message){
        System.out.println(title+": "+message);
    }

    /**
     * A method to print out a warning of which title that is doing something
     * @param title the title to be used for the message, recommendation is to use the same everywhere, example application name
     * @param message a message that is in the {@link String} format
     */
    public static void warn(String title, String message){
        System.out.println("\\u001B[31m "+title+": "+message);
    }
}
