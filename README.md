# RentalApp

#Abstract
We are going to develop an android application that helps the landlords to post available rentals. Users need to login with their Google account into the app. Once a user logs in to the app from his phone, their session is maintained until the user logs out manually. All the landlords can edit their post, mark it as rented or cancelled. The users can search the posts based on keyword, location, property type and price range. The user can mark the properties as favorites and can browse through them easily by using favorites list. We didn’t make any changes to the project requirements. In addition, we are planning to support images of the properties. We are also planning to support the saved search feature and we will try to implement the push notification that will send notifications based on the notification frequency. On clicking the notification, the app will be opened with the list of results.

Following are the services that will be required to be deployed to cloud to support our application.

#Common
1.	Signing in with Google

#Landlord
1.	createPost
2.	editPost
3.	markPostAsRented
4.	markPostAsCancelled
5.	getNumberOfViews
6.	sendEmail (need not be exposed. Can be utilized by other services)
7.	uploadPicture

#User
1.	searchProperty
2.	getPropertyDetails

#User specific database operations
1.	setPropertyAsFavorite
2.	addSavedSearch
3.	getSavedSearches
4.	getFavoriteList

#Technologies
We are going to build this app using Android Studio and SQLite for local database. The services will be built using NodeJS. These cloud services will be deployed to Amazon EC2. We are going to use MongoDB to support this application from the cloud.


