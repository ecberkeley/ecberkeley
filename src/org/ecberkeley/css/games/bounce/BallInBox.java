package org.ecberkeley.css.games.bounce;

// Description: This Graphics panel simulates a ball bouncing in a box.
//         Animation is done by changing instance variables
//         in the timer's actionListener, then calling repaint().
//         * Flicker can be reduced by drawing into a BufferedImage, 
//           and/or using a clip region.
//         * The edge of the oval could be antialiased (using Graphics2).
// Author: Fred Swartz
// Date:   February 2005
// Author homepage: http://www.fredosaurus.com/fred/index.html
// Copyright: http://www.opensource.org/licenses/mit-license.php

// Modified 2011-07-13 Laramie Crocker, ecberkeley.org

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/////////////////////////////////////////////////////////////// BouncingBall
public class BallInBox extends JPanel {
    //============================================== fields
    //... Instance variables representing the ball.
    private List<Ball> ballList = new ArrayList<Ball>() ;
    public void addBall(int x, int y, int velocityX, int velocityY){
        Ball b = new Ball(x,y,velocityX, velocityY);
        ballList.add(b);
    }
    public void addBall(Ball b){
        ballList.add(b);
    }
    //private Ball m_ball         = new Ball(0, 0, 2, 3);
    //private Ball m_ball2         = new Ball(10, 10, 4, 6);

    //... Instance variables for the animiation
    private int   m_interval  = 5;  // Milliseconds between updates.
    private Timer m_timer;           // Timer fires to anmimate one step.

    //========================================================== constructor
    /** Set panel size and creates timer. */
    public BallInBox() {
        setPreferredSize(new Dimension(200, 80));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        m_timer = new Timer(m_interval, new TimerAction());
    }
    
    //========================================================= setAnimation
    /** Turn animation on or off.
     *@param turnOnOff Specifies state of animation.
     */
    public void setAnimation(boolean turnOnOff) {
        if (turnOnOff) {
            m_timer.start();  // start animation by starting the timer.
        } else {
            m_timer.stop();   // stop timer
        }
    }

    //======================================================= paintComponent
    public void paintComponent(Graphics g) {
        super.paintComponent(g);  // Paint background, border
        for (Ball b: ballList){
            b.draw(g);           // Draw the ball.
        }
    }
    
    //////////////////////////////////// inner listener class ActionListener
    class TimerAction implements ActionListener {
        int i = 0;
        //================================================== actionPerformed
        /** ActionListener of the timer.  Each time this is called,
         *  the ball's position is updated, creating the appearance of
         *  movement.
         *@param e This ActionEvent parameter is unused.
         */
        public void actionPerformed(ActionEvent e) {
            for (Ball b: ballList){
                b.setBounds(getWidth(), getHeight());
                b.move();  // Move the ball.
            }
            repaint();      // Repaint indirectly calls paintComponent.
            //i++;
            //paintImmediately(0,0,400, 400);
            //System.out.println("painting "+i);
        }
    }
}//endclass
