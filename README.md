#PreScoop
[ 3 days research/proposal, 14 days for implementation ] <br>
https://play.google.com/store/apps/details?id=com.project.salminnella.prescoop

A discovery app for San Francisco area preschools.  The school information is collected from the State of California's webiste, in the department of Social Services.  The information is curated and hosted in Firebase, where it is downloaded to the app when it launches. From the MainActivity there are search and filter features to find local preschools. Also included in the MainActivity are options for sorting, viewing schools in google maps, and bookmarking schools.
Technologies used for Retrofit for Yelp's API, SQLite, CursorAdapter, ASync Tasks, RecyclerView, and Collapsing Toolbar.  
Utilized picasso, google maps play services, gson, and bottom bar libraries.

Also includes California state inspection reports, licensing, citations, and complaints when available for each school. Yelp's api for school reviews, and linked to actual inspection reports provided publicly by the state of
California.  

The most difficult part of this project was finding the information that parents really
need to know. I was limited with api’s to use, and California is currently 1 of 12 states that
doesn’t have licensing, citations or complaints digitized.  I really want this information available
to other parents, so i decided to use firebase to make it available more quickly.  I can have the
app in the playstore, and continue to publish new information that will be immediately available
to the users.

<section>
<img src="https://github.com/salminnella/PreScoop/blob/master/images/prescoop_main_screen.png" />
<img src="https://github.com/salminnella/PreScoop/blob/master/images/prescoop_details_screen.png" />
</section>
<br><br>
<section>
<img src="https://github.com/salminnella/PreScoop/blob/master/images/prescoop_maps_screen.png" />
<img src="https://github.com/salminnella/PreScoop/blob/master/images/prescoop_yelp_webview_screen.png" />
</section>

A couple paragraphs about the general approach you took in your design process that point to your research.md file
If necessary, any special instructions on how to build the app in Android Studio
Descriptions of any unsolved problems or major hurdles you had to overcome
<br><br>

# Research Plan<br>
<a target="blank" href=https://docs.google.com/document/d/1tLCYZqlyAGL9vsnobFoW8eAG9by7S0Qlvvk8bEQ0-Dw/edit?usp=sharing>Google Docs - Research Plan Document</a>

User research including personas

User stories – who are your users, what do they want, and why?

<a target="blank" href=https://docs.google.com/spreadsheets/d/1aZrJd5ExxoLYNeqR8eRYOrmz9xsdQMrlARZViJ0FwL0/edit?usp=sharing>Google Sheets - Competitive Analysis</a>

<a target="blank" href=https://docs.google.com/document/d/1ntsqWdS79uoOWZr59Q5gU0yFBW2w4QNRe7l7fATkCR8/edit?usp=sharing>Google Docs - Problem Statement</a>

<b>Feature Prioritization</b><br>
<img src="https://github.com/salminnella/PreScoop/blob/master/images/FeaturePrioritization.jpg" />
<br><br>
  1.) Search For Schools<br>
  2.) Sort Schools by: price, range, rating<br>
  3.) View Schools in map<br>
  4.) Click a marker to see school details<br>
  5.) Bookmark schools<br>
  6.) View Reviews for each school<br>
  7.) Allow parents to leave reviews<br>
  8.) Advanced Search - address, type, radius, name, rating<br>
  9.) Add personal notes about each school<br>
  10.) Add reminder dates for application deadlines to calendar<br>
  11.) Can start the application process directly from the app<br>
  12.) School photos<br>
  13.) Teacher / Staff bio’s<br>
  14.) Number of Students<br>
  15.) Teacher / Student Ratio<br>
  16.) Private vs Public<br>
  17.) Description of daily activities<br>
  18.) School comparisons<br>
<br><br>
Screenshots of your wireframes – sketches of major views / interfaces in your application

# Proposal

<a href=https://docs.google.com/presentation/d/1R4ocKcAEOTe85FziP8jbmKSRxBv0l-MijKgYLByGGM8/edit?usp=sharing>Google Slides - Proposal Document</a>
