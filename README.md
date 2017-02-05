# Eclipse plugin for Hibernate Search
This is the repository of Hibernate Search plugin for Eclipse. Its development started as a part of Google Summer of Code 2015 and is still going on. 

# Eclipse Marketplace
It can be installed from Eclipse Marketplace: https://marketplace.eclipse.org/content/hibernate-search-plugin

# Video tutorials
A short video example of how to use the tool can be found [here](https://www.youtube.com/watch?v=aFPijD3zutM)

# Build the plugin
In order to simply build the project, do the following:

         git clone https://github.com/bdshadow/jbosstools-hibernate-search.git
         cd jbosstools-hibernate-search
         mvn clean verify -DskipTests

`-DskipTests` is needed, because currently there are still some problems with tests.

# Contribution
Do the following steps to configure your Eclipse workspace:

1. Set target platform. [Here](https://github.com/jbosstools/jbosstools-devdoc/blob/master/building/target_platforms/target_platforms_for_consumers.adoc) you can read information about it. Shortly:
  1. Get the code of target platform: [https://github.com/jbosstools/jbosstools-target-platforms] (https://github.com/jbosstools/jbosstools-target-platforms)
  2. Checkout the branch you need (currently, I use 4.60.x)
  3. Build the target platform
  4. Open jbosstools-multiple.target in Eclipse and click "Set as Target Platform"
2. Hibernate-Search tools need Hibernate tools to be in Eclipse workspace. In its turn, Hibernate tools need [jbosstools-base (common module)] (https://github.com/jbosstools/jbosstools-base/tree/master/common) in workspace. That's why you can get the code and import them as maven projects step-by-step, or watch the video, attached to https://issues.jboss.org/browse/JBIDE-19590 to import everything automatically.
3. Get the code of Hibernate Search tools and import it into the eclipse workspace
4. Build jbosstools-base (common) module, then build Hibernate tools, and olny after that try "clean verify" on the current project (Hibernate Search Tools), add skip tests if you want to build everything quicker.
5. Right-click on Hibernate-Search tools project -> Run as... -> Eclipse Application. It must start a new Eclipse instance with working HSearch plugin.
