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

	public BLFacadeImplementation()  {		
		System.out.println("Creating BLFacadeImplementation instance");
		
		
		    dbManager=new DataAccess();
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
	@WebMethod public List<String> getDestinationCities(String from){
		return dbManager.getArrivalCities(from);		
	}

	/**
	 * {@inheritDoc}
	 */
   @WebMethod
   public Ride createRide( String from, String to, Date date, int nPlaces, float price, String driverEmail ) throws RideMustBeLaterThanTodayException, RideAlreadyExistException{
	   return dbManager.createRide(from, to, date, nPlaces, price, driverEmail);
   };
	
   /**
    * {@inheritDoc}
    */
	@WebMethod 
	public List<Ride> getRides(String from, String to, Date date){
		return dbManager.getRides(from, to, date);
	}

    
	/**
	 * {@inheritDoc}
	 */
	@WebMethod 
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date){
		return dbManager.getThisMonthDatesWithRides(from, to, date);
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

