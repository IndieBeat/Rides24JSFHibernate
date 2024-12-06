package modelo.beans;



import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named("indexBean")
public class IndexBean{


	public String createride() {
		return "createRide";
	}
	
	public String queryride() {
		return "queryRide";
	}
	
	public String index() {
		return "index";
	}

}
