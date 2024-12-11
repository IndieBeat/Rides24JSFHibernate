package modelo.businessLogic;
import java.util.Date;

import java.util.List;
import java.util.ResourceBundle;

import javax.jws.WebMethod;
import javax.jws.WebService;

import modelo.dataAccess.DataAccess;
import modelo.domain.Driver;
import modelo.domain.Ride;
import modelo.exceptions.RideAlreadyExistException;
import modelo.exceptions.RideMustBeLaterThanTodayException;

/**
 * It implements the business logic as a web service.
 */
@WebService(endpointInterface = "businessLogic.BLFacade")
public class BLFacadeImplementation  implements BLFacade {
	DataAccess dbManager;
	private static BLFacadeImplementation bl;

	private BLFacadeImplementation()  {		
		System.out.println("Creating BLFacadeImplementation instance");
	    dbManager=new DataAccess();
	}
	
	public static BLFacadeImplementation getInstance() {
		if(bl==null) {
			return new BLFacadeImplementation();
		}
		return bl;
	}
	
    public BLFacadeImplementation(DataAccess da)  {
		dbManager=da;		
	}
    
    
    /**
     * {@inheritDoc}
     */
    @WebMethod public List<String> getDepartCities(){
		return dbManager.getDepartCities();
    }
    /**
     * {@inheritDoc}
     */
	@WebMethod public List<String> getDestinationCities(String origin){
		return dbManager.getArrivalCities(origin);		
	}

	/**
	 * {@inheritDoc}
	 */
   @WebMethod
   public Ride createRide( String origin, String destination, Date date, int nPlaces, float price, String driverEmail ) throws RideMustBeLaterThanTodayException, RideAlreadyExistException{
	   return dbManager.createRide(origin, destination, date, nPlaces, price, driverEmail);
   };
	
   /**
    * {@inheritDoc}
    */
	@WebMethod 
	public List<Ride> getRides(String origin, String destination, Date date){
		return dbManager.getRides(origin, destination, date);
	}

    
	/**
	 * {@inheritDoc}
	 */
	@WebMethod 
	public List<Date> getThisMonthDatesWithRides(String origin, String destination, Date date){
		return dbManager.getThisMonthDatesWithRides(origin, destination, date);
	}

	@Override
	public void initializeBD() {
		dbManager.initializeDB();
	}

	@Override
	public Driver getDriver(String email) {
		// TODO Auto-generated method stub
		Driver driver=dbManager.getDriver(email);
		return driver;
	}

}

