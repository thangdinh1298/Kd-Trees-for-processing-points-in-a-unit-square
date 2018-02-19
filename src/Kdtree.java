import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.Comparator;


public class Kdtree {
    Node root;
    Comparator<Point2D> xOrder = Point2D.X_ORDER;
    Comparator<Point2D> yOrder = Point2D.Y_ORDER;
    int N;
    private class Node{
        private boolean isVertical;
        private Point2D p;
        private Node lb;  // left-below nodes
        private Node ru;  // right-upper nodes
        public Node(Point2D p, boolean isVertcal){
            this.p = p;
            this.isVertical = isVertcal;
        }
    }

    public         Kdtree(){
        root = null;
        N = 0;
    }                               // construct an empty set of points
    public           boolean isEmpty(){
        return root == null;
    }                      // is the set empty?
    public               int size(){
        return N;
    }                         // number of points in the set
    private Node insert(Point2D p, Node curNode, boolean isVertical){
        if(curNode == null) {
            N++;
            return new Node(p, isVertical);
        }
        if(isVertical){
            int cmp = xOrder.compare(curNode.p,p);
            if(cmp < 0){
                curNode.ru = insert(p, curNode.ru, !curNode.isVertical);
            }
            else if(cmp > 0){
                curNode.lb = insert(p, curNode.lb, !curNode.isVertical);
            }
//            return curNode; //remove duplicates
        }
        else{
            int cmp = yOrder.compare(curNode.p,p);
            if(cmp < 0){
                curNode.ru = insert(p, curNode.ru, !curNode.isVertical);
            }
            else if(cmp > 0){
                curNode.lb = insert(p, curNode.lb, !curNode.isVertical);
            }
            return curNode;
        }
    }

    public  void insert(Point2D p){

    }              // add the point to the set (if it is not already in the set)
    public           boolean contains(Point2D p){

    }            // does the set contain point p?
    public              void draw() {

    }                        // draw all points to standard draw
    public Iterable<Point2D> range(RectHV rect){

    }             // all points that are inside the rectangle (or on the boundary)
    public           Point2D nearest(Point2D p){

    }             // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args)  {

    }                // unit testing of the methods (optional)
}
