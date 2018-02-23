import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Comparator;


public class KdTree {
    private Node root;
    private Comparator<Point2D> xOrder = Point2D.X_ORDER;
    private Comparator<Point2D> yOrder = Point2D.Y_ORDER;
    private int N;
    private class Node{
        private boolean isVertical;
        private Point2D p;
        private Node lb;  // left-below nodes
        private Node ru;  // right-upper nodes
        public Node(Point2D p, boolean isVertical, RectHV rectHV){
            this.p = p;
            this.isVertical = isVertical;
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
    private Node insert(Point2D p, Node curNode, boolean isVertical, double xmin, double ymin, double xmax, double ymax){
        if(curNode == null) {
            N++;
            return new Node(p, isVertical, new RectHV(xmin,ymin,xmax,ymax));
        }
        if(p.equals(curNode.p)) return curNode; // if the points are equal, return the node
        if(isVertical){
            int cmp = xOrder.compare(curNode.p,p);
            if(cmp <= 0){
                curNode.ru = insert(p, curNode.ru, !curNode.isVertical,
                        curNode.p.x(),curNode.rectHV.ymin(),curNode.rectHV.xmax(),curNode.rectHV.ymax());
            }
            else if(cmp > 0){
                curNode.lb = insert(p, curNode.lb, !curNode.isVertical,
                        curNode.rectHV.xmin(),curNode.rectHV.ymin(),curNode.p.x(),curNode.rectHV.ymax());
            }

        }
        else{
            int cmp = yOrder.compare(curNode.p,p);
            if(cmp <= 0){
                curNode.ru = insert(p, curNode.ru, !curNode.isVertical,
                        curNode.rectHV.xmin(),curNode.p.y(),curNode.rectHV.xmax(),curNode.rectHV.ymax());
            }
            else if(cmp > 0){
                curNode.lb = insert(p, curNode.lb, !curNode.isVertical,
                        curNode.rectHV.xmin(),curNode.rectHV.ymin(),curNode.rectHV.xmax(),curNode.p.y());
            }
        }
        return curNode;
    }

    public  void insert(Point2D p){
        if(p == null) throw new IllegalArgumentException();
        root = insert(p,root,true, 0,0,1,1);
    }              // add the point to the set (if it is not already in the set)
    public           boolean contains(Point2D p){
        if(p == null) throw new IllegalArgumentException();
        return contains(p,root);
    }            // does the set contain point p?
    private boolean contains(Point2D p, Node curNode){
        if(curNode == null) return false;
        if(curNode.p.equals(p)) return true;
        else{
            if(curNode.isVertical){
                int cmp = xOrder.compare(curNode.p,p);
                if(cmp <= 0){
                    return contains(p,curNode.ru);
                }
                else {
                    return contains(p,curNode.lb);
                }
            }
            else{
                int cmp = yOrder.compare(curNode.p,p);
                if(cmp <= 0){
                    return contains(p,curNode.ru);
                }
                else {
                    return contains(p,curNode.lb);
                }
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
        draw(curNode.lb);
        draw(curNode.ru);
    }
    public Iterable<Point2D> range(RectHV rectHV){
        if (rectHV == null) throw new IllegalArgumentException();
        if (root == null) return null;
        Stack<Point2D> s = new Stack();
        Point2D point2D = range(rectHV,root,s);
        if(point2D != null) s.push(point2D);
        return s;
    }             // all points that are inside the rectangle (or on the boundary)
    private Point2D range (RectHV rectHV, Node curNode, Stack<Point2D> s){
        if(curNode.lb != null && curNode.lb.rectHV.intersects(rectHV)){
            Point2D point2D = range(rectHV,curNode.lb,s);
            if(point2D != null) s.push(point2D);
        }
        if(curNode.ru != null && curNode.ru.rectHV.intersects(rectHV)){
            Point2D point2D = range(rectHV,curNode.ru,s);
            if(point2D != null) s.push(point2D);
        }
        if(curNode.rectHV.intersects(rectHV)){
            if(rectHV.contains(curNode.p))
            return curNode.p;
        }
        return null;
    }
    public Point2D nearest(Point2D p){
        if(p == null) throw new IllegalArgumentException();
        if (root == null) return null;
        if(root == null) return null;
        double[] nearestDisSquared = {Double.MAX_VALUE};
        Point2D[] nearestPoint = new Point2D[1];
        nearest(p,root, nearestDisSquared, nearestPoint);
        return nearestPoint[0];
    }             // a nearest neighbor in the set to point p; null if the set is empty

    private void nearest(Point2D p, Node curNode, double[] distance, Point2D[] nearestPoint){
        if(p.distanceSquaredTo(curNode.p) <= distance[0]){
            distance[0] = p.distanceSquaredTo(curNode.p);
            nearestPoint[0] = curNode.p;
        }
        if(curNode.lb != null && curNode.ru != null){
            double disToLb = curNode.lb.rectHV.distanceSquaredTo(p);
            double disToRu = curNode.ru.rectHV.distanceSquaredTo(p);
            if(disToLb < disToRu){
                if( disToLb <= distance[0]){
                    nearest(p,curNode.lb,distance,nearestPoint);
                }
                if(disToRu <= distance[0]){
                    nearest(p,curNode.ru, distance, nearestPoint);
                }
            }
            else if(disToLb >= disToRu){
                if(disToRu <= distance[0]){
                    nearest(p,curNode.ru, distance, nearestPoint);
                }
                if( disToLb <= distance[0]){
                    nearest(p,curNode.lb,distance,nearestPoint);
                }
            }
        }
        else if( curNode.lb != null){
            double disToLb = curNode.lb.rectHV.distanceSquaredTo(p);
            if(disToLb < distance[0])
            nearest(p,curNode.lb,distance,nearestPoint);
        }
        else if( curNode.ru != null){
            double disToRu = curNode.ru.rectHV.distanceSquaredTo(p);
            if(disToRu < distance[0])
            nearest(p,curNode.ru, distance, nearestPoint);
        }
    }
    public static void main(String[] args)  {
        KdTree tree2d = new KdTree();
        ArrayList<Point2D> arrayList = new ArrayList<>();
        In in  = new In(args[0]);
        while(!in.isEmpty()){
            double x = in.readDouble();
            double y = in.readDouble();
            tree2d.insert(new Point2D(x,y));
            arrayList.add(new Point2D(x,y));
        }
        tree2d.draw();
//        for(Point2D p: arrayList){
//            System.out.println(tree2d.contains(p));
//        }
        Point2D query = new Point2D(0.99,0.99);
        Point2D nearest = tree2d.nearest(query);
        StdDraw.line(nearest.x(),nearest.y(),query.x(),query.y());
    }                // unit testing of the methods (optional)
}
