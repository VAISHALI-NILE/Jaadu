package com.example.jaadu;



public class Responcegeneration {
    public String ContactName = null;
    public String inputcnt = null;
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
        if (input.contains("open calculator")) {
            return "opening calculator";
        }
        if (input.contains("open contacts")) {
            return "opening contacts";
        }
        if (input.contains("open email")) {
            return "opening email";
        }
        if (input.contains("open music")) {
            return "opening music";
        }
        if (input.contains("open messages")) {
            return "opening messages";
        }
        if (input.contains("open maps")) {
            return "opening maps";
        }
        if (input.contains("open photos") || input.contains("open gallery")) {
            return "opening photos";
        }
        if(input.contains("open your settings"))
        {
            return "opening my settings";
        }
        if (input.contains("bye"))
        {
            return "Bye bye hope to see u soon!";
        }
        if(input.contains("call"))
        {
            if(ContactName != null){
                return "Calling "+ContactName;
            }
            else
            {
                return "Contact not found";
            }
        }
        return "responce is not predefined";
    }
    public void calling(String c,String in)
    {
        ContactName = c;
        inputcnt = in;
    }
}
