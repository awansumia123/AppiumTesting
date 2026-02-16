package org.example;

public class methodsDemo {

    static void main() {
        methodsDemo d = new methodsDemo();
       String name = d.getData();
        IO.println(name);

    }


    public String getData()
    {
        IO.print("Hello World");
        return "Rahul shetty ";
    }


}



