# Sakai android app

This is the android app for Duke Sakai website. User can use this app to view
sites informations.

## Basic Functions
1. Allowed user use their Duke netID and passwd to login
2. User can view their assignments
3. User can view their lessons
4. User can view the Reources and Download the file in reources in Android download 
manager
5. User can view their grades
6. User can view the annoucement
7. User can view Box content in the app
8. User can view their profile

## Development Setup
1. Sownload Android Studio.
2. Clone the project.
3. Open "./Sakai" as project in Android Studio.
4. Make sure connected to Internet and wait for gradle build finish.
5. If you don't have a android device, select emulator to run it.
6. Else use USB cable to connect your device and allow debug mode in your device 
and select your device in Android Studio to run it.

## Code structure

These are main activities to build the basic app structure:
        
        AppBaseActivity.java    //base activity template
        Login.java              //Login activity
        Profile.java            //Profile activity
        sites.java              //activity shows every sites
        eachSite.java           //Activity shows content in each sites
        Announcement.java       //Announcements
        eachAnnounce.java       //Each Announcement
        Gradebook.java          //Activity shows the gradebook
        Resources.java          //Activity show resources
        eachResource.java       //Activity show each resource
        Assignment.java         //Assignment
        eachAssign.java         //Each Assignment
        Lesson.java             //Lesson in webview
        Notification.java       //Notifications

These are helper and tool activities:
        
        ListAdapterRes.java 
        ListCellRes.java
        PdfViewer.java        
        ListAdapter.java
        ListCell.java
        HttpHandler.java
        SplashActivity.java 
        ListAdapterAssign.java 
        ListCellAssign.java 
        ResClickListener.java


## Work status
### Sprint 3
_**Finish Status in S3**_:

* Chenfan: Add notification
* Lei: Add announcement and each announcement section
* Naiyu: Upgrade the UI of gradebook/ The change to the UI of assignment/ design the UI for the announcement.
* Sifan: Resource file downloading and view pdf/ Change resources to adapt folders
* Siyue: reorder list/ new layout of assignment(open and close)/ Dynamic title(show each course's name)/ Change resources to adapt folders
* Yunzhen: Add hamburger button to the new annoucement sites /Change profile, place it to the top
* Zifan: Redesign the UI of profile /Adjust some UI of the app/ Create the splash screen for sakai
* Yichuan: Resource file downloading and view pdf/ Add lesson section
* Zhengyi: Fix bugs of back button and bugs in annoucement/ Fix bugs of profile crashing, lessons crashing, logout crashing

_**To do list**_:
* Chenfan: Change cardview in each site to adapt different sites
* Lei: Fix bugs of announcement and each announcement section
* Naiyu: Resource UI/ Each assign UI
* Yunzhen: Add Feedback function/ Add help button
* Zifan: Resource UI/ Each assign UI
* Zhengyi: Add box section


### Sprint 2

_**Finish Status in S2**_:
1. Review all original code, restructure some code, add some comments (Yichuan Shi)
2. pdf view function in a demo button (Yichuan Shi)
3. add some links to hamburger button (Yichuan Shi)
4. Implement hamburger using inherent in every sub-view (Yunzhen Zou)
5. Intergrate Login into app (Yunzhen Zou, Zhengyi Jiang)
6. Add logout (Yunzhen Zou, Zhengyi Jiang)
7. Activities control flow optimize (Zhengyi Jiang, Yunzhen Zou)
8. Create Resource page (Sifan Wang, Lei Chen)
9. Design the interface of each research item. Pull data to be shown on resource page (Sifan Wang, Lei Chen)
10. Create Announcement page. Attempted pulling data for display (still in progess) (Sifan Wang, Lei Chen, Chenfan Li)
11. Updated the logic and the data structure of sites to obtain information sorted by terms (Siyue Zhou, Naiyu Yin, Zifan Peng)
12. Redesign the UI in Sites page, sort all sties by types (Siyue Zhou, Naiyu Yin, Zifan Peng)
13. Changed the logic and list view in Gradebooks, UI changed (Siyue Zhou, Naiyu Yin, Zifan Peng)
14. Modify UIs of navigation bar, assignment, change title displayed in subsites to make them more user friendly (Siyue Zhou, Naiyu Yin, Zifan Peng)

_**To do in S3**_:

_**Todo list**_:
1. Time String convert into correct time, in year 2018, find out the error using Imported Library Date
2. Link Hamburgur button to Announcement Page 
3. Link each_announcement to Announcement Page, rewrite use of item



