package com.upgrade.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
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

    @Column(name="startDate",nullable = false)
	private long startDate;

    @Column(name="endDate",nullable = false)
	private long endDate;
	
	@ManyToOne
	@JoinColumn(name = "customer")
	private Customer customer;


}
