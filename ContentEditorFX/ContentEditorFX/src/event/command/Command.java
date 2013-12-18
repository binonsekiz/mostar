package event.command;

public interface Command {

	public void perform();
	public void undo();
	
}
