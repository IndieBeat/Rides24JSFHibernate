package modelo.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import modelo.businessLogic.BLFacadeImplementation;
import modelo.configuration.UtilDate;
import modelo.domain.Driver;
import modelo.domain.Ride;
import modelo.exceptions.RideAlreadyExistException;
import modelo.exceptions.RideMustBeLaterThanTodayException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named("ridebean")
@SessionScoped
public class CreateRideBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String from;
	private String to;
	private int seats;
	private Date date;
	private float price;
	private Driver driver;
	
	private BLFacadeImplementation bl=new BLFacadeImplementation();
	private List<Date> highlightedDates=new ArrayList<Date>();

	public CreateRideBean() {
		driver = new Driver("Test driver", "test");
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
		System.out.println(from + " " + to + " " + date + " " + seats + " " + price);

		try {
			bl.createRide(from, to, date, seats, price, driver.getEmail());
		} catch (RideMustBeLaterThanTodayException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RideAlreadyExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("LLega aqui");
		return "ok";
	}

}