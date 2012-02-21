Gwt Hogan
=========

Description
-----------

GwtHogan is a templating solution for GWT.

It changes the Gwt paradigm of widget based code and brings a more jquery + templates approach to coding while maintaining the power of GWT/Java as a strongly typed language and very efficient javascript compiler

Features
--------

* Generator which pre-compiles the templates
* Can work with Native Java Object data or JSON object data
* Can manipulate the template and add widgets to the template using GQuery

Getting started
---------------

* Check out the source and run 'mvn clean install'
* Change directory into the examples directory, and run mvn gwt:run

Setup:
------

Use the following maven dependency. NOTE: there's no distribution, so for now you'll have to use your local repository.

    &lt;dependency&gt;
      &lt;groupId&gt;com.villagechief.gwt&lt;/groupId&gt;
      &lt;artifactId&gt;gwt-hogan&lt;/artifactId&gt;
      &lt;version&gt;${project.version}&lt;/version&gt;
    &lt;/dependency&gt;

Add the following to your gwt.xml file:

    &lt;!-- required for gwt-hogan --&gt;
    &lt;inherits name="com.google.gwt.json.JSON" /&gt;
    &lt;!-- required for doing jquery like expressions --&gt; 
    &lt;inherits name='com.google.gwt.query.Query'/&gt;
    &lt;!-- required for passing java objects to templates --&gt;
    &lt;inherits name="com.google.web.bindery.autobean.AutoBean"/&gt;
    &lt;!-- include gwt-hogan --&gt;
    &lt;inherits name="com.villagechief.gwt.hogan.gwtHogan" /&gt;

The HoganWidget:
----------------

A template is made up of 2 components, the html mustache file and a Java class. For example:

MyTemplate.java:

    public class MyTemplate extends HoganWidget {
      public Test(){
        addParameter("planet", new JSONString("world"));
      }
    }
MyTemplate.html:

    Hello {{planet}}

MyTemplate can then be used like any other widget. But it MUST be constructed using GWT.create. For example:

    MyTemplate myTemplate = GWT.create(MyTemplate.class);
    RootPanel.get().add(myTemplate);


Adding Widgets must be done after the widget is loaded onto the page. You can use
GQuery to provide a selector to the add function which will be the container

MyTemplate.html:

    Click this button: &lt;span id="button"&gt;&lt;/span&gt;
    
MyTemplate.java:

    public class MyTemplate extends HoganWidget {
      public void onLoad(){
        super.onLoad();
        add(new Button(), $("#button1"));
      }
    }


Using Java Objects:
-------------------
Gwt Java objects need to be managed by the Gwt AutoBeans framework. 
See http://code.google.com/p/google-web-toolkit/wiki/AutoBean for more details.

AutoBeans and *Managed* java objects can be passed as parameters to a HoganWidget

Data object:

    public interface TestData {
    	public String getField1();
    	public void setField1(String v);
    }
    
Factory:

    public interface TestDataFactory extends AutoBeanFactory {
    	AutoBean&lt;TestData&gt; data();
    }
    
MyTemplate.java:

    public class MyTemplate extends HoganWidget {
      public Test(){
        TestDataFactory beanFactory = GWT.create(TestDataFactory.class);
        TestData test1 = beanFactory.data().as(); //get a managed TestData object
        test1.setField1("foo");
        
        addParameter("data", test1);
      }
    }
    
MyTemplate.html

    Field1: {{data.field1}}


Event Handling and DOM Handling
-------------------------------
A HoganWidget doesn't contain widgets the way a normal GWT Composite widget does, but you can use GwtQuery to manipulate the DOM.

So for a template like:

    Hello &lt;span id="planet"&gt;&lt;/span&gt;

You can put in your Template widget:

    import static com.google.gwt.query.client.GQuery.*;
    public MyTemplate()}
      $("#planet").text("world");
    }

References
----------

* AutoBeans: http://code.google.com/p/google-web-toolkit/wiki/AutoBean
* GwtQuery: http://code.google.com/p/gwtquery/wiki/GettingStarted
* GWT Json: http://google-web-toolkit.googlecode.com/svn/javadoc/1.6/com/google/gwt/json/client/package-tree.html

