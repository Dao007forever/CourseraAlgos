import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class KdTree {
	private static class Node {
		Point2D point;
		Comparator<Point2D> comparator;
		int size;
		Node left, right;
		
		public Node(Point2D point, boolean xOrder, int size) {
			this.point = point;
			if (xOrder)
				this.comparator = Point2D.X_ORDER;
			else this.comparator = Point2D.Y_ORDER;
			this.size = size;
		}
	}
	
	private Node root;
	
	public KdTree() {
	}
	
	public boolean isEmpty() {
		// is the set empty?
		return (root == null);
	}
	
	public int size() {
		if (root == null) return 0;
		// number of points in the set
		return root.size;
	}
	
	private int size(Node x) {
        if (x == null) return 0;
        else return x.size;
    }
	
	public void insert(Point2D p) {
		// add the point p to the set (if it is not already in the set)
		root = insert(root, p, true);
	}
	
	private Node insert(Node n, Point2D point, boolean xOrder) {
		if (n == null) {
			return new Node(point, xOrder, 1);
		}
		
		if (n.point.compareTo(point) == 0) return n;
		
		int cmp = n.comparator.compare(point, n.point);
		if (cmp < 0) n.left = insert(n.left, point, !xOrder);
		else if (cmp >= 0) n.right = insert(n.right, point, !xOrder);
		n.size = size(n.left) + size(n.right) + 1;
		return n;
	}
	
	public boolean contains(Point2D p) {
		// does the set contain the point p?
		return contains(root, p);
	}
	
	private boolean contains(Node n, Point2D p) {
		if (n == null) return false;
		if (n.point.compareTo(p) == 0) return true;
		
		int cmp = n.comparator.compare(p, n.point);
		if (cmp < 0) return contains(n.left, p);
		else return contains(n.right, p);
	}
	
	public void draw() {
		// draw all of the points to standard draw
		draw(root);
	}
	
	private void draw(Node n) {
		if (n == null) return;
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(.01);
		n.point.draw();
		
		if (n.comparator == Point2D.X_ORDER) {
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.setPenRadius();
			StdDraw.line(n.point.x(), 0, n.point.x(), 1);
		} else {
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.setPenRadius();
			StdDraw.line(0, n.point.y(), 1, n.point.y());
		}
		
		draw(n.left);
		draw(n.right);
	}
	
	private int intersectCmp(Node n, RectHV r) {
		Point2D p1 = new Point2D(r.xmin(), r.ymin());
		Point2D p2 = new Point2D(r.xmax(), r.ymax());
		int cmp1 = n.comparator.compare(n.point, p1);
		int cmp2 = n.comparator.compare(n.point, p2);
		
		if (cmp1 > 0 && cmp2 <= 0) {
			return 0;
		} else if (cmp2 > 0) {
			return -1;
		} else {
			return 1;
		}
	}
	
	public Iterable<Point2D> range(RectHV rect) {
		// all points in the set that are inside the rectangle
		return range(root, rect);
	}
	
	private List<Point2D> range(Node n, RectHV rect) {
		if (n == null) return Collections.emptyList();
		
		List<Point2D> points = new ArrayList<Point2D>();
		if (rect.contains(n.point)) {
			points.add(n.point);
		}
		int cmp = intersectCmp(n, rect);
		if (cmp == 0) {
			points.addAll(range(n.left, rect));
			points.addAll(range(n.right, rect));
		} else if (cmp == -1) {
			points.addAll(range(n.left, rect));
		} else {
			points.addAll(range(n.right, rect));
		}

		return points;
	}
	
	public Point2D nearest(Point2D p) {
		RectHV r = new RectHV(0, 0, 1, 1);
		return nearest(root, p, Double.MAX_VALUE, r);
	}
	
	private Point2D nearest(Node n, Point2D p, double currentMin, RectHV currentRect) {
		if (n == null) return null;
		
		Point2D min = null;
		double dist = p.distanceSquaredTo(n.point);
		if (dist == 0) {
			return n.point;
		}
		if (dist < currentMin) {
			currentMin = dist;
			min = n.point;
		}
		
		RectHV rLeft = null, rRight = null;
		int cmp = n.comparator.compare(p, n.point);
		if (n.comparator == Point2D.X_ORDER) {
			rLeft = new RectHV(currentRect.xmin(), currentRect.ymin(),
					n.point.x(), currentRect.ymax());
			rRight = new RectHV(n.point.x(), currentRect.ymin(),
					currentRect.xmax(), currentRect.ymax());
		} else {
			rLeft = new RectHV(currentRect.xmin(), currentRect.ymin(),
					currentRect.xmax(), n.point.y());
			rRight = new RectHV(currentRect.xmin(), n.point.y(),
					currentRect.xmax(), currentRect.ymax());
		}
		
		if (cmp <= 0) {
			Point2D left = nearest(n.left, p, currentMin, rLeft);
			if (left != null) {
				double distance = p.distanceSquaredTo(left);
				if (currentMin > distance) {
					currentMin = distance;
					min = left;
				}
			}
			
			if (rRight.distanceSquaredTo(p) < currentMin) {
				Point2D right = nearest(n.right, p, currentMin, rRight);
				if (right != null) {
					double distance = p.distanceSquaredTo(right);
					if (currentMin > distance) {
						currentMin = distance;
						min = right;
					}
				}
			}
		} else {
			Point2D right = nearest(n.right, p, currentMin, rRight);
			if (right != null) {
				double distance = p.distanceSquaredTo(right);
				if (currentMin > distance) {
					currentMin = distance;
					min = right;
				}
			}
			
			if (rLeft.distanceSquaredTo(p) < currentMin) {
				Point2D left = nearest(n.left, p, currentMin, rLeft);
				if (left != null) {
					double distance = p.distanceSquaredTo(left);
					if (currentMin > distance) {
						currentMin = distance;
						min = left;
					}
				}
			}
		}
		
		return min;
	}
}
