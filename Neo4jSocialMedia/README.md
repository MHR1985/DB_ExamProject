# Neo4jSocialMedia"

## Authors:

Martin Høigaard Cupello,
Kenneth Leo Hansen,
Simon Bojesen,
Frederik Blem

## About:

A mini-social network with persons and posts.

## How to run
* Clone project
* Run "docker-compose up"
* Import Neo4jSocialMedia_Client.json into Postman.
* run POST "/person/create" command first to populate some test data.
* run POST "/post/create" command second to create post data.
* run GET "/post/Dan" Take the post id
* run POST "/post/like" add post id from the previous get to the body.
* run GET "/person/averageFollowers" to get average for each persons followers
* run POST "/person/similarlikes" to get an overview of who has liked the same people.
* run POST "/post/like" and an id from GET "/post/Dan" to unlike a post
* run POST "/person/follows" and add "follower": "nameWhoFollows" and "target" : "targetToFollow" to add a new follower to the target

