# FitnessAppUI
This application helps calculate your daily macronutrients intake, and also helps you track and monitor your daily intake.

The application is structured mainly by  several activities(screens). 
The Launcher Activity is the LoginActivity where the user can login with his already set account. 
The username and password for his account can be made in the RegisterActivity. 

After successful login HomeActivity is shown for the user. The Home Activity generally shows circles with the macronutrients that were being set or calculated in the
CalculateMacrosActivity and SetYourOwnMacrosActivity. The circles are made with animation that expands and narrows. This animation will be triggered when there is change
with the macros shown in the circles. By clicking on these circle, the user is entering the DailyMacroIntakeActivity where the user can write his meals. By going back the user
enters the HomeActivity again and the circles animate because there is change with the daily intake. While being in the DailyMacroIntakeActivity the user can delete, update his
daily meals. Also he can give a name to the meals, and also save them. By clicking the heart button in the toolbar of the DailyMacroIntakeActivity the user enters the 
FavoriteMealsActivity, which shows the saved meals for the user. Every day in 5 Am the daily intake is cleared and moved to history. Clicking the history button from HomeActivity,
shows you MealsHistoryActivity which is keeping track of the daily intake for the current month, aswell did he train or rest. 

**Rest day suggestion**

After some changes being made, the application is monitoring the training days of the user. If the user has trained two days in a row, the next day when he opens his application, 
there will be rest day suggestion dialog, which will remind you that you have to rest, and give you option to set your day to rest. Also you can go with training day again.
If the user is not training two days in the row, the application goes in its basic scenario, and there will be standard dialog which will make you choose between training and rest day.

The network calls are made with retrofit 2.0. For making this calls I used kotlin coroutines for making these calls on backround thread. 
The application checks the connectivity, and if there is no internet connection, No internet connection dialog will be shown. This prevents the app from crashing and tell the user 
to connect to the internet.
