public class Subtask extends Task {
	private Epictask epictask;

	public Subtask(String name, String description, Epictask epictask) {
		super(name, description);
		this.epictask = epictask;
		epictask.add(this);
	}

	public Epictask getEpicTask() {
		return epictask;
	}

	@Override
	public String toString() {
		return "Subtask{'" + getName() + "'='"
				+ getDescription() + "', status=" + getStatus() + ", id=" + getId()
				+ '}';
	}
}
