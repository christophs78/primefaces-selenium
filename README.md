[![Maven](https://img.shields.io/maven-central/v/org.primefaces.extensions/primefaces-selenium.svg)](https://repo1.maven.org/maven2/org/primefaces/extensions/primefaces-selenium/)
[![Javadocs](http://javadoc.io/badge/org.primefaces.extensions/primefaces-selenium.svg)](http://javadoc.io/doc/org.primefaces.extensions/primefaces-extensions)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Join the chat at https://gitter.im/primefaces-extensions/primefaces-extensions](https://badges.gitter.im/primefaces-extensions/primefaces-extensions.svg)](https://gitter.im/primefaces-extensions/primefaces-extensions?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Build Status](https://travis-ci.org/primefaces-extensions/primefaces-selenium.svg?branch=master)](https://travis-ci.org/primefaces-extensions/primefaces-selenium)
[![Stackoverflow](https://img.shields.io/badge/StackOverflow-primefaces-chocolate.svg)](https://stackoverflow.com/questions/tagged/primefaces-extensions)

# primefaces-selenium
PrimeFaces testing support based on JUnit5, Selenium and the concept of page ojects / fragements.
It also supports JUnit5 parallel test execution to speed up tests.

This is the successor of primefaces-arquillian and heavily inspired by Arquillian Graphene.

### Compatibility
Only tested on PrimeFaces 8.0+.

### Status
Currently only the following components are implemented (partially):

#### HTML
- Link

#### JSF / PrimeFaces
- AccordionPanel
- AutoComplete
- Calendar
- CommandButton
- CommandLink
- DatePicker
- InputMask
- InputNumber
- InputSwitch
- InputText
- InputTextarea
- Messages
- Panel
- SelectBooleanCheckbox
- SelectBooleanButton
- SelectManyCheckbox
- SelectOneButton
- SelectOneMenu
- SelectOneRadio
- Slider
- Spinner
- TabView
- TextEditor
- ToggleSwitch

Contributions are very welcome ;)

### Usage

Example view:
```java
import org.openqa.selenium.support.FindBy;
import org.primefaces.extensions.selenium.AbstractPrimePage;
import org.primefaces.extensions.selenium.component.InputText;
import org.primefaces.extensions.selenium.component.SelectOneMenu;

public class IndexPage extends AbstractPrimePage {

    @FindBy(id = "form:manufacturer")
    private SelectOneMenu manufacturer;

    @FindBy(id = "form:car")
    private InputText car;

    public SelectOneMenu getManufacturer() {
        return manufacturer;
    }

    public InputText getCar() {
        return car;
    }

    @Override
    public String getLocation() {
        return "index.xhtml";
    }
}
```

Example test:
```java
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.primefaces.extensions.selenium.AbstractPrimePageTest;

public class IndexPageTest extends AbstractPrimePageTest {

    @Inject
    private AnotherPage another;

    @Test
    public void myFirstTest(IndexPage index) throws InterruptedException {
        // right page?
        Assertions.assertTrue(index.isAt());
        assertNotDisplayed(index.getCar());

        // just to follow the browser with a human eye for the showcase :D - not need in your real tests
        Thread.sleep(2000);

        // select manufacturer
        assertDisplayed(index.getManufacturer());
        index.getManufacturer().select("BMW");
        Assertions.assertTrue(index.getManufacturer().isSelected("BMW"));

        // just to follow the browser with a human eye for the showcase :D - not need in your real tests
        Thread.sleep(2000);

        // type car
        assertDisplayed(index.getCar());
        index.getCar().setValue("E30 M3");

        // just to follow the browser with a human eye for the showcase :D - not need in your real tests
        Thread.sleep(2000);

        another.goTo();
        
        ...
    }
}
```

### Build & Run
- Build by source (mvn clean install)
- Run "primefaces-selenium-example" project (mvn clean install)

### Releasing
- Update pom.xml to new version `e.g 8.0.2`
- Run `mvn -N versions:update-child-modules -DgenerateBackupPoms=false` to update all child modules versions
- Commit and push the changes to GitHub
- In GitHub create a new Release titled `8.0.2` to tag this release
- Run `mvn clean deploy -Prelease` to push to Maven Central
