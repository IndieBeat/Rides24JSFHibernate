package modelo.beans;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named("indexbean")
@SessionScoped
public class IndexBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

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
