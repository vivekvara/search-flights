package com.myapp.vo;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class ProviderVO {
	
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private String origin;
	private Date departureTime;
	private String destination;
	private Date destinationTime;
	private double price;

	public ProviderVO(String origin, Date departureTime, String destination, Date destinationTime, double price) {
		super();
		this.origin = origin;
		this.departureTime = departureTime;
		this.destination = destination;
		this.destinationTime = destinationTime;
		this.price = price;
	}

	public ProviderVO(String origin, LocalDateTime departureTime, String destination, LocalDateTime destinationTime,
			double price) {
		super();
		this.origin = origin;
		this.departureTime = Date.from(departureTime.atZone(ZoneId.systemDefault()).toInstant());
		this.destination = destination;
		this.destinationTime = Date.from(destinationTime.atZone(ZoneId.systemDefault()).toInstant());
		this.price = price;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public Date getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Date getDestinationTime() {
		return destinationTime;
	}

	public void setDestinationTime(Date destinationTime) {
		this.destinationTime = destinationTime;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		final ProviderVO providerVO = (ProviderVO) obj;
		if (this == providerVO) {
			return true;
		} else {
			return (this.origin.equals(providerVO.origin) && this.destination.equals(providerVO.destination)
					&& this.departureTime.equals(providerVO.departureTime)
					&& this.destinationTime.equals(providerVO.destinationTime) && this.price == providerVO.price);
		}
	}

	@Override
	public int hashCode() {
		int hashno = 7;
		hashno = 13 * hashno + (origin == null ? 0 : origin.hashCode());
		return hashno;
	}

	@Override
	public String toString() {
		return "ProviderVO [origin=" + origin + ", departureTime=" + departureTime + ", destination=" + destination
				+ ", destinationTime=" + destinationTime + ", price=" + price + "]";
	}
	
	public String formatted() {
//		return origin + " --> " + destination + " (" + dateFormat.format(departureTime) + " --> " + dateFormat.format(destinationTime) + ") - $" + price;
		return String.format("%s --> %s (%s --> %s) - $%d", origin, destination, dateFormat.format(departureTime), dateFormat.format(destinationTime), price);
	}

}
