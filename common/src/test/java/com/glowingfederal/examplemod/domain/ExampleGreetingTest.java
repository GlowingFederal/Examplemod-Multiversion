package com.glowingfederal.examplemod.domain;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExampleGreetingTest {
    @Test public void greetsPlayer() {
        assertEquals("Hello, Alex! Welcome to ExampleMod.", ExampleGreeting.create("Alex"));
    }
    @Test public void trimsNames() {
        assertEquals("Hello, Steve! Welcome to ExampleMod.", ExampleGreeting.create("  Steve  "));
    }
    @Test public void preservesUnicode() {
        assertEquals("Hello, Zoë! Welcome to ExampleMod.", ExampleGreeting.create("Zoë"));
    }
    @Test(expected = IllegalArgumentException.class) public void rejectsNull() {
        ExampleGreeting.create(null);
    }
    @Test(expected = IllegalArgumentException.class) public void rejectsBlank() {
        ExampleGreeting.create("  ");
    }
}
