
## This repository is for WHE Android development

 Min OS : 8 

Programming language -> KOTLIN

Development architecture - MVVM

### Development guidelines:

#### Class files

Class names should be in UpperCamelcase.

For example LogInActivity, LogInFragment.



#### Resources files

Resources file names are written in lowercase_underscore.


| Element | Prefix |
| --- | --- |
| `TextView` | text_ |
| `ImageView` | image_ |
| `Button` | button_ |


#### Layout files

Layout files should match the name of the Android components that they are intended for.

| Component | Prefix | Layout Name |
| :---         |     :---:      |          ---: |
| Activity   | activity_     | activity_main.xml    |
| Fragment    | fragment_       | fragment_details.xml      |
| Dialog   | dialog_     | dialog_new_item.xml    |
| AdapterView    | fragment_       | fragment_details.xml      |
| Activity item  | item_     | item_person.xml    |
| Custom view layout    | view_       | view_profile_avatar.xml      |


#### Naming conventions for icons 

| Asset Type | Prefix | Example |
| :---         |     :---:      |          ---: |
| Icons   | ic_     | ic_profile.png   |
| Launcher icons    | ic_launcher       | ic_launcher_calendar.png      |
| Menu icons and Action Bar icons   | ic_menu     | ic_menu_about.png    |
| Status bar icons    | ic_stat_notify       | ic_stat_notify_msg.png      |
| Tab icons  | item_     | ic_tab    | ic_tab_new.png      |
| Dialog icons    | ic_dialog       | ic_dialog_login.png      |


#### Menu files

Menu files should match the name of the component. For example, if we are defining a menu file that is going to be used in the UserActivity, then the name of the file should be activity_user.xml





#### Values files

Resource files in the values folder should be plural, e.g. strings.xml, styles.xml, colors.xml, dimens.xml, attrs.xml
