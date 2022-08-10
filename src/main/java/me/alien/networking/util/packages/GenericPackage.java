package me.alien.networking.util.packages;

import java.io.Serializable;

public class GenericPackage<T extends Serializable> extends NetworkPackage{
    public T data;

    public GenericPackage(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "GenericPackage{" +
                "data=" + data +
                '}';
    }
}
