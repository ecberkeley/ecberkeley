package org.ecberkeley.css.collections.samplebeans;

public class BinaryTree {
    public BinaryTree(String s){
        rootnode = new TreeNode(s);
    }

    static class TreeNode {

        TreeNode leftReference;
        TreeNode rightReference;
        String nodeValue;

        public TreeNode(String nodeValue) {
            this.nodeValue = nodeValue;
        }
    }

    public static void main(String[] args) {
        //new BinaryTree().run();
        BinaryTree bt = new BinaryTree("dddd");
        bt.add("aaaaa");
        bt.add("ffff");
        bt.add("ddddd");
        bt.add("bbbbb");
        bt.add("ccccc");
        bt.add("hhhh");
        bt.printTree();
        System.out.println("search: " + bt.search("ccccc"));
    }

    public void run() {
        // tree root node.
        String rootValue = "A";
        TreeNode rootnode = new TreeNode(rootValue);
        System.out.println("root node created. " + rootnode.nodeValue);

        // insertNode new element starting with rootnode.
        insertNode(rootnode, "AA");
        insertNode(rootnode, "BB");
        insertNode(rootnode, "cc");
        insertNode(rootnode, "dd");
        insertNode(rootnode, "EE");
        System.out.println("print the content of  tree in  binary tree order");
        System.out.println("print the content of  tree in  binary tree order");

        printTree(rootnode, 0);

    }

    private TreeNode rootnode = new TreeNode("A");

    public void add(String s) {
        insertNode(rootnode, s);
    }

    /*
     *
     *  inserting new parentNode to tree.
     *  binary tree set the smaller nodeValue on left and the
        bigger nodeValue on the right
     * parent TreeNode in the first case will be root node.
     */
    public void insertNode(TreeNode parentNode, String nodeValue) {
        if (nodeValue.compareTo(parentNode.nodeValue) < 0) {
            if (parentNode.leftReference != null) {
                // Go more depth to left.
                insertNode(parentNode.leftReference, nodeValue);
            } else {
                //System.out.println(" LEFT:   new node value " + nodeValue + "  , its root  " + parentNode.nodeValue);
                parentNode.leftReference = new TreeNode(nodeValue);
            }
        } else if (nodeValue.compareTo(parentNode.nodeValue) >= 0) {

            if (parentNode.rightReference != null) {
                // Go more depth to right
                insertNode(parentNode.rightReference, nodeValue);
            } else {
                //System.out.println("  Right: new node value  " + nodeValue + ", its root " + parentNode.nodeValue);
                parentNode.rightReference = new TreeNode(nodeValue);
            }
        }
    }

    String indent(int level)  {
        StringBuilder sb = new  StringBuilder();
        for (int i=0; i<=level; i++){
            sb.append("    ");
        }
        return sb.toString();
    }
    /*
     *
     * recursivly printing the content of the tree.
     */
    public void printTree(TreeNode node, int level) {
        String ind = indent(level);
        if (node != null) {
            System.out.println(ind+ node.nodeValue);
            //System.out.println(ind+"left:");
            printTree(node.leftReference, level+1);
            //System.out.println(ind+"right:");
            printTree(node.rightReference, level+1);
        }  else {
            System.out.println(ind+ "<null>");
        }

    }
    public void printTree(){
        printTree(rootnode, 0);
    }

    public TreeNode search(String nodeValue) {
        return search(rootnode, nodeValue);

    }

    public TreeNode search(TreeNode parentNode, String nodeValue) {
        int c = nodeValue.compareTo(parentNode.nodeValue);
        if (c == 0) {
            return parentNode;
        } else if (c < 0) {
            if (parentNode.leftReference != null) {
                return search(parentNode.leftReference, nodeValue);
            }
        } else {
            if (parentNode.rightReference != null) {
                return search(parentNode.rightReference, nodeValue);
            }
        }
        return null;
    }
}
