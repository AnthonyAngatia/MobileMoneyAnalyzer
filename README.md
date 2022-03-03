# MoMo Analyzer(Work in Progress)
MoMo analyzer is an application that primarily helps the user analyse their mobile money transactions.

# How it works
1. MoMo uses android `content providers` to fetches mobile money messages from SMS inbox.
2. The fetched messages are parsed using `regex` to identify and extract important parts of the message.
3. The extracted data is stored in a `Room sqlite` database and processed by different functions in the program.

# Features
1. Analyse expenditure and income of the user.
2. Allow users to set expenditure targets and alert them if the target is surpassed.
3. Allow user to classify their transactions.
4. Users can export their transaction receipts in a csv.
5. Search and filter through your transaction receipts.

# Tech 
- Android with Kotlin
- Architecture components(Livedata, Navigation components, Room database etc)

# Screenshots
![Home screen](github-assets/images/HomeScreen.png)
![Other screen](github-assets/images/OtherScreen.png)

