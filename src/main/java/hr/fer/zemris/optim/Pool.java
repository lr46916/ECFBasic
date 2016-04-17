package hr.fer.zemris.optim;

public interface Pool<T> {
	/**
	 * "Frees" instance of given element. It now belongs to this Pool and should
	 * NOT be changed or used anywhere else. "element" object can be acquired
	 * only trough {@link #getElement()} method which returns an instance of
	 * class T not used anymore.
	 * 
	 * @param element
	 *            Object to be "freed" and possibly stored in Pool
	 */
	public void free(T element);

	/**
	 * Gets an instance of Type T. This instance could beforehand given to the
	 * Pool trough {@link #free(Object)} method. If there are no currently freed
	 * instances in the Pool it should create a new instance of such type.
	 * 
	 * Note that it is not an obligation of the Pool to initialize
	 * data members of object returned. Data members can have any random values
	 * assigned depending on instances given to the Pool trough
	 * {@link #free(Object)} method.
	 * 
	 * @return Instance of type T
	 */
	public T getElement();
}
