package me.alien.networking.util.packages;

import java.io.Serializable;

/**
 * Used to send standard classes that is {@link Serializable} or can't extend the {@link NetworkPackage}
 * @param <T> It's the standard class that implements the {@link Serializable} interface
 */
public class GenericPackage<T extends Serializable> extends NetworkPackage{
    /**
     * The object of class {@code T} that should be sent
     */
    public T data;

    /**
     * A standard constructor that will address the object to {@link #data}
     * @param data the object of class {@code T} to store
     */
    public GenericPackage(T data) {
        this.data = data;
    }

    /**
     * Default toString() method inherited but modified from {@link Object#toString()}
     * @return a string that's describing the {@link GenericPackage} content
     */
    @Override
    public String toString() {
        return "GenericPackage{" +
                "data=" + data +
                '}';
    }
}
