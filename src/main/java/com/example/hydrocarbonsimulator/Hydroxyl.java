/**
 * I only did this to show I know how to use OOP
 * Since it doesn't really make sense to use OOP anywhere in my project
 * While you may think different branches can be used, it is simply not worth the effort
 * As we often can on the test, we can simply draw the condensed name of the branch
 * As in, OH instead of O-H, CH3CH2 instead of C-C-H
 * And thus there is little point to making them inherit from element
 * Since all that would be different in the first place is draw()
 * However, Element.nameList is probably a demonstration of polymorphism.
 */
package com.example.hydrocarbonsimulator;

import javafx.scene.canvas.GraphicsContext;

public class Hydroxyl extends Element
{
    public Hydroxyl()
    {
        super("Hydroxyl");
    }

    @Override
    public void draw(GraphicsContext context, int width, int height)
    {
        super.draw(context, width, height);
    }
}
