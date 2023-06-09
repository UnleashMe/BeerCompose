# BeerCompose

## Introduction
This is my first project and it's an app for a beer bar. 

## Tcchnologies I've used in this project
- Language: Kotlin
- Coroutines
- Room
- Retrofit
- Hilt
- DataStore

## Content

The app itself is pretty straightforward. User can scroll through list of menu items, which includes drinks and snacks. There's also screen, which shows detailed information about menu item. 
User can register, using his phone number(actually there's no need to use an actual number - any number will do as long as it hasn't been chosen by anyone else) and then there will be option to like or add item to cart.
There's a profile screen which allows user to logout, change name or password and delete an account. Favourites screen just showing list of liked items, nothing special.
Cart screen shows everything that's been added to cart and counts the price. On order button the cart empties and toast pops off, indicating that order is being processed (although there's no delivery included, so don't be alarmed).
App also includes admin options for user whose phone number is 777. That user can edit menu items, add new ones or delete them. 

## How it works

So when first launched app downloads the menu items from the server my friend has generously provided. The server itself is not ideal, it goes to sleep every now and then, so the response might take a while. 
Then eventually the data is loaded and it's being cashed to local database, so we don't need to use the server every time. On admin options add, update and delete the app first sends query to server, then waits for response and, if it's successful, sends the same query to database, so there won't be difference between our sources.
On user's registration their information is saved to database, although the password is hashed, so there's some protection. Database consists of 4 tables: Menu items, users, and their join tables - likes and cart.
When logged, user is saved to dataStore, so there's no need to repeat this on every launch.
