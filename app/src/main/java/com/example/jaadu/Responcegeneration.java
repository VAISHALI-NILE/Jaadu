package com.example.jaadu;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Responcegeneration {
    private Context context;
    public static String ContactName = null;
    public String inputcnt = null;
    public Responcegeneration(Context context)
    {
        this.context = context;
    }
    public String responce(String input)
    {
        input = input.toLowerCase();
        if( input.contains("hello") )
        {
            return "Hello human how can I help you";
        }
        if(input.contains("hii") )
        {
            return "Hello there! What brings you here";
        }
        if(input.contains("hey"))
        {
            return "Hey! What's up, Earthling";
        }

        if (input.contains("your name") | input.contains("who are you") |input.contains("what is your name"))
        {
            return "I am Jaddu an alien came from far far galaxy To assist you";
        }
        if (input.contains("open youtube")) {
            return "Opening YouTube for you";
        }
        if (input.contains("open spotify")) {
            return "Launching Spotify for your entertainment";
        }
        if (input.contains("open calendar")) {
            return "Opening the calendar. Any important dates";
        }
        if (input.contains("open calculator")) {
            return "Sure, opening the calculator for you";
        }
        if (input.contains("open contacts")) {
            return "Accessing your contacts";
        }
        if (input.contains("open email")) {
            return "Opening your email. Any important messages";
        }
        if (input.contains("open music")) {
            return "Let's play some music! Opening your music app";
        }
        if (input.contains("open messages")) {
            return "Checking your messages. Anything urgent";
        }
        if (input.contains("open maps")) {
            return "Launching maps";
        }
        if (input.contains("open photos") || input.contains("open gallery")) {
            return "Opening your photo gallery. Memories await!";
        }
        if(input.contains("open your settings"))
        {
            return "opening my settings";
        }
        if(input.contains("tell me a space fact"))
        {
            return "Did you know that a day on Venus is longer than a year on Venus? It takes about 243 Earth days for Venus to complete one rotation, but only about 225 Earth days to orbit the Sun!";
        }
        if (input.contains("tell me a joke"))
        {
            return "Sure thing! Why did the astronaut break up with the star? It needed space!...\nHaaa haaa haaa!";
        }
        if(input.contains("can you predict the future"))
        {
            return "The future is as mysterious as a black hole! But let me try \nI predict more adventures, laughter, and a cosmic bond between us.";
        }
        if(input.contains("sing a song"))
        {
            return "Sure thing! Let's sing \n You should take it as a compliment\n" +
                    "That I got drunk and made fun of the way you talk\n" +
                    "You should think about the consequence\n" +
                    "Of your magnetic field being a little too strong\n" +
                    "And I got a boyfriend, he's older than us\n" +
                    "He's in the club doing, I don't know what\n" +
                    "You're so cool, it makes me hate you so much (I hate you so much)\n" +
                    "Whisky on ice, Sunset and Vine\n" +
                    "You've ruined my life, by not being mine\n" +
                    "You're so gorgeous\n" +
                    "I can't say anything to your face\n" +
                    "'Cause look at your face (gorgeous)\n" +
                    "And I'm so furious\n" +
                    "At you for making me feel this way\n" +
                    "But what can I say?\n" +
                    "You're gorgeous";
        }
        if(input.contains("what is your favorite food") | input.contains("what food you like")| input.contains("what you like to eat"))
        {
            return "Oh, I'm fascinated by popcorn! The way it pops is like a tiny, edible supernova. A delightful treat during my movie nights on Earth.";
        }
        if(input.contains("what is the meaning of life"))
        {
            return "Ah, the eternal question! The meaning of life is like an intergalactic dance – full of twists, turns, and a sprinkle of stardust. It's a journey of discovery!";
        }
        if (input.contains("who is your developer") | input.contains("who is your developer"))
        {
            return "The great Vaishali Nile";
        }
        if(input.contains("who is your friend"))
        {
            return "Vaishali Nile is my only friend  so i don't need any one";
        }
        if(input.contains("do you like to dance"))
        {
            return "yes.. Will you give me the pleasure to dance with you";
        }
        if (input.contains("bye"))
        {
            return "bye,Goodbye! Until we meet again.";
        }
        if (input.contains("good night"))
        {
            return "You to have a Good night and Sleep tight";
        }
        if(input.contains("play") && input.contains("on youtube") )
        {
            return "Playing video on Youtube";
        }
        if (input.contains("set timer for")) {
            return "Setting timer";
        }
        if (input.contains("set alarm for")) {
            return "Setting alarm";
        }
        if (input.contains("do you want to become friends") | input.contains("will you be my friend")) {
            return "I don't mind but my developer is bit possessive so u gota deal with it....hmmm";
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
        else if(input.contains("how") || input.contains("what")||input.contains("who")||input.contains("search"))
        {
            String searchQuery =input;

            performGoogleSearch(searchQuery);
            return "Here are some results";
        }
        else {
            return "my developer didn't teach me this one";
            //return "Think and ask again ... be mature";
        }
    }
    private void performGoogleSearch(String searchQuery) {
        String searchUrl = "https://www.google.com/search?q=" + Uri.encode(searchQuery);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl));
        context.startActivity(intent);
    }
    public void calling(String c)
    {
        ContactName = c;
//        inputcnt = in;
    }
}
