package comp2402a3;

public class Point2D {
	public int x, y;

	public Point2D(int x, int y){
		this.x = x;
		this.y = y;
	}

	public Point2D(){}

	public String toString() {
		return "(" + x + "," + y + ")";
	}

	public boolean equals(Object o){
		if (!(o instanceof Point2D)) return false;
		Point2D p = (Point2D)o;
		if(p.x != this.x)return false;
		if(p.y != this.y)return false;
		return true;
	}
}
