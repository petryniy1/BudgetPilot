# BudgetPilot

BudgetPilot is an Android application for tracking personal and household finances.

The app helps users manage cash and card accounts, record income and expense operations, filter transactions, and monitor their current balance in one place.

## Current Features

- Account management for cash and card-based funds
- Income and expense operation tracking
- Transaction filtering by date, category and account
- Balance overview
- Local data storage with Room
- English UI
- Legacy Android project modernization in progress

## Tech Stack

- Kotlin
- Android SDK
- XML layouts
- ViewBinding
- RecyclerView
- MVVM
- Room
- SQLite
- DAO
- Repository pattern
- Mappers
- Kotlin Coroutines
- Flow
- StateFlow
- LiveData
- Hilt
- Jetpack Navigation Component
- Material Components
- Gradle
- Android Gradle Plugin
- JUnit
- Espresso
- Git
- GitHub Pull Requests

## Architecture Notes

The project follows an MVVM-based structure with separated data, domain and presentation layers.

The current modernization focuses on improving the data layer, replacing legacy money holder terminology with a clearer account-based model, and preparing the codebase for future features such as analytics, receipt scanning and bank synchronization.

## Planned Improvements

- Replace legacy domain naming with account-based models
- Improve UI state handling with StateFlow
- Add more unit tests for ViewModels and mappers
- Add analytics for expenses by category
- Add charts for monthly balance and spending trends
- Add receipt scanning support
- Add bank synchronization support
- Consider gradual migration to Jetpack Compose

## Project Status

This project is actively being modernized and used as a portfolio Android application.
