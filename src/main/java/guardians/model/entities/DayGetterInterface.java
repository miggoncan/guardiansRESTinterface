package guardians.model.entities;

/**
 * This interface will allow handling days of of a month independently of the
 * underlying class
 * 
 * @author miggoncan
 */
public interface DayGetterInterface {
	/**
	 * @return The day of the month
	 */
	public Integer getDay();
}
