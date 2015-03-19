alfresco-value-assistance
=========================

Configurable value assistance module for Alfresco Share that allows picklists to be managed using datalists.

Solution presented at Alfresco Summit 2013
https://www.youtube.com/watch?v=NcYswC0S7To

It starts at 19:00 minutes

This project was first described here http://blog.tsgrp.com/2013/07/10/alfresco-data-list-driven-value-assistance/

It was when I got the source code and changed it to Alfresco SDK project format.

This project tries to reimplement (at least part of) the functionalities shown on this post http://blog.tsgrp.com/2014/12/17/harnessing-the-power-of-alfresco-data-lists-for-cascading-value-assistance/

At the moment, only 2 level dependency is working.

Building
========

In order to package this project, you have to follow the steps described on this post http://ohej.dk/2014/10/alfresco-4-2-x-and-alfresco-sdk/

It's all about editing your ~/.m2/settings.xml file and include Sonatype's repository.
Bellow is a copy of my settings.xml to be used as a reference:

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                          http://maven.apache.org/xsd/settings-1.0.0.xsd">
        <localRepository/>
        <interactiveMode/>
        <usePluginRegistry/>
        <offline/>
        <pluginGroups/>
        <servers/>
        <mirrors/>
        <proxies/>
        <profiles>
                <profile>
                        <id>allow-snapshots</id>
                        <activation><activeByDefault>true</activeByDefault></activation>
                        <repositories>
                                <repository>
                                        <id>snapshots-repo</id>
                                        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
                                        <releases><enabled>false</enabled></releases>
                                        <snapshots><enabled>true</enabled></snapshots>
                                </repository>
                        </repositories>
                </profile>
        </profiles>
        <activeProfiles/>
</settings>
```
