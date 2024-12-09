package modelo.dataAccess;

import java.io.File;
import java.net.NoRouteToHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import modelo.JPAUtil;
import modelo.configuration.UtilDate;
import modelo.domain.Driver;
import modelo.domain.Ride;
import modelo.exceptions.RideAlreadyExistException;
import modelo.exceptions.RideMustBeLaterThanTodayException;

/**
 * It implements the data access to the objectDb database
 */
public class DataAccess {
	private EntityManager em;

	public DataAccess(Boolean initializeMode) {
		initializeDB();
	}

	public DataAccess() {
		new DataAccess(false);
	}

	/**
	 * This is the data access method that initializes the database with some events
	 * and questions. This method is invoked by the business logic (constructor of
	 * BLFacadeImplementation) when the option "initialize" is declared in the tag
	 * dataBaseOpenMode of resources/config.xml file
	 */
	public void initializeDB() {
		
		try {
			
			this.em=JPAUtil.getEntityManager();
			em.getTransaction().begin();

			Calendar today = Calendar.getInstance();

			int month = today.get(Calendar.MONTH);
			int year = today.get(Calendar.YEAR);
			if (month == 12) {
				month = 1;
				year += 1;
			}

			// Create drivers
			Driver driver1 = new Driver("driver1@gmail.com", "Aitor Fernandez");
			Driver driver2 = new Driver("driver2@gmail.com", "Ane Gaztañaga");
			Driver driver3 = new Driver("driver3@gmail.com", "Test driver");

			// Create rides
			Ride r11=driver1.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 4, 7);
			Ride r12=driver1.addRide("Donostia", "Gazteiz", UtilDate.newDate(year, month, 6), 4, 8);
			Ride r13=driver1.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 25), 4, 4);
			Ride r14=driver1.addRide("Donostia", "Iruña", UtilDate.newDate(year, month, 7), 4, 8);

			Ride r21=driver2.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 3, 3);
			Ride r22=driver2.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 25), 2, 5);
			Ride r23=driver2.addRide("Eibar", "Gasteiz", UtilDate.newDate(year, month, 6), 2, 5);

			Ride r31=driver3.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 14), 1, 3);

			em.persist(driver1);
			em.persist(driver2);
			em.persist(driver3);

			em.persist(r11);
			em.persist(r12);
			em.persist(r13);
			em.persist(r14);

			em.persist(r21);
			em.persist(r22);
			em.persist(r23);
			
			em.persist(r31);

			em.getTransaction().commit();
			System.out.println("Db initialized");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method returns all the cities where rides depart
	 * 
	 * @return collection of cities
	 */
	public List<String> getDepartCities() {
		try {
			this.em=JPAUtil.getEntityManager();
			em.getTransaction().begin();
			TypedQuery<String> query = em.createQuery("SELECT DISTINCT r.from FROM Ride r ORDER BY r.from",
					String.class);
			List<String> cities = query.getResultList();
			em.getTransaction().commit();
			return cities;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
	}

	/**
	 * This method returns all the arrival destinations, from all rides that depart
	 * from a given city
	 * 
	 * @param from the depart location of a ride
	 * @return all the arrival destinations
	 */
	public List<String> getArrivalCities(String from) {
		try {
			this.em=JPAUtil.getEntityManager();
			em.getTransaction().begin();
			TypedQuery<String> query = em.createQuery("SELECT DISTINCT r.to FROM Ride r WHERE r.from=?1 ORDER BY r.to",
					String.class);
			query.setParameter(1, from);
			List<String> arrivingCities = query.getResultList();
			em.getTransaction().commit();
			return arrivingCities;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
	}

	/**
	 * This method creates a ride for a driver
	 * 
	 * @param from        the origin location of a ride
	 * @param to          the destination location of a ride
	 * @param date        the date of the ride
	 * @param nPlaces     available seats
	 * @param driverEmail to which ride is added
	 * 
	 * @return the created ride, or null, or an exception
	 * @throws RideMustBeLaterThanTodayException if the ride date is before today
	 * @throws RideAlreadyExistException         if the same ride already exists for
	 *                                           the driver
	 */
	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail)
			throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
		System.out.println(">> DataAccess: createRide=> from= " + from + " to= " + to + " driver=" + driverEmail
				+ " date " + date);
		try {
			if (new Date().compareTo(date) > 0) {
				System.out.println("Error al crear ride con mala fecha");
				throw new RideMustBeLaterThanTodayException(
						ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorRideMustBeLaterThanToday"));
			}
			this.em=JPAUtil.getEntityManager();
			em.getTransaction().begin();

			Driver driver = em.find(Driver.class, driverEmail);
			if (driver.doesRideExists(from, to, date)) {
				em.getTransaction().commit();
				throw new RideAlreadyExistException(
						ResourceBundle.getBundle("Etiquetas").getString("DataAccess.RideAlreadyExist"));
			}
			Ride ride = driver.addRide(from, to, date, nPlaces, price);
			// next instruction can be obviated
			em.persist(ride);
			em.persist(driver);
			
			System.out.println("Ride persisteado");
			em.getTransaction().commit();
			System.out.println("Ride commiteado");

			return ride;
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			em.getTransaction().commit();
			return null;
		} finally {
			em.close();
		}

	}

	/**
	 * This method retrieves the rides from two locations on a given date
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date the date of the ride
	 * @return collection of rides
	 */
	public List<Ride> getRides(String from, String to, Date date) {
		System.out.println(">> DataAccess: getRides=> from= " + from + " to= " + to + " date " + date);

		try {
			this.em=JPAUtil.getEntityManager();
			em.getTransaction().begin();
			
			List<Ride> res = new ArrayList<>();
			TypedQuery<Ride> query = em.createQuery("SELECT r FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date=?3",
					Ride.class);
			query.setParameter(1, from);
			query.setParameter(2, to);
			query.setParameter(3, date);
			
			List<Ride> rides = query.getResultList();
			em.getTransaction().commit();
			
			for (Ride ride : rides) {
				res.add(ride);
			}
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
	}

	/**
	 * This method retrieves from the database the dates a month for which there are
	 * events
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date of the month for which days with rides want to be retrieved
	 * @return collection of rides
	 */
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		try {
			this.em=JPAUtil.getEntityManager();
			em.getTransaction().begin();

			System.out.println(">> DataAccess: getEventsMonth");
			List<Date> res = new ArrayList<>();

			Date firstDayMonthDate = UtilDate.firstDayMonth(date);
			Date lastDayMonthDate = UtilDate.lastDayMonth(date);

			TypedQuery<Date> query = em.createQuery(
					"SELECT DISTINCT r.date FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date BETWEEN ?3 and ?4",
					Date.class);

			query.setParameter(1, from);
			query.setParameter(2, to);
			query.setParameter(3, firstDayMonthDate);
			query.setParameter(4, lastDayMonthDate);
			
			List<Date> dates = query.getResultList();
			em.getTransaction().commit();
			
			for (Date d : dates) {
				res.add(d);
			}
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
	}

	public Driver getDriver(String email) {
		try {
			this.em=JPAUtil.getEntityManager();
			em.getTransaction().begin();

			Driver driver = em.find(Driver.class, email);
			
			em.getTransaction().commit();
			return driver;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			em.close();
		}
		
		return null;
	}

}
