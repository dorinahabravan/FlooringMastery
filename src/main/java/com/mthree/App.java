package com.mthree;

import com.mthree.controller.FlooringMasteryController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class App {
    public static void main(String[] args) {


        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

        applicationContext.scan("com.mthree");
        applicationContext.refresh();
        FlooringMasteryController controller = applicationContext.getBean("flooringMasteryController",
                FlooringMasteryController.class);
        controller.run();


    }
}
