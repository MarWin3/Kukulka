import javax.swing.JFrame;


public class MainFrame extends JFrame {
	Clock Clock = new Clock(6000);


	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {

		new MainFrame();

	}

	public MainFrame() {
		ClockListener pelnaGodzina = new ClockListener() {
			@Override
			public void budzik(ClockEvent e) {

             Clock.kuku();
			}
		};

		Clock.addClockListener(pelnaGodzina);
		getContentPane().add(Clock);

		pack();
		setVisible(true);
		setBounds(500, 100, 430, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}