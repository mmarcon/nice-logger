#The Problem

Mobile apps can be hard to debug. When you are sitting at your desk and your Android phone is connected to your shiny Macbook while you are testing your awesome application everything works nicely.

No network failures, location detection works perfectly fine, error conditions are handled properly and your [Logcat](http://developer.android.com/tools/help/logcat.html) is clean. Then you leave the office, and while you are walking home you decide to test your app one more time. Obviously nothing works.

What can it be? Poor internet connection? Impossible to get a GPS fix? Your backend died or is behaving unexpectedly? You have no way to find out, unless you have your computer with you so you can connect your device to the USB port and check the logs. Often that is not the case.

One way to solve this is to always keep a copy of your logs in a file that you can analyze later on. This is actually very helpful when trying to track down the issue. However you still don't know what happened until you are again in front a computer. If you are like me, and you don't like not knowing immediately what is going on with your fantastic Android app, then keep reading.

#Nice Logger

The goal of this project is to have a log console that you can attach to your user interface when your app is in development mode so you can see your log messages in real time. Additionally, Nice Logger will also show clearly whether a WiFi or data connection is available and connected, it will display clearly when some error condition happened and if a `Location` was logged it will show it on a map, so you can double check if you actually are where your app thinks you are.

The project is still at a pretty early stage, but it's coming together very nicely. Here is what I have at the moment:

![Nice Logger](https://raw.github.com/mmarcon/nice-logger/master/docs/nice-logger.png)