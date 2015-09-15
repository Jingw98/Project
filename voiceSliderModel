package projectGUI;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JSlider;

// MySliderUI class change the shape and the color of the voice slider
// Taken from http://webservices.ctocio.com.cn/java/311/9138811.shtml
public class MySliderUI extends javax.swing.plaf.metal.MetalSliderUI {
	public void paintThumb(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		// fill the oval shape for the current location of thumb
		g2d.fillOval(thumbRect.x, thumbRect.y, thumbRect.width,
				thumbRect.height);
	}

	/** */
	/**
	 * paint the track of the scale
	 */
	public void paintTrack(Graphics g) {
		int cy, cw;
		Rectangle trackBounds = trackRect;
		if (slider.getOrientation() == JSlider.HORIZONTAL) {
			Graphics2D g2 = (Graphics2D) g;
			cy = (trackBounds.height / 2) - 2;
			cw = trackBounds.width;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g2.translate(trackBounds.x, trackBounds.y + cy);
			// set the color of the background
			g2.setPaint(Color.lightGray);
			g2.fillRect(0, -cy, cw, cy * 2);
			int trackLeft = 0;
			int trackRight = 0;
			trackRight = trackRect.width - 1;
			int middleOfThumb = 0;
			int fillLeft = 0;
			int fillRight = 0;
			// calculate the coordinate
			middleOfThumb = thumbRect.x + (thumbRect.width / 2);
			middleOfThumb -= trackRect.x;
			if (!drawInverted()) {
				fillLeft = !slider.isEnabled() ? trackLeft : trackLeft + 1;
				fillRight = middleOfThumb;
			} else {
				fillLeft = middleOfThumb;
				fillRight = !slider.isEnabled() ? trackRight - 1
						: trackRight - 2;
			}
			// set the gradual changed color
			g2.setPaint(new GradientPaint(0, 0, new Color(0, 100, 100), cw, 0,
					new Color(100, 255, 230), true));
			g2.fillRect(0, -cy, fillRight - fillLeft, cy * 2);
			g2.setPaint(slider.getBackground());
			Polygon polygon = new Polygon();
			polygon.addPoint(0, cy);
			polygon.addPoint(0, -cy);
			polygon.addPoint(cw, -cy);
			g2.fillPolygon(polygon);
			polygon.reset();
			g2.setPaint(Color.white);
			g2.drawLine(0, cy, cw - 1, cy);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_OFF);
			g2.translate(-trackBounds.x, -(trackBounds.y + cy));
		} else {
			super.paintTrack(g);
		}
	}
}
