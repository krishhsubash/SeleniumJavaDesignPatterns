# Visual Sequence Flow Diagrams
**Test Automation Framework - Function Invocation Flows**

---

## 1. Main Test Execution Flow

```
┌──────────────────────────────────────────────────────────────────────┐
│                         MAVEN BUILD PROCESS                          │
└──────────────────────────────────────────────────────────────────────┘
                                   │
                                   ▼
                    ┌──────────────────────────┐
                    │  Maven Surefire Plugin   │
                    │      (3.2.2)             │
                    └──────────────────────────┘
                                   │
                                   ▼
                    ┌──────────────────────────┐
                    │ JUnit Platform Suite     │
                    │      Engine              │
                    └──────────────────────────┘
                                   │
                                   ▼
                    ┌──────────────────────────┐
                    │   Cucumber Engine        │
                    └──────────────────────────┘
                                   │
                                   ▼
┌──────────────────────────────────────────────────────────────────────┐
│                          TestRunner.java                             │
│  @Suite                                                              │
│  @IncludeEngines("cucumber")                                        │
│  @SelectClasspathResource("features")                               │
└──────────────────────────────────────────────────────────────────────┘
                                   │
                ┌──────────────────┼──────────────────┐
                │                  │                  │
                ▼                  ▼                  ▼
        ┌──────────────┐  ┌──────────────┐  ┌──────────────┐
        │ Scan Features│  │  Glue Code   │  │   Plugins    │
        │   (.feature) │  │com.automation│  │  (Reporting) │
        └──────────────┘  └──────────────┘  └──────────────┘
                │                  │                  │
                └──────────────────┼──────────────────┘
                                   ▼
                    ┌──────────────────────────┐
                    │  FOR EACH FEATURE FILE   │
                    └──────────────────────────┘
                                   │
            ┌──────────────────────┼──────────────────────┐
            ▼                      ▼                      ▼
    ┌─────────────┐        ┌─────────────┐      ┌─────────────┐
    │ GoogleSearch│        │    Login    │      │   Adapter   │
    │  .feature   │        │  .feature   │      │  .feature   │
    └─────────────┘        └─────────────┘      └─────────────┘
            │                      │                      │
            └──────────────────────┼──────────────────────┘
                                   ▼
                    ┌──────────────────────────┐
                    │  SCENARIO EXECUTION      │
                    │  (See details below)     │
                    └──────────────────────────┘
```

---

## 2. Scenario Execution Flow (Per Scenario)

```
┌────────────────────────────────────────────────────────────────┐
│                    SCENARIO LIFECYCLE                          │
└────────────────────────────────────────────────────────────────┘

    ╔═══════════════════════════════════════════════════╗
    ║             @Before Hook (Hooks.setUp)            ║
    ╚═══════════════════════════════════════════════════╝
                           │
                           ├─→ Print scenario name
                           ├─→ Print tags
                           └─→ Initialize context
                           │
                           ▼
    ┌───────────────────────────────────────────────────┐
    │               EXECUTE STEPS IN ORDER              │
    └───────────────────────────────────────────────────┘
                           │
        ┌──────────────────┼──────────────────┐
        ▼                  ▼                  ▼
  ┌──────────┐       ┌──────────┐      ┌──────────┐
  │  Given   │       │   When   │      │   Then   │
  │  Steps   │   →   │   Steps  │  →   │  Steps   │
  └──────────┘       └──────────┘      └──────────┘
        │                  │                  │
        └──────────────────┼──────────────────┘
                           │
                           ▼
           ┌───────────────────────────────┐
           │  Step Definition Class        │
           │  (e.g., GoogleSearchSteps)    │
           └───────────────────────────────┘
                           │
                           ▼
           ┌───────────────────────────────┐
           │  Get WebDriver Instance       │
           │  DriverManager.getDriver()    │
           └───────────────────────────────┘
                           │
                           ▼
           ┌───────────────────────────────┐
           │  Find & Interact with Element │
           │  (See Element Interaction)    │
           └───────────────────────────────┘
                           │
                           ▼
    ╔═══════════════════════════════════════════════════╗
    ║           @After Hook (Hooks.tearDown)            ║
    ╚═══════════════════════════════════════════════════╝
                           │
                           ├─→ Check scenario status
                           ├─→ Capture screenshot (if failed)
                           ├─→ DriverManager.quitDriver()
                           └─→ Print final status
                           │
                           ▼
                    ┌──────────────┐
                    │  NEXT SCENARIO│
                    └──────────────┘
```

---

## 3. Driver Creation Flow (Singleton + Strategy + Factory + Builder)

```
┌────────────────────────────────────────────────────────────────┐
│  Step Definition: DriverManager.getDriver()                    │
└────────────────────────────────────────────────────────────────┘
                           │
                           ▼
        ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
        ┃   PATTERN 1: SINGLETON              ┃
        ┃   DriverManager                     ┃
        ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
                           │
                    [Is driver null?]
                     /           \
                 YES /             \ NO
                    /               \
                   ▼                 ▼
          ┌─────────────┐     ┌──────────────┐
          │Create Driver│     │Return Existing│
          └─────────────┘     │   Driver     │
                   │          └──────────────┘
                   ▼
        ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
        ┃   PATTERN 5: STRATEGY               ┃
        ┃   ExecutionStrategyFactory          ┃
        ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
                           │
                  getStrategy(env)
                           │
       ┌───────────────────┼───────────────────┐
       ▼                   ▼                   ▼
┌─────────────┐    ┌──────────────┐   ┌──────────────┐
│LocalExecution│   │CloudExecution│   │GridExecution │
│  Strategy   │    │   Strategy   │   │   Strategy   │
│     ✓       │    │      ✗       │   │      ✗       │
└─────────────┘    └──────────────┘   └──────────────┘
       │
       │ createDriver(browser, headless)
       ▼
┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
┃   PATTERN 2: FACTORY                ┃
┃   BrowserFactoryProvider            ┃
┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
                   │
          getFactory(browser)
                   │
   ┌───────────────┼───────────────┐
   ▼               ▼               ▼
┌────────┐   ┌─────────┐   ┌──────────┐
│ Chrome │   │Firefox  │   │   Edge   │
│Factory │   │Factory  │   │ Factory  │
│   ✓    │   │   ✗     │   │    ✗     │
└────────┘   └─────────┘   └──────────┘
    │
    │ createDriver(headless)
    ▼
┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
┃   PATTERN 3: BUILDER                ┃
┃   ChromeOptionsBuilder              ┃
┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
    │
    ├─→ .setHeadless(true/false)
    ├─→ .addArguments("--disable-gpu")
    ├─→ .setPageLoadStrategy()
    └─→ .build()
    │
    ▼
┌────────────────────────────┐
│    ChromeOptions           │
└────────────────────────────┘
    │
    │ new ChromeDriver(options)
    ▼
┌────────────────────────────┐
│    WebDriver Instance      │
│    (Chrome Browser)        │
└────────────────────────────┘
    │
    │ Store in ThreadLocal
    ▼
┌────────────────────────────┐
│  Return to Step Definition │
└────────────────────────────┘
```

---

## 4. Element Interaction Flow (Decorator Pattern)

```
┌────────────────────────────────────────────────────────────────┐
│  Step: driver.findElement(By.id("search"))                     │
└────────────────────────────────────────────────────────────────┘
                           │
                           ▼
                  ┌─────────────────┐
                  │  WebElement     │
                  │  (Selenium API) │
                  └─────────────────┘
                           │
                           ▼
        ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
        ┃   PATTERN 4: DECORATOR              ┃
        ┃   WebElementDecoratorFactory        ┃
        ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
                           │
               create(element, mode)
                           │
        ┌──────────────────┼──────────────────┐
        ▼                  ▼                  ▼
  ┌──────────┐       ┌──────────┐      ┌──────────┐
  │PRODUCTION│       │  DEBUG   │      │ MINIMAL  │
  │  Mode    │       │   Mode   │      │   Mode   │
  │    ✓     │       │    ✗     │      │    ✗     │
  └──────────┘       └──────────┘      └──────────┘
        │
        │ PRODUCTION Mode Chain:
        │
        ▼
┌────────────────────────────┐
│ ErrorHandlingWebElement    │ ← Layer 1: Catch exceptions
└────────────────────────────┘
        │ wraps
        ▼
┌────────────────────────────┐
│   LoggingWebElement        │ ← Layer 2: Log actions
└────────────────────────────┘
        │ wraps
        ▼
┌────────────────────────────┐
│  ScreenshotWebElement      │ ← Layer 3: Take screenshots
└────────────────────────────┘
        │ wraps
        ▼
┌────────────────────────────┐
│  Original WebElement       │ ← Base element
└────────────────────────────┘

Invocation Chain:
=================
element.click()
    ↓
ErrorHandling.click()
    ├─→ try {
    │       Logging.click()
    │           ├─→ log("Clicking...")
    │           │   Screenshot.click()
    │           │       ├─→ screenshot("before")
    │           │       │   originalElement.click()
    │           │       └─→ screenshot("after")
    │           └─→ log("Clicked!")
    └─→ } catch(e) { handle error }

Result: Enhanced WebElement returned to Step Definition
```

---

## 5. Page Object Model (POM) Flow

```
┌────────────────────────────────────────────────────────────────┐
│  Step: I search for "Selenium"                                 │
└────────────────────────────────────────────────────────────────┘
                           │
                           ▼
        ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
        ┃   PATTERN 6: PAGE OBJECT MODEL      ┃
        ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
                           │
                           ▼
┌─────────────────────────────────────────────────────┐
│  new GoogleSearchPage(driver)                       │
└─────────────────────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────┐
│  extends BasePage                                   │
│  └─→ super(driver)                                  │
│      PageFactory.initElements(driver, this)         │
└─────────────────────────────────────────────────────┘
                           │
        ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
        ┃   PATTERN 7: PAGEFACTORY            ┃
        ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
                           │
                    Process @FindBy
                           │
        ┌──────────────────┼──────────────────┐
        ▼                  ▼                  ▼
  ┌──────────┐       ┌──────────┐      ┌──────────┐
  │searchBox │       │searchBtn │      │ results  │
  │ @FindBy  │       │ @FindBy  │      │ @FindBy  │
  └──────────┘       └──────────┘      └──────────┘
        │
        ▼
┌─────────────────────────────────────────────────────┐
│  Page Methods (Fluent API - Method Chaining)       │
├─────────────────────────────────────────────────────┤
│  search(query)                                      │
│    └─→ searchBox.sendKeys(query)                   │
│        searchBtn.click()                            │
│        return this  ← Allows chaining              │
│                                                      │
│  isResultsDisplayed()                               │
│    └─→ return results.isDisplayed()                │
└─────────────────────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────┐
│  Step Definition:                                   │
│  searchPage.search("Selenium")                      │
│            .isResultsDisplayed() // chaining       │
└─────────────────────────────────────────────────────┘
```

---

## 6. Adapter Pattern Flow

```
┌────────────────────────────────────────────────────────────────┐
│  Test: Use different automation tools via unified interface    │
└────────────────────────────────────────────────────────────────┘
                           │
                           ▼
        ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
        ┃   PATTERN 9: ADAPTER                ┃
        ┃   AdapterFactory                    ┃
        ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
                           │
              createAdapter(type)
                           │
        ┌──────────────────┼──────────────────┐
        ▼                                     ▼
┌─────────────────┐                  ┌─────────────────┐
│ SeleniumAdapter │                  │PlaywrightAdapter│
│       ✓         │                  │       ✗         │
└─────────────────┘                  └─────────────────┘
        │                                     │
implements                             implements
        │                                     │
        └──────────────┬──────────────────────┘
                       ▼
        ┌──────────────────────────────┐
        │  WebAutomationAdapter        │
        │  (Unified Interface)         │
        ├──────────────────────────────┤
        │  + navigateTo(url)           │
        │  + click(locator)            │
        │  + type(locator, text)       │
        │  + getText(locator)          │
        │  + isDisplayed(locator)      │
        │  + getAttribute(loc, attr)   │
        │  + quit()                    │
        └──────────────────────────────┘

Selenium Implementation:
========================
SeleniumAdapter.click(locator)
    └─→ WebElement el = driver.findElement(By...)
        el.click()

Playwright Implementation:
==========================
PlaywrightAdapter.click(locator)
    └─→ page.click(locator)

Result: Same interface, different implementations!
        Tests can switch tools without changing code.
```

---

## 7. Screenplay Pattern Flow

```
┌────────────────────────────────────────────────────────────────┐
│  Test: User searches for "Selenium" (Actor-centric)            │
└────────────────────────────────────────────────────────────────┘
                           │
                           ▼
        ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
        ┃   PATTERN 10: SCREENPLAY            ┃
        ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
                           │
                           ▼
┌─────────────────────────────────────────────────────┐
│  Actor actor = new Actor("User")                    │
└─────────────────────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────┐
│  actor.can(BrowseTheWeb.with(driver))               │
│  └─→ Ability: Use WebDriver                        │
└─────────────────────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────┐
│  actor.attemptsTo(                                  │
│      Navigate.to("https://google.com"),            │
│      Enter.text("Selenium").into("name=q"),        │
│      Click.on("name=btnK")                         │
│  )                                                   │
└─────────────────────────────────────────────────────┘
                           │
        ┌──────────────────┼──────────────────┐
        ▼                  ▼                  ▼
  ┌──────────┐       ┌──────────┐      ┌──────────┐
  │ Navigate │       │  Enter   │      │  Click   │
  │Interaction│      │Interaction│     │Interaction│
  └──────────┘       └──────────┘      └──────────┘
        │                  │                  │
        │ performAs(actor) │                  │
        ▼                  ▼                  ▼
  ┌──────────┐       ┌──────────┐      ┌──────────┐
  │driver.get│       │el.sendKeys│     │el.click() │
  └──────────┘       └──────────┘      └──────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────┐
│  actor.asks(                                        │
│      TheTitle.ofThePage()                           │
│  )                                                   │
└─────────────────────────────────────────────────────┘
                           │
                           ▼
                  ┌─────────────────┐
                  │  TheTitle       │
                  │  Question       │
                  └─────────────────┘
                           │
                           │ answeredBy(actor)
                           ▼
                  ┌─────────────────┐
                  │ driver.getTitle()│
                  └─────────────────┘
                           │
                           ▼
                  ┌─────────────────┐
                  │Return title value│
                  └─────────────────┘
```

---

## 8. Facade Pattern Flow

```
┌────────────────────────────────────────────────────────────────┐
│  Test: Read data from different file types                     │
└────────────────────────────────────────────────────────────────┘
                           │
                           ▼
        ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
        ┃   PATTERN 8: FACADE                 ┃
        ┃   FileHandlerFacade                 ┃
        ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
                           │
        Simple Interface for Complex Subsystems
                           │
        ┌──────────────────┼──────────────────┐
        ▼                  ▼                  ▼
┌─────────────┐    ┌─────────────┐   ┌─────────────┐
│ readText    │    │  readCSV    │   │  readJSON   │
│   File()    │    │   File()    │   │   File()    │
└─────────────┘    └─────────────┘   └─────────────┘
        │                  │                  │
        ▼                  ▼                  ▼
┌─────────────┐    ┌─────────────┐   ┌─────────────┐
│ TextFile    │    │  CSVFile    │   │  JSONFile   │
│  Handler    │    │  Handler    │   │  Handler    │
└─────────────┘    └─────────────┘   └─────────────┘
        │                  │                  │
        │                  │                  │
        └──────────────────┼──────────────────┘
                           │
                  Complex Operations:
                  ═══════════════════
                  - Open file
                  - Parse format
                  - Handle exceptions
                  - Close resources
                  - Return data
                           │
                           ▼
                  ┌─────────────────┐
                  │  Return Data    │
                  │  to Test        │
                  └─────────────────┘

Database Facade (Similar):
===========================
DBHandlerFacade
    ├─→ DatabaseConnectionManager
    ├─→ QueryExecutor
    └─→ ResultSetMapper

Usage:
======
facade.executeQuery("SELECT * FROM users")
    └─→ Hides: connect → execute → map → close
```

---

## 9. Complete Integration Flow (All Patterns Together)

```
USER INITIATES TEST
        │
        ▼
┌───────────────┐
│ Maven Process │
└───────────────┘
        │
        ▼
┌───────────────────────────────────────────────────────┐
│           TestRunner (JUnit Platform Suite)           │
└───────────────────────────────────────────────────────┘
        │
        ▼
┌───────────────────────────────────────────────────────┐
│              Cucumber Engine                          │
│  - Scans features/                                    │
│  - Parses Gherkin                                     │
│  - Matches step definitions                           │
└───────────────────────────────────────────────────────┘
        │
        ▼
    @Before Hook
        │
        ▼
┌───────────────────────────────────────────────────────┐
│              Step Definition Execution                │
└───────────────────────────────────────────────────────┘
        │
        ├──→ DriverManager (SINGLETON)
        │        │
        │        ├──→ ExecutionStrategyFactory (STRATEGY)
        │        │        │
        │        │        └──→ LocalExecutionStrategy
        │        │                 │
        │        │                 ├──→ BrowserFactoryProvider (FACTORY)
        │        │                 │        │
        │        │                 │        └──→ ChromeDriverFactory
        │        │                 │                 │
        │        │                 │                 └──→ ChromeOptionsBuilder (BUILDER)
        │        │                 │                          │
        │        │                 └──────────────────────────┘
        │        │                          │
        │        └──────────────────────────┘
        │                 │
        ▼                 ▼
     WebDriver       WebElement
        │                 │
        │                 ├──→ WebElementDecoratorFactory (DECORATOR)
        │                 │        │
        │                 │        └──→ ErrorHandling → Logging → Screenshot
        │                 │                      │
        │                 └──────────────────────┘
        │                          │
        ├──→ GoogleSearchPage (POM)
        │        │
        │        └──→ PageFactory (PAGEFACTORY)
        │                 │
        ├──→ Actor (SCREENPLAY)
        │        │
        │        ├──→ BrowseTheWeb (Ability)
        │        ├──→ Navigate, Enter, Click (Interactions)
        │        └──→ TheTitle, TheText (Questions)
        │                 │
        ├──→ AdapterFactory (ADAPTER)
        │        │
        │        └──→ SeleniumAdapter / PlaywrightAdapter
        │                 │
        └──→ FileHandlerFacade (FACADE)
                 │
                 └──→ TextFileHandler, CSVFileHandler, etc.
        │
        ▼
    @After Hook
        │
        ├──→ Screenshot (if failed)
        └──→ DriverManager.quitDriver()
        │
        ▼
    Reports Generated
        │
        ├──→ HTML Report
        ├──→ JSON Report
        └──→ XML Report
        │
        ▼
    Maven Build Result
```

---

## 10. Summary Diagram - Pattern Hierarchy

```
                    ┌──────────────────┐
                    │   TestRunner     │
                    │   (Orchestrator) │
                    └──────────────────┘
                            │
        ┌───────────────────┼───────────────────┐
        ▼                   ▼                   ▼
  ┌──────────┐        ┌──────────┐       ┌──────────┐
  │  Hooks   │        │  Steps   │       │ Reports  │
  │ (@Before)│        │ Definitions│     │(Listeners)│
  │ (@After) │        │           │       │          │
  └──────────┘        └──────────┘       └──────────┘
                            │
        ┌───────────────────┼───────────────────┐
        ▼                   ▼                   ▼
  ┌──────────┐        ┌──────────┐       ┌──────────┐
  │SINGLETON │        │ STRATEGY │       │ FACTORY  │
  │ Driver   │   →    │Execution │   →   │ Browser  │
  │ Manager  │        │ Strategy │       │ Factory  │
  └──────────┘        └──────────┘       └──────────┘
        │                                       │
        │                                       ▼
        │                                 ┌──────────┐
        │                                 │ BUILDER  │
        │                            →    │ Options  │
        │                                 │ Builder  │
        │                                 └──────────┘
        ▼                                       │
  ┌──────────┐                                 │
  │WebDriver │  ←──────────────────────────────┘
  │ Instance │
  └──────────┘
        │
        ├──────────────────┬──────────────────┬──────────────────┐
        ▼                  ▼                  ▼                  ▼
  ┌──────────┐       ┌──────────┐      ┌──────────┐      ┌──────────┐
  │DECORATOR │       │   POM    │      │ ADAPTER  │      │SCREENPLAY│
  │ Element  │       │  Pages   │      │Interface │      │  Actor   │
  │Enhancement│      │          │      │          │      │          │
  └──────────┘       └──────────┘      └──────────┘      └──────────┘
                            │
                            ▼
                      ┌──────────┐
                      │ FACADE   │
                      │File & DB │
                      │ Handlers │
                      └──────────┘
```

---

## Legend

```
✓ = Currently invoked in standard test run
✗ = Not invoked (requires specific configuration)

Boxes:
═══════ = Design Pattern boundary
─────── = Regular component boundary
┌─────┐ = Class or component

Arrows:
   →    = Direct invocation
   ├─→  = Branch invocation
   └─→  = Final invocation in chain
```
