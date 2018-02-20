import edu.princeton.cs.algs4.*;

import java.util.Comparator;


public class KdTree {
    Node root;
    Comparator<Point2D> xOrder = Point2D.X_ORDER;
    Comparator<Point2D> yOrder = Point2D.Y_ORDER;
    int N;
    Stack s = new Stack();
    private class Node{
        private boolean isVertical;
        private Point2D p;
        private Node lb;  // left-below nodes
        private Node ru;  // right-upper nodes
        public Node(Point2D p, boolean isVertcal, RectHV rectHV){
            this.p = p;
            this.isVertical = isVertcal;
            this.rectHV = rectHV;
        }
        private RectHV rectHV;
    }

    public KdTree(){
        root = null;
        N = 0;
    }                               // construct an empty set of points
    public           boolean isEmpty(){
        return root == null;
    }                      // is the set empty?
    public               int size(){
        return N;
    }                         // number of points in the set
    private Node insert(Point2D p, Node curNode, boolean isVertical, RectHV rectHV){
        if(curNode == null) {
            N++;
            return new Node(p, isVertical, rectHV);
        }
        if(isVertical){
            int cmp = xOrder.compare(curNode.p,p);
            if(cmp <= 0){ // todo fix equal points
                curNode.ru = insert(p, curNode.ru, !curNode.isVertical,
                        new RectHV(curNode.p.x(),rectHV.ymin(),rectHV.xmax(),rectHV.ymax()));
            }
            else if(cmp > 0){
                curNode.lb = insert(p, curNode.lb, !curNode.isVertical,
                        new RectHV(rectHV.xmin(),rectHV.ymin(),curNode.p.x(),rectHV.ymax()));
            }
//            return curNode; //remove duplicates
        }
        else{
            int cmp = yOrder.compare(curNode.p,p);
            if(cmp <= 0){// todo: fix equal points
                curNode.ru = insert(p, curNode.ru, !curNode.isVertical,
                        new RectHV(rectHV.xmin(),curNode.p.y(),rectHV.xmax(),rectHV.ymax()));
            }
            else if(cmp > 0){
                curNode.lb = insert(p, curNode.lb, !curNode.isVertical,
                        new RectHV(rectHV.xmin(),rectHV.ymin(),rectHV.xmax(),curNode.p.y()));
            }
        }
        return curNode;
    }

    public  void insert(Point2D p){
        root = insert(p,root,true, new RectHV(0,0,1,1));
    }              // add the point to the set (if it is not already in the set)
    public           boolean contains(Point2D p){
        return contains(p,root);
    }            // does the set contain point p?
    private boolean contains(Point2D p, Node curNode){
        if(curNode == null) return false;
        if(curNode.isVertical){
            int cmp = xOrder.compare(curNode.p,p);
            if(cmp < 0){
                return contains(p,curNode.ru);
            }
            else if(cmp > 0){
                return contains(p,curNode.lb);
            }
            else{
                if(p.equals(curNode.p)) return true;
                return contains(p,curNode.ru);
            }
        }
        else{
            int cmp = yOrder.compare(curNode.p,p);
            if(cmp < 0){
                return contains(p,curNode.ru);
            }
            else if(cmp > 0){
                return contains(p,curNode.lb);
            }
            else{
                if(p.equals(curNode.p)) return true;
                return contains(p,curNode.ru);
            }
        }
    }
    public              void draw() {
        draw(root);
        StdDraw.show();
    }                        // draw all points to standard draw
    private void draw(Node curNode){
        if(curNode == null) return;
        StdDraw.setPenRadius(0.005);
        if(curNode.isVertical){
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(curNode.p.x(),curNode.rectHV.ymin(),curNode.p.x(),curNode.rectHV.ymax());
        }
        else if(!curNode.isVertical){
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(curNode.rectHV.xmin(),curNode.p.y(),curNode.rectHV.xmax(),curNode.p.y());
        }
        StdDraw.setPenRadius(0.015);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(curNode.p.x(),curNode.p.y());
        StdDraw.setPenRadius(0.01);
        curNode.rectHV.draw();
        draw(curNode.lb);
        draw(curNode.ru);
    }
    public Iterable<Point2D> range(RectHV rectHV){
        if (rectHV == null) return null;
        Point2D point2D = range(rectHV,root);
        if(point2D != null) s.push(point2D);
        return s;
    }             // all points that are inside the rectangle (or on the boundary)
    private Point2D range (RectHV rectHV, Node curNode){ // bugged
        if(curNode == null) return null;
        if(curNode.lb != null && curNode.lb.rectHV.intersects(rectHV)){
            Point2D point2D = range(rectHV,curNode.lb);
            if(point2D != null) s.push(point2D);
        }
        if(curNode.ru != null && curNode.ru.rectHV.intersects(rectHV)){
            Point2D point2D = range(rectHV,curNode.ru);
            if(point2D != null) s.push(point2D);
        }
        if(curNode.rectHV.intersects(rectHV)) return curNode.p;
        return null;
    }
//    public           Point2D nearest(Point2D p){
//
//    }             // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args)  {
        KdTree tree2d = new KdTree();
        In in  = new In(args[0]);
        while(!in.isEmpty()){
            double x = in.readDouble();
            double y = in.readDouble();
            tree2d.insert(new Point2D(x,y));
        }
        tree2d.draw();
    }                // unit testing of the methods (optional)
}