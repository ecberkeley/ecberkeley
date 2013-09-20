package org.ecberkeley.css.games.bounce;

// Description: Panel to layout buttons and graphics area.
// Author: Fred Swartz
// Date:   February 2005
// Author homepage: http://www.fredosaurus.com/fred/index.html
// Copyright: http://www.opensource.org/licenses/mit-license.php

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/////////////////////////////////////////////////////////////////// BBPanel
public class BBPanel extends JPanel {
    BallInBox m_bb;   // The bouncing ball panel
    
    //========================================================== constructor
    /** Creates a panel with the controls and bouncing ball display. */
    public BBPanel() {
        System.out.println("Created BBPanel.......................");
        //... Create components
        m_bb = new BallInBox();

        //m_bb.addBall(0, 0, 2, 3);

       // m_bb.addBall(10, 10, 4, 6);
       // m_bb.addBall(15, 15, 10, 20);

        JButton startButton = new JButton("Start");        
        JButton stopButton  = new JButton("Stop");
        
        //... Add Listeners
        startButton.addActionListener(new StartAction());
        stopButton.addActionListener(new StopAction());
        
        //... Layout inner panel with two buttons horizontally
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        
        //... Layout outer panel with button panel above bouncing ball
        this.setLayout(new BorderLayout());
        this.add(buttonPanel, BorderLayout.NORTH);
        this.add(m_bb       , BorderLayout.CENTER);
    }

    public void addBall(int x, int y, int velocityX, int velocityY){
        m_bb.addBall(x,y,velocityX, velocityY);
    }
    public void addBall(Ball b){
            m_bb.addBall(b);
        }

    ////////////////////////////////////// inner listener class StartAction
    class StartAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            m_bb.setAnimation(true);
        }
    }
    
    
    //////////////////////////////////////// inner listener class StopAction
    class StopAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            m_bb.setAnimation(false);
        }
    }
}//endclass BBPanel
