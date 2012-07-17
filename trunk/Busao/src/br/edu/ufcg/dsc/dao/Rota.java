package br.edu.ufcg.dsc.dao;

public class Rota {
	
	private long id;
	private String routename;
	private String colour;
	private String urlRoute;
	private long difBetweenBus;
	private String startTime;
	private String endTime;
	private long timePerTotal;
	private long numBus;
	private String days;
	
	public Rota(long id, String routename, String colour, String urlRoute,
			long difBetweenBus, String startTime, String endTime,
			long timePerTotal, long numBus, String days) {
		super();
		this.id = id;
		this.routename = routename;
		this.colour = colour;
		this.urlRoute = urlRoute;
		this.difBetweenBus = difBetweenBus;
		this.startTime = startTime;
		this.endTime = endTime;
		this.timePerTotal = timePerTotal;
		this.numBus = numBus;
		this.days = days;
	}

	public Rota() {}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
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
	
	public long getDifBetweenBus() {
		return difBetweenBus;
	}

	public void setDifBetweenBus(long difBetweenBus) {
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

	public long getTimePerTotal() {
		return timePerTotal;
	}

	public void setTimePerTotal(long timePerTotal) {
		this.timePerTotal = timePerTotal;
	}

	public long getNumBus() {
		return numBus;
	}

	public void setNumBus(long numBus) {
		this.numBus = numBus;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	@Override
	public String toString() {
		return routename + " - " + colour + "\n" +
				"Onibus: " + numBus + "\n" +
				"Diferenca entre onibus: " + difBetweenBus + "\n" +
				"Inicio: " + startTime + " Fim: " + endTime;
	}
	
}