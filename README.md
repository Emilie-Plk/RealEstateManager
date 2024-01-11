# RealEstateManager :city_sunrise:
## A realestate management app designed for realtors

### Presentation
**RealEstateManager** is an Android app tailored for real estate agents, offering them the possibility of creating and editing new properties, listing them in a list or on a map, and also fitering them to find the perfect villa fitting customers' needs. You'll also find a loan simulator, how convenient!

If the internet connectivity is being fussing, no worries: you can save your property form safely (and locally) and it'll be accessible later: only reason is we need internet connection to fetch the location data from the entered address.

The app is supported in France and US so far. Currency rate is updated (with Fixer API) every 4h for better precision for conversion 💱 (EUR and USD)

> [!IMPORTANT]
> You'll need valid Google Maps and Fixer API keys to compile and run the app, just add yours into local.properties (`GOOGLE_API_KEY=YOUR_KEY` and `FIXER_API_KEY=YOUR_KEY`).
> [Fixer](https://fixer.io/) has a free plan, and for Maps you might want to restrain your key to **Places** (for Autocomplete), **Maps Static** and **Geocoding** APIs; don't forget to activate **Maps SDK** for Android too 🗺️ and you're good to go! 
___

### Tech stack
* Written in `Kotlin`, the star of the show! ✨
* `MVVM architecture` + `Domain layer` + `Repository pattern` to stay clean an organized (as possible...)
* `Room` for a robust, local data access with use of SQLite power! 
* DI using `Hilt` :syringe:
* `Retrofit` to easily manage API calls :phone: (allo?)
* Unit tests using `Mockk` and `Turbine`, just to make sure everything's fine :suspect:
* `Kover` for test coverage (use command `./gradlew koverHtmlReportDebug` to generate test report!) with ~60% branch coverage
___
### Demo 
Whether you call them [gif] or [dʒif] doesn't matter much, they're always usefull to provide a sneak peek of the app

#### List view

![Main demo](https://github.com/Emilie-Plk/RealEstateManager/assets/96174269/a0f3bd27-e1b2-487a-b716-77d6e122e7dc)


#### Filter properties

![Filtering real estate properties](https://github.com/Emilie-Plk/RealEstateManager/assets/96174269/e4a8d588-0414-48bf-8a69-23eb040a021a)


#### Create new real estate property

![Creating new property](https://github.com/Emilie-Plk/RealEstateManager/assets/96174269/cbbcd0cf-250e-4a13-aec8-13bb4f093cd7)


#### Add new property or pick a draft 
(oui that's a trap, this one's not a gif)

![image](https://github.com/Emilie-Plk/RealEstateManager/assets/96174269/94ff0f46-ccbd-4576-a25a-bd18bf0654a8)

> [!WARNING]
> This app has been developped for learning purposes (OpenClassrooms); this is part of their Android developpement training.
