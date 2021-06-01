# About the Project
This project was made for an Exam in Databases.<br>
The project is a social networks bare bones, with users, posts and followers.<br>
We use 3 different database types to do that and they are implemented through docker compose.

# Functional requirements
* User can register an account 						- Implemented
* User has to be able to login 						- Implemented
* User can chat with another user 					- Implemented
* User can see history of chat messages 			- Implemented
* User gets notification when receiving a message 	- Implemented
* User can follow another user 						- Implemented
* User can unfollow another user					- Implemented
* User can make a post with tagged users			- Implemented
* User can upload a profile picture					- Implemented
* <strike>User can see who follows him/her</strike>	- Dropped due to time constraints
* User can change among his old profile pictures	- Implemented

# Nonfunctional requirements
* There is a limit of 128 active connections. 		- Implemented - Redis is set up 
* Password are always hashed so they are not immediately readable.	- Implemented 
* The application will work in Chrome and firefox browser. 		- Tested only these two browsers
* The system will run in docker containers.			- Implemented

# Types of db:
social media platform:
* sql       - stores profile images file path and user login data
* graph     - stores user follows and posts
* redis     - chat between users, stores chat history, stores chat notifications

# Installation
1. Before you begin the installation process make sure you have docker installed.
2. To install the program you must first clone the repository from the attached GitHub.
3. After cloning open a terminal in the root project folder and run this command “docker-compose up”. Then wait for docker to install all the required components.
4. When docker is done installing, run the main called TheSocialNetworkApplication.java.
You can now access the program by typing in localhost:8080 in a browser.

# Authors
Simon Schønberg Bojesen<br>
Martin Høigaard Cupello<br>
Kenneth Leo Hansen<br>
Frederik Blem

