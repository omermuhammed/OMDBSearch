# OMDBSearch

A test app I wrote using OMDB API. You will need to register [here][omdbapi] and get your own api key for this app to work.
This test app has total of 4 fragments:
- SplashscreenFragment: Dagger not integrated, other fragments use Dagger
- HomeFragment: Has the search box in the nav bar, and a recyclerview to display search results. Clicking on a list row will launch MovieDetailsFragment that shows the movie details.
- MovieDetailsFragment: This shows the movie details, it also has a button at the bottom. When navigating to this screen from HomeFragment this button will allow us to save this movie to local database. But when we come here from FavoritesFragment this button will allow us to remove it from the local database.
- FavoritesFragment: This screen displays the saved movies from local database. We can get to this screen from the navigation drawer.

This application implements the following concepts :
- Single Activity and multiple fragments setup
- ViewModels and ViewModelScope usage
- Using LiveData and MutableLiveData for handling network/database requests
- Room for network call response storage
- Navigation component for navigation graph and navigation drawer
- Kotlin coroutines usage with suspend keyword
- DI with Dagger 2, using ActivityLifecycleCallback


[omdbapi]: http://www.omdbapi.com/apikey.aspx