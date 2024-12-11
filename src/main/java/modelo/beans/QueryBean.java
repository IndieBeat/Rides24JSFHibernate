package modelo.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.primefaces.event.SelectEvent;

import domain.Ride;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.component.UIOutput;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Named;
import modelo.businessLogic.BLFacadeImplementation;

@Named("querybean")
@SessionScoped
public class QueryBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private String origin;
	private String destination;

	private Date date;
	
	private List<Ride> rides;
	
	private List<String> originList;
	private List<String> destinationList;
	
	private BLFacadeImplementation bl=new BLFacadeImplementation();

	public QueryBean() {
		this.originList=bl.getDepartCities();
		this.origin=originList.get(0);
		this.destinationList=bl.getDestinationCities(this.origin);
		this.destination=destinationList.get(0);
	}
	
	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<Ride> getRides() {
		return rides;
	}

	public void setRides(List<Ride> rides) {
		this.rides = rides;
	}

	public List<String> getOriginList() {
		return originList;
	}

	public void setOriginList(List<String> originList) {
		this.originList = originList;
	}

	public List<String> getDestinationList() {
		return destinationList;
	}

	public void setDestinationList(List<String> destinationList) {
		this.destinationList = destinationList;
	}
	
	public void onItemSelectedListener(AjaxBehaviorEvent event){
	    String origin = (String) ((UIOutput)event.getSource()).getValue();

		this.destinationList=bl.getDestinationCities(origin);
		this.destination=destinationList.get(0);
	}
}
