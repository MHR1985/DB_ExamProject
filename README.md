# Functional requirements
* User can register an account
* User has to be able to login
* User can chat with another user
* User can see history of chat messages
* User gets notification when recieving a message
* User can follow another user
* User can unfollow another user
* User can make a post with tagged users
* User can upload a profile picture
* User can see who follows him/her

# Nonfunctional requirements
* There is a limit of 128 active connections.
* Password are always hashed so they are not immediately readable.
* The application will work in Chrome and firefox browser.
* The system will run in docker containers.

# Types of db:

social media platform:
* sql       - stores profile images file path and user login data
* graph     - stores user follows and posts
* redis     - chat between users, stores chat history, stores chat notifications