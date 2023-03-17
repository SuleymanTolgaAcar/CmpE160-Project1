import java.awt.Color;

public class suleyman_tolga_acar {
    public static void main(String[] args) {
        int canvas_width = 400; // specify canvas width
        int canvas_height = 400; // specify canvas height
        StdDraw.setCanvasSize(canvas_width, canvas_height);
        StdDraw.setXscale(0, 1); // x axis scale is in the range [0,1]
        StdDraw.setYscale(0, 1); // y axis scale is in the range [0,1]
        double circle_center_x = 0.5; // circle parameters: (x,y) coordinates, radius
        double circle_center_y = 0.5;
        double circle_radius = 0.45;
        double circle_border_thickness = 0.012; // circle border thickness
        Color circle_color = StdDraw.PRINCETON_ORANGE;
        StdDraw.clear(StdDraw.WHITE); // clear the screen first
        StdDraw.setPenColor(circle_color); // draw circle
        StdDraw.setPenRadius(circle_border_thickness);
        StdDraw.circle(circle_center_x, circle_center_y, circle_radius);
        StdDraw.save("output_figure.png"); // save drawing to an image file
    }
}