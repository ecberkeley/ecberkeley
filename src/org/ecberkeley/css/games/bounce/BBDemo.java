package org.ecberkeley.css.games.bounce;


// Description: Illustrates animation with a ball bouncing in a box
//              Possible extensions: faster/slower button,
// Author: Fred Swartz
// Date:   February 2005 ...
// Author homepage: http://www.fredosaurus.com/fred/index.html
// Copyright: http://www.opensource.org/licenses/mit-license.php

import javax.swing.*;

public class BBDemo extends JFrame {
    
    public BBDemo() {
        setTitle("Bouncing Ball Demo 2");
        bbPanel =  new BBPanel();
        add(bbPanel);
        setContentPane(bbPanel);
        pack();
        setVisible(true);
        getBBPanel().addBall(5,5,8,8);
        //getBBPanel().addBall(50, 10, 6, 3);
    }

    private BBPanel bbPanel;
    public BBPanel getBBPanel(){
        return bbPanel;
    }
    
    public static void main(String[] args) {
        BBDemo bbDemo = new BBDemo();
        bbDemo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
