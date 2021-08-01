# Marvel

A short demonstration of android using Marvel Characters

Architecture Notes
- MVVM
- Activities only render views from observed data and respond to events
- ViewModel isolates all logic to enable unit testing
- Repository - fetches data and send either data or errors to viewmodel
- Endless scrolling capabilities for the massive character list using 20 item pages
