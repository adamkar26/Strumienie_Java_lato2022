package com.SKP;

import java.util.ArrayList;

public class StatefullClass {
    private ArrayList<Integer> nm = new ArrayList<Integer>();

    public int modify(int number){
        if(nm.contains(number)){
            return  number;
        }
        nm.add(number);
        return 0;
    }
}
