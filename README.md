Please ensure you have read and understood the license for usage of this software before reading the rest of this document.

# Intro

Thanks for viewing my project. This application is a No Limit/Limit Hold'em Poker Analyser. This means it will take your hand histories generated by online poker client software (assuming you've installed the full client), ingest them, and present you back with the enriched data in a table, as well as a bunch of graphs/reports. You're hand histories will be synchronised in the GUI, sorted and exportable to an Excel spreadsheet (.csv) to enable more custom analysis by you the player.

# Testing and Running the code

A gradle wrapper has been provided. To run the code

1.`cd ${pokerAnalyserRootDir}`
2. `./gradlew run`

This command will compile the code, run the associated tests and launch the application's GUI for you. You can also run:

1. `./gradlew test`

to just run the tests for the code base. These are far from comprehensive but test the most valuable parts of the code base to ensure
the data extracted is high-quality.

To get any use out of the application, you must navigate from the menu toolbar -> open, and provide some hand histories. A bunch of hand histories of my own have been used for testing purposes and are supplied for this purpose. You can select a folder and all hand histories in the folder will be ingested, or you can select a single file.

## What does this application do?

This application is used to break up the data from your online poker hand histories from two of the most popular online poker rooms - PartyPoker and PokerStars. I started with PartyPoker data and then enabled PokerStars later. For this reason there may be more issues ingesting data from PokerStars hand histories than from PartyPoker.

This application will organise hand histories in the GUI by cash/tournament style games and then by the table stakes/buy-in amounts.

The purpose of the application is for it to be an aid to learning to play better poker. There are no plans to monetise this application.

### Currently implemented

* Data ingestion for party poker hand histories
* Data ingestion for poker stars hand histories
* Data export to Excel/.csv for one or all hand histories

### Currently not implemented

* JMenu tool bar buttons - most of them don't work except for the open/export
* there is a button on the toolbar called 'dashboard' - I'd like this to display a report for all/hands ingested across the various groups of table stakes.

# Requirements

Currently the only requirements are:

* JDK 1.7 or above
* gradle - strictly speaking this is not necessary due to the gradle wrapper provided.

I'd like to reimplement a lot of this using Groovy so that will soon become a dependency. Groovy removes a ton of Java boilerplate code so should simplify the code.

## Vision

* I am making this project public so others can make more use of it. I'd like it to be developed further and for the data that's ingested to be utilised more to help improve play so that a player doesn't need a tool to help them but can play unaided by software against all types of players.

Features I'd like to see:
* Reports - These reports can be generated in the application in real time, OR they could be exported as Excel Spreadsheets.
* Monitoring live games - My plan was also to make this a live, in-game tool for monitoring the hand histories as they are updated at the end of each hand so that a player can click a 'watch this game' button and any new hand histories dumped to a file will be ingested and can be used to view whats going on at any given moment.
* Profiling players - Another feature I wanted to add was the ability to use some AI/Neural network for prototyping players so that a profile could be matched to each player.
* Strategy in Poker - I'd also like to highlight strategic plays for the player. 3 low cards is a good opportunity to raise in a 2-way pot, when I have late position for example. And playing 2 medium-suited connectors in a 5-way, raised pot, could yield a monster pot. etc. A good time to bluff a large amount knowing you'll probably lose (to appear to give action) could be when your opponent is short stacked. etc.
* Correlations - I'd like to see what general correlations there are between various factors. Short stacks fold more to maintain their money vs. going on tilt and blowing it all with a mediocre/bad hand.
* Game trainer - in my own experience the times I've played the best are when my memory worked almost flawlessly and I've studied the game lots. I'd like to develop some brain training games to see if those help improve my own play. There is a package com.morphiles.game that has various objects that could help in this endeavour.

... I'll leave the rest to your imagination.

##  Known Issues & TODOs

* There are a bunch of TODO's to fix in the code.
* There are a whole host of tests that would be desireable
* There is a need for a massive amount of refactoring
* There is a need to remove the now obsolete MainGui code (except for the main method)
* I was in the process of moving to an MVC model for the application to decouple the models and controllers from the GUI. This needs a large amount of analysis to removed dead code.
* The Gui is a multithreaded application and aims to keep the Gui interactive whilst the data is being processed. There are some issues with here that need fixing and which are intermittent.
* The tabs for the hand histories have a fairly poor close button implemented. This should be improved.
