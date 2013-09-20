package org.ecberkeley.css.lang;

public class Cloning implements Cloneable {

    public String myMember = "helloMyMember";

    public String toString() {
        return "MyObject:" + myMember;
    }

    public Cloning clone() throws CloneNotSupportedException {
        //Object.clone does the construction of *this* class, if we just say "implements Cloneable".
        Cloning other = (Cloning) super.clone();
        other.myMember = "helloClone";
        return other;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Cloning.clone() " + ((new Cloning()).clone()));
        String[] sa = {"aa", "bb", "cc"};
        String[] sb = sa.clone();

        sa[1] = "zz";
        for (String s: sb){
            System.out.println("clone: "+s);
        }
    }
}
