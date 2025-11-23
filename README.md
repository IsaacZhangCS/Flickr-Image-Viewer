The Flickr API key needs to be added to `local.properties` file in the root directory, it was not pushed to Github 
as to not expose the API key. Please add the FLICKR_API_KEY to local.properties file if you need to run the project

FLICKR_API_KEY=API_KEY_HERE

Everything else should run without any changes needed.

Approach was fairly standard - CLEAN architecture, with Retrofit + Compose

Domain layer handles extracting only the data we really care about from the DTO

Pagination was handled in the ViewModel with a simple tracking of currentPage, and totalPages