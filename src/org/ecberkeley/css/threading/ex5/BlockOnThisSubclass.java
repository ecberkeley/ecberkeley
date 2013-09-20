package org.ecberkeley.css.threading.ex5;

public class BlockOnThisSubclass extends BlockOnThis {
    public void run(){
        while (true){
            block();
        }
    }
}
