_**Introduction**_:

Studying and working at Duke, we are engaging with Sakai any time at any place. Many of Sakai accesses are made with mobile device. Now we can visit the website directly
through browser, which is relatively slow. An alternative is an accessory version of Sakai as part of DukeMobile application. Both have same problem: bad UI design disable us from
using it effectively. After contacting official Sakai develop staff, we are guaranteed to have REST APIs offered by OIT that support all the features. Leveraging some of these APIs, the Duke Sakai App will be able to work for an authenticated user a subset of Sakai functions and display them in a better Android user interface.

_**Installation/Configuration**_:
1. Device: Android phone
2. Installation: Install the Sakai APK to an Android phone
3. Requirement: Connected to the Internet via WI-FI or Cellular


_**Overall Code structure**_:
1. Authenticate: 
    * Redirecting users to the https://shib.oit.duke.edu, ask users to input their netId and password
    * Getting UserId from the Sakai database once the user is successfully authenticated.

2. Sites view:
    * Parsing the json data of user’s programs into a list according to the UserId
    * Redirecting users to the specific site once users tap the corresponding row, pass the siteid to each site page
3. Each site view
    * Showing buttons of “Assignments” and “Gradebook”.
4. Assignments view
    * Parsing the json data of assignments into the list according to the selected course siteId
    * Redirecting users to instruction page, pass the assignment information to the instruction page
5. Instructions
    * Parsing the html data of the instructions into a textview.
6. Gradebook view
    * Parsing the json data of grades into the list according to the selected course siteId
7. Profile
    * Paring the json data of user’s information into the corresponding field.




