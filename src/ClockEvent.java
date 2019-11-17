import java.awt.AWTEvent;

public class ClockEvent extends AWTEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int CLOCK_EVENT = AWTEvent.RESERVED_ID_MAX + 123;

	public ClockEvent(Object source) {
		super(source, CLOCK_EVENT);
		// TODO Auto-generated constructor stub
	}

}
