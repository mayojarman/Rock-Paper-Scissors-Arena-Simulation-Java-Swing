
public enum ObjectType {
	PAPER(1),
	SCISSORS(2),
	ROCK(3);
	
	private final int value;
	
    private ObjectType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
