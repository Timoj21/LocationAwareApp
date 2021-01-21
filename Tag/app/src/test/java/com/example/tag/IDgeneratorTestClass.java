package com.example.tag;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IDgeneratorTestClass {


    @Test
    public void testGenerate(){
        String expected = IDgenerator.generate();
        int length = 10;
        assertEquals(expected.length(), length);
    }
}
