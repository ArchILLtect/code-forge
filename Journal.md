# Journal

Document project progress, the development process, accomplishments, snags, and time spent on this class. You may use whatever format suits you, as long as this information is shared.

[Time Log](TimeLog.md)

### Week 1 - Spring 2024

**1/19/24** I originally built this project in the spring of 2016, but would like to revisit it with fresh eyes this semester, so I deleted much of what was there to so that this becomes more of a realtime demo project. Right now the project is set up where students should be as of the end of week 2 or so. It has a problem statement, a start at a project plan, this journal and a time log, and the basic directory and package structure. Looking forward to reworking this!

### Week 3 - Spring 2024

**1/30/24** Today's goal is to get this project ready for checkpoint 1. Per the course website, checkpoint 1 is: Problem statement, user stories, project plan, screen design and reflection statements pushed to GitHub. Be sure to identify which stories are part of the Minimum Viable Product (MVP). Link to your repository in student repo. This is due in the middle of next week.

Started poking around to see what is currently available for snow depth data.
https://www.weatherstreet.com/city_snow_depth/54843-Hayward-WI-snow-depth.htm


### Week 1

1/18/2016

Tasks Completed:
* Chose a project goal
* Reviewed SkinnySki.com
* Created this repository
* Wrote the problem statement
* Started documenting the project plan
* Began listing technologies and how they fit in this project

I have a solid idea for the individual project and am anxious to get started. One area I struggled with a little bit was getting specific on the project plan with regard to what exactly I'll be doing in the later weeks. I think once I have the screen layout complete this coming week, it will make it much easier to add more detail to the plan.

I also started looking at a few different services that could be used:

* Here's the home page for the google maps web services: https://developers.google.com/maps/web-services/overview?hl=en This can be used to display a map of the trail location.
* A service to identify the current snow depth, most recent snowfal, etc.  Looks like this data is available: http://www.weather.gov/nerfc/snow, but I did not quickly find a service that returns the data. Will keep searching. Scraping the details from something like this, may work as well: http://www.nohrsc.noaa.gov/nsa/reports.html?region=National&var=snowdepth&dy=2016&dm=1&dd=24&units=e&sort=value&filter=0
* A service for current weather conditions migth be helpful as well. http://openweathermap.org looks promising and they have a free tier. There's also some integration with google maps, so I think I may be able to project weather data over the google map?

### Week 2

1/29/2016

Where did the week go?!?!  Prep for classes and my grad class took up a fair amount of time this week, but I set aside a little time today to work on the screen design. I had said I was not going to go fancy with the design and would just whiteboard it or draw on paper, but when I sat down to do it, I really did want to do something a little prettier, AND using a tool allows copy and paste, which is faster than redrawing (for me anyway). So... I did a quick search of free wireframe tools available. One of the first I found with a "free wireframe tool" Google search was https://wireframe.cc/pro/.  I signed up for the 7-day free trial, learned the ins and outs of the tool, and created my wireframes in about 1 1/2 hours.  You can view the wireframes, complete with links here: https://wireframe.cc/pro/pp/af3ee6b9238237 and I also exported them as images into this repository since after the 7-day trial I'm not sure if I'll still be able to use that link or not.   I felt like this was a valuable process - it forced me to think about what I really want/need to capture and how to structure the site itself. I'm sure there will be tweaks as I build it out, but I think I'm in good shape to create the real jsps and the database.

I also created a web application project in intelliJ and connected it to my project repository in github. This way my code, journa, plan and all related documents are in a single place.  I recorded a short video as I did this so others can see one approach to adding a remote.  The git hub article on adding a remote is [here](https://help.github.com/articles/adding-a-remote/).  There are some plugins for editing/previewing markdown in intelliJ. I installed all of the recommended plugins. The HTML preview is a nice option.

Did a first deploy of my project website to openshift:  built a war in intellij of that new project and deployed to openshift using the tomcat manager.  It's not much to look at, but it's good to confirm the process. Here it is: http://tomcat-madjava.rhcloud.com/FatBikeTrailReports%5Fwar/

Created all jsps with content placeholders.

1/30/2016

Reviewed the wireframes with a potential users. Made a few small updates to include grooming type and reworded a few items for clarity.

### Week 3

2/2/2016

Added unit testing, logging, and hibernate to the application to set up for
demonstrating DAOs and Hibernate. Created packages, added a User entity and
built the persistence layer to demonstrate both a sql and hibernate dao. Only
one method is built out at this point, along with a test for it. This was a
good way to ensure everything is connected and working before I build out
more. Eventually I'll remove the SQL dao, it's in there as an example for
student for now.

Ran into an issue with hibernate - needed the geronimo jar. We did not run
into this last semester so either this is related to tomEE or the new
version of intelliJ.

2/5/2016

Designed and created all the tables today. IntelliJ's database view is even
better than I thought. Originally I named a table "user" and decided I wanted
to call it "users" instead.  I already had a dao written (as a demo for
class) with sql referring to "user". Changing the name of the table in the
database few, prompted the change automatically in the dao. How cool is
that?! It did not, however change user to users in the hibernate.cfg.xml,
but I have to wonder if there isn't a plugin that might handle making the
changes in the hibernate configs too. If not, it could be a useful addition.
Ooh, writing plugins for IDEs - possible independent research topic.

### Week 4

2/12/2016

Added in the authentication/authorization piece today. I had
originally designed a join table between the users and users_roles table,
called user_role and was storing the roles in a role table. I had hoped I
could (relatively easily) configure tomcat for this, however,  tomcat's
JDBCRealm relies specifically on a user table and a user roles table that each have a
username column making my join table unnecessary. Similarly, the user
roles tables must have the role name on it. To keep things as simple as
possible for class examples, I am back to the structure tomcat requires
which meant eliminating the role and user_role tables.

Regenerated the database diagrams to account for above.

The current config is set such that anyone can view pages listed immediately
under the web directory. The pages in the "user" directory require login
and the user role.

2/13/2016

Added in bootstrap. I haven't worked with bootstrap before, but it was
pretty easy to add.  I chose to copy in files rather than pointing to the
CDN locations so that the application will work if I'm offline.  Not 100%
sure if I have the best structure for housing the js/css/fonts, etc, but
it's a start. Also made the navbar more dynamic - took me a little while as
I'm rusty with jquery/javascript - it's been a LOOOOONG time since I
worked with those.

Resources:
* [Setting Navbar items to active](https://rjartiaga.wordpress.com/2015/03/25/bootstrap-navbar-classactive/)
* [Bootstrap tutorial](http://www.w3schools.com/bootstrap/)

Automatically generated entities for each table along with the hibernate
config. I'm not confident the various relationships are built correctly with
the automatic portion and this may have had to do with not generating all
entities at the same time.  This is basically a note to myself to check on
this going forward.  Regardless of whether some of the generated items will
need tweaking, it was really nice to be able to quickly get the classes and
hibernate config built.

### Week 7
3/2/2016

Time got away from me the last couple weeks with some major projects due in
my grad class and project reviews/grading for Advanced Java. This week that
grade class ended, and I have just under a week before the next
one starts, so I need to hit the Fat Bike Trail Reports application hard to
catch back up!

I looked at the OpenWeatherMap api today to get current weather data by zip
code. http://openweathermap.org/api. This does not seem to have snow depth,
and I'd really like to be able to stick with one weather service.  Here is a
possibility but looks like it is paid only: http://www.meteogroup
.com/en/gb/sectors/media/mobile-apps/weather-api.html. From what I'm finding,
services tend to provide snow depth data  for ski resorts. I may need to add this as a
trail report  item so we have a way to collect the data ourselves.  Found
it! The data is here:  http://www.nohrsc.noaa.gov/nsa/reports.html?region=Northern_Great_Lakes&var=snowdepth&dy=2016&dm=3&dd=2&units=e&sort=value&filter=0
So I could grab the text file for this if there isn't a service behind it.

3/3/2016 There's a team in the evening section who may take on the task of
creating a
service to access the snow depth based on a zip code using the data link
above. :)
Added the user signup/registration functionality. This is not done, but is
a first cut at adding the user from a form to the db. Need to add bootstrap
styling and validation to the form.

3/5/2016 Decided to use Bootstrap Validator as my independent research topic.
It's been a long time since I've done any frontend development and Bootstrap
is new to me.  I know there is a lot of slick validation that can be added
in as well, so after poking around at a few options, I decided to go with
Bootstrap Validator for now: http://1000hz.github
.io/bootstrap-validator/#validator-examples. Honestly, I chose it because
it was one of the top results in a google search and I was able to get it
working pretty quickly.

3/6/2016 Added in the TrailDao and TrailReportDao. When I was writing unit
tests, I noticed tests failing because the id of the inserted record was
being returned as 0.  Turns out the generated hbm.xml file did not have the
the generator class set to native:   
```<id name="id" type="int" column="id">
     <generator class="native"/>
   </id>```
I'm noticing as I build the daos, that they are all similar, I think these
could all be a subclass of some abstract Dao.  See this as an example: http://www.java2s.com/Code/Java/Hibernate/GenericDaoFindAll.htm

### Week 9

3/24 I bought the domain fatbiketrailreports.com today for $.99 on goDaddy.
I set this up so that the domain points to the application on
openshift. This was very easy to configure right on the goDaddy site.

Added jstl impl library to the project so I can use the jstl tags. Now
displaying all trail reports on the index page in a table. Not pretty, but it
is displaying data.

3/25 I set up the database on openshift today. I'm finally forced to do
this work because index.jsp relies on the db, so to get things working at all
on openshift, I need to get the database up there.

1. First step was to get a dump of the sql:

```
Paulas-MacBook-Pro:bin paulawaite$ sudo ./mysqldump -u root -p  --databases 
FAT_BIKE_TRAIL_REPORTS > /Users/paulawaite/Documents/dump.sql
                                         Enter password: 
                                         Paulas-MacBook-Pro:bin paulawaite$ 
```

1. Run the sql on openshift. Voila! That's it!

*EXCEPT* the version of MySql on Openshift is different from what I have
locally. So I wasn't able to use the CURRENT_TIMESTAMP for my
DATETIME fields. "It is
version specific and is now allowed for DATETIME columns as of version 5.6
http://dev.mysql.com/doc/refman/5.6/en/timestamp-initialization.html"

I also had repeated issues with an invalid token messages. I was working at
the public library at the time and wonder if it wasn't related to their wifi.
If I opened a new browser tab and went back into phpmyadmin, I could run sql
once and then would get the token error again.

Next up I needed to configure the server.xml on my openshift tomcat instance to
use the new database for authentication. Remember the jdbc realm authentication
work we did earlier in the semester? Create a tomcat user with read only
access in the database and then add something like this to the openshift server
.xml. Your server/host will be different.

```
<Realm className="org.apache.catalina.realm.JDBCRealm"
      driverName="com.mysql.jdbc.Driver"
      connectionURL="jdbc:mysql://127.5.237.130:3306/FAT_BIKE_TRAIL_REPORTS?user=tomcat&amp;password=tomcat"
      userTable="users" userNameCol="email_address" userCredCol="password"
      userRoleTable="users_roles" roleNameCol="role"/>
```                      

Remember to put the mysql driver jar in the lib directory of the openshift
tomcat instance.

And lastly, I need to change the hibernate config for the openshift database,
which essentially means changing the host from localhost to the server that
hosting MySql on Openshift: 127.5.237.130.

I ran into a goofy issue not being able to login to the tomcat manager app
when I had the realm in the server.xml file. The way we originally added
realms to our server.xml essentially enforced that realm for all apps
deployed in that tomcat instance.  I need to set the realm
up to be application specific rather than global to get around this issue.

The solution: set up a realm for the specific application/context:

```
<!-- Change below for Openshift Deploy<Host name="localhost"  appBase="webapps" -->
          <Host name="OPENSHIFT_APP_DNS" appBase="webapps"
            unpackWARs="true" autoDeploy="true">
			
	        <!-- setting up realm only for fat bike trail reports app -->
		<Context path="/fbtr" debug="0" privileged="true" docBase="fbtr"> 	
			<Realm className="org.apache.catalina.realm.JDBCRealm"
	         driverName="com.mysql.jdbc.Driver"
	         connectionURL="jdbc:mysql://127.5.237.130:3306/FAT_BIKE_TRAIL_REPORTS?user=tomcat&amp;password=tomcat"
	         userTable="users" userNameCol="email_address" userCredCol="password"
	         userRoleTable="users_roles" roleNameCol="role"/>
		 </Context> 
			
        <!-- SingleSignOn valve, share authentication between web applications
             Documentation at: /docs/config/valve.html -->
        <!--
        <Valve className="org.apache.catalina.authenticator.SingleSignOn" />
        -->

        <!-- Access log processes all example.
             Documentation at: /docs/config/valve.html
             Note: The pattern used is equivalent to using pattern="common" -->
       <!-- <Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs"
               prefix="localhost_access_log." suffix=".txt"
               pattern="%h %l %u %t &quot;%r&quot; %s %b" />-->

      </Host>
```

When troubleshooting your deploys to Openshift, it essential that you review
the Catalina logs.  See this discussion for information on how to ssh to the
server and navigate to the logs directory. http://stackoverflow.com/questions/28469051/where-to-see-console-log-in-openshift

### Week 10

4/5 Add the servlet to add a new trail. I started simple just to verify the
functionality, so I'm currently just passing in the trail name and zip code.
The data is making it to the database.

I'm noticing a few issues: 1) page content is sneaking up under the nav bar
and 2) when navigating from pages in the user directory to pages not in the
user directory, the paths are off. An extra "user" is added to the path, so I
have a little work to do with the relative paths here.

### Week 11

4/6 Fixed the formatting issue with the nav bar by adding a little padding
in the body.

Fixed the relative path issue by moving all jsps back into the same directory
and instead individually listing those that are restricted (need to log in to
access) in the web.xml.

Put some styling around the trail report displafy.

Added an about page.

4/7 Spent some time working on navigation through the site and a few styling
things. I'm stuck trying to get the footer to stick to the bottom. I'm going
to put that on hold a bit and move onto other things.

### Week 13

4/20

Currently at 85% coverage on TrailDao. Also 85% on TrailReports Dao.
SessionFactory is at 88% (covered using the daotests).
Added in more fields in add trail jsp. Realized I need to complete daos for
difficulty and type so that I can populate dropdowns.

4/24

Decided to add an abstract dao - I'm getting sick of the redundant code and
it doesn't set a very good example not to do it.  Also added tests,
specifically for Difficulty. On to type next... And then I should go back and
fix/remove existing daos and use abstract instead. The only thing that should
remain in the daos are anything specific.  Here's an interesting article to
even eliminate that part using hibernate named queries: http://www.ibm.com/developerworks/library/j-genericdao/

4/25-26

Finished adding the difficulty dropdown and the trail type dropdown.
Connected new fields with the servlet so recently added fields are saved.

### Week 15

5/6

It's refactoring day.  After talking with Matt and seeing Alex's presentation
on using annotations for hibernate entities, I decided to do away with the
xml config method and use annotations instead. I delete all the old entities
and hibernate entity xmls and regenerated in IntelliJ.  I did have to add
the annotation for auto-generated ids. I also got rid of the user interface
dao since it was only used as an example. Things seem much cleaner in the
project and tests pass.  