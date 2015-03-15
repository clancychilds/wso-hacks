# Introduction #

This article explains how to send relevant Google Website Optimizer (GWO) events to Google Analytics (GA).  Once these events are in GA, you can use Advanced Segmentation to gain deeper insight as to how the users of your website change their behavior as a function of the experiments you are running.

# The code #

There are several steps to complete the integration, as follows:

  1. Add the GA / GWO tags to the test page.
  1. Add a filter to your base profile(s) to remove GWO tracking.
  1. Create a new profile which will be your GWO tracking profile.
  1. Create the custom segments in this new profile.

### 1. Tag the experiment page ###

**If using ga.js**
```
<script type="text/javascript">
  var gaJsHost = (("https:" == document.location.protocol) ? " https://ssl." : "http://www.");
  document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js ' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">

  //Pull the experiment variation - see notes in the article for options.
  var gwoVar = utmx("combination");
  if (!gwoVar) {
    gwoVar = "inactive";
  }

  //Get the pageTracker for your primary GA account.
  var firstTracker = _gat._getTracker("UA-xxxxx-x");
  firstTracker._initData();

  //Track the GWO test event, by appending special GWO values onto the original page URL 
  firstTracker._trackPageview("/my/page.html" + "?gwo_exp=1234567890&gwo_var= + gwoVar);

  //Get the pageTracker for your GWO account.
  var secondTracker = _gat._getTracker("UA-yyyyy-y");
  secondTracker._initData();

  //Track the GWO test event, where 1234567890 is the experiment id.
  secondTracker._trackPageview("/1234567890/test");
</script>
```

**If using urchin.js**
```
<script>
  if(typeof(urchinTracker)!='function')document.write('<sc'+'ript src="'+
'http'+(document.location.protocol=='https:'?'s://ssl':'://www')+
'.google-analytics.com/urchin.js'+'"></sc'+'ript>')
</script>
<script>

  //Pull the experiment variation - see notes in the article for options.
  var gwoVar = utmx("combination");
  if (!gwoVar) {
    gwoVar = "inactive";
  }

  //Get _uacct for your primary GA account.
  _uacct = 'UA-xxxxx-x';

  //Track the GWO test event, by appending special GWO values onto the original page URL 
  urchinTracker("/my/page.html" + "?gwo_exp=1234567890&gwo_var= + gwoVar);

  //Get _uacct for your GWO account.
  _uacct = 'UA-yyyyy-y';

  //Track the GWO test event, where 1234567890 is the experiment id.
  urchinTracker("/1234567890/test/");
</script>
```

**Implemention Options**

The `utmx()` function is responsible for returning the variant of the test that the current visitor is seeing.  The output can be customised as described in the table.

| **Function** | **Description** |
|:-------------|:----------------|
| `utmx("variation_number","section_name")` | Returns the number of the variation.  If the original was selected for this section, the 0 will be returned.  Otherwise, number 1 will be returned if the first alternative was selected for this section (MV or A/B).  Note that the section name for an A/B experiment is "A/B". |
| `utmx("combination")` | Returns the combination number selected for this visitor to the experiment.  This number is between 0 and N-1 inclusively, and will correspond with the combination numbers from the GWO front end. |
| `utmx("combination_string")` | Returns a string of the form "X-Y-Z" where the number of items separated by dashes is the count of the number of sections (1 in the case of an A/B experiment).  The numbers between the dashes are the numbers of the individual alternatives chose for each section.  The first number corresponds to the first section. |

In addition, undefined can also be returned by any of these functions.  This implies that the visitor is not participating in the experiment and should be shown default content.

**Additional Notes**

These tags will add additional information to the page URL which essentially log relevant Website Optimizer events which have occurred on the page.  The following table explains the events which are captured.

| **Page** | **Description** |
|:---------|:----------------|
| `/my/page.html?gwo_exp=1234567890&gwo_var=inactive` |A visit to the test page by a visitor who is not participating in the experiment.  N.B. They will have seen the original content.|
| `/my/page.html?gwo_exp=1234567890&gwo_var=0` |A visit to the test page by a visitor who is participating in the experiment and saw the original content.|
| `/my/page.html?gwo_exp=1234567890&gwo_var=1` |A visit to the test page by a visitor who is participating in the experiment and saw combination 1.|
| `/my/page.html?gwo_exp=1234567890&gwo_var=2` |A visit to the test page by a visitor who is participating in the experiment and saw combination 2 etc...|

### 2. Add a filter to your base profile(s) to remove GWO tracking. ###

As a result of the last step, in the content report, GA will report a different version of the same test page depending on the variation served by GWO.  In most cases, you will want to filter the parameters out of the traffic in your base profile.  Here is the process for doing that:

  * Go to the settings for your base profile and add a filter.
  * Create an Advanced Filter called "Remove GWO tracking parameters"
  * In "Field A -> Extract A", select Request URI and `(^.*)(.gwo_exp.*)`
  * In "Output To -> Constructor", select Request URI and `$A1`

Now repeat for any additional profiles you want to filter.

### 3. Create a new profile which will be your GWO tracking profile. ###

Now, lets create a new profile which will be used for GWO reporting.  Here are the steps:

  * From the Analytics settings page, create a new profile.
  * Create the profile for the existing domain and call it "GWO reporting profile".
  * Make sure you add back in filters which are required for your data to make sense.

### 4. Create the segments ###

Once the GWO events are being logged in your new GA profile, you should create custom segments for each event you are interested in reporting against.  You can then segment your GA reports to find out how visitor behavoir changed as a function of the experiment variation they were shown.  The steps are as follows:

  * Create a new custom segment.
  * Choose dimension: Page.
  * Enter the appropriate value: e.g. `/my/page.html?gwo_exp=1234567890&gwo_var=0`
  * Give it a descriptive name: e.g. `My Experiment : Original`
  * Save, and you are done!