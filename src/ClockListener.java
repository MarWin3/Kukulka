import java.util.EventListener;

public interface ClockListener extends EventListener {
	public abstract void budzik(ClockEvent e);
}
