package modelo.beans;



import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import modelo.businessLogic.BLFacadeImplementation;

@Named("indexBean")
public class IndexBean{

	private static BLFacadeImplementation bl=null;
	
	public IndexBean() {
		if(bl==null)
			bl=new BLFacadeImplementation();
	}
	
	public static BLFacadeImplementation getInstance() {
		return bl;
	}

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
