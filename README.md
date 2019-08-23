## Airline
An Android app that let's you find airline schedules. Consuming https://developer.lufthansa.com/docs api

#Prerequisites and installation

*Kotlin:* kotlin is used to write the code
*Dependencies:* Connect to internet and install all dependencies in the module app build.gradle file

#Note

1. For easy cloning and running the project purpose. Token will not expire
So you can  clone the project and run

2. You have to choose from the suggested airport. Maybe a spinner should have been used.
 But AutoCompleteTextView was used for designing purpose.


3. Searching a schedule while supplying two Airport code can result to empty or
 restricted result due to limited result of free API
- you can try origin airport as *AAA*, destination Airport as *AAB* and choose date as 2019-08-22

#Technologies

MVVM - Structured Model-View-ViewModel architecture.
Kotlin Coroutines - Thread management and concurrency.
Retrofit - Rest API calls
Google Maps - map Display and polylin implemented
Architecture Components - Lifecycle and Jetpack components.
