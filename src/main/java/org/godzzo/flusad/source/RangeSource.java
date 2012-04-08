package org.godzzo.flusad.source;


public class RangeSource<T> extends AbstractSource {
	public static final String MODE_RANDOM = "random";
	public static final String MODE_STEP = "step";
	
	private T min;
	private T max;
	
	private T value;
	private T step;
	
	private boolean firstPick = true;
	
	public RangeSource() {
		super();
	}
	public RangeSource(String name, T min, T max) {
		this();
		
		setName(name);
		setMin(min);
		setMax(max);
	}
	
	@Override
	public void load() throws Exception {
		setFirstPick(true);
	}
	
	@Override
	public Object pickOne(String field, String mode) throws Exception {
		if (mode == null) mode = MODE_RANDOM;
		
		if (min instanceof Integer) {
			if (mode.equals(MODE_RANDOM)) {
				return pickRandomInteger();
			} else {
				return stepInteger();
			}
		} else if (min instanceof Double) {
			if (mode.equals(MODE_RANDOM)) {
				return pickRandomDouble();
			} else {
				throw new RuntimeException("The step is only supported for Integer!");
			}
		} else {
			throw new RuntimeException("Integer and Double is the only supported RangeSource at now!");
		}
	}
	
	@SuppressWarnings("unchecked")
	protected Object stepInteger() {
		if (isFirstPick()) {
			
			setValue(getMin());
			setFirstPick(false);
		} else {
			
			Integer valueInt = (Integer) getValue(); 
			Integer stepInt = (Integer) getStep();
			Integer maxInt = (Integer) getMax();
			
			if (stepInt == null) stepInt = new Integer(1);
			
			Integer nextValue = valueInt + stepInt;
			
			if (nextValue > maxInt) {
				setValue(getMin());
			} else {
				setValue((T) nextValue);
			}
		}
		
		return getValue();
	}
	
	protected Object pickRandomDouble() {
		Double maxDbl = (Double) max;
		Double minDbl = (Double) min;
		Double nextDbl;
		Double diffDbl = maxDbl-minDbl;
		
		do {
			nextDbl = getContext().getRandom().nextDouble()*diffDbl;
		} while (nextDbl > diffDbl);
		
		return nextDbl+minDbl;
	}
	
	protected Object pickRandomInteger() {
		Integer maxInt = (Integer) getMax();
		Integer minInt = (Integer) getMin();
		
		int nextInt = getContext().getRandom().nextInt(maxInt-minInt+1);
		
		return nextInt+minInt;
	}
	
	public T getMin() {
		return min;
	}
	public RangeSource<T> setMin(T min) {
		this.min = min;
		return this;
	}
	public T getMax() {
		return max;
	}
	public RangeSource<T> setMax(T max) {
		this.max = max;
		return this;
	}
	public T getValue() {
		return value;
	}
	public RangeSource<T> setValue(T value) {
		this.value = value;
		return this;
	}
	public boolean isFirstPick() {
		return firstPick;
	}
	public RangeSource<T> setFirstPick(boolean firstPick) {
		this.firstPick = firstPick;
		return this;
	}
	public T getStep() {
		return step;
	}
	public RangeSource<T> setStep(T step) {
		this.step = step;
		return this;
	}
}
