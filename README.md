How I did it
============

1. Download IntelliJ
2. Create new Java project
3. Create lib directory
4. copy core.jar there
5. Right-click lib directory, "Add as library..."
6. Copy-paste the example code:

package com.e_gueli;

import processing.core.PApplet;

public class Main extends PApplet {

    public void settings(){
        size(200, 200);
    }

    public void draw(){
        background(0);
        ellipse(mouseX, mouseY, 20, 20);
    }

    public static void main(String... args){
        PApplet.main(Main.class.getName());
    }
}

7. Tools > Kotlin > Configure Kotlin in project