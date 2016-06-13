# CardGamesAPI
#
# A Restful API with Java, AngularJS

Coding prerequisites for Windows
- Browser - Mozilla Firefox v46.0.1
- JDK - Java SE Development Kit 8
- Project Management Tool - Maven v3.3.9
- IDE - IntelliJ IDEA Community Edition 2016 1.2 IDE
- Git - Version control system v2.6.1.windows.1
- Javascript runtime - nodejs v4.4.5 (dl from nodejs.org)
- npm Package Manager for various dev tools - npm v2.15.5 (comes with nodejs)
-> Make sure the locations of both npm and http-server are added in environment variable 'PATH' 
- bower Web Package Manager for the runtime angular code - Bower v1.7.9
- npm is preconfigured to run bower also
- PhantomJs as headless browser for testing
- GlassFish Application Server - open source GlassFish Java EE 7 App Server 4.1.1 (jdk 8u60 min)
Other Java EE servers for EAR are: IBM WebSphere, Apache TomEE Jaxrs, Oracle Weblogic, RedHat Wildfly (former Jboss)
- Tomcat is for War only and suitable for Spring so Java EE is not really needed.

Coding ecosystem:
Use GitHub as version control
Maven as the java software build tool to automated
 - lib dependencies folder in m2
 - run tests etc
 - create the manifest.mf with correct entries
 - package everything
 - lifecycle management so test after compile
Java EE 7 from 2013 as the back-end language
 - enterprise edition for adding javax.* to java se
 - object relational mapping
 - jms, ws, xml with java 2 xml binding (jaxb) for reading (unmarshalling xml), e-mail
 - javabeans standard (no arg constr. private prop, serializable)
 - servlets for adding http session, cookies or rewriting urls to java
 - servlets need a web-container and are packages in a war file webapp (servlet is java with html)
Spring 1.4.0 instead of CDI - Contexts and Dependency Injection
 - for building decoupled systems
 - instead of creating objects for aggregations, spring injects this magically
 - makes Java EE a breeze to work with
AngularJS 1.5.6 as the frond-end framework
 - this is what html should have been (dynamic views in webapps)
 - directives as a new feature to invent new reusable html syntax: hides complex dom, css and behaviour
 - 2way data binding (project your model to the dom view)
 - MVVM (model is the data in js object, view is generated html, vm is $scope to detect and broadcast changes to state)
 - controller sets initial state and $scope with methods to control behavior
Frond-end testing during the maven build
- grunt is used to automate tasks eg. testing
- Jasmine as javascript unit test tool integrated with maven
Bootstrap als css standard
 - an html, css, javascript framework build at twitter
 - build user interface components
 - has tools to use class name for coloring or button shaping instead of writing css
Javascript in src/main/javascript
 - find an element in html and change the content (text or pic)
 - start with <script>
