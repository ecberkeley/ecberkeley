package org.ecberkeley.css.plots;

//This code was copied and modified from the jmath plot code at
//   http://code.google.com/p/jmathplot/
//   see license: http://www.opensource.org/licenses/bsd-license.php
//  Also used are the libraries:
//   http://code.google.com/p/jmatharray/
// See also:
//   http://code.google.com/p/surfaceplotter

import javax.swing.*;
 
import org.math.plot.*;
import static java.lang.Math.*;
import static org.math.array.DoubleArray.*;
 
public class GridPlotsExample {
        private Plot3DPanel plot;
        public Plot3DPanel getPlot(){
        	return plot;
        }
        
        public GridPlotsExample(){
                // create your PlotPanel (you can use it as a JPanel) with a legend at SOUTH
                this.plot = new Plot3DPanel("SOUTH");
                // put the PlotPanel in a JFrame like a JPanel
                JFrame frame = new JFrame("a plot panel");
                frame.setSize(600, 600);
                frame.setContentPane(plot);
                frame.setVisible(true);
        }
        
        public void addExamplePlots(){
        	// define your data
                double[] x = increment(0.0, 0.1, 1.0); 
                double[] y = increment(0.0, 0.05, 1.0);
                double[][] z1 = f1(x, y);
                double[][] z2 = f2(x, y);
                
                // add grid plot to the PlotPanel
                getPlot().addGridPlot("z=cos(PI*x)*sin(PI*y)", x, y, z1);
                getPlot().addGridPlot("z=sin(PI*x)*cos(PI*y)", x, y, z2);
                
        }
 
        // function definition: z=cos(PI*x)*sin(PI*y)
        public static double f1(double x, double y) {
                double z = cos(x * PI) * sin(y * PI);
                return z;
        }
 
        // grid version of the function
        public static double[][] f1(double[] x, double[] y) {
                double[][] z = new double[y.length][x.length];
                for (int i = 0; i < x.length; i++)
                        for (int j = 0; j < y.length; j++)
                                z[j][i] = f1(x[i], y[j]);
                return z;
        }
 
        // another function definition: z=sin(PI*x)*cos(PI*y)
        public static double f2(double x, double y) {
                double z = sin(x * PI) * cos(y * PI);
                return z;
        }
 
        // grid version of the function
        public static double[][] f2(double[] x, double[] y) {
                double[][] z = new double[y.length][x.length];
                for (int i = 0; i < x.length; i++)
                        for (int j = 0; j < y.length; j++)
                                z[j][i] = f2(x[i], y[j]);
                return z;
        }
        
        //============= main entry method ==================
        
        public static void main(String[] args) {
        	GridPlotsExample gp = new GridPlotsExample();
        	gp.addExamplePlots();
        	//another example: 
            //     gp.getPlot().addGridPlot("z=0.5*cos(PI*x)*3*sin(PI*y)", x, y, z1);
        }
}
