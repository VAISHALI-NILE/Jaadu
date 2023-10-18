package com.example.jaadu;



public class Responcegeneration {
    public String responce(String input)
    {
        input = input.toLowerCase();
        if(input.contains("hi") | input.contains("hello") | input.contains("hey"))
        {
            return "Hello human how can I help you";
        }
        if (input.contains("what is your name"))
        {
            return "I am Jaddu an alien came from far far galaxy";
        }
        if (input.contains("open youtube")) {
            return "opening youtube";
        }
        if (input.contains("open spotify")) {
            return "opening spotify";
        }
        if (input.contains("open calendar")) {
            return "opening calendar";
        }
        if (input.contains("bye"))
        {
            return "Bye bye hope to see u soon!";
        }

        return "responce is not predefined";
    }
}
