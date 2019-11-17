import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.io.File;
import java.util.Calendar;
import java.util.TimeZone;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Clock extends JPanel implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int spacing = 35;
	private static final float radPerSecMin = (float) (Math.PI / 30.0);
	private static final float radPerNum = (float) (Math.PI / -6);
	private int size;
	private int centerX;
	private int centerY;
	JLabel kukulka = new JLabel("");

	Color colorSecond, colorMHour, colorNumber;

	TimeZone timeZone = TimeZone.getDefault();
	Calendar cal = (Calendar) Calendar.getInstance(timeZone);

	private int h = cal.get(Calendar.HOUR), m = cal.get(Calendar.MINUTE), s = cal.get(Calendar.SECOND);

	private int speed = 1;


	public Clock(int speed) {
		this.speed = speed;
		kukulka.setForeground(Color.red);
		kukulka.setPreferredSize(new Dimension(200, 800));
		kukulka.setFont(new Font(kukulka.getFont().getName(), kukulka.getFont().getStyle(), 20));
		kukulka.setVisible(false);
		this.add(kukulka);
		Thread t = new Thread(this);
		t.start();

	}

	public void addClockListener(ClockListener listener) {
		listenerList.add(ClockListener.class, listener);
	}

	public void removeClockListener(ClockListener listener) {
		listenerList.remove(ClockListener.class, listener);
	}

	@Override
	public void processEvent(AWTEvent evt) {
		ClockListener[] listeners = listenerList.getListeners(ClockListener.class);
		if (evt instanceof ClockEvent) {
			for (int i = 0; i < listeners.length; i++) {
				ClockEvent ge = (ClockEvent) evt;
				listeners[i].budzik(ge);
			}
		} else
			super.processEvent(evt);
	}

	@Override
	public void run() {
		EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
		while (true) {
			try {
				Thread.sleep(1000 * 60 / speed);
			} catch (InterruptedException e) {
			}
			if (++s == 60) {
				s = 0;

				if (++m == 60) {
					m = 0;

					if (++h == 24)
						h = 0;
					ClockEvent ce = new ClockEvent(this);
					queue.postEvent(ce);
				}
			}
			if (m == 20)
				kukulka.setVisible(false);
			repaint();
		}
	}

	@Override
	public void paint(Graphics g) {
		
		super.paint(g);

		

		g.setColor(new Color(124, 136, 162));
		g.fillOval(25, spacing, 350, 350);
		g.setColor(Color.WHITE);
		g.fillOval(35, spacing + 10, 330, 330);

		size = 400 - spacing;
		centerX = 400 / 2;
		centerY = 400 / 2 + 10;

		drawClockFace(g);
		drawNumberClock(g);
		drawHands(g, h, m, s, Color.RED, Color.BLACK);

		
		g.setColor(Color.BLACK);
		g.fillOval(centerX - 5, centerY - 5, 10, 10);
		g.setColor(Color.RED);
		g.fillOval(centerX - 3, centerY - 3, 6, 6);

	}

	private void drawClockFace(Graphics g) {
		// TODO Auto-generated method stub

			for (int sec = 0; sec < 60; sec++) {
			int ticStart;
			if (sec % 5 == 0) {
				ticStart = size / 2 - 10;
			} else {
				ticStart = size / 2 - 5;
			}
			drawRadius(g, centerX, centerY, radPerSecMin * sec, ticStart - 20, size / 2 - 20, Color.BLACK);

		}
	}

	private void drawRadius(Graphics g, int x, int y, double angle, int minRadius, int maxRadius, Color colorNumber) {
		float sine = (float) Math.sin(angle);
		float cosine = (float) Math.cos(angle);
		int dxmin = (int) (minRadius * sine);
		int dymin = (int) (minRadius * cosine);
		int dxmax = (int) (maxRadius * sine);
		int dymax = (int) (maxRadius * cosine);
		g.setColor(colorNumber);
		g.drawLine(x + dxmin, y + dymin, x + dxmax, y + dymax);
	}

	private void drawNumberClock(Graphics g) {
		// TODO Auto-generated method stub
		for (int num = 12; num > 0; num--) {
			drawnum(g, radPerNum * num, num);
		}
	}

	private void drawnum(Graphics g, float angle, int n) {
		// TODO Auto-generated method stub
		float sine = (float) Math.sin(angle);
		float cosine = (float) Math.cos(angle);
		int dx = (int) ((size / 2 - 20 - 25) * -sine);
		int dy = (int) ((size / 2 - 20 - 25) * -cosine);

		g.drawString("" + n, dx + centerX - 5, dy + centerY + 5);
	}

	private void drawHands(Graphics g, double hour, double minute, double second, Color colorSecond, Color colorMHour) {
		// TODO Auto-generated method stub
		double rsecond = (second * 6) * (Math.PI) / 180;
		double rminute = ((minute + (second / 60)) * 6) * (Math.PI) / 180;
		double rhours = ((hour + (minute / 60)) * 30) * (Math.PI) / 180;

		g.setColor(colorSecond);
		g.drawLine(centerX, centerY, centerX + (int) (150 * Math.cos(rsecond - (Math.PI / 2))),
				centerY + (int) (150 * Math.sin(rsecond - (Math.PI / 2))));
		g.setColor(colorMHour);
		g.drawLine(centerX, centerY, centerX + (int) (120 * Math.cos(rminute - (Math.PI / 2))),
				centerY + (int) (120 * Math.sin(rminute - (Math.PI / 2))));
		g.drawLine(centerX, centerY, centerX + (int) (90 * Math.cos(rhours - (Math.PI / 2))),
				centerY + (int) (90 * Math.sin(rhours - (Math.PI / 2))));
	}

	public void kuku() {
		kukulka.setText("Minê³a godzina " + minelaGodzina() + " !");
		playSound();
		kukulka.setVisible(true);
	}

	public int minelaGodzina() {
		return h;
	}

	public void playSound() {
		try {
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(new File("music\\kukulka.wav").getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception ex) {
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}
}
