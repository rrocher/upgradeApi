package com.upgrade.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

import com.sun.istack.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter 
@Setter 
@ToString
@EqualsAndHashCode
@NoArgsConstructor 
@Table(name = "reservation")
@NamedQuery(name = "Reservation.findAll", query = "SELECT r FROM Reservation r")
public class Reservation {
	

	public Reservation(Reservation reservation) {
		this.customer = reservation.getCustomer();
		this.identifier = reservation.getIdentifier();
		this.startDate = reservation.getStartDate();
		this.endDate = reservation.getEndDate();
	}

	private static final long serialVersionUID = 1L;
	
	@Id
    @Column(name="identifier",nullable = false)
	private String identifier;
	
	@Version
	@NotNull
	private long version;

    @Column(name="startDate",nullable = false)
	private long startDate;

    @Column(name="endDate",nullable = false)
	private long endDate;
	
	@ManyToOne
	@JoinColumn(name = "customer")
	private Customer customer;

	public Reservation(String identifier, long startDate, long endDate, Customer customer) {
		super();
		this.identifier = identifier;
		this.startDate = startDate;
		this.endDate = endDate;
		this.customer = customer;
	}
	


}
