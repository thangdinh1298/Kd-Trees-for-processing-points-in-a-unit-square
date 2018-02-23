import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;

import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> rbBST;
    public         PointSET(){
        rbBST = new TreeSet<>();
    }                               // construct an empty set of points
    public           boolean isEmpty(){
        return rbBST.isEmpty();
    }                      // is the set empty?
    public               int size(){
        return rbBST.size();
    }                         // number of points in the set
    public              void insert(Point2D p){
        if(p== null) throw new IllegalArgumentException("Points can't be null");
        rbBST.add(p);
    }              // add the point to the set (if it is not already in the set)
    public           boolean contains(Point2D p){
        return rbBST.contains(p);
    }            // does the set contain point p?
    public              void draw(){
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);

        for (Point2D p : rbBST)
            StdDraw.point(p.x(), p.y());
//        StdDraw.line(0,0,this.nearest(new Point2D(0,0)).x(),this.nearest(new Point2D(0,0)).y());
        StdDraw.show();
    }                         // draw all points to standard draw
    public Iterable<Point2D> range(RectHV rect){
        Stack<Point2D> s = new Stack<>();
        for(Point2D p: rbBST){
            if(rect.contains(p)) s.push(p);
        }
        return s;
    }             // all points that are inside the rectangle (or on the boundary)
    public           Point2D nearest(Point2D p){
        if(rbBST.isEmpty()) return null;
        double minDis = Double.MAX_VALUE;
        double dis = 0;
        Point2D curChamp = null;
        for(Point2D q: rbBST){
            dis = q.distanceTo(p);
            if(dis < minDis) {
                curChamp = q;
                minDis = dis;
            }
        }
        return curChamp;
    }             // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args){
        Point2D base = new Point2D(0,0);
        PointSET set = new PointSET();
        In in  = new In(args[0]);
        while(!in.isEmpty()){
            double x = in.readDouble();
            double y = in.readDouble();
            set.insert(new Point2D(x,y));
        }
        System.out.println(set.isEmpty());
        set.draw();
        System.out.println(set.isEmpty());
        System.out.println(set.size());
    }                  // unit testing of the methods (optional)
}
