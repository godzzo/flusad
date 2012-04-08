package org.godzzo.flusad.source;

import org.godzzo.flusad.config.Context;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;

// TODO: Interval Constructor need to refactor to this setters, for readability
// TODO: Interval amount and min is collide, need to reorg.
public class DateIncrementSource extends AbstractSource {
	private LocalDate value = new LocalDate();
	private LocalDate toValue; // Need to be null, because the single value loops.
	
	private Interval nextInterval;
	private Position nextPosition;
	
	private Interval toInterval;
	private Position toPosition;
	
	private LocalDate minValue;
	private LocalDate maxValue;
	
	private boolean firstPick = true;
	
	public DateIncrementSource() {
		super();
	}
	public DateIncrementSource(String name) {
		this();
		setName(name);
	}
	
	@Override
	public void load() throws Exception {
		setFirstPick(true);
		nextToValue();
		positionValue();
	}
	
	@Override
	public Object pickOne(String field, String mode) throws Exception {
		if (field == null) field = "value";
		
		if (field.equals("value")) {
			nextValue();
			nextToValue();
			
			checkValue();
			
			return getValue().toDate();
		} else if (field.equals("to_value")) {
			return getToValue().toDate();
		} else {
			throw new RuntimeException("Field not found: "+field+" (value, to_value)!");
		}
	}
	
	protected void checkValue() {
		
		LocalDate toValue = getToValue() == null? getValue(): getToValue();
		
		if (
			(getMinValue() != null && getValue().isBefore(getMinValue())) || 
			(getMaxValue() != null && toValue.isAfter(getMaxValue()))
		) {
			setValue(getMinValue());
			positionValue();
			nextToValue();
		}
	}
	
	protected void nextValue() {
		if (!isFirstPick()) {
			setValue(getNextInterval().invoke(getValue(), getContext()));
		} else {
			setFirstPick(false);
		}
		
		positionValue();
	}
	
	protected void positionValue() {
		if (nextPosition != null) {
			setValue(nextPosition.invoke(getValue(), getContext()));
		}
	}
	
	protected void nextToValue() {
		if (toInterval != null) {
			setToValue(toInterval.invoke(getValue(), getContext()));
		}
		
		if (toPosition != null) {
			setToValue(toPosition.invoke(getToValue(), getContext()));
		}
	}
	
	public static class Interval {
		DurationFieldType fieldType; int amount, randomMax = -1, randomMin = 0;
		
		public Interval(DurationFieldType fieldType, int amount) {
			this.fieldType = fieldType; this.amount = amount;
		}
		public Interval(DurationFieldType fieldType, int amount, int randomMax) {
			this(fieldType, amount); this.randomMax = randomMax;
		}
		public Interval(DurationFieldType fieldType, int amount, int randomMax, int randomMin) {
			this(fieldType, amount, randomMax); this.randomMin = randomMin;
		}
		
		public LocalDate invoke(LocalDate source, Context context) {
			int position = amount;
			
			if (randomMax > 0) {
				int limit = randomMax - randomMin;
				position = context.getRandom().nextInt(limit+1)+randomMin;
			}
			
			return source.withFieldAdded(fieldType, position);
		}
	}
	
	public static class Position {
		DateTimeFieldType fieldType; int value;
		
		public Position(DateTimeFieldType fieldType, int value) {
			this.fieldType = fieldType; this.value = value;
		}
		
		public LocalDate invoke(LocalDate source, Context context) {
			return source.withField(fieldType, value);
		}
	}
	
	public Interval getNextInterval() {
		return nextInterval;
	}
	public DateIncrementSource setNextInterval(Interval nextInterval) {
		this.nextInterval = nextInterval;
		return this;
	}
	public Interval getToInterval() {
		return toInterval;
	}
	public DateIncrementSource setToInterval(Interval toInterval) {
		this.toInterval = toInterval;
		return this;
	}
	public LocalDate getValue() {
		return value;
	}
	public DateIncrementSource setValue(LocalDate value) {
		this.value = value;
		return this;
	}
	public LocalDate getToValue() {
		return toValue;
	}
	public DateIncrementSource setToValue(LocalDate toValue) {
		this.toValue = toValue;
		return this;
	}
	public Position getNextPosition() {
		return nextPosition;
	}
	public DateIncrementSource setNextPosition(Position nextPosition) {
		this.nextPosition = nextPosition;
		return this;
	}
	public Position getToPosition() {
		return toPosition;
	}
	public DateIncrementSource setToPosition(Position toPosition) {
		this.toPosition = toPosition;
		return this;
	}
	public LocalDate getMinValue() {
		return minValue;
	}
	public LocalDate getMaxValue() {
		return maxValue;
	}
	public DateIncrementSource setMinValue(LocalDate minValue) {
		this.minValue = minValue;
		return this;
	}
	public DateIncrementSource setMaxValue(LocalDate maxValue) {
		this.maxValue = maxValue;
		return this;
	}
	public boolean isFirstPick() {
		return firstPick;
	}
	public DateIncrementSource setFirstPick(boolean firstPick) {
		this.firstPick = firstPick;
		return this;
	}
	
	@Override
	public int getSize() {
		int count = 0;
		
		if (getMinValue() != null && getMaxValue() != null) {
			setValue(getMinValue());
			
			do {
				nextValue();
				count++;
			} while (getMaxValue().isAfter(getValue()));
			
			setValue(getMinValue());
			setFirstPick(true);
		} else {
			throw new RuntimeException("Unvalid call, need to set min and max!");
		}
		
		return count;
	}
}
