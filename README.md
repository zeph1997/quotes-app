# Quotes Mobile Application

# <p align="center">Quotes Mobile Application</p>

<p align="center">A simple mobile application with widget to display daily quotes to motivate you for the day!</p>

## Contents

  * [Motivation](#motivation)
  * [Demo](#demo)
  * [Tech Stack](#tech-stack)
  * [To Do](#to-do)

## Motivation

The motivation for building this application is to try out the following:
* Widgets on both iOS and Android
* Volley Requests on Android
* An introduction for me to learn iOS

I have been putting learning iOS on hold due to the need to switch to a different operating system and completely new language. However, I decided that this December shall be the start of familiarising myself with Swift.

## Demo

This is a very simple application that will show you a quote when you open the application. However, the main use is the widget, which updates daily and will display a new random quote that will hopefully motviate you as you start your day!

### Widget View

![Widget Screenshot](/readme/widget.jpg)

Text size will automatically resize to a smaller font size if the quote gets too long. Quote will refresh every day.

## Tech Stack

I curated a list of quotes that I find motivational and inspirational. I then created a Flask application to expose API endpoints to retrieve a random quote and hosted it on Heroku. The database used is Firebase.

The mobile application will perform a GET request to the Flask application and display the quote on the widget and application.

Will add in an image for clearer depiction in the future.

## To Do
* Complete iOS application
* Add in endpoint for specific days so the quote will not dynamically change
* Create categories of quotes
