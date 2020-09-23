# upgradeApi
This api provides
- A Reservation feature for a campsite for a given date range with the default being 1 month for a maximum of 3 days
- A feture to get information of the availability of the campsite
- Ability to modify an existing reservation on the campsite

To run it, clone the repository and in the folder execute the following steps:

- mvn clean install (This will build a docker image with this name: upgrade-images/upgrade-api:latest)
- java -jar target/upgrade-api-0.0.1-SNAPSHOT.jar (This will start a java application with an embedded h2 database)

