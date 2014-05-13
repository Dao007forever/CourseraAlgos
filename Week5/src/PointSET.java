import java.util.ArrayList;
import java.util.List;

public class PointSET {
	private SET<Point2D> points;
	
	public PointSET() {
		// construct an empty set of points
		points = new SET<Point2D>();
	}
   
	public boolean isEmpty() {
		// is the set empty?
		return points.isEmpty();
	}
	
	public int size() {
		// number of points in the set
		return points.size();
	}
	
	public void insert(Point2D p) {
		// add the point p to the set (if it is not already in the set)
		points.add(p);
	}
	
	public boolean contains(Point2D p) {
		// does the set contain the point p?
		return points.contains(p);
	}
	
	public void draw() {
		// draw all of the points to standard draw
		for (Point2D p : points) {
			p.draw();
		}
	}
	
	public Iterable<Point2D> range(RectHV rect) {
		// all points in the set that are inside the rectangle
		List<Point2D> result = new ArrayList<Point2D>();
		for (Point2D p : points) {
			if (rect.contains(p)) {
				result.add(p);
			}
		}
		return result;
	}
	
	public Point2D nearest(Point2D p) {
		// a nearest neighbor in the set to p; null if set is empty
		if (points.isEmpty()) return null;
		
		Point2D save = points.iterator().next();
		double min = p.distanceSquaredTo(points.iterator().next());
		double distance;
		for (Point2D n : points) {
			distance = n.distanceSquaredTo(p);
			if (distance < min) {
				min = distance;
				save = n;
			}
		}
		return save;
	}
}