package modelo.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modelo.businessLogic.BLFacadeImplementation;
import modelo.domain.Driver;
import modelo.exceptions.RideAlreadyExistException;
import modelo.exceptions.RideMustBeLaterThanTodayException;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named("ridebean")
@SessionScoped
public class CreateRideBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private BLFacadeImplementation bl;
	
	private String from;
	private String to;
	private int seats;
	private Date date;
	private float price;
	private Driver driver;
	
	private List<Date> highlightedDates=new ArrayList<Date>();

	public CreateRideBean() {
		this.bl=IndexBean.getInstance();
		this.driver = bl.getDriver("driver1@gmail.com");
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public List<Date> getHighlightedDates() {
		return highlightedDates;
	}

	public void setHighlightedDates(List<Date> highlightedDates) {
		this.highlightedDates = highlightedDates;
	}

	public String crear() {
		try {
			bl.createRide(from, to, date, seats, price, driver.getEmail());
		} catch (RideMustBeLaterThanTodayException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RideAlreadyExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		emptyForm();
		return "ok";
	}

	public String cancel() {
		emptyForm();
		return "index";
	}
	
	public void emptyForm() {
		this.from="";
		this.to="";
		this.seats=0;
		this.price=0f;
		this.date=null;
	}
}