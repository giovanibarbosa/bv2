package br.edu.ufcg.dsc.util;

public class RouteListView {

	private String routename;
	private String colour;
	private String urlRoute;
	private int difBetweenBus;
	private String startTime;
	private String endTime;
	private int timePerTotal;
	private int numBus;
	
	public RouteListView(String routename, String colour, String urlRoute,
			int difBetweenBus, String startTime, String endTime,
			int timePerTotal, int numBus) {
		super();
		this.routename = routename;
		this.colour = colour;
		this.urlRoute = urlRoute;
		this.difBetweenBus = difBetweenBus;
		this.startTime = startTime;
		this.endTime = endTime;
		this.timePerTotal = timePerTotal;
		this.numBus = numBus;
	}
	
	public String getRoutename() {
		return routename;
	}
	
	public void setRoutename(String routename) {
		this.routename = routename;
	}
	
	public String getColour() {
		return colour;
	}
	
	public void setColour(String colour) {
		this.colour = colour;
	}
	
	public String getUrlRoute() {
		return urlRoute;
	}
	
	public void setUrlRoute(String urlRoute) {
		this.urlRoute = urlRoute;
	}
	
	public int getDifBetweenBus() {
		return difBetweenBus;
	}
	
	public void setDifBetweenBus(int difBetweenBus) {
		this.difBetweenBus = difBetweenBus;
	}
	
	public String getStartTime() {
		return startTime;
	}
	
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	public String getEndTime() {
		return endTime;
	}
	
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public int getTimePerTotal() {
		return timePerTotal;
	}
	
	public void setTimePerTotal(int timePerTotal) {
		this.timePerTotal = timePerTotal;
	}
	
	public int getNumBus() {
		return numBus;
	}
	
	public void setNumBus(int numBus) {
		this.numBus = numBus;
	}
}
