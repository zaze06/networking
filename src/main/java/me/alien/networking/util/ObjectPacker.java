package me.alien.networking.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ObjectPacker {
    ArrayList<Pair<Class, Character>> classHeaders = new ArrayList<>();

    /**
     * Registers a class to a Character for networking.
     * @param clazz The class that should be assigned a character
     * @param header The specific character to assign the class too
     * @throws IndexOutOfBoundsException If the character is outside the range of the standers ASCII table
     * @throws ArrayStoreException If the clazz or header is already specified
     * @deprecated use {@link #registerClass(Class)} instead.
     */
    @Deprecated
    public void registerClass(Class clazz, char header){
        if((int)header > 126){
            throw new IndexOutOfBoundsException(header);
        }
        if(classHeaders.stream().anyMatch(pair -> pair.getValue()==header||pair.getKey()==clazz)){
            throw new ArrayStoreException("Already contain header or clazz");
        }
        classHeaders.add(new Pair<>(clazz, header));
    }

    /**
     * Registers a class to a Character for networking.
     * @param clazz The class that should be assigned a character
     * @throws IndexOutOfBoundsException If the character is outside the range of the standers ASCII table
     */
    public void registerClass(Class clazz){
        int header = ((int)classHeaders.get(classHeaders.size()-1).getValue())+1;
        if(header > 126){
            throw new IndexOutOfBoundsException(header);
        }
        classHeaders.add(new Pair<>(clazz, (char) header));
    }

    /**
     * returns the class connected to the header.
     * @param header a header character that specifies a class in the list.
     * @return the class that the header is connected too
     * @throws NullPointerException if the header wasn't connected to a class.
     */
    public Class getClass(char header){
        List<Pair<Class, Character>> list = classHeaders.stream().filter(pair -> pair.getValue() == header).toList();
        if(list.isEmpty()){
            throw new NullPointerException("header have not ben specified to a class");
        }
        return list.get(0).getKey();
    }
}
