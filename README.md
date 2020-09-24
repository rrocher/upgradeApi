# upgradeApi
This api provides
- A Reservation feature for a campsite for a given date range with the default being 1 month for a maximum of 3 days with validation checks
- A feature to get information of the availability of the campsite
- Ability to modify an existing reservation on the campsite

To run it, clone the repository and in the folder execute the following steps:

- mvn clean install (This will build a docker image with this name: upgrade-images/upgrade-api:latest)
- java -jar target/upgrade-api-0.0.1-SNAPSHOT.jar (This will start a java application with an embedded h2 database)

Then you can access documentation on the API through the swagger UI at http://localhost:8080/swagger-ui.html#/reservation-controller
reservation-controller
API 


POST
/v1/bookings
Add a reservation for a given customer after validation

GET
/v1/bookings/{identifier}
Retrieve the reservation for a given identifier

PUT
/v1/bookings/{identifier}
Modify a reservation for a given customer after validation

DELETE
/v1/bookings/{identifier}
Delete a reservation for a given identifier

GET
/v1/bookings/availabilities
Retrieve the availabilities for a future reservation

GET
/v1/bookings/customer
Retrieve the reservations for a given customer

